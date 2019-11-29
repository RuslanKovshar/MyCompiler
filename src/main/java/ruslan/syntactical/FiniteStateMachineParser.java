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
    private int state = INITIAL_STATE;
    private List<Token> tokens;
    private Set<Integer> errorStates = ErrorHolder.getErrors().keySet();
    private Set<Integer> finalStates = new HashSet<>(errorStates);

    public FiniteStateMachineParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private void analyze() throws WrongSyntaxException {
        for (Token token : tokens) {
            state = getNextState(state, token);
            if (finalStates.contains(state)) {
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
            nextState = SyntacticalStateHolder.getStates().get(new State(state, "other"));
        }
        return nextState;
    }
}
