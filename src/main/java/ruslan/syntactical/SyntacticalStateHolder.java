package ruslan.syntactical;

import ruslan.Keywords;
import ruslan.lexical.State;
import ruslan.token.TokenTypes;

import java.util.HashMap;
import java.util.Map;

public class SyntacticalStateHolder {

    static private Map<State, Integer> states = new HashMap<>();

    static public Map<State, Integer> getStates() {
        return states;
    }

    static {
        //Program initialization parsing
        states.put(new State(1, Keywords.PROGRAM.toString()), 2);
        states.put(new State(1, "other"), 101);
        states.put(new State(2, TokenTypes.IDENTIFIER.toString()), 3);
        states.put(new State(2, "other"), 102);
        states.put(new State(3, TokenTypes.NEW_LINE.toString()), 4);
        states.put(new State(3, "other"), 103);
    }
}
