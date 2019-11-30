package ruslan.syntactical;

import ruslan.Keywords;
import ruslan.lexical.State;
import ruslan.token.TokenTypes;

import java.util.HashMap;
import java.util.Map;

class SyntacticalStateHolder {

    static private Map<State, Integer> states = new HashMap<>();

    static Map<State, Integer> getStates() {
        return states;
    }

    static {
        /*Program initialization parsing*/
        states.put(new State(1, Keywords.PROGRAM.toString()), 2);
        states.put(new State(1, TokenTypes.OTHER.toString()), 101);
        states.put(new State(2, TokenTypes.IDENTIFIER.toString()), 3);
        states.put(new State(2, TokenTypes.OTHER.toString()), 102);
        states.put(new State(3, TokenTypes.NEW_LINE.toString()), 4);
        states.put(new State(4, TokenTypes.NEW_LINE.toString()), 4);
        states.put(new State(3, TokenTypes.OTHER.toString()), 505);

        /*Declaration block parsing*/
        states.put(new State(4, Keywords.VAR.toString()), 5);
        states.put(new State(4, TokenTypes.OTHER.toString()), 103);
        states.put(new State(5, TokenTypes.NEW_LINE.toString()), 6);
        states.put(new State(6, TokenTypes.NEW_LINE.toString()), 6);
        states.put(new State(5, TokenTypes.OTHER.toString()), 505);

        /*Declaration*/
        states.put(new State(6, Keywords.INT.toString()), 7);
        states.put(new State(6, Keywords.DOUBLE.toString()), 7);
        states.put(new State(6, Keywords.BOOLEAN.toString()), 7);
        states.put(new State(6, TokenTypes.OTHER.toString()), 104);

        /*Identifier list*/
        states.put(new State(7, TokenTypes.IDENTIFIER.toString()), 8);
        states.put(new State(7, TokenTypes.OTHER.toString()), 105);
        states.put(new State(8, TokenTypes.NEW_LINE.toString()), 9);
        states.put(new State(9, TokenTypes.NEW_LINE.toString()), 9);
        states.put(new State(8, TokenTypes.PUNCTUATION.toString()), 7);
        states.put(new State(8, TokenTypes.OTHER.toString()), 505);
        states.put(new State(9, Keywords.INT.toString()), 7);
        states.put(new State(9, Keywords.DOUBLE.toString()), 7);
        states.put(new State(9, Keywords.BOOLEAN.toString()), 7);

        /*End declaration block*/
        states.put(new State(9, Keywords.ENDVAR.toString()), 10);
        states.put(new State(9, TokenTypes.OTHER.toString()), 106);
        states.put(new State(10, TokenTypes.NEW_LINE.toString()), 11);
        states.put(new State(11, TokenTypes.NEW_LINE.toString()), 11);
        states.put(new State(10, TokenTypes.OTHER.toString()), 505);

        /*main block*/
        states.put(new State(11, Keywords.BEGIN.toString()), 12);
        states.put(new State(12, TokenTypes.NEW_LINE.toString()), 13);
        states.put(new State(13, TokenTypes.NEW_LINE.toString()), 13);

        /*read(<identifier list>)*/
        states.put(new State(13, Keywords.READ.toString()), 14);
        states.put(new State(14, TokenTypes.L_PARENTHESIS_OPERATION.toString()), 15);
        states.put(new State(14, TokenTypes.OTHER.toString()), 107);
        states.put(new State(15, TokenTypes.IDENTIFIER.toString()), 16);
        states.put(new State(15, TokenTypes.OTHER.toString()), 105);
        states.put(new State(16, TokenTypes.PUNCTUATION.toString()), 15);
        states.put(new State(16, TokenTypes.R_PARENTHESIS_OPERATION.toString()), 17);
        states.put(new State(16, TokenTypes.OTHER.toString()), 108);
        states.put(new State(17, TokenTypes.NEW_LINE.toString()), 13);
        states.put(new State(13, TokenTypes.NEW_LINE.toString()), 13);

        /*assign*/
        states.put(new State(13, TokenTypes.IDENTIFIER.toString()), 20);
        states.put(new State(20, TokenTypes.ASSIGN_OPERATION.toString()), 21);
        states.put(new State(20, TokenTypes.OTHER.toString()), 109);
        states.put(new State(21, TokenTypes.OTHER.toString()), 111);

        /*statement*/
        states.put(new State(21, TokenTypes.ADDITION_OPERATION.toString()), 22);
        states.put(new State(22, TokenTypes.INT.toString()), 23);
        states.put(new State(22, TokenTypes.DOUBLE.toString()), 23);
        states.put(new State(22, TokenTypes.IDENTIFIER.toString()), 23);
        states.put(new State(22, TokenTypes.OTHER.toString()), 110);
        states.put(new State(21, TokenTypes.INT.toString()), 23);
        states.put(new State(21, TokenTypes.DOUBLE.toString()), 23);
        states.put(new State(21, TokenTypes.BOOLEAN.toString()), 23);
        states.put(new State(21, TokenTypes.IDENTIFIER.toString()), 23);
        states.put(new State(21, TokenTypes.L_PARENTHESIS_OPERATION.toString()), 21);

        states.put(new State(70, TokenTypes.R_PARENTHESIS_OPERATION.toString()), 23);
        states.put(new State(70, TokenTypes.OTHER.toString()), 108);

        states.put(new State(23, TokenTypes.NEW_LINE.toString()), 13);
        states.put(new State(13, TokenTypes.NEW_LINE.toString()), 13);
    }
}
