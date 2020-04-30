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

    public List<Token> translateToPostfix(List<Token> tokens) {
        List<Token> statementsTokens = getStatementsTokens(tokens);
        List<Token> result = new ArrayList<>();

        for (int i = 0; i < statementsTokens.size(); i++) {
            Token token = statementsTokens.get(i);

            /*Assign translation*/
            if (token.getType() == TokenTypes.IDENTIFIER) {
                result.add(token);
                Token assignToken = statementsTokens.get(++i);
                i++;

                List<Token> performTokens = new ArrayList<>();
                i = getPerformTokens(statementsTokens, i, performTokens, TokenTypes.NEW_LINE);

                result.addAll(polizTransformer.transform(performTokens));
                result.add(assignToken);
            }

            /*Write translation*/
            if (token.getLexeme().equals(Keywords.WRITE.toString())) {
                i += 2;

                List<Token> performTokens = new ArrayList<>();
                i = getPerformTokens(statementsTokens, i, performTokens, TokenTypes.R_PARENTHESIS_OPERATION);

                result.addAll(polizTransformer.transform(performTokens));
                result.add(outToken);
            }

            /*Read translation*/
            if (token.getLexeme().equals(Keywords.READ.toString())) {
                i += 2;

                while (statementsTokens.get(i).getType() != TokenTypes.R_PARENTHESIS_OPERATION) {
                    Token identToken = statementsTokens.get(i);
                    if (identToken.getType() != TokenTypes.PUNCTUATION) {
                        result.add(identToken);
                        result.add(inputToken);
                    }
                    i++;
                }

            }
        }


        result.forEach(System.out::println);

        return result;
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
