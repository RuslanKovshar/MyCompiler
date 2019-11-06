package ruslan;

public class Token {
    private int lineNumber;
    private String lexeme;
    private String token;
    private Integer index;

    public Token(int lineNumber, String lexeme, String token, Integer index) {
        this.lineNumber = lineNumber;
        this.lexeme = lexeme;
        this.token = token;
        this.index = index;
    }

    public Token(int lineNumber, String lexeme, String token) {
        this.lineNumber = lineNumber;
        this.lexeme = lexeme;
        this.token = token;
    }

    Token(int lineNumber, String lexeme, Integer index) {
        this.lineNumber = lineNumber;
        this.lexeme = lexeme;
        this.index = index;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public String toString() {
        /*return "Token{" +
                "lineNumber=" + lineNumber +
                ", lexeme='" + lexeme + '\'' +
                ", token='" + token + '\'' +
                ", index=" + index +
                "}";*/
        return String.format("%3d %14s %14s %3s", lineNumber, lexeme, token == null ? "" : token, index == null ? "" : index.toString());
    }
}
