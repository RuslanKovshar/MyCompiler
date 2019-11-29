package ruslan.syntactical;

import java.util.HashMap;
import java.util.Map;

import static ruslan.Keywords.PROGRAM;

public class ErrorHolder {

    static private Map<Integer, String> errors = new HashMap<>();

    public static Map<Integer, String> getErrors() {
        return errors;
    }

    static {
        errors.put(101, "Program must start with '" + PROGRAM.toString() + "' word!");
        errors.put(102, "Program name must be identifier!");
        errors.put(103, "New line expected");
    }
}
