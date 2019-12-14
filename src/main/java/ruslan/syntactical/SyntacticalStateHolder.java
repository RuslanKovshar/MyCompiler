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
        states.put(new State(11, TokenTypes.OTHER.toString()), 114);
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
        states.put(new State(17, TokenTypes.OTHER.toString()), 505);
        states.put(new State(13, TokenTypes.NEW_LINE.toString()), 13);

        /*assign*/
        states.put(new State(13, TokenTypes.IDENTIFIER.toString()), 20);
        states.put(new State(20, TokenTypes.ASSIGN_OPERATION.toString()), 21);
        states.put(new State(20, TokenTypes.OTHER.toString()), 109);
        states.put(new State(21, TokenTypes.OTHER.toString()), 111);

        /*expression*/
        states.put(new State(21, TokenTypes.ADDITION_OPERATION.toString()), 22);
        states.put(new State(22, TokenTypes.INT.toString()), 23);
        states.put(new State(22, TokenTypes.DOUBLE.toString()), 23);
        states.put(new State(22, TokenTypes.IDENTIFIER.toString()), 23);
        states.put(new State(22, TokenTypes.OTHER.toString()), 110);
        states.put(new State(21, TokenTypes.INT.toString()), 23);
        states.put(new State(21, TokenTypes.DOUBLE.toString()), 23);
        states.put(new State(21, TokenTypes.BOOLEAN.toString()),23);
        states.put(new State(21, TokenTypes.IDENTIFIER.toString()), 23);
        states.put(new State(21, TokenTypes.L_PARENTHESIS_OPERATION.toString()), 21);
        states.put(new State(22, TokenTypes.L_PARENTHESIS_OPERATION.toString()), 22);

        states.put(new State(23, TokenTypes.POW_OPERATION.toString()), 21);
        states.put(new State(23, TokenTypes.MULTIPLICATION_OPERATION.toString()), 21);
        states.put(new State(23, TokenTypes.ADDITION_OPERATION.toString()), 21);

        states.put(new State(23, TokenTypes.RELATIVE_OPERATION.toString()), 21);

        states.put(new State(25, TokenTypes.R_PARENTHESIS_OPERATION.toString()), 23);
        states.put(new State(25, TokenTypes.OTHER.toString()), 108);

        states.put(new State(23, TokenTypes.OTHER.toString()), 112);
        states.put(new State(23, TokenTypes.NEW_LINE.toString()), 13);
        states.put(new State(13, TokenTypes.NEW_LINE.toString()), 13);

        /*write*/
        states.put(new State(13, Keywords.WRITE.toString()), 30);
        states.put(new State(30, TokenTypes.L_PARENTHESIS_OPERATION.toString()), 31);
        states.put(new State(30, TokenTypes.OTHER.toString()), 107);
        states.put(new State(31, TokenTypes.IDENTIFIER.toString()), 32);
        states.put(new State(31, TokenTypes.OTHER.toString()), 105);
        states.put(new State(32, TokenTypes.PUNCTUATION.toString()), 31);

        states.put(new State(31, TokenTypes.DOUBLE.toString()), 23);
        states.put(new State(31, TokenTypes.INT.toString()), 23);
        states.put(new State(31, TokenTypes.ADDITION_OPERATION.toString()), 22);
        states.put(new State(31, TokenTypes.L_PARENTHESIS_OPERATION.toString()), 21);

        states.put(new State(32, TokenTypes.POW_OPERATION.toString()), 21);
        states.put(new State(32, TokenTypes.MULTIPLICATION_OPERATION.toString()), 21);
        states.put(new State(32, TokenTypes.ADDITION_OPERATION.toString()), 21);
        states.put(new State(32, TokenTypes.RELATIVE_OPERATION.toString()), 21);

        states.put(new State(32, TokenTypes.R_PARENTHESIS_OPERATION.toString()), 33);
        states.put(new State(32, TokenTypes.OTHER.toString()), 108);
        states.put(new State(33, TokenTypes.NEW_LINE.toString()), 13);
        states.put(new State(13, TokenTypes.NEW_LINE.toString()), 13);

        /*cycle*/
        states.put(new State(13, Keywords.DO.toString()), 40);
        states.put(new State(40, TokenTypes.OTHER.toString()), 113);
        states.put(new State(40, Keywords.WHILE.toString()), 41);
        states.put(new State(41, TokenTypes.L_PARENTHESIS_OPERATION.toString()), 42);
        states.put(new State(41, TokenTypes.OTHER.toString()), 107);

        states.put(new State(42, TokenTypes.DOUBLE.toString()), 46);
        states.put(new State(42, TokenTypes.INT.toString()), 46);
        states.put(new State(42, TokenTypes.IDENTIFIER.toString()), 46);

        states.put(new State(46, TokenTypes.RELATIVE_OPERATION.toString()), 21);

        states.put(new State(42, TokenTypes.BOOLEAN.toString()), 32);

        states.put(new State(42, TokenTypes.ADDITION_OPERATION.toString()), 22);
        states.put(new State(42, TokenTypes.L_PARENTHESIS_OPERATION.toString()), 21);

        states.put(new State(13, Keywords.ENDDO.toString()), 49);

        /*if*/
        states.put(new State(13, Keywords.IF.toString()), 50);

        states.put(new State(13, Keywords.ELSE.toString()), 55);
        states.put(new State(50, TokenTypes.OTHER.toString()), 42);
        states.put(new State(13, Keywords.ENDIF.toString()), 59);


        states.put(new State(13, TokenTypes.OTHER.toString()), 112);

        states.put(new State(13, Keywords.ENDBEGIN.toString()), 70);
    }
}
