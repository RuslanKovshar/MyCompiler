package ruslan.syntactical;

import ruslan.token.Token;
import ruslan.exceptions.WrongSyntaxException;

import java.util.List;

public class SyntacticalAnalyzer {
    private List<Token> tokens;
    private int index = 0;

    public SyntacticalAnalyzer(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void parse() {
        tokens.forEach(System.out::println);
    }

    public void parseProgramName() throws WrongSyntaxException {
        Token token = tokens.get(0);
        //token.getToken().equals();
    }
}
