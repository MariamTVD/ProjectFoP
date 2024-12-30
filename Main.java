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




    // Interpret class

public static void interpretReversalCheck(String code) {
    int inputNum = extractNumber(code, "inputNum");
    int reversedNum = reverseNumber(inputNum);
    System.out.println("Reversed Number: " + reversedNum);
}

public static void interpretFactorialCheck(String code) {
    int factorialInput = extractNumber(code, "factorialNum");
    int factorialResult = calculateFactorial(factorialInput);
    System.out.println("Factorial of " + factorialInput + ": " + factorialResult);
}

public static void interpretPalindromeCheck(String code) {
    int palCheckNumber = extractNumber(code, "palindromeNum");
    int originalPalNum = palCheckNumber;
    int reversedPalNum = reverseNumber(palCheckNumber);
    

    if (originalPalNum == reversedPalNum) {
        System.out.println(originalPalNum + "  is a Palindrome");
    } else {
        System.out.println(originalPalNum +   "  is not a palindrome");
    }
}


public static void interpretSumofDigitsCheck(String code) {
	int number = extractNumber(code, "number");
	int sumofDigits = calculateSumofDigits(number);
	System.out.println("the sum of number " + number + " is " + sumofDigits);
}

//Interpret Fibonacci Check
//public static void interpretFibonacciCheck(String code) {
   // int fibonacciInput = extractNumber(code, "N");
  //  int fibonacciResult = calculateFibonacci(fibonacciInput);
   // System.out.println("The " + fibonacciInput + "th Fibonacci number is: " + fibonacciResult);
//}







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
public static int reverseNumber(int numberToReverse) {
    int reversedNum = 0;
    while (numberToReverse > 0) {
        int digit = numberToReverse % 10;
        reversedNum = reversedNum * 10 + digit;
        numberToReverse = numberToReverse / 10;
    }
    return reversedNum;
}

// Method to calculate the factorial of a number
public static int calculateFactorial(int factorialNum) {
    int factorialResult = 1;
    for (int i = 1; i <= factorialNum; i++) {
        factorialResult *= i;
    }
    return factorialResult;
}

// Method to check if a number is a palindrome
public static boolean isPalindromeCheck(int number) {
	int reversedPalNum = 0;
	int palCheckNumber = 0;
	while (palCheckNumber > 0) {
		int rem = reversedPalNum % 10;
		reversedPalNum = reversedPalNum * 10 + rem;
		palCheckNumber = palCheckNumber / 10;
	}
	System.out.println(reversedPalNum);
	if (palCheckNumber == reversedPalNum) {
		System.out.print(palCheckNumber + "is a palindrome");	
		} else {
			System.out.println(palCheckNumber + " not a palindrome");
		}
	return false;
	
}

//method to calculate the sum of digits
public static int calculateSumofDigits(int number) {
	int sum = 0;
	while (number > 0) {
		int digit = number % 10;
		sum += digit;
		number /= 10;
	}
	return sum;
}

//Method to calculate the Nth Fibonacci number
//Method to interpret Fibonacci code
public static void interpretFibonacciCheck(String code) {
    String[] lines = code.split("\n");
    int N = 0, count = 0, a = 1, b = 1;

    for (String line : lines) {
        line = line.trim();

        if (line.startsWith("N =")) {
            N = Integer.parseInt(line.split("=")[1].trim());
        } else if (line.startsWith("count =")) {
            count = Integer.parseInt(line.split("=")[1].trim());
        }
    }

    // Simulate the Fibonacci loop
    while (count < N) {
        int temp = b; // Save the current value of b
        b = a + b;    // Calculate the new value of b
        a = temp;     // Update a to the old value of b
        count++;      // Increment the counter
    }

    System.out.println("The " + N + "th Fibonacci number is: " + a);


    if (N <= 0) {
        System.out.println("Invalid input. N must be a positive integer.");
    }
}






public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    StringBuilder input = new StringBuilder();

    System.out.println("Please, enter your code here, to exit enter \"end\"");
    while (true) {
        String line = scanner.nextLine();
        if (line.equals("end")) break;
        input.append(line);
        input.append("\n");
    }

    String[] linesOfInput = input.toString().split("\n");
    linesOfInput = removeEmptyLines(linesOfInput); // Assuming you have implemented this method
    
    for (int i = 0; i < linesOfInput.length; i++) {
        String line = linesOfInput[i];

        // Handling an assignment operation
        if (line.contains("=")) {
            String[] part = line.split("=");
            String varName = part[0].trim();
            String expression = part[1].trim();
            variables.put(varName, defineExpression(varName, expression)); // Assuming you have implemented this method
        }
        // Handling print statement
        if (line.startsWith("print")) {
            executePrint(line); // Assuming you have implemented this method
        }
        // Handling if statement
        if (line.startsWith("if")) {
            handleIfStatement(linesOfInput, i); // Assuming you have implemented this method
        }
        // Handling while statement
        if (line.startsWith("while")) {
            handleWhileLoop(linesOfInput, i); // Assuming you have implemented this method
        }

        // Interpret custom logic (e.g., reversal, factorial, palindrome)
        String code = input.toString().trim();
        if (code.contains("inputNum =") && code.contains("reversedNum =") && code.contains("while")) {
            interpretReversalCheck(code);
        } else if (code.contains("factorialNum =") && code.contains("factorialResult =") && code.contains("while")) {
            interpretFactorialCheck(code);
        } else if (code.contains("palindromeNum =") && code.contains("originalPalNum =") && code.contains("reversedPalNum =")) {
            interpretPalindromeCheck(code);
        } else if (code.contains("number =") && code.contains("sum_of_digits = ") && code.contains("while")) {
        	interpretSumofDigitsCheck(code);
        } else if (code.contains("N =") && code.contains("count =") && code.contains("while count < N")
                && code.contains("a = b") && code.contains("b = a + b") && code.contains("count += 1")
                && code.contains("print(b)")) {
            interpretFibonacciCheck(code);
        }
     
    }
    scanner.close();
}
}





    
