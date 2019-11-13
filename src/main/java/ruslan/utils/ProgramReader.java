package ruslan.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ProgramReader {

    public static String readProgram(String path) {
        String program = "";
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(path));
            program = new String(bytes);
            program += "\n";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return program;
    }
}
