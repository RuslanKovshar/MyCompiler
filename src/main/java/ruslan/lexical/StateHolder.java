package ruslan.lexical;

import java.util.HashMap;
import java.util.Map;

class StateHolder {
    static Map<State, Integer> stateTransitionFunction = new HashMap<>();

    static {
        stateTransitionFunction.put(new State(0, "Letter"), 1);
        stateTransitionFunction.put(new State(1, "Letter"), 1);
        stateTransitionFunction.put(new State(1, "Digit"), 1);
        stateTransitionFunction.put(new State(1, "Bottom"), 1);
        stateTransitionFunction.put(new State(1, "other"), 2);

        stateTransitionFunction.put(new State(0, "Digit"), 4);
        stateTransitionFunction.put(new State(4, "Digit"), 4);
        stateTransitionFunction.put(new State(4, "Letter"), 333);
        stateTransitionFunction.put(new State(5, "Letter"), 333);
        stateTransitionFunction.put(new State(333, "-"), 334);
        stateTransitionFunction.put(new State(333, "+"), 334);
        stateTransitionFunction.put(new State(334, "Digit"), 335);
        stateTransitionFunction.put(new State(335, "Digit"), 335);
        stateTransitionFunction.put(new State(335, "other"), 6);

        stateTransitionFunction.put(new State(0, "dot"), 5);
        stateTransitionFunction.put(new State(4, "dot"), 5);
        stateTransitionFunction.put(new State(4, "other"), 9);
        stateTransitionFunction.put(new State(5, "Digit"), 5);
        stateTransitionFunction.put(new State(5, "other"), 6);

        stateTransitionFunction.put(new State(5, "dot"), 77);
        stateTransitionFunction.put(new State(77, "Digit"), 77);
        stateTransitionFunction.put(new State(77, "dot"), 77);
        stateTransitionFunction.put(new State(77, "other"), 103);

        stateTransitionFunction.put(new State(0, "!"), 33);
        stateTransitionFunction.put(new State(33, "="), 15);
        stateTransitionFunction.put(new State(33, "other"), 102);

        stateTransitionFunction.put(new State(0, "<"), 21);
        stateTransitionFunction.put(new State(21, "="), 15);// <= rel_op
        stateTransitionFunction.put(new State(21, "other"), 55);// < rel_op

        stateTransitionFunction.put(new State(0, ">"), 22);
        stateTransitionFunction.put(new State(22, "="), 15);// >= rel_op
        stateTransitionFunction.put(new State(22, "other"), 55);// > rel_op

        stateTransitionFunction.put(new State(0, "="), 20);
        stateTransitionFunction.put(new State(20, "="), 15);//== rel_op
        stateTransitionFunction.put(new State(20, "other"), 55);// = assign_op


        stateTransitionFunction.put(new State(0, "ws"), 0);
        stateTransitionFunction.put(new State(0, "nl"), 13);


        stateTransitionFunction.put(new State(0, "*"), 44);
        stateTransitionFunction.put(new State(44, "*"), 45);
        stateTransitionFunction.put(new State(44, "other"), 56);

        stateTransitionFunction.put(new State(0, "+"), 14);
        stateTransitionFunction.put(new State(0, "-"), 14);
        stateTransitionFunction.put(new State(0, "/"), 14);
        stateTransitionFunction.put(new State(0, "("), 14);
        stateTransitionFunction.put(new State(0, ")"), 14);

        stateTransitionFunction.put(new State(0, "coma"), 14);

        stateTransitionFunction.put(new State(0, "other"), 101);
    }
}
