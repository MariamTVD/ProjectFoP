import java.util.Scanner;
import java.util.*;

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
