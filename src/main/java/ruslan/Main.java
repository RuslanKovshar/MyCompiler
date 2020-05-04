package ruslan;

import ruslan.exceptions.WrongSyntaxException;
import ruslan.lexical.LexicalAnalyzer;
import ruslan.postfix.PostfixInterpreter;
import ruslan.postfix.PostfixTranslator;
import ruslan.postfix.Variable;
import ruslan.syntactical.FiniteStateMachineParser;
import ruslan.syntactical.Parser;
import ruslan.token.Token;
import ruslan.utils.ProgramReader;

import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        final String PATH = "src/main/resources/myProgram.thend";
        final String program = ProgramReader.readProgram(PATH);
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(program);
        List<Token> tokens = lexicalAnalyzer.analyze();
        Parser parser = new FiniteStateMachineParser(tokens);
        try {
            parser.parse();
            PostfixTranslator translator = new PostfixTranslator(tokens);
            List<Token> prn = translator.translateToPostfix();
            Map<String, Variable> variableMap = translator.getVariableMap();
            PostfixInterpreter interpreter = new PostfixInterpreter(prn,variableMap);
            interpreter.doInterpretation();
        } catch (WrongSyntaxException e) {
            e.printStackTrace();
        }
    }
}
