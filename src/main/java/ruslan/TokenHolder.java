package ruslan;

import java.util.HashMap;
import java.util.Map;

class TokenHolder {

    static Map<String,String > tableOfLanguageTokens = new HashMap<>();
    static Map<Integer,String> tableIdentFloatInt = new HashMap<>();
    static {
        tableOfLanguageTokens.put("Program","keyword");
        tableOfLanguageTokens.put("begin","keyword");
        tableOfLanguageTokens.put("endbegin","keyword");
        tableOfLanguageTokens.put("if","keyword");
        tableOfLanguageTokens.put("endif","keyword");
        tableOfLanguageTokens.put("do","keyword");
        tableOfLanguageTokens.put("enddo","keyword");
        tableOfLanguageTokens.put("while","keyword");
        tableOfLanguageTokens.put("end","keyword");
        tableOfLanguageTokens.put("int","keyword");
        tableOfLanguageTokens.put("=","assign_op");
        tableOfLanguageTokens.put(".","dot");
        tableOfLanguageTokens.put(" ","ws");
        tableOfLanguageTokens.put("\t","ws");
        tableOfLanguageTokens.put("\n","nl");
        tableOfLanguageTokens.put("-","add_op");
        tableOfLanguageTokens.put("+","add_op");
        tableOfLanguageTokens.put("*","mult_op");
        tableOfLanguageTokens.put("/","mult_op");
        tableOfLanguageTokens.put("(","par_op");
        tableOfLanguageTokens.put(")","par_op");

        tableOfLanguageTokens.put("==","rel_op");
        tableOfLanguageTokens.put("!=","rel_op");
        tableOfLanguageTokens.put("<","rel_op");
        tableOfLanguageTokens.put(">","rel_op");
        tableOfLanguageTokens.put("<=","rel_op");
        tableOfLanguageTokens.put(">=","rel_op");

        tableIdentFloatInt.put(2,"ident");
        tableIdentFloatInt.put(6,"double");
        tableIdentFloatInt.put(9,"int");
    }
}
