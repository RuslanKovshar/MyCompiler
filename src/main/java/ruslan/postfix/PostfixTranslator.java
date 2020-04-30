package ruslan.postfix;

import ruslan.token.Token;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PostfixTranslator {

    public void translateToPostfix(List<Token> tokens) {
        List<Token> statementsTokens = getStatementsTokens(tokens);
        statementsTokens.forEach(System.out::println);
    }

    private List<Token> getStatementsTokens(List<Token> tokens) {
        Iterator<Token> iterator = tokens.iterator();
        while (iterator.next().getLexeme().equals("begin")) {
            if (iterator.next().getLexeme().equals("begin")) break;
        }


        List<Token> result = new ArrayList<>();
        while (true) {
            Token next = iterator.next();
            if (next.getLexeme().equals("endbegin")) break;
            result.add(next);
        }

        return result;
    }
}
