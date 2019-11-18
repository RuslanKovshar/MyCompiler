package ruslan.syntactical;

import org.apache.log4j.Logger;
import ruslan.Keywords;
import ruslan.exceptions.WrongSyntaxException;
import ruslan.token.Token;
import ruslan.token.TokenTypes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ruslan.Keywords.*;
import static ruslan.token.TokenTypes.KEYWORD;

public class SyntacticalAnalyzer {
    private static final Logger log = Logger.getLogger(SyntacticalAnalyzer.class);
    private List<Token> tokens;
    private Set<Token> identifierList = new HashSet<>();
    private int index = 0;

    public SyntacticalAnalyzer(List<Token> tokens) {
        //tokens.add(new Token(0, "", 1));
        this.tokens = tokens;
    }

    public void parse() {
        try {
            checkBlocks();
            parseProgramName();
            parseDeclarationBlock();
            parseMainBlock();
        } catch (WrongSyntaxException e) {
            log.error(e.getErrorMessage());
        }
    }

    private void checkBlocks() throws WrongSyntaxException {
        List<Keywords> keywords = tokens.stream()
                .filter(token -> token.getType() == KEYWORD)
                .map(token -> valueOf(token.getLexeme().toUpperCase()))
                .collect(Collectors.toList());
        checkBlockToken(keywords, VAR);
        checkBlockToken(keywords, ENDVAR);
        checkBlockToken(keywords, BEGIN);
        checkBlockToken(keywords, ENDBEGIN);
    }

    private void checkBlockToken(List<Keywords> keywords, Keywords word) throws WrongSyntaxException {
        keywords.stream()
                .filter(keyword -> keyword == word)
                .findAny()
                .orElseThrow(() -> new WrongSyntaxException("Error on blocks",1));
    }

    private void parseMainBlock() throws WrongSyntaxException {
        index++;
        newLine();
        Token beginToken = tokens.get(index++);
        if (!(beginToken.getType() == KEYWORD && beginToken.getLexeme().equals(BEGIN.toString()))) {
            throw new WrongSyntaxException("'" + BEGIN.toString() + "' expected!", beginToken.getLineNumber());
        }
        newLine();
        parseStatementList();


        Token endToken = tokens.get(index - 1);
        if (endToken.getType() != KEYWORD && !endToken.getLexeme().equals(BEGIN.toString())) {
            throw new WrongSyntaxException("'" + ENDBEGIN.toString() + "' expected!", endToken.getLineNumber());
        }
    }

    private void parseStatementList() throws WrongSyntaxException {
        Token token = tokens.get(index++);
        System.out.println(token);

        switch (token.getType()) {
            case IDENTIFIER:
                parseAssign();
                parseStatementList();
                break;
            case KEYWORD:
                parseStatementKeyword(token);
                break;
        }
    }

    private void parseStatementKeyword(Token token) throws WrongSyntaxException {
        if (!isEndToken(token)) {
            switch (Keywords.valueOf(token.getLexeme().toUpperCase())) {
                case READ:
                    parseRead();
                    break;
                case WRITE:
                    parseWrite();
                    break;
                case DO:
                    parseCycle();
                    break;
                case IF:
                    parseIf();
                    break;
                default:
                    throw new WrongSyntaxException("Wrong keyword", token.getLineNumber());
            }
            parseStatementList();
        }
    }

    private void parseIf() throws WrongSyntaxException {
        System.out.println("If");
        newLine();
    }

    private void parseCycle() throws WrongSyntaxException {
        System.out.println("do");
        newLine();
    }

    private boolean isEndToken(Token token) {
        return token.getType() == KEYWORD && token.getLexeme().equals(ENDBEGIN.toString());
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
        newLine();
    }

    private void parseWrite() throws WrongSyntaxException {
        System.out.println("write");
        newLine();
    }

    private void parseAssign() throws WrongSyntaxException {
        System.out.println("assign");
        newLine();
    }

    private void parseStatement() throws WrongSyntaxException {

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
                (typeToken.getLexeme().equals(INT.toString()) ||
                        typeToken.getLexeme().equals(DOUBLE.toString()) ||
                        typeToken.getLexeme().equals(BOOLEAN.toString()))) {
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

        /*if (!identifierList.add(token)) {
            throw new WrongSyntaxException("Variable '" + token.getLexeme() + "' already defined in the scope",
                    token.getLineNumber());
        }*/

        if (tokens.get(this.index).getType() == TokenTypes.PUNCTUATION) {
            this.index++;
            parseIdentifierList();
        }
    }
}
