import java.util.*;

public class Main {

    public static Map<String, Integer> variables = new HashMap<>();

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
 // Method for evaluating arithmetic expressions
    // (defining whether we are assigning number value or we are changing value
    // according to already defined variable) (EXAMPLE: x = 10 or x = x + 5)
    private static int evaluateOperand(String operand) {
        operand = operand.trim();
        if (variables.containsKey(operand)) {
            return variables.get(operand); // Fetch the variable value if defined
        } else {
            return Integer.parseInt(operand); // Parse as integer
        }
    }

    // Method for evaluating arithmetic expressions
    public static int defineExpression(String varName, String expression) {
        int value;

        if (expression.contains("+")) {
            String[] operands = expression.split("\\+");
            value = evaluateOperand(operands[0]) + evaluateOperand(operands[1]);
            variables.put(varName, value);
        } else if (expression.contains("-")) {
            String[] operands = expression.split("-");
            value = evaluateOperand(operands[0]) - evaluateOperand(operands[1]);
        } else if (expression.contains("*")) {
            String[] operands = expression.split("\\*");
            value = evaluateOperand(operands[0]) * evaluateOperand(operands[1]);
        } else if (expression.contains("/")) {
            String[] operands = expression.split("/");
            value = evaluateOperand(operands[0]) / evaluateOperand(operands[1]);
        } else if (expression.contains("%")) {
            String[] operands = expression.split("%");
            value = evaluateOperand(operands[0]) % evaluateOperand(operands[1]);
        } else {
            // If no operator, it must be a single number or variable
            value = evaluateOperand(expression);
        }
        return value;
    }


 // Method to evaluate boolean conditions
    public static boolean evaluateCondition(String condition) {
        String[] parts = condition.split(" ");
        int left = evaluateOperand(parts[0]);
        String operator = parts[1];
        int right = evaluateOperand(parts[2]);

        return switch (operator) {
            case "==" -> left == right;
            case "!=" -> left != right;
            case "<" -> left < right;
            case "<=" -> left <= right;
            case ">" -> left > right;
            case ">=" -> left >= right;
            default -> throw new IllegalArgumentException("Invalid operator: " + operator);
        };
    }


    // Method to handle if statements
    public static void handleIfStatement(String[] lines, int startIndex) {
        // Defining syntax error
        if (!lines[startIndex].contains(":")) {
            System.out.println("Syntax Error, \":\" must be included, please try again");
            return;
        }
        String condition = lines[startIndex].substring(3, lines[startIndex].indexOf(':')).trim();
        boolean conditionResult = evaluateCondition(condition);
        int i = startIndex + 1;
        while (i < lines.length && !lines[i].trim().equals("else") && !lines[i].trim().startsWith("if") &&
        lines[i].startsWith("\t")) {
            if (conditionResult) {
                executeCommand(lines[i]);
            }
            i++;
        }
    }

    public static void handleWhileLoop(String[] lines, int startIndex) {
    // Defining syntax error
    if (!lines[startIndex].contains(":")) {
        System.out.println("Syntax Error, \":\" must be included, please try again");
        return;
    }
    String conditionLine = lines[startIndex].trim();
    String condition = conditionLine.substring(5, lines[startIndex].indexOf(':')).trim();
    List<String> loopBody = new ArrayList<>();
    int i = startIndex + 1;

    // collect all lines in the loop body
    while (i < lines.length && lines[i].startsWith("\t")) {
        loopBody.add(lines[i].trim());
        i++;
    }
    // execute loop while the condition is true
    while (evaluateCondition(condition)) {
        for (String command : loopBody) {
            executeCommand(command);
        }
    }
}
        /// Method to execute a command
    public static void executeCommand(String line) {
        line = line.trim();

        if (line.contains("=")) {
            // Handle variable assignment
            String[] parts = line.split("=");
            String varName = parts[0].trim();
            String expression = parts[1].trim();
            variables.put(varName, defineExpression(varName, expression)); // Evaluate and store the variable
        } else if (line.startsWith("print")) {
            executePrint(line);
        }
    }

    // method for printing
    public static void executePrint(String input) {
            int startIndex = input.indexOf("(") + 1;
            int endIndex = input.lastIndexOf(")");
            String toPrint = input.substring(startIndex, endIndex).replace( "\"", "").trim();

    if (variables.containsKey(toPrint)) {
        System.out.println(variables.get(toPrint)); //Print variable value
    } else {
        System.out.println(toPrint);  //Print raw string
    }
    }



class Tokenizer { // Tokenizer class:Supports integers, decimals, operators, and parentheses,Skips spaces for clean processing,Throws an error for invalid characters.

 
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
       StringBuilder numberBuffer = new StringBuilder(); // Collects multi-digit numbers


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
                if ("+-*/%".indexOf(c) >= 0) {
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



    // CustomInterpreter class 
    public static void interpretReversalCheck(String code) {
        int num = extractNumber(code, "num");
        int reversedNum = reverseNumber(num);
        System.out.println("Reversed Number: " + reversedNum);
    }

    public static void interpretFactorialCheck(String code) {
        int n = extractNumber(code, "n");
        int factorial = calculateFactorial(n);
        System.out.println("Factorial of " + n + ": " + factorial);
    }

    public static void interpretPalindromeCheck(String code) {
        int number = extractNumber(code, "number");
        int originalNumber = number;
        int reversedNumber = reverseNumber(number);

        if (originalNumber == reversedNumber) {
            System.out.println("Palindrome");
        } else {
            System.out.println("Not a palindrome");
        }
    }

    // Helper method to extract the number assigned to a variable
    public static int extractNumber(String code, String variable) {
        String[] lines = code.split("\n");
        for (String line : lines) {
            if (line.contains(variable)) {
                String[] parts = line.split("=");
                if (parts.length > 1) {
                    return Integer.parseInt(parts[1].trim());
                }
            }
        }
        throw new IllegalArgumentException("Variable " + variable + " not found.");
    }

    // Method to reverse the digits of a number
    public static int reverseNumber(int num) {
        int reversedNum = 0;
        while (num > 0) {
            int digit = num % 10;
            reversedNum = reversedNum * 10 + digit;
            num = num / 10;
        }
        return reversedNum;
    }

    // Method to calculate the factorial of a number
    public static int calculateFactorial(int n) {
        int factorial = 1;
        for (int i = 1; i <= n; i++) {
            factorial *= i;
        }
        return factorial;
    }

     // Method to check if a number is a palindrome
    public static boolean isPalindrome(int number) {
        int originalNumber = number;
        int reversedNumber = reverseNumber(number);
        return originalNumber == reversedNumber;
    }
   

public class CustomInterpreter {
    public static void main(String[] args) {
        // Prompt user to input Python-like code
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please, enter your Python-like code here. Type 'end' to exit.");

        // Read the input code line by line
        StringBuilder input = new StringBuilder();
        while (true) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("end")) break; // Stop input when "end" is entered
            input.append(line).append("\n"); // Append the input line to the code
        }

        // Convert the input to a string and decide which operation to interpret
        String code = input.toString();
        if (code.contains("fibonacci")) {
            interpretRecursiveFibonacci(code); // Handle Fibonacci calculation
        } else if (code.contains("sum_digits")) {
            interpretSumOfDigits(code); // Handle sum of digits calculation
        } else {
            System.out.println("No valid operation found in the input.");
        }
    }

    // Method to interpret Python-like code for recursive Fibonacci calculation
    public static void interpretRecursiveFibonacci(String code) {
        // Ensure the code contains necessary keywords for Fibonacci calculation
        if (!code.contains("let") || !code.contains("fibonacci")) {
            System.out.println("No valid Fibonacci calculation found in the input.");
            return;
        }

        try {
            // Parse the variable declaration (e.g., "let n = 10")
            String[] lines = code.split("\n");
            int n = 0; // Default value for the Fibonacci term
            for (String line : lines) {
                if (line.startsWith("let")) { // Look for a "let" statement
                    String[] parts = line.split("=");
                    n = Integer.parseInt(parts[1].trim()); // Extract and parse the value of n
                }
            }

            // Compute the Fibonacci number recursively
            int result = calculateFibonacci(n);
            System.out.println("Fibonacci(" + n + ") = " + result); // Print the result
        } catch (Exception e) {
            System.out.println("Error interpreting code: " + e.getMessage());
        }
    }

    // Recursive method to calculate the Nth Fibonacci number
    public static int calculateFibonacci(int n) {
        // Base cases: F(0) = 0, F(1) = 1
        if (n <= 1) return n;
        // Recursive case: F(n) = F(n-1) + F(n-2)
        return calculateFibonacci(n - 1) + calculateFibonacci(n - 2);
    }

    // Method to interpret Python-like code for sum of digits calculation
    public static void interpretSumOfDigits(String code) {
        // Ensure the code contains necessary keywords for sum of digits calculation
        if (!code.contains("let") || !code.contains("sum_digits")) {
            System.out.println("No valid sum of digits calculation found in the input.");
            return;
        }

        try {
            // Parse the variable declaration (e.g., "let num = 1234")
            String[] lines = code.split("\n");
            int number = 0; // Default value for the number
            for (String line : lines) {
                if (line.startsWith("let")) { // Look for a "let" statement
                    String[] parts = line.split("=");
                    number = Integer.parseInt(parts[1].trim()); // Extract and parse the number
                }
            }

            // Compute the sum of the digits
            int result = sumDigits(number);
            System.out.println("Sum of digits(" + number + ") = " + result); // Print the result
        } catch (Exception e) {
            System.out.println("Error interpreting code: " + e.getMessage());
        }
    }

    // Method to calculate the sum of digits of a number
    public static int sumDigits(int number) {
        int sum = 0; // Initialize the sum
        while (number > 0) {
            sum += number % 10; // Add the last digit to the sum
            number /= 10; // Remove the last digit
        }
        return sum; // Return the total sum
    }
}


    


    }
    
public class PythonLikeInterpreter {

    private static final Map<String, Integer> variables = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> codeLines = new ArrayList<>();

        System.out.println("Please, enter your code here. Type \"end\" to finish:");

        // Read all lines of code until 'end' is entered
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.equals("end")) break;
            codeLines.add(line);
        }

        executeCode(codeLines);
    }

    private static void executeCode(List<String> codeLines) {
        int i = 0;
        // Execute code line by line
        while (i < codeLines.size()) {
            String line = codeLines.get(i).trim();

            if (line.startsWith("let ")) {
                handleAssignment(line);
            } else if (line.startsWith("print")) {
                handlePrint(line);
            } else if (line.startsWith("while ")) {
                i = handleWhileLoop(codeLines, i);
            }
            i++;
        }
    }

    private static void handleAssignment(String line) {
        String[] parts = line.replace("let ", "").split("=");
        String variable = parts[0].trim();
        int value = evaluateExpression(parts[1].trim());
        variables.put(variable, value);
    }

    private static void handlePrint(String line) {
        String variable = line.replace("print", "").trim();
        if (variables.containsKey(variable)) {
            System.out.println(variables.get(variable));
        } else {
            System.out.println("Error: Undefined variable " + variable);
        }
    }

    private static int handleWhileLoop(List<String> codeLines, int currentIndex) {
        String conditionLine = codeLines.get(currentIndex).replace("while", "").trim();
        String[] conditionParts = conditionLine.split("<");
        String variable = conditionParts[0].trim();
        int limit = evaluateExpression(conditionParts[1].trim());

        // Collect loop body lines
        List<String> loopBody = new ArrayList<>();
        int i = currentIndex + 1;
        while (i < codeLines.size() && !codeLines.get(i).trim().equals("end")) {
            loopBody.add(codeLines.get(i).trim());
            i++;
        }

        // Execute the loop
        while (variables.getOrDefault(variable, 0) < limit) {
            // Execute the loop body
            for (String line : loopBody) {
                if (line.startsWith("let ")) {
                    handleAssignment(line);
                } else if (line.startsWith("print")) {
                    handlePrint(line);
                }
            }
            // Ensure the variable gets updated after each iteration
            if (variables.containsKey(variable)) {
                int updatedValue = variables.get(variable) + 1;
                variables.put(variable, updatedValue);  // Update the variable's value
            }
        }

        return i; // Return the line index to continue after the loop
    }

    private static int evaluateExpression(String expression) {
        // Handle simple integer expressions
        String[] parts = expression.split("\\+");
        int value = 0;
        for (String part : parts) {
            part = part.trim();
            if (variables.containsKey(part)) {
                value += variables.get(part); // Add the variable's value
            } else {
                try {
                    value += Integer.parseInt(part); // Try parsing the value as an integer
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Invalid expression: " + expression);
                }
            }
        }
        return value;
    }
}

 





    
    
    
}

