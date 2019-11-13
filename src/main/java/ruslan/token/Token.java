package ruslan.token;

import java.util.Objects;

public class Token {
    private int lineNumber;
    private String lexeme;
    private TokenTypes type;
    private Integer index;

    public Token(int lineNumber, String lexeme, TokenTypes type, Integer index) {
        this.lineNumber = lineNumber;
        this.lexeme = lexeme;
        this.type = type;
        this.index = index;
    }

    public Token(int lineNumber, String lexeme, Integer index) {
        this.lineNumber = lineNumber;
        this.lexeme = lexeme;
        this.index = index;
    }

    public Token(int lineNumber, String lexeme, TokenTypes type) {
        this.lineNumber = lineNumber;
        this.lexeme = lexeme;
        this.type = type;
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

    public TokenTypes getType() {
        return type;
    }

    public void setType(TokenTypes type) {
        this.type = type;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return String.format("|%3d | %14s | %24s | %3s |", lineNumber,
                lexeme,
                type == null ? "" : type.name(),
                index == null ? "" : index.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return  Objects.equals(lexeme, token.lexeme) &&
                type == token.type &&
                Objects.equals(index, token.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineNumber, lexeme, type, index);
    }
}
