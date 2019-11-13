package ruslan.syntactical;

import ruslan.exceptions.WrongSyntaxException;
import ruslan.token.Token;
import ruslan.token.TokenTypes;

import java.util.ArrayList;
import java.util.List;

public class SyntacticalAnalyzer {
    private List<Token> tokens;
    private List<Token> identifierList = new ArrayList<>();
    private int index = 0;

    public SyntacticalAnalyzer(List<Token> tokens) {
        tokens.add(new Token(999, "temp", 5));
        this.tokens = tokens;
    }

    public void parse() {
        try {
            parseProgramName();
            parseDeclarationBlock();
        } catch (WrongSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void parseProgramName() throws WrongSyntaxException {
        Token programKeywordToken = tokens.get(index++);
        if (programKeywordToken.getType() != TokenTypes.KEYWORD)
            throw new WrongSyntaxException("Program must start with keyword!");
        if (!programKeywordToken.getLexeme().equals("Program"))
            throw new WrongSyntaxException("Program must start with 'Program' word!");
        Token programNameToken = tokens.get(index++);
        if (programNameToken.getType() != TokenTypes.IDENTIFIER)
            throw new WrongSyntaxException("Program name must be identifier!");
    }

    private void parseDeclarationBlock() throws WrongSyntaxException {
        Token varToken = tokens.get(index++);
        if (varToken.getType() != TokenTypes.KEYWORD)
            throw new WrongSyntaxException("Declaration block must start with keyword!");
        if (!varToken.getLexeme().equals("var"))
            throw new WrongSyntaxException("Declaration block must start with 'var' word!");
        parseDeclaration();
    }

    private void parseDeclaration() throws WrongSyntaxException {
        if (tokens.get(index).getLexeme().equals("begin")) return;

        Token typeToken = tokens.get(index++);
        if (typeToken.getType() == TokenTypes.KEYWORD &&
                (typeToken.getLexeme().equals("int") ||
                        typeToken.getLexeme().equals("double") ||
                        typeToken.getLexeme().equals("boolean"))) {
            parseIdentifierList();
        } else {
            throw new WrongSyntaxException("Declaration must start with type of variable");
        }
        parseDeclaration();
    }

    private void parseIdentifierList() throws WrongSyntaxException {
        Token token = tokens.get(index++);
        System.out.println(token);
        if (token.getType() != TokenTypes.IDENTIFIER) throw new WrongSyntaxException("Identifier expected");
        if (identifierList.contains(token)) {
            throw new WrongSyntaxException("Variable '" + token.getLexeme() + "' already defined in the scope");
        } else {
            identifierList.add(token);
        }
        if (tokens.get(this.index).getType() == TokenTypes.PUNCTUATION) {
            this.index++;
            parseIdentifierList();
        }
    }
}
