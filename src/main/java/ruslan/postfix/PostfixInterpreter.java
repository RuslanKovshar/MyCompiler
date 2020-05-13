package ruslan.postfix;

import ruslan.Keywords;
import ruslan.token.Token;
import ruslan.token.TokenTypes;

import java.util.*;

public class PostfixInterpreter {
    private final Stack<InterpretationData> resultStack = new Stack<>();
    private final Scanner scanner = new Scanner(System.in);
    private final Map<String, Variable> variableMap;
    private final List<Token> prn;
    private int index;

    public PostfixInterpreter(List<Token> prn, Map<String, Variable> variableMap) {
        this.prn = prn;
        this.variableMap = variableMap;
    }

    public void doInterpretation() {
        for (index = 0; index < prn.size(); index++) {
            Token token = prn.get(index);
            TokenTypes type = token.getType();
            if (isVariable(type)) {
                pushTokenToStack(token, type);
            }
            if (type == TokenTypes.ASSIGN_OPERATION) {
                assign();
            }
            if (type == TokenTypes.U_MINUS) {
                negative();
            }
            if (token.getLexeme().equals(Commands.OUT.name())) {
                write();
            }
            if (token.getLexeme().equals(Commands.INPUT.name())) {
                read();
            }
            if (token.getLexeme().equals(Commands.JMP.name())){
                doJumpToLabel();
            }
            if (token.getLexeme().equals(Commands.JF.name())) {
                doJumpFalse();
            }
            if (isNumeric(type)) {
                doNumeric(token.getLexeme());
            }
            if (isRelative(type)) {
                doRelative(token.getLexeme());
            }
        }
    }

    private void doJumpToLabel() {
        Token jumpLabel = prn.get(index - 1);

        int oldIndex = index;

        for (;index < prn.size();index++){
            if (prn.get(index).getLexeme().equals(jumpLabel.getLexeme())) {
                return;
            }
        }

        for (index = 0; index < oldIndex; index++){
            if (prn.get(index).getLexeme().equals(jumpLabel.getLexeme())) {
                return;
            }
        }
    }

    private void doJumpFalse() {
        InterpretationData flag = resultStack.pop();
        if (flag.getData().equals("false")) {
            String lexeme = prn.get(index - 1).getLexeme();
            while (!prn.get(index).getLexeme().equals(lexeme)){
                index++;
            }
        }
    }

    private void doRelative(String operation) {
        InterpretationData firstValue = resultStack.pop();
        InterpretationData secondValue = resultStack.pop();

        Variable first = createVariable(firstValue);
        Variable second = createVariable(secondValue);

        String firstType = first.getType();
        String secondType = second.getType();
        if (firstType.equals(secondType)) {
            if (!firstType.equals(Keywords.BOOLEAN.toString())) {
                boolean relative = performRelativeOperation(Double.parseDouble(first.getValue()),
                        Double.parseDouble(second.getValue()),
                        operation);

                resultStack.push(new InterpretationData(relative + "", TokenTypes.BOOLEAN));
            } else {
                if (operation.equals("==")) {
                    boolean equals = first.getValue().equals(second.getValue());
                    resultStack.push(new InterpretationData(equals + "", TokenTypes.BOOLEAN));
                } else if (operation.equals("!=")) {
                    boolean equals = first.getValue().equals(second.getValue());
                    resultStack.push(new InterpretationData(!equals + "", TokenTypes.BOOLEAN));
                } else {
                    throw new IllegalStateException("operator " + operation +
                            " cannot be applied for '" + firstType + "' '" + secondType + "'");
                }
            }
        } else {
            throw new IllegalStateException("operator " + operation +
                    " cannot be applied for '" + firstType + "' '" + secondType + "'");
        }
    }

    private void read() {
        String input = "";

        InterpretationData pop = resultStack.pop();
        Variable variable = getVariable(pop.getData());

        String type = variable.getType();
        if (type.equals("int")) {
            input = scanner.nextInt() + "";
        }
        if (type.equals("double")) {
            input = scanner.nextDouble() + "";
        }
        if (type.equals("boolean")) {
            input = scanner.nextBoolean() + "";
        }
        variable.setValue(input);

    }

    private void doNumeric(String operation) {
        InterpretationData firstValue = resultStack.pop();
        InterpretationData secondValue = resultStack.pop();

        Variable first = createVariable(firstValue);
        Variable second = createVariable(secondValue);

        checkForBoolean(first);
        checkForBoolean(second);

        if (first.getType().equals("int") && second.getType().equals("int")) {
            int i = (int) performNumericOperation(Integer.parseInt(first.getValue()), Integer.parseInt(second.getValue()), operation);
            resultStack.push(new InterpretationData(i + "", TokenTypes.INT));
        }
        if (first.getType().equals("double") || second.getType().equals("double")) {
            double i = performNumericOperation(Double.parseDouble(first.getValue()), Double.parseDouble(second.getValue()), operation);
            resultStack.push(new InterpretationData(i + "", TokenTypes.DOUBLE));
        }
    }

    private void checkForBoolean(Variable variable) {
        if (variable.getType().equals(Keywords.BOOLEAN.toString())) {
            throw new IllegalArgumentException("incorrect types");
        }
    }

    private Variable createVariable(InterpretationData data) {
        if (data.getType() == TokenTypes.IDENTIFIER) {
            Variable variable = getVariable(data.getData());
            if (variable.getValue() == null) {
                throw new IllegalArgumentException("variable " + data.getData() + " not initialized");
            }
            return variable;
        }

        return new Variable(data.getData(), data.getType().toString().toLowerCase());
    }

    private void write() {
        InterpretationData dataToWrite = resultStack.pop();

        if (dataToWrite.getType() == TokenTypes.IDENTIFIER) {
            Variable variable = getVariable(dataToWrite.getData());

            String value = variable.getValue();

            if (value == null) {
                throw new IllegalArgumentException("variable " + dataToWrite.getData() + " not initialized");
            }

            System.out.println(value);
        } else {
            System.out.println(dataToWrite.getData());
        }
    }

    private void negative() {
        InterpretationData data = resultStack.pop();

        if (data.getType() == TokenTypes.IDENTIFIER) {
            Variable variable = getVariable(data.getData());
            if (variable.getValue() == null) {
                throw new IllegalArgumentException("variable " + data.getData() + " not initialized");
            }

            String type = variable.getType();
            String variableValue = variable.getValue();
            if (type.equals(Keywords.INT.toString())) {
                resultStack.push(new InterpretationData(-Integer.parseInt(variableValue) + "", TokenTypes.INT));
            }
            if (type.equals(Keywords.DOUBLE.toString())) {
                resultStack.push(new InterpretationData(-Double.parseDouble(variableValue) + "", TokenTypes.DOUBLE));
            }
            if (type.equals(Keywords.BOOLEAN.toString())) {
                throw new IllegalStateException("operator '-'" +
                        " cannot be applied for 'boolean'");
            }
        } else {
            data.setData("-" + data.getData());
            resultStack.push(data);
        }
    }

    private void assign() {
        InterpretationData value = resultStack.pop();
        InterpretationData variableToAssign = resultStack.pop();

        TokenTypes variableToAssignType = value.getType();
        String data = value.getData();

        if (value.getType() == TokenTypes.IDENTIFIER) {
            Variable variable = getVariable(data);
            if (variable.getValue() == null) {
                throw new IllegalArgumentException("variable " + data + " not initialized");
            }
            data = variable.getValue();
            variableToAssignType = TokenTypes.valueOf(variable.getType().toUpperCase());
        }

        Variable variable = getVariable(variableToAssign.getData());
        String variableType = variable.getType();
        if (variableType.equals("int") && variableToAssignType == TokenTypes.INT
                || variableType.equals("double") && variableToAssignType == TokenTypes.DOUBLE
                || variableType.equals("boolean") && variableToAssignType == TokenTypes.BOOLEAN) {
            variable.setValue(data);
        } else {
            throw new IllegalStateException("expected type: " + variableType + " - current type " + variableToAssignType);
        }
    }

    private boolean isVariable(TokenTypes type) {
        return type == TokenTypes.DOUBLE
                || type == TokenTypes.INT
                || type == TokenTypes.BOOLEAN
                || type == TokenTypes.IDENTIFIER;
    }

    private boolean isNumeric(TokenTypes type) {
        return type == TokenTypes.ADDITION_OPERATION
                || type == TokenTypes.MULTIPLICATION_OPERATION
                || type == TokenTypes.POW_OPERATION;
    }

    private boolean isRelative(TokenTypes type) {
        return type == TokenTypes.RELATIVE_OPERATION;
    }

    private void pushTokenToStack(Token token, TokenTypes type) {
        resultStack.push(new InterpretationData(token.getLexeme(), type));
    }

    private Variable getVariable(String key) {
        Variable variable = variableMap.get(key);
        if (variable != null) {
            return variable;
        }
        throw new IllegalArgumentException("undefined variable " + key);
    }

    private double performNumericOperation(double num1, double num2, String op) {
        switch (op) {
            case "+":
                return num2 + num1;
            case "-":
                return num2 - num1;
            case "*":
                return num2 * num1;
            case "/":
                return num2 / num1;
            case "**":
                return Math.pow(num2, num1);
            default:
                throw new RuntimeException();
        }
    }

    private boolean performRelativeOperation(double num1, double num2, String op) {
        switch (op) {
            case "==":
                return num2 == num1;
            case "!=":
                return num2 != num1;
            case ">":
                return num2 > num1;
            case ">=":
                return num2 >= num1;
            case "<":
                return num2 < num1;
            case "<=":
                return num2 <= num1;
            default:
                throw new RuntimeException();
        }
    }
}
