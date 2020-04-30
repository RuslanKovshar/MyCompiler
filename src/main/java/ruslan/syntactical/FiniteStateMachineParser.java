package ruslan.syntactical;

import org.apache.log4j.Logger;
import ruslan.exceptions.WrongSyntaxException;
import ruslan.lexical.State;
import ruslan.token.Token;
import ruslan.token.TokenTypes;

import java.util.*;

public class FiniteStateMachineParser implements Parser {
    private static final Logger log = Logger.getLogger(FiniteStateMachineParser.class);
    private final int INITIAL_STATE = 1;
    private final int EXPRESSION_START_STATE = 21;
    private final int EXPRESSION_END_STATE = 23;
    private int state = INITIAL_STATE;
    private List<Token> tokens;
    private Set<Integer> errorStates = ErrorHolder.getErrors().keySet();
    private Stack<Token> parenthesis = new Stack<>();
    private Stack<Token> cycleStack = new Stack<>();
    private Stack<Token> ifStack = new Stack<>();
    private Stack<Token> beginStack = new Stack<>();

    public FiniteStateMachineParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    @Override
    public void parse() throws WrongSyntaxException {
        int i;
        for (i = 0; i < tokens.size(); i++) {
            state = getNextState(state, tokens.get(i));
            if (state == EXPRESSION_START_STATE || state == 22 || state == 31 || state == 42) {
                if (tokens.get(i).getType() == TokenTypes.L_PARENTHESIS_OPERATION) {
                    parenthesis.push(tokens.get(i));
                }
            }
            if (state == 12) {
                beginStack.push(tokens.get(i));
            }
            if (state == 70) {
                beginStack.pop();
                break;
            }
            if (state == 40) {
                cycleStack.push(tokens.get(i));
            }
            if (state == 49) {
                if (!cycleStack.empty()) {
                    cycleStack.pop();
                    state = 13;
                } else {
                    throw new WrongSyntaxException("Cycle expected", tokens.get(i).getLineNumber());
                }
            }
            if (state == 50) {
                ifStack.push(tokens.get(i));
                state = 42;
            }
            if (state == 55) {
                if (!ifStack.empty()) {
                    state = 13;
                } else {
                    throw new WrongSyntaxException("'else' without 'if'", tokens.get(i).getLineNumber());
                }
            }
            if (state == 59) {
                if (!ifStack.empty()) {
                    ifStack.pop();
                    state = 13;
                } else {
                    throw new WrongSyntaxException("If expected", tokens.get(i).getLineNumber());
                }
            }
            if (state == EXPRESSION_END_STATE) {
                if (!parenthesis.empty()) {
                    TokenTypes type = tokens.get(i + 1).getType();
                    if (type != TokenTypes.ADDITION_OPERATION &&
                            type != TokenTypes.MULTIPLICATION_OPERATION &&
                            type != TokenTypes.POW_OPERATION &&
                            type != TokenTypes.RELATIVE_OPERATION) {
                        parenthesis.pop();
                        state = 25;
                    }
                } else {
                    if (tokens.get(i + 1).getType() == TokenTypes.R_PARENTHESIS_OPERATION) {
                        state = 32;
                    }
                }
            }

            if (state == 32) {
                if (!parenthesis.empty()) {
                    parenthesis.pop();
                    state = 32;
                }
            }

            if (errorStates.contains(state)) {
                throw new WrongSyntaxException(ErrorHolder.getErrors().get(state), tokens.get(i).getLineNumber());
            }
        }
        for (int j = i + 1; j < tokens.size(); j++) {
            if (tokens.get(j).getType() != TokenTypes.NEW_LINE) {
                throw new WrongSyntaxException("Statements out of program", tokens.get(tokens.size() - 1).getLineNumber());
            }
        }
        if (!ifStack.empty()) {
            throw new WrongSyntaxException("Non closed if", tokens.get(tokens.size() - 1).getLineNumber());
        }
        if (!cycleStack.empty()) {
            throw new WrongSyntaxException("Non closed cycle", tokens.get(tokens.size() - 1).getLineNumber());
        }
        if (!beginStack.empty()) {
            throw new WrongSyntaxException("Non closed program", tokens.get(tokens.size() - 1).getLineNumber());
        }
    }

    private int getNextState(int state, Token token) {
        String type;
        if (token.getType() == TokenTypes.KEYWORD) {
            type = token.getLexeme();
        } else {
            type = token.getType().toString();
        }

        State tokenState = new State(state, type);
        System.out.println(tokenState);
        int nextState;
        try {
            nextState = SyntacticalStateHolder.getStates().get(tokenState);
        } catch (Exception e) {
            nextState = SyntacticalStateHolder.getStates().get(new State(state, TokenTypes.OTHER.toString()));
        }
        return nextState;
    }
}