package ruslan.logic;

import ruslan.logic.poliz.PolizPerformer;
import ruslan.logic.poliz.PolizTransformer;
import ruslan.token.Token;
import ruslan.token.TokenTypes;

import java.util.ArrayList;
import java.util.List;

public class LogicAnalyzer {

    private PolizPerformer performer = new PolizPerformer();
    private PolizTransformer transformer = new PolizTransformer();

    public void perform(List<Token> tokens) {
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).getType() == TokenTypes.ASSIGN_OPERATION) {
                int j = i + 1;
                List<Token> performTokens = new ArrayList<>();
                while (tokens.get(j).getType() != TokenTypes.NEW_LINE) {
                    //System.out.println(tokens.get(j).getLexeme());
                    performTokens.add(tokens.get(j));
                    j++;
                }
                List<Token> transform = transformer.transform(performTokens);
                transform.forEach(token -> System.out.print(token.getLexeme() + " "));
                System.out.println();
                double result = performer.perform(transform);
                System.err.println(result);
                i = j;
            }
        }

    }
}
