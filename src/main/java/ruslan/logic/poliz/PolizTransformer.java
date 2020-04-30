package ruslan.logic.poliz;

import ruslan.token.Token;
import ruslan.token.TokenTypes;

import java.util.*;

public class PolizTransformer {

    private final Map<TokenTypes, Integer> priorityTable = new HashMap<>();
    private final Stack<Token> stack = new Stack<>();

    {
        priorityTable.put(TokenTypes.POW_OPERATION, 4);

        priorityTable.put(TokenTypes.MULTIPLICATION_OPERATION, 2);
        //priorityTable.put('/', 2);

        //priorityTable.put('+', 1);
        priorityTable.put(TokenTypes.ADDITION_OPERATION, 1);

        priorityTable.put(TokenTypes.U_MINUS, 1);

        priorityTable.put(TokenTypes.L_PARENTHESIS_OPERATION, -1);
        priorityTable.put(TokenTypes.R_PARENTHESIS_OPERATION, -1);
    }

    public List<Token> transform(List<Token> tokens) {
        // StringBuilder output = new StringBuilder();
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType() == TokenTypes.ADDITION_OPERATION
                    && token.getLexeme().equals("-")) {
                if (i == 0) {
                    token.setType(TokenTypes.U_MINUS);
                    token.setLexeme("UM");
                } else {
                    Token prevToken = tokens.get(i - 1);
                    TokenTypes prevTokenType = prevToken.getType();
                    if (prevTokenType == TokenTypes.ADDITION_OPERATION
                            || prevTokenType == TokenTypes.MULTIPLICATION_OPERATION
                            || prevTokenType == TokenTypes.POW_OPERATION
                            || prevTokenType == TokenTypes.L_PARENTHESIS_OPERATION) {
                        token.setType(TokenTypes.U_MINUS);
                        token.setLexeme("UM");
                    }
                }
            }
        }

        List<Token> result = new ArrayList<>();

        for (Token token : tokens) {
            TokenTypes type = token.getType();
            if (type == TokenTypes.INT || type == TokenTypes.DOUBLE || type == TokenTypes.IDENTIFIER) {
                result.add(token);
            } else {
                if (stack.isEmpty()) {
                    stack.push(token);
                } else {
                    if (type == TokenTypes.L_PARENTHESIS_OPERATION) {
                        stack.push(token);
                    } else {
                        if (type == TokenTypes.R_PARENTHESIS_OPERATION) {
                            while (true) {
                                Token peek = stack.peek();
                                if (peek.getType() == TokenTypes.L_PARENTHESIS_OPERATION) {
                                    stack.pop();
                                    break;
                                } else {
                                    result.add(stack.pop());
                                }
                            }
                        } else {
                            if (type != TokenTypes.U_MINUS) {
                                int inputPriority = priorityTable.get(token.getType());

                                while (!stack.empty() && inputPriority <= priorityTable.get(stack.peek().getType())) {
                                    Token pop = stack.pop();
                                    result.add(pop);
                                }
                            }
                            stack.push(token);
                        }
                    }
                }
            }
        }

        while (!stack.empty()) {
            result.add(stack.pop());
        }
        return result;
    }
}
