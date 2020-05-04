package ruslan.lexical;

import ruslan.token.Token;
import ruslan.token.TokenHolder;
import ruslan.token.TokenTypes;

import java.util.*;

public class LexicalAnalyzer  {

    private final int INITIAL_STATE = 0;
    private int state = INITIAL_STATE;
    private int lineNumber = 1;
    private final String program;
    private final List<Integer> finalStates = new ArrayList<>(Arrays.asList(2, 6, 9, 12, 13, 14, 15, 101, 102, 55, 103, 90, 83, 56, 45));
    private final List<Integer> mainStates = new ArrayList<>(Arrays.asList(2, 6, 9)); //states for int double ind
    private final List<Integer> errorStates = new ArrayList<>(Arrays.asList(102, 103));
    private String lexeme = "";
    private int index = -1;

    private final List<Token> tokens = new ArrayList<>();
    private final List<Token> constants = new ArrayList<>();
    private final List<Token> variables = new ArrayList<>();

    public LexicalAnalyzer(String program) {
        this.program = program;
    }

    public List<Token> analyze() {
        char ch;
        while (index < program.length() - 1) {
            index++;
            ch = program.charAt(index);

            String classOfCh = classOfChar(ch);
            if (classOfCh == null) {
                System.err.println("Error in line: " + lineNumber + " unexpected symbol: " + ch);
            } else {
                state = nextState(state, classOfCh);
                if (isFinalState(state)) {
                    processing(ch);
                } else if (state == 0) {
                    lexeme = "";
                } else {
                    lexeme += ch;
                }
            }
        }
        printTable(tokens);
        printTable(variables);
        printTable(constants);
        return tokens;
    }

    private void printTable(List<Token> tokens) {
        tokens.forEach(System.out::println);
        System.out.println();
    }

    private TokenTypes getToken(int state, String lexeme) {
        if (TokenHolder.tableOfLanguageTokens.containsKey(lexeme)) {
            return TokenHolder.tableOfLanguageTokens.get(lexeme);
        } else {
            return TokenHolder.tableIdentFloatInt.get(state);
        }
    }

    private int getIndex() {
        if (state == 2) {
            return checkToken(variables);
        }
        if (state == 6 || state == 9 || state == 90) {
            return checkToken(constants);
        }
        return -1;
    }

    private int checkToken(List<Token> tokens) {
        int tokenIndex;
        Optional<Token> any = tokens.stream().filter(token -> token.getLexeme().equals(lexeme)).findAny();
        if (any.isPresent()) {
            tokenIndex = any.get().getIndex();
        } else {
            tokens.add(new Token(lineNumber, lexeme, tokens.size() + 1));
            tokenIndex = tokens.size();
        }
        return tokenIndex;
    }

    private void processing(char ch) {
        TokenTypes type;
        if (mainStates.contains(state)) {
            if (lexeme.equals("true") || lexeme.equals("false")) state = 90;
            type = getToken(state, lexeme);
            if (type != TokenTypes.KEYWORD) {
                int tokenIndex = getIndex();
                tokens.add(new Token(lineNumber, lexeme, type, tokenIndex));
            } else {
                tokens.add(new Token(lineNumber, lexeme, type));
            }
            lexeme = "";
            index--;
            state = 0;
        }
        if (state == 13) {
            lexeme += ch;
            type = getToken(state, lexeme);
            tokens.add(new Token(lineNumber++, lexeme, type));
            lexeme = "";
            state = 0;
        }
        if (state == 12 || state == 14 || state == 15 || state == 45 || state == 83) {
            lexeme += ch;
            type = getToken(state, lexeme);
            tokens.add(new Token(lineNumber, lexeme, type));
            lexeme = "";
            state = 0;
        }
        if (state == 55 || state == 56) {// single rel_op < > =
            type = getToken(state, lexeme);
            tokens.add(new Token(lineNumber, lexeme, type));
            lexeme = "";
            state = 0;
            index--;
        }
        if (errorStates.contains(state)) {
            System.err.println("Error in line: " + lineNumber + " unexpected symbol: " + lexeme);
            state = 0;
            lexeme = "";
            index--;
        }
        if (state == 101) {
            lexeme += ch;
            System.out.println("Error in line: " + lineNumber + " unexpected symbol: " + lexeme);
            state = 0;
            lexeme = "";
        }
    }

    private boolean isFinalState(int state) {
        return finalStates.contains(state);
    }

    private int nextState(int stateNumber, String classOfCh) {
        State state = new State(stateNumber, classOfCh);
        if (LexicalStateHolder.stateTransitionFunction.containsKey(state)) {
            return LexicalStateHolder.stateTransitionFunction.get(state);
        } else {
            return LexicalStateHolder.stateTransitionFunction.get(new State(stateNumber, "other"));
        }
    }

    private String classOfChar(char ch) {
        String res = null;
        if (ch == '.') res = "dot";
        if (ch == ',') res = "coma";
        if (ch == '_') res = "Bottom";
        if ("abcdefghijklmnopqrstuvwxyz".indexOf(ch) > -1 || "abcdefghijklmnopqrstuvwxyz".toUpperCase().indexOf(ch) > -1)
            res = "Letter";
        if ("0123456789".indexOf(ch) > -1) res = "Digit";
        if ("\t".indexOf(ch) > -1) res = "ws";
        if ((int)ch == 32 || (int)ch == 13) res = "ws";
        if (ch == '\n') res = "nl";
        if ("!+-=()<>*/".indexOf(ch) > -1) res = "" + ch;
        return res;
    }
}
