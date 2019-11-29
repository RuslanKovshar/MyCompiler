package ruslan;

import ruslan.lexical.LexicalAnalyzer;
import ruslan.syntactical.FiniteStateMachineParser;
import ruslan.syntactical.Parser;
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
        parser.parse();
    }
}
