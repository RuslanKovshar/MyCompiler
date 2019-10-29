package ruslan;

import java.util.regex.Pattern;

public enum Tokens {

    ID("^[a-zA-Z]\\w*$"),
    INT("^-*\\d+$"),
    DOUBLE("[+-]?\\d+\\.?(\\d+)?"),
    BOOLEAN("^true|false$"),
    KEYWORD("^Program|var|int|double|boolean|read|write|begin|do|if|else|enddo|endif|endbegin|while$"),
    ASSIGN_OP("^=$"),
    ADD_OP("^\\+|-$"),
    MULT_OP("^*|/$"),
    REL_OP("^<|==|!=|<=|>=|>$"),
    BRACKETS_OP("^\\(|\\)$"),
    PUNCT("^\\.|,|:$"),
    SPACE("^ |\\t$"),
    EOL("^\\n$"),
    UNDEFINED("");

    private String regEx;

    Tokens(String regEx) {
        this.regEx = regEx;
        Pattern pattern = Pattern.compile("^\\D*\\.\\D*$");
    }

    public String getRegEx() {
        return regEx;
    }
}
