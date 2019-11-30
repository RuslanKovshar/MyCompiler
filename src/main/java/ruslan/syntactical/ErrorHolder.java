package ruslan.syntactical;

import ruslan.token.TokenTypes;

import java.util.HashMap;
import java.util.Map;

import static ruslan.Keywords.*;

class ErrorHolder {

    static private Map<Integer, String> errors = new HashMap<>();

    static Map<Integer, String> getErrors() {
        return errors;
    }

    static {
        errors.put(505, "New line expected");
        errors.put(101, "Program must start with '" + PROGRAM.toString() + "' word!");
        errors.put(102, "Program name must be identifier!");
        errors.put(103, "Declaration block must start with '" + VAR.toString() + "' keyword!");
        errors.put(104, "Declaration must start with type of variable");
        errors.put(105, "Identifier expected");
        errors.put(106, "Declaration or '" + ENDVAR.toString() + "' keyword");
        errors.put(107, "'(' expected");
        errors.put(108, "')' expected");
        errors.put(109, "Not a statement");
        errors.put(110, "Variable expected");
        errors.put(111, "Expression expected");
    }
}
