# ProjectFoP
Interprets codes written in Python language and shows results in java.

Creating a python interpreter. 

Introduction to Interpreter: Our team designed and implemented a simple interpreter for python. Program works in the following way: when user starts the program, they are given a list of algorithms which can be handled by program. User has to choose Index of their desired algorithm and insert the number. After that, they are given a snippet of python algorithm and the output of that code. Algorithms are already provided to the user, so there is no need for them to input code by themselves. 


Features of the program: Program can handle essential features such as:
1) Variable assignment -> Variables are kept in the Map<String, Integer>, so they can be easily accessed when needed. We can simply retrieve already defined values or simply assign a new one.

2) Arithmetic operations such as: +, -, *, /, % -> Program can handle simple arithmetic operations. (Operations like +=, -=, *=, /=, %= are not supported by our program).

3) Conditional statements: if, else -> Program handles if/else statements, via checking if given condition is true. Thereby, our program can handle Boolean expressions and return answer of the condition on every iteration. Additionally, program handles syntax errors, such as lack of ":" at the end of if/else condition statement(According to python syntax).

4)Iterative control flow: while loop-> If keyword "while" is detected, program starts a loop, which is executed while given condition is true. Program uses List<String> to collect all lines of loop, which are detected by "\t" ,as in python body of while statement is distincted by tabs. Program is also designed to handle syntax errors, such as defining lack of ":" at the end of while condition statement(According to python syntax). 
5) Output operation: printing -> If keyword "print()" is detected, program outputs statement within parentheses.


Process of creating project: As a group of 4, our first step was analyzing problem which had to be solved, creating a plan for solving that problem and finding ways to implement those solutions. Group members worked on various function calls, to handle different operations and then we integrated all out work. Even though, there were a lot of problems arisen during process, we as a team handled those problems and did our best to implement simple interpreter for python.

