package ruslan.logic.poliz;

import ruslan.token.Token;
import ruslan.token.TokenTypes;

import java.util.List;
import java.util.Stack;

public class PolizPerformer {

    private Stack<Double> stack = new Stack<>();

    public double perform(List<Token> tokens) {
        for (Token token : tokens) {
            TokenTypes type = token.getType();
            if (type == TokenTypes.DOUBLE || type == TokenTypes.INT || type == null) {
                double value = Double.parseDouble(token.getLexeme());
                stack.push(value);
            } else if (type == TokenTypes.U_MINUS) {
                double newValue = -stack.pop();
                stack.push(newValue);
            } else {
                double num = performOperation(stack.pop(), stack.pop(), token.getLexeme());
                stack.push(num);
            }
        }

        return stack.pop();
    }

    private double performOperation(double num1, double num2, String op) {
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
}
