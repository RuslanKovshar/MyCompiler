package ruslan.syntactical;

import org.apache.log4j.Logger;
import ruslan.exceptions.WrongSyntaxException;
import ruslan.token.Token;
import ruslan.token.TokenTypes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ruslan.Keywords.*;

public class SyntacticalAnalyzer {
    private static final Logger log = Logger.getLogger(SyntacticalAnalyzer.class);
    private List<Token> tokens;
    private Set<Token> identifierList = new HashSet<>();
    private int index = 0;

    public SyntacticalAnalyzer(List<Token> tokens) {
        tokens.add(new Token(0, "", 1));
        this.tokens = tokens;
    }

    public void parse() {
        try {
            parseProgramName();
            parseDeclarationBlock();
            parseMainBlock();
        } catch (WrongSyntaxException e) {
            log.error(e.getErrorMessage());
        }
    }

    private void parseMainBlock() throws WrongSyntaxException {
        index++;
        newLine();
        Token beginToken = tokens.get(index++);
        if (beginToken.getType() != TokenTypes.KEYWORD && !beginToken.getLexeme().equals(BEGIN.toString())) {
            throw new WrongSyntaxException("'" + BEGIN.toString() + "' expected!", beginToken.getLineNumber());
        }
        newLine();
        parseStatementList();


        Token endToken = tokens.get(index);
        if (endToken.getType() != TokenTypes.KEYWORD && !endToken.getLexeme().equals(BEGIN.toString())) {
            throw new WrongSyntaxException("'" + ENDBEGIN.toString() + "' expected!", endToken.getLineNumber());
        }
    }

    private void parseStatementList() throws WrongSyntaxException {
        Token token = tokens.get(index++);
        System.out.println(token);


        switch (token.getType()) {
            case IDENTIFIER:
                System.out.println("sout");
                break;
            default: return;
        }
        parseStatementList();
    }

    private void parseStatement() throws WrongSyntaxException {

    }

    private void parseProgramName() throws WrongSyntaxException {
        Token programKeywordToken = tokens.get(index++);
        if (programKeywordToken.getType() != TokenTypes.KEYWORD) {
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
        if (varToken.getType() != TokenTypes.KEYWORD) {
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
        if (tokens.get(index).getLexeme().equals(ENDVAR.toString())){
            return;
        }

        Token typeToken = tokens.get(index++);
        if (typeToken.getType() == TokenTypes.KEYWORD &&
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
        if (!identifierList.add(token)) {
            throw new WrongSyntaxException("Variable '" + token.getLexeme() + "' already defined in the scope",
                    token.getLineNumber());
        }
        if (tokens.get(this.index).getType() == TokenTypes.PUNCTUATION) {
            this.index++;
            parseIdentifierList();
        }
    }
}
