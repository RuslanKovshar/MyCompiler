package ruslan.syntactical;

import ruslan.exceptions.WrongSyntaxException;

public interface Parser {

    void parse() throws WrongSyntaxException;
}
