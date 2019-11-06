package ruslan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

class LexicalAnalyzer {

    private final int INITIAL_STATE = 0;
    private int state = INITIAL_STATE;
    private int lineNumber = 1;
    private String program;
    private List<Integer> finalStates = new ArrayList<>(Arrays.asList(2, 6, 9, 12, 13, 14, 15, 101, 102, 55, 103, 90, 83, 56, 45));
    private List<Integer> mainStates = new ArrayList<>(Arrays.asList(2, 6, 9)); //states for int double ind
    private List<Integer> errorStates = new ArrayList<>(Arrays.asList(/*101,*/ 102, 103));
    private String lexeme = "";
    private int index = -1;

    private List<Token> tokens = new ArrayList<>();
    private List<Token> constants = new ArrayList<>();
    private List<Token> variables = new ArrayList<>();

    void analyze() {
        readProgram();
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
    }

    private void printTable(List<Token> tokens) {
        tokens.forEach(System.out::println);
        System.out.println("\n");
    }

    private String getToken(int state, String lexeme) {
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
        if (state == 6 || state == 9) {
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
        if (state == 13) {
            lineNumber++;
            state = 0;
        }
        String token;
        if (mainStates.contains(state)) {
            token = getToken(state, lexeme);
            if (!token.equals("keyword")) {
                int tokenIndex = getIndex();
                tokens.add(new Token(lineNumber, lexeme, token, tokenIndex));
            } else {
                tokens.add(new Token(lineNumber, lexeme, token));
            }
            lexeme = "";
            index--;
            state = 0;
        }
        if (state == 12 || state == 14 || state == 15 || state == 45 || state == 83) {
            lexeme += ch;
            token = getToken(state, lexeme);
            tokens.add(new Token(lineNumber, lexeme, token));
            lexeme = "";
            state = 0;
        }
        if (state == 55 || state == 56) {// single rel_op < > =
            token = getToken(state, lexeme);
            tokens.add(new Token(lineNumber, lexeme, token));
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
        if (StateHolder.stateTransitionFunction.containsKey(state)) {
            return StateHolder.stateTransitionFunction.get(state);
        } else {
            return StateHolder.stateTransitionFunction.get(new State(stateNumber, "other"));
        }
    }

    private String classOfChar(char ch) {
        String res = null;
        if (ch == '.') res = "dot";
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

    private void readProgram() {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get("src/main/resources/myProgram.thend"));
            program = new String(bytes);
            program += "\n";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
