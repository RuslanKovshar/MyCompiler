package ruslan.token;

import ruslan.Keywords;

import java.util.HashMap;
import java.util.Map;

import static ruslan.Keywords.*;
import static ruslan.token.TokenTypes.*;

public class TokenHolder {

    public static Map<String, TokenTypes> tableOfLanguageTokens = new HashMap<>();
    public static Map<Integer, TokenTypes> tableIdentFloatInt = new HashMap<>();

    static {
        tableOfLanguageTokens.put(PROGRAM.toString(), KEYWORD);
        tableOfLanguageTokens.put(BEGIN.toString(), KEYWORD);
        tableOfLanguageTokens.put(ENDBEGIN.toString(), KEYWORD);
        tableOfLanguageTokens.put(IF.toString(), KEYWORD);
        tableOfLanguageTokens.put(ELSE.toString(), KEYWORD);
        tableOfLanguageTokens.put(ENDIF.toString(), KEYWORD);
        tableOfLanguageTokens.put(VAR.toString(), KEYWORD);
        tableOfLanguageTokens.put(ENDVAR.toString(), KEYWORD);
        tableOfLanguageTokens.put(DO.toString(), KEYWORD);
        tableOfLanguageTokens.put(ENDDO.toString(), KEYWORD);
        tableOfLanguageTokens.put(WHILE.toString(), KEYWORD);
        tableOfLanguageTokens.put(READ.toString(), KEYWORD);
        tableOfLanguageTokens.put(WRITE.toString(), KEYWORD);
        //tableOfLanguageTokens.put("end", KEYWORD);
        tableOfLanguageTokens.put(Keywords.INT.toString(), KEYWORD);
        tableOfLanguageTokens.put(Keywords.DOUBLE.toString(), KEYWORD);
        tableOfLanguageTokens.put(Keywords.BOOLEAN.toString(), KEYWORD);
        tableOfLanguageTokens.put("=", ASSIGN_OPERATION);
        tableOfLanguageTokens.put(".", DOT);
        tableOfLanguageTokens.put(" ", SPACE);
        tableOfLanguageTokens.put("\t", SPACE);
        tableOfLanguageTokens.put("\n", NEW_LINE);
        tableOfLanguageTokens.put("-", ADDITION_OPERATION);
        tableOfLanguageTokens.put("+", ADDITION_OPERATION);
        tableOfLanguageTokens.put("*", MULTIPLICATION_OPERATION);
        tableOfLanguageTokens.put("/", MULTIPLICATION_OPERATION);
        tableOfLanguageTokens.put("(", L_PARENTHESIS_OPERATION);
        tableOfLanguageTokens.put(")", R_PARENTHESIS_OPERATION);

        tableOfLanguageTokens.put("**", POW_OPERATION);

        tableOfLanguageTokens.put("==", RELATIVE_OPERATION);
        tableOfLanguageTokens.put("!=", RELATIVE_OPERATION);
        tableOfLanguageTokens.put("<", RELATIVE_OPERATION);
        tableOfLanguageTokens.put(">", RELATIVE_OPERATION);
        tableOfLanguageTokens.put("<=", RELATIVE_OPERATION);
        tableOfLanguageTokens.put(">=", RELATIVE_OPERATION);

        tableOfLanguageTokens.put(",", PUNCTUATION);

        tableIdentFloatInt.put(2, IDENTIFIER);
        tableIdentFloatInt.put(6, TokenTypes.DOUBLE);
        tableIdentFloatInt.put(9, TokenTypes.INT);
        tableIdentFloatInt.put(90, TokenTypes.BOOLEAN);
    }
}
