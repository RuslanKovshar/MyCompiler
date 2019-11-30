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

    public FiniteStateMachineParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private void analyze() throws WrongSyntaxException {
        for (Token token : tokens) {
            state = getNextState(state, token);
            if (state == EXPRESSION_START_STATE) {
                if (token.getType() == TokenTypes.L_PARENTHESIS_OPERATION) {
                    parenthesis.push(token);
                }
            }
            if (state == EXPRESSION_END_STATE) {
                System.out.println(parenthesis);
                if (!parenthesis.empty()) {
                    parenthesis.pop();
                    state = 70;
                }
            }
            if (errorStates.contains(state)) {
                throw new WrongSyntaxException(ErrorHolder.getErrors().get(state), token.getLineNumber());
            }
        }
    }

    @Override
    public void parse() {
        try {
            analyze();
        } catch (WrongSyntaxException e) {
            log.error(e.getErrorMessage());
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
