package ruslan.token;

import java.util.HashMap;
import java.util.Map;

import static ruslan.token.TokenTypes.*;

public class TokenHolder {

    public static Map<String, TokenTypes> tableOfLanguageTokens = new HashMap<>();
    public static Map<Integer, TokenTypes> tableIdentFloatInt = new HashMap<>();

    static {
        tableOfLanguageTokens.put("Program", KEYWORD);
        tableOfLanguageTokens.put("begin", KEYWORD);
        tableOfLanguageTokens.put("endbegin", KEYWORD);
        tableOfLanguageTokens.put("if", KEYWORD);
        tableOfLanguageTokens.put("endif", KEYWORD);
        tableOfLanguageTokens.put("var", KEYWORD);
        tableOfLanguageTokens.put("do", KEYWORD);
        tableOfLanguageTokens.put("enddo", KEYWORD);
        tableOfLanguageTokens.put("while", KEYWORD);
        tableOfLanguageTokens.put("end", KEYWORD);
        tableOfLanguageTokens.put("int", KEYWORD);
        tableOfLanguageTokens.put("double", KEYWORD);
        tableOfLanguageTokens.put("boolean", KEYWORD);
        tableOfLanguageTokens.put("=", ASSIGN_OPERATION);
        tableOfLanguageTokens.put(".", DOT);
        tableOfLanguageTokens.put(" ", SPACE);
        tableOfLanguageTokens.put("\t", SPACE);
        tableOfLanguageTokens.put("\n", NEW_LINE);
        tableOfLanguageTokens.put("-", ADDITION_OPERATION);
        tableOfLanguageTokens.put("+", ADDITION_OPERATION);
        tableOfLanguageTokens.put("*", MULTIPLICATION_OPERATION);
        tableOfLanguageTokens.put("/", MULTIPLICATION_OPERATION);
        tableOfLanguageTokens.put("(", PARENTHESIS_OPERATION);
        tableOfLanguageTokens.put(")", PARENTHESIS_OPERATION);

        tableOfLanguageTokens.put("**", POW_OPERATION);

        tableOfLanguageTokens.put("==", RELATIVE_OPERATION);
        tableOfLanguageTokens.put("!=", RELATIVE_OPERATION);
        tableOfLanguageTokens.put("<", RELATIVE_OPERATION);
        tableOfLanguageTokens.put(">", RELATIVE_OPERATION);
        tableOfLanguageTokens.put("<=", RELATIVE_OPERATION);
        tableOfLanguageTokens.put(">=", RELATIVE_OPERATION);

        tableOfLanguageTokens.put(",", PUNCTUATION);

        tableIdentFloatInt.put(2, IDENTIFIER);
        tableIdentFloatInt.put(6, DOUBLE);
        tableIdentFloatInt.put(9, INT);
        tableIdentFloatInt.put(90, BOOLEAN);
    }
}
