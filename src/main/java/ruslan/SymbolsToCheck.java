package ruslan;

import java.util.Arrays;
import java.util.Optional;

public enum SymbolsToCheck {
    IUI('='),
    PLUS('+'),
    MINUS('-'),
    L_BRACKET('('),
    R_BRACKET(')'),
    // EOL('\n'),
    DOT('.');

    private char ch;

    SymbolsToCheck(char ch) {
        this.ch = ch;
    }

    public static boolean contains(char ch) {
        return Arrays.stream(values()).anyMatch(val -> val.ch == ch);
    }

    public static Optional<SymbolsToCheck> getSymbol(char ch) {
        return Arrays.stream(values()).filter(val -> val.ch == ch).findAny();
    }

    public char getCh() {
        return ch;
    }

    public StringBuilder filterStr(String str, int i) {
        StringBuilder builder = new StringBuilder(str);
        if (this == DOT) {
            if (!(Character.isDigit(str.charAt(i - 1)) || Character.isDigit(str.charAt(i + 1)))) {

                builder.insert(i, ' ');
                if (i < str.length() - 2) {
                    builder.insert(i + 2, ' ');
                }
            }
        } else {
            builder.insert(i, ' ');
            if (i < str.length() - 2) {
                builder.insert(i + 2, ' ');
            }
        }
        return builder;
    }
}
