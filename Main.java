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
        } else if (expression.contains("//")) {
            String[] operands = expression.split("//");
            try {
                int dividend = evaluateOperand(operands[0].trim());
                int divisor = evaluateOperand(operands[1].trim());
                value = divisor == 0 ? dividend : dividend / divisor; // Prevent zero division
            } catch (ArithmeticException e) {
                return 0;
            }
        } else if (expression.contains("%")) {
            String[] operands = expression.split("%");
            try {
                value = evaluateOperand(operands[0]) % evaluateOperand(operands[1]);
            } catch (ArithmeticException e){ return evaluateOperand(operands[0]); }
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
	// Method to handle else statement
	 public static void handleElseStatement(String[] lines, int startIndex) {
        int i = startIndex + 1;
        
        while (i < lines.length && !lines[i].trim().startsWith("if") && lines[i].startsWith("\t")) {
            executeCommand(lines[i]);
            i++;
        }
    }
	 // Method to execute a command
    public static void executeCommand(String line) {
        line = line.trim();

        if (line.contains("=")) {
            // Handle variable assignment
            String[] parts = line.split("=", 2);
            String varName = parts[0].trim();
            String expression = parts[1].trim();
            variables.put(varName, defineExpression(varName,expression)); // Evaluate and store the variable
        } else if (line.startsWith("print")) {
            executePrint(line);
        }
    }
	
    // private static void handleAssignment(String line) {
    //     String[] parts = line.replace("let ", "").split("=");
    //     String variable = parts[0].trim();
    //     int value = evaluateExpression(parts[1].trim());
    //     variables.put(variable, value);
    // }

    // private static void handlePrint(String line) {
    //     String variable = line.replace("print", "").trim();
    //     if (variables.containsKey(variable)) {
    //         System.out.println(variables.get(variable));
    //     } else {
    //         System.out.println("Error: Undefined variable " + variable);
    //     }
    // }

    private static void handleWhileLoop (String[] lines, int startIndex) {
	    // Defining syntax error
        if (!lines[startIndex].contains(":")) {
            System.out.println("Syntax Error, \":\" must be included, please try again");
            return;
        }
        String conditionLine = lines[startIndex].trim();
        String condition = conditionLine.substring(5, lines[startIndex].indexOf(':')).trim();
        List<String> loopBody = new ArrayList<>();
        int i = startIndex + 1;

        // Collect loop body lines
        while (i < lines.length && lines[i].startsWith("\t")) {
             loopBody.add(lines[i].trim());
            i++;
        }
// execute loop while the condition is true
        while (evaluateCondition(condition)) {
            for (String command : loopBody) {
		 // checking if we have nested loop
		if(command.contains("if"))
                    handleIfStatement(loopBody.toArray(new String[0]), loopBody.indexOf(command));
                executeCommand(command);
            }
        }
       
    }

    // private static int evaluateExpression(String expression) {
    //     // Handle simple integer expressions
    //     String[] parts = expression.split("\\+");
    //     int value = 0;
    //     for (String part : parts) {
    //         part = part.trim();
    //         if (variables.containsKey(part)) {
    //             value += variables.get(part); // Add the variable's value
    //         } else {
    //             try {
    //                 value += Integer.parseInt(part); // Try parsing the value as an integer
    //             } catch (NumberFormatException e) {
    //                 throw new RuntimeException("Invalid expression: " + expression);
    //             }
    //         }
    //     }
    //     return value;
    
    // }

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

	
// Creating method for interpreting python code
public static void InterpretInput(String input) {
        String[] linesOfInput = input.split("\n");
        linesOfInput = removeEmptyLines(linesOfInput); // clear out empty lines

        for (int i = 0; i < linesOfInput.length; i++) {
            String line = linesOfInput[i];

            // handling an assignment operation
            if (line.contains("=") && !line.startsWith("while") && !line.startsWith("if")) {
                String[] part = line.split("=");
                String varName = part[0].trim();
                String expression = part[1].trim();
                variables.put(varName, defineExpression(varName, expression));
            }
            // handling print assignment
            if (line.startsWith("print"))
                executePrint(line);
            // handling if 
            if (line.startsWith("if"))
                handleIfStatement(linesOfInput, i);
            // handling else
	    if (line.startsWith("else"))
                handleElseStatement(linesOfInput, i);
            // handling while loop
            if (line.startsWith("while")) {
                handleWhileLoop(linesOfInput, i);
            }
        }

    }


	

public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    String input;
 System.out.println("Please, enter algorithm number here, to exit enter \"end\"");
        System.out.println("""
                              1. Sum of First N Numbers
                              2. Factorial of N
                              3. GCD of Two Numbers
                              4. Reverse a Number
                              5. Check if a Number is Prime
                              6. Check if a Number is Palindrome
                              7. Find the Largest Digit in a Number
                              8. Sum of Digits
                              9. Multiplication Table
                              10. Nth Fibonacci Number
                """);

        while (true) {
            System.out.print("Enter chosen number or enter \"end\" to exit : ");
            String line = scanner.nextLine();

            // Exit condition
            if (line.equalsIgnoreCase("end")) {
                System.out.println("Exiting program. Goodbye!");
                break;
            }

            try {
                int choice = Integer.parseInt(line);
                switch (choice) {
                    case 1 -> input = TestAlgorithms.alg1;
                    case 2 -> input = TestAlgorithms.alg2;
                    case 3 -> input = TestAlgorithms.alg3;
                    case 4 -> input = TestAlgorithms.alg4;//Algorithm prints 43210 instead of 4321
                    case 5 -> input = TestAlgorithms.alg5;//Algorithm does not print anything
                    case 6 -> input = TestAlgorithms.alg6;//Algorithm prints false, as alg4 is not working
                    case 7 -> input = TestAlgorithms.alg7;//Algorithm does not change value of variable "largest"
                    case 8 -> input = TestAlgorithms.alg8;
                    case 9 -> input = TestAlgorithms.alg9;
                    case 10 -> input = TestAlgorithms.alg10;
                    default -> {
                        System.out.println("Invalid choice. Please enter a number between 1 and 10.");
                        continue;
                    }
                }
                System.out.println("Your selected snippet of code: " + "\n" + input + "\n");
                System.out.print("Answer is " + "\n");
                InterpretInput(input);
                System.out.println();

            } catch (NumberFormatException e) {}
               
       

        // // Interpret custom logic (e.g., reversal, factorial, palindrome)
        // String code = input.toString().trim();
        // if (code.contains("inputNum =") && code.contains("reversedNum =") && code.contains("while")) {
        //     interpretReversalCheck(code);
        // } else if (code.contains("factorialNum =") && code.contains("factorialResult =") && code.contains("while")) {
        //     interpretFactorialCheck(code);
        // } else if (code.contains("palindromeNum =") && code.contains("originalPalNum =") && code.contains("reversedPalNum =")) {
        //     interpretPalindromeCheck(code);
        // } else if (code.contains("number =") && code.contains("sum_of_digits = ") && code.contains("while")) {
        // 	interpretSumofDigitsCheck(code);
        // } else if (code.contains("N =") && code.contains("count =") && code.contains("while count < N")
        //         && code.contains("a = b") && code.contains("b = a + b") && code.contains("count += 1")
        //         && code.contains("print(b)")) {
        //     interpretFibonacciCheck(code);
        // }

   
}
}
}
 
