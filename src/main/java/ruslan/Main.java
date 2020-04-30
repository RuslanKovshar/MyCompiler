package ruslan;

import ruslan.exceptions.WrongSyntaxException;
import ruslan.lexical.LexicalAnalyzer;
import ruslan.logic.LogicAnalyzer;
import ruslan.postfix.PostfixTranslator;
import ruslan.syntactical.FiniteStateMachineParser;
import ruslan.syntactical.Parser;
import ruslan.syntactical.RecursiveDescentParser;
import ruslan.token.Token;
import ruslan.utils.ProgramReader;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        final String PATH = "src/main/resources/myProgram.thend";
        final String program = ProgramReader.readProgram(PATH);
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(program);
        List<Token> tokens = lexicalAnalyzer.analyze();
        Parser parser = new FiniteStateMachineParser(tokens);
        try {
            parser.parse();
            PostfixTranslator translator = new PostfixTranslator();
            translator.translateToPostfix(tokens);
            //LogicAnalyzer logicAnalyzer = new LogicAnalyzer();
            //logicAnalyzer.perform(tokens);
        } catch (WrongSyntaxException e) {
            e.printStackTrace();
        }
    }
}
