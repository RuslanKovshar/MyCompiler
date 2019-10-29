package ruslan;

import java.util.HashMap;
import java.util.Map;

class SymbolsContainer {
    static Map<String,String> symbols = new HashMap<>();
    static {
        symbols.put("\\+","+");
        symbols.put("\\-","-");
        symbols.put("\\(","(");
        symbols.put("\\)",")");
        symbols.put("\n","\n");
        symbols.put("^\\D*\\.\\D*$",".");
    }
}
