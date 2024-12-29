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
                String varName = part[0].trim();
                String expression = part[1].trim();
                variables.put(varName, defineExpression(varName, expression));
            }
            //handling print 
            if (line.startsWith("print")) {
                    executePrint(line);
             }
            // handling if statement
            if (line.startsWith("if")){
                    handleIfStatement(linesOfInput, i);
             }
            // handling while statement
                if(line.startsWith("while")){
                handleWhileLoop(linesOfInput, i);
            }
        }
        
    }


public class SimpleInterpreter {

    // Method to sum the first N numbers
    public static int sumOfFirstNNumbers(int N) {
        int sum = 0;
        int i = 1;
        while (i <= N) {
            sum += i;
            i++;
        }
        return sum;
    }

    // Method to compute the factorial of N
    public static int factorial(int N) {
        int result = 1;
        while (N > 1) {
            result *= N;
            N--;
        }
        return result;
    }

    // Method to compute the GCD of two numbers
    public static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // Method to reverse a number
    public static int reverseNumber(int n) {
        int reversed = 0;
        while (n != 0) {
            int digit = n % 10;
            reversed = reversed * 10 + digit;
            n /= 10;
        }
        return reversed;
    }

    // Method to check if a number is prime
    public static boolean isPrime(int n) {
        if (n <= 1) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    // Method to check if a number is a palindrome
    public static boolean isPalindrome(int n) {
        int original = n;
        int reversed = 0;
        while (n != 0) {
            int digit = n % 10;
            reversed = reversed * 10 + digit;
            n /= 10;
        }
        return original == reversed;
    }

    // Method to find the largest digit in a number
    public static int findLargestDigit(int n) {
        int largest = 0;
        while (n != 0) {
            int digit = n % 10;
            largest = Math.max(largest, digit);
            n /= 10;
        }
        return largest;
    }

    // Method to sum the digits of a number
    public static int sumOfDigits(int n) {
        int sum = 0;
        while (n != 0) {
            sum += n % 10;
            n /= 10;
        }
        return sum;
    }

    // Method to print the multiplication table for N
    public static void multiplicationTable(int n) {
        int i = 1;
        while (i <= 10) {
            System.out.println(n + " * " + i + " = " + (n * i));
            i++;
        }
    }

    // Method to calculate the Nth Fibonacci number
    public static int fibonacci(int n) {
        if (n <= 1) return n;
        int a = 0, b = 1, result = 0;
        int i = 2; // Since we already have the first two numbers
        while (i <= n) {
            result = a + b;
            a = b;
            b = result;
            i++;
        }
        return result;
    }

    // Method to process user input and execute the corresponding algorithm
    public static void executeCommand(String command) {
        String[] parts = command.split(" ");
        String operation = parts[0];
        int[] args = new int[parts.length - 1];

        // Parse arguments from the input
        for (int i = 1; i < parts.length; i++) {
            args[i - 1] = Integer.parseInt(parts[i]);
        }

        // Execute the corresponding method based on the command
        switch (operation) {
            case "sumOfFirstNNumbers":
                System.out.println(sumOfFirstNNumbers(args[0]));
                break;
            case "factorial":
                System.out.println(factorial(args[0]));
                break;
            case "gcd":
                System.out.println(gcd(args[0], args[1]));
                break;
            case "reverseNumber":
                System.out.println(reverseNumber(args[0]));
                break;
            case "isPrime":
                System.out.println(isPrime(args[0]));
                break;
            case "isPalindrome":
                System.out.println(isPalindrome(args[0]));
                break;
            case "findLargestDigit":
                System.out.println(findLargestDigit(args[0]));
                break;
            case "sumOfDigits":
                System.out.println(sumOfDigits(args[0]));
                break;
            case "multiplicationTable":
                multiplicationTable(args[0]);
                break;
            case "fibonacci":
                System.out.println(fibonacci(args[0]));
                break;
            default:
                System.out.println("Unknown command.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a command to execute (or 'exit' to quit):");
        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            executeCommand(input);
        }
    }
}



    
}

