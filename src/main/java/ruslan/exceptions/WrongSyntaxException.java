package ruslan.exceptions;

public class WrongSyntaxException extends Exception {
    private final int lineNumber;
    public WrongSyntaxException(String message, int lineNumber) {
        super(message);
        this.lineNumber = lineNumber;
    }

    public String getErrorMessage() {
        return "[ON LINE №" + lineNumber + "]: " + getMessage();
    }
}
