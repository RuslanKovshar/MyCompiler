package ruslan.postfix;

import ruslan.Keywords;
import ruslan.logic.poliz.PolizTransformer;
import ruslan.token.Token;
import ruslan.token.TokenTypes;

import java.util.*;

public class PostfixTranslator {

    private final PolizTransformer polizTransformer = new PolizTransformer();
    private final Map<String, Variable> variableMap = new HashMap<>();
    private final Token outToken = new Token(-1, "OUT", -1);
    private final Token inputToken = new Token(-2, "INPUT", -2);
    private final Token jfToken = new Token(-8, "JF", -8);
    private final Token jmpToken = new Token(-8, "JMP", -8);
    private final List<Token> result = new ArrayList<>();
    private final List<Token> labels = new ArrayList<>();
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

        /*If statement translation*/
        if (token.getLexeme().equals(Keywords.IF.toString())) {
            index++;

            List<Token> performTokens = new ArrayList<>();
            index = getPerformTokens(statementsTokens, index, performTokens, TokenTypes.NEW_LINE);
            result.addAll(polizTransformer.transform(performTokens));

            /* label creation */
            Token openLabel = new Token(-7, "m" + (labels.size() + 1), -7);
            labels.add(openLabel);

            result.add(openLabel);
            result.add(jfToken);

            Token closeLabel = new Token(-7, "m" + (labels.size() + 1), -7);

            while (!statementsTokens.get(index).getLexeme().equals(Keywords.ENDIF.toString())) {
                Token currentToken = statementsTokens.get(index);
                if (currentToken.getLexeme().equals(Keywords.ELSE.toString())) {
                    labels.add(closeLabel);

                    result.add(closeLabel);
                    result.add(jmpToken);
                    result.add(openLabel);
                } else {
                    translateToken(currentToken);
                }
                index++;
            }

            labels.add(closeLabel);
            result.add(closeLabel);
        }

        /*do while translation*/
        if (token.getLexeme().equals(Keywords.DO.toString())) {
            index += 3;

            /* label creation */
            Token openLabel = new Token(-7, "m" + (labels.size() + 1), -7);
            labels.add(openLabel);

            result.add(openLabel);

            Token closeLabel = new Token(-7, "m" + (labels.size() + 1), -7);

            /* condition parsing */
            List<Token> performTokens = new ArrayList<>();
            index = getPerformTokens(statementsTokens, index, performTokens, TokenTypes.R_PARENTHESIS_OPERATION);
            result.addAll(polizTransformer.transform(performTokens));

            labels.add(closeLabel);
            result.add(closeLabel);
            result.add(jfToken);

            while (!statementsTokens.get(index).getLexeme().equals(Keywords.ENDDO.toString())) {
                Token currentToken = statementsTokens.get(index);
                translateToken(currentToken);
                index++;
            }

            result.add(openLabel);
            result.add(jmpToken);
            result.add(closeLabel);
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

        String type = null;
        while (iterator.hasNext()) {
            Token next = iterator.next();
            String lexeme = next.getLexeme();
            if (lexeme.equals(Keywords.BEGIN.toString())) break;
            if (lexeme.equals(Keywords.INT.toString())
                    || lexeme.equals(Keywords.DOUBLE.toString())
                    || lexeme.equals(Keywords.BOOLEAN.toString())) {
                type = lexeme;
            }
            if (next.getType() == TokenTypes.IDENTIFIER && type != null) {
                variableMap.put(lexeme, new Variable(null, type));
            }
        }

        List<Token> result = new ArrayList<>();
        while (iterator.hasNext()) {
            Token next = iterator.next();
            if (next.getLexeme().equals(Keywords.ENDBEGIN.toString())) break;
            result.add(next);
        }

        return result;
    }

    public Map<String, Variable> getVariableMap() {
        return variableMap;
    }
}
