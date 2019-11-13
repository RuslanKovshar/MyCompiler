package ruslan;

import ruslan.lexical.LexicalAnalyzer;
import ruslan.syntactical.SyntacticalAnalyzer;
import ruslan.token.Token;
import ruslan.utils.ProgramReader;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        final String PATH = "src/main/resources/myProgram.thend";
        final String program = ProgramReader.readProgram(PATH);
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(program);
        List<Token> tokens = lexicalAnalyzer.analyze();
        SyntacticalAnalyzer syntacticalAnalyzer = new SyntacticalAnalyzer(tokens);
        syntacticalAnalyzer.parse();
    }
}
