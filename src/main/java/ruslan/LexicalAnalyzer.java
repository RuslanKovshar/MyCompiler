package ruslan;

import java.util.*;
import java.util.stream.Collectors;

class LexicalAnalyzer {

    void analyze() {
        String inputString = "Program programName\n var int x=3 \n double. = 5 a =! b do. while( enddo). if endif .2 0.1 !";
        StringBuilder tempString = new StringBuilder(inputString);
        int counter = 0;
        for (int i = 0; i < inputString.length(); i++) {
            char ch = inputString.charAt(i);
            Optional<SymbolsToCheck> symbol = SymbolsToCheck.getSymbol(ch);
            if (symbol.isPresent()) {
                tempString = symbol.get().filterStr(tempString.toString(), i+counter);
                i += 3;
                counter+=2;
            }
        }
        inputString = tempString.toString();
        String[] words = inputString.split(" +");
        Map<String, Tokens> result = new HashMap<>();
        Arrays.stream(words)
                .forEach(word -> {
                            List<Tokens> tokensStream = Arrays.stream(Tokens.values())
                                    .filter(token -> word.matches(token.getRegEx())).collect(Collectors.toList());
                            Optional<Tokens> token;
                            if (tokensStream.contains(Tokens.KEYWORD)) {
                                token = Optional.of(Tokens.KEYWORD);
                            } else {
                                try {
                                    token = Optional.of(tokensStream.get(0));
                                } catch (IndexOutOfBoundsException e) {
                                    token = Optional.of(Tokens.UNDEFINED);
                                }
                            }

                            result.put(word, token.get());
                        }
                );
        result.forEach((key, value) -> System.out.println(String.format("%10s  %6s", key, value)));
    }

}
