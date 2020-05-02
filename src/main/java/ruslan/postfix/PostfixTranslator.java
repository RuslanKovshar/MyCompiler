package ruslan.postfix;

import ruslan.Keywords;
import ruslan.logic.poliz.PolizTransformer;
import ruslan.token.Token;
import ruslan.token.TokenTypes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PostfixTranslator {

    private final PolizTransformer polizTransformer = new PolizTransformer();
    private final Token outToken = new Token(-1, "OUT", -1);
    private final Token inputToken = new Token(-2, "INPUT", -2);
    private final List<Token> result = new ArrayList<>();
    private final List<Token> statementsTokens;
    private int index;

    public PostfixTranslator(List<Token> tokens) {
        this.statementsTokens = getStatementsTokens(tokens);
    }

    public List<Token> translateToPostfix() {
        for (index = 0; index < statementsTokens.size(); index++) {
            translateToken(statementsTokens.get(index));
        }

        result.forEach(System.out::println);

        return result;
    }

    private void translateToken(Token token) {
        /*Assign translation*/
        if (token.getType() == TokenTypes.IDENTIFIER) {
            result.add(token);
            Token assignToken = statementsTokens.get(++index);
            index++;

            List<Token> performTokens = new ArrayList<>();
            index = getPerformTokens(statementsTokens, index, performTokens, TokenTypes.NEW_LINE);

            result.addAll(polizTransformer.transform(performTokens));
            result.add(assignToken);
        }

        /*Write translation*/
        if (token.getLexeme().equals(Keywords.WRITE.toString())) {
            index += 2;

            List<Token> performTokens = new ArrayList<>();
            index = getPerformTokens(statementsTokens, index, performTokens, TokenTypes.R_PARENTHESIS_OPERATION);

            result.addAll(polizTransformer.transform(performTokens));
            result.add(outToken);
        }

        /*Read translation*/
        if (token.getLexeme().equals(Keywords.READ.toString())) {
            index += 2;

            while (statementsTokens.get(index).getType() != TokenTypes.R_PARENTHESIS_OPERATION) {
                Token identToken = statementsTokens.get(index);
                if (identToken.getType() != TokenTypes.PUNCTUATION) {
                    result.add(identToken);
                    result.add(inputToken);
                }
                index++;
            }

        }
    }

    private int getPerformTokens(List<Token> statementsTokens, int i, List<Token> performTokens, TokenTypes type) {
        while (statementsTokens.get(i).getType() != type) {
            performTokens.add(statementsTokens.get(i));
            i++;
        }
        return i;
    }

    private List<Token> getStatementsTokens(List<Token> tokens) {
        Iterator<Token> iterator = tokens.iterator();
        while (true) {
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
