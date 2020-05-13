package ruslan.syntactical;

import org.apache.log4j.Logger;
import ruslan.Keywords;
import ruslan.exceptions.WrongSyntaxException;
import ruslan.token.Token;
import ruslan.token.TokenTypes;

import java.util.*;
import java.util.stream.Collectors;

import static ruslan.Keywords.*;
import static ruslan.token.TokenTypes.*;

public class RecursiveDescentParser implements Parser {
    private static final Logger log = Logger.getLogger(RecursiveDescentParser.class);
    private final List<Token> tokens;
    private int index = 0;

    public RecursiveDescentParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    @Override
    public void parse() {
        try {
            checkBlocks();
            parseProgramName();
            parseDeclarationBlock();
            parseMainBlock();
        } catch (WrongSyntaxException e) {
            log.error(e.getErrorMessage());
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void checkBlocks() throws WrongSyntaxException {
        List<Keywords> keywords = tokens.stream()
                .filter(token -> token.getType() == KEYWORD)
                .map(token -> Keywords.valueOf(token.getLexeme().toUpperCase()))
                .collect(Collectors.toList());
        boolean containsAll = keywords.containsAll(Arrays.asList(VAR, ENDVAR, BEGIN, ENDBEGIN));
        if (!containsAll) throw new WrongSyntaxException("Error on blocks", 1);
    }

    private void parseMainBlock() throws WrongSyntaxException {
        index++;
        newLine();
        Token beginToken = tokens.get(index++);
        if (!(beginToken.getType() == KEYWORD && beginToken.getLexeme().equals(BEGIN.toString()))) {
            throw new WrongSyntaxException("'" + BEGIN.toString() + "' expected!", beginToken.getLineNumber());
        }
        parseStatementList(ENDBEGIN);
        Token endToken = tokens.get(index);
        if (!endToken.getLexeme().equals(ENDBEGIN.toString())) {
            throw new WrongSyntaxException("'" + ENDBEGIN.toString() + "' expected!", endToken.getLineNumber());
        }
    }

    private void parseStatementList(Keywords endWord) throws WrongSyntaxException {
        parseStatement(endWord);
        testFunc(endWord);
    }

    private void testFunc(Keywords endWord) throws WrongSyntaxException {
        newLine();
        Token token = tokens.get(index);
        if (!token.getLexeme().equals(endWord.toString())) {
            parseStatement(endWord);
        }
    }

    private void parseStatement(Keywords keywords) throws WrongSyntaxException {
        Token token = tokens.get(index);
        System.out.println(token);
        if (token.getType() == IDENTIFIER) {
            index++;
            parseAssign();
            testFunc(keywords);
            return;
        }
        if (token.getLexeme().equals(READ.toString())) {
            index++;
            parseRead();
            testFunc(keywords);
            return;
        }
        if (token.getLexeme().equals(WRITE.toString())) {
            index++;
            parseWrite();
            testFunc(keywords);
            return;
        }
        if (token.getLexeme().equals(DO.toString())) {
            index++;
            parseCycle();
            testFunc(keywords);
            return;
        }
        if (token.getLexeme().equals(IF.toString())) {
            index++;
            parseIf();
            testFunc(keywords);
            return;
        }
        if (token.getType() != NEW_LINE) {
            throw new WrongSyntaxException("Unexpected token", token.getLineNumber());
        }
    }

    private void parseIf() throws WrongSyntaxException {
        parseCondition();
        Optional<String> any = tokens.stream()
                .skip(index)
                .filter(token -> token.getType() == KEYWORD)
                .map(Token::getLexeme)
                .filter(token -> token.equals(ELSE.toString()))
                .findAny();

        if (any.isPresent()) {
            parseStatementList(ELSE);
            Token elseToken = tokens.get(index++);
        }
        parseStatementList(ENDIF);
        index++;
    }

    private void parseCycle() throws WrongSyntaxException {
        Token whileToken = tokens.get(index++);
        if (!whileToken.getLexeme().equals(WHILE.toString())) {
            throw new WrongSyntaxException("'while' expected", whileToken.getLineNumber());
        }
        Token leftB = tokens.get(index++);
        if (!leftB.getLexeme().equals("(")) {
            throw new WrongSyntaxException("'(' expected", leftB.getLineNumber());
        }
        parseCondition();
        Token rightB = tokens.get(index++);
        if (!rightB.getLexeme().equals(")")) {
            throw new WrongSyntaxException("')' expected", rightB.getLineNumber());
        }
        parseStatementList(ENDDO);
        index++;
    }

    private void parseRead() throws WrongSyntaxException {
        Token leftB = tokens.get(index++);

        if (!leftB.getLexeme().equals("(")) {
            throw new WrongSyntaxException("'(' Expected", leftB.getLineNumber());
        }

        parseIdentifierList();

        Token rightB = tokens.get(index++);
        if (!rightB.getLexeme().equals(")")) {
            throw new WrongSyntaxException("')' Expected", rightB.getLineNumber());
        }
    }

    private void parseWrite() throws WrongSyntaxException {
        Token leftB = tokens.get(index++);
        if (!leftB.getLexeme().equals("(")) {
            throw new WrongSyntaxException("'(' Expected", leftB.getLineNumber());
        }

        Token token = tokens.get(index);
        if (token.getType() == IDENTIFIER) {
            int oldIndex = index;
            try {
                parseIdentifierList();
            } catch (WrongSyntaxException e) {
                index = oldIndex;
                parseRelExpression();
            }
        } else {
            parseRelExpression();
        }

        Token rightB = tokens.get(index++);
        if (!rightB.getLexeme().equals(")")) {
            throw new WrongSyntaxException("')' Expected", rightB.getLineNumber());
        }
    }

    private void parseAssign() throws WrongSyntaxException {
        Token assignToken = tokens.get(index++);
        if (!assignToken.getLexeme().equals("=")) {
            throw new WrongSyntaxException("'=' expected", assignToken.getLineNumber());
        }
        parseRelExpression();
    }

    private void parseCondition() throws WrongSyntaxException {
        parseExpression();
        Token token = tokens.get(index);
        if (token.getType() != TokenTypes.RELATIVE_OPERATION) {
            throw new WrongSyntaxException("Relative operation expected", token.getLineNumber());
        }
        index++;
        parseExpression();
    }

    /**
     * rel_expression = expression, {comp, expression};
     * expression = term, { ("+"|"-"), term, };
     * term = factor, { ("*" | "/"), factor };
     * factor = base { ("**") base};
     * base = ["+" | "-"], (base | constant | ("(" expression ")") | identifier)
     */
    private void parseRelExpression() throws WrongSyntaxException {
        parseExpression();
        Token token = tokens.get(index);
        if (token.getType() == TokenTypes.RELATIVE_OPERATION) {
            index++;
            parseExpression();
        }
    }

    private void parseExpression() throws WrongSyntaxException {
        parseTerm();
        Token token = tokens.get(index);
        if (token.getType() == ADDITION_OPERATION) {
            index++;
            parseTerm();
        }
    }

    private void parseTerm() throws WrongSyntaxException {
        parseFactor();
        Token token = tokens.get(index);
        if (token.getType() == MULTIPLICATION_OPERATION) {
            index++;
            parseFactor();
        }
    }

    private void parseFactor() throws WrongSyntaxException {
        Token token = tokens.get(index);
        if (token.getType() == ADDITION_OPERATION) {
            index++;
        }
        Token secondToken = tokens.get(index++);
        if (secondToken.getType() == IDENTIFIER) {
            return;
        }
        if (secondToken.getType() == TokenTypes.INT ||
                secondToken.getType() == TokenTypes.DOUBLE ||
                secondToken.getType() == TokenTypes.BOOLEAN) {
            return;
        }
        if (secondToken.getLexeme().equals("(")) {
            parseExpression();
            Token closeBracket = tokens.get(index++);
            if (!closeBracket.getLexeme().equals(")")) {
                throw new WrongSyntaxException("')' expected", closeBracket.getLineNumber());
            }
        } else {
            throw new WrongSyntaxException("Unexpected token", token.getLineNumber());
        }
    }

    private void parseProgramName() throws WrongSyntaxException {
        Token programKeywordToken = tokens.get(index++);
        if (programKeywordToken.getType() != KEYWORD) {
            throw new WrongSyntaxException("Program must start with keyword!", programKeywordToken.getLineNumber());
        }
        if (!programKeywordToken.getLexeme().equals(PROGRAM.toString())) {
            throw new WrongSyntaxException("Program must start with '" + PROGRAM.toString() + "' word!",
                    programKeywordToken.getLineNumber());
        }
        Token programNameToken = tokens.get(index++);
        if (programNameToken.getType() != TokenTypes.IDENTIFIER) {
            throw new WrongSyntaxException("Program name must be identifier!", programNameToken.getLineNumber());
        }
        newLine();
    }

    private void newLine() throws WrongSyntaxException {
        Token newLineToken = tokens.get(index++);
        if (newLineToken.getType() != TokenTypes.NEW_LINE) {
            throw new WrongSyntaxException("New Line expected!", newLineToken.getLineNumber());
        } else {
            skipLine();
            index--;
        }
    }

    private void skipLine() {
        Token newLineToken = tokens.get(index++);
        if (newLineToken.getType() == TokenTypes.NEW_LINE) {
            skipLine();
        }
    }

    private void parseDeclarationBlock() throws WrongSyntaxException {
        Token varToken = tokens.get(index++);
        if (varToken.getType() != KEYWORD) {
            throw new WrongSyntaxException("Declaration block must start with keyword!", varToken.getLineNumber());
        }
        if (!varToken.getLexeme().equals(VAR.toString())) {
            throw new WrongSyntaxException("Declaration block must start with '" + VAR.toString() + "' word!",
                    varToken.getLineNumber());
        }
        newLine();
        parseDeclaration();
    }

    private void parseDeclaration() throws WrongSyntaxException {
        if (tokens.get(index).getLexeme().equals(ENDVAR.toString())) {
            return;
        }

        Token typeToken = tokens.get(index++);
        if (typeToken.getType() == KEYWORD &&
                (typeToken.getLexeme().equals(Keywords.INT.toString()) ||
                        typeToken.getLexeme().equals(Keywords.DOUBLE.toString()) ||
                        typeToken.getLexeme().equals(Keywords.BOOLEAN.toString()))) {
            parseIdentifierList();
            newLine();
        } else {
            throw new WrongSyntaxException("Declaration must start with type of variable", typeToken.getLineNumber());
        }
        parseDeclaration();
    }

    private void parseIdentifierList() throws WrongSyntaxException {
        Token token = tokens.get(index++);
        if (token.getType() != TokenTypes.IDENTIFIER) {
            throw new WrongSyntaxException("Identifier expected", token.getLineNumber());
        }

        if (tokens.get(this.index).getType() == TokenTypes.PUNCTUATION) {
            this.index++;
            parseIdentifierList();
        }
    }
}
