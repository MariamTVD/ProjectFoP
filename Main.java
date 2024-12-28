import java.util.Scanner;
import java.util.*;
 import java.util.ArrayList;
import java.util.List;

public class Main {

    public static Map<String, Integer> values = new HashMap<>();

      // Removing unnecessary empty lines from user input
    public static String[] removeEmptyLines(String[] lines) {
        List<String> nonEmptyLines = new ArrayList<>();
        for (String line : lines) {
            // Check if the line is not empty or there are only whitespaces
            if (!line.trim().isEmpty()) {
                nonEmptyLines.add(line);
            }
        }
        return nonEmptyLines.toArray(new String[0]);
    }
    
    class print {
    public static void print() {
        System.out.println("Start coding here: ");  // Gives instruction

        Scanner scanner = new Scanner(System.in);  // For user input
        String input = scanner.nextLine();  // Reads the entire input line

        // Check if the command starts with "print"
        if (input.startsWith("print(") && input.endsWith(")")) {
            // Extract the content between the parentheses
            int startIndex = input.indexOf("(") + 1;
            int endIndex = input.lastIndexOf(")");
            String toPrint = input.substring(startIndex, endIndex).replace("\"", ""); // Remove quotes
            System.out.println(toPrint);  // Print the extracted content
        } else {
            System.out.println("Syntax error or unknown command.");
        }

        scanner.close();  // Close the scanner to avoid resource leak
    }
}

class Tokenizer {
    // Define possible token types
    enum TokenType { NUMBER, OPERATOR, PARENTHESIS }

    // Represents a single token
    static class Token {
        TokenType type;
        String value;

        Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString() {
            return "Token{" +
                    "type=" + type +
                    ", value='" + value + '\'' +
                    '}';
        }
    }


    // Main tokenize function
    public List<Token> tokenize(String expression) {
        List<Token> tokens = new ArrayList<>(); // List to hold tokens
        char[] chars = expression.toCharArray(); // Convert input to characters
        StringBuilder numberBuffer = new StringBuilder(); // Temporary storage for multi-digit numbers

        for (char c : chars) {
            // Build numbers
            if (Character.isDigit(c) || c == '.') {
                numberBuffer.append(c); // Collect digits into number buffer
            } else {
                // If we were collecting a number, finalize it
                if (!numberBuffer.isEmpty()) {
                    tokens.add(new Token(TokenType.NUMBER, numberBuffer.toString()));
                    numberBuffer.setLength(0); // Clear the buffer
                }

                // Handle operators
                if (c == '+' || c == '-' || c == '*' || c == '/' || c == '%') {
                    tokens.add(new Token(TokenType.OPERATOR, String.valueOf(c)));
                }
                // Handle parentheses
                else if (c == '(' || c == ')') {
                    tokens.add(new Token(TokenType.PARENTHESIS, String.valueOf(c)));
                }
                // Skip spaces
                else if (Character.isWhitespace(c)) {
                    continue;
                } else {
                    throw new IllegalArgumentException("Invalid character in expression: " + c);
                }
            }
        }

        // Add any leftover number in the buffer
        if (!numberBuffer.isEmpty()) {
            tokens.add(new Token(TokenType.NUMBER, numberBuffer.toString()));
        }

        return tokens; // Return the list of tokens
    }
}
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder input = new StringBuilder();

        System.out.println("Please, enter your code here, to exit enter \"end\"");

        while(true){
            String line = scanner.nextLine();
            if(line.equals("end")) break;
            input.append(line);
            input.append("\n");
        }
   String[] linesOfInput = input.toString().split("\n");
        linesOfInput = removeEmptyLines(linesOfInput); // clear out empty lines
        

        for (int i = 0; i < linesOfInput.length; i++) {
            String line = linesOfInput[i];

            // handling an assignment operation
            if(line.contains("=")) {
                String[] part = line.split("=");
                String key = part[0].trim();
                int value = Integer.parseInt(part[1].trim());
                Main.values.put(key, value );
            
            }
        }
        
    }
}

