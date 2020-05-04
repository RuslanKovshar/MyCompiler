package ruslan.postfix;

import ruslan.token.TokenTypes;

public class InterpretationData {
    private String data;
    private TokenTypes type;

    public InterpretationData() {
    }

    public InterpretationData(String data, TokenTypes type) {
        this.data = data;
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public TokenTypes getType() {
        return type;
    }

    public void setType(TokenTypes type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "InterpretationData{" +
                "data='" + data + '\'' +
                ", type=" + type +
                '}';
    }
}
