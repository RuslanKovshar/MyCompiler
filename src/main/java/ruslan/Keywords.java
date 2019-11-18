package ruslan;

public enum Keywords {
    INT,
    DOUBLE,
    BOOLEAN,
    VAR,
    ENDVAR,
    DO,
    ENDDO,
    BEGIN,
    ENDBEGIN,
    IF,
    ENDIF,
    PROGRAM,
    WHILE,
    READ,
    WRITE;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
