package com.company;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class sum implements CalculatorInterface {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void sum(String[] array) {
        boolean flag = false;
        boolean var = false;
        Map<String, Integer> mapForVariables = new HashMap<>();
        while (!flag) {
            String input = scanner.nextLine();
            String validation = validator(input);

            switch (validation) {
                case "/exit":
                    System.out.println("Shut down.... \n See you later");
                    return;
                case "/help":
                    System.out.println("Your best friend in solving math equations:\n Example of imput: 2 + 5 + 7 \n/\n a=100 \n b=100 \na * b + ( a * b )" +
                            "\n !!!!!!!! There must be 1 space between every digit, sign or paranthesis");
                    continue;
                case "The expression is wrong, try again !":
                    System.out.println("The expression is invalid, try again !!");
                    continue;
                case "The command is wrong!":
                    System.out.println("The command is invalid : Available commands : \n/help (to see how the program works) \n /exit ( to exit the program)");
                    continue;
                default:
                    break;
            }
            String validatorVar = checkVar(input);
            Matcher matchCommand = Pattern.compile("([a-z]* = [0-9]+|[a-z]* = [a-z]*)"
                    , Pattern.CASE_INSENSITIVE)
                    .matcher(validatorVar);
            Matcher invalidIdentifier = Pattern.compile("([a-z]+[0-9]+ = \\w+|[0-9]+[a-z]+ = \\w+)"
                    , Pattern.CASE_INSENSITIVE)
                    .matcher(validatorVar);
            Matcher invalidAssignment = Pattern.compile("([0-9]* = [0-9]+ = [0-9]+|[a-z]* = [0-9]+[a-z] = [0-9]+[a-z]+ |[a-z]* = [a-z]+[0-9]+|" +
                            "[a-z]* = [0-9]+[a-z]+)| [a-z]* = [a-z]+[0-9]+[a-z]+ [a-z]* = [0-9]+[a-z]+[0-9]+|[a-z]* = [0-9]+ = [0-9]+"
                    , Pattern.CASE_INSENSITIVE)
                    .matcher(validatorVar);
            Matcher oneInput = Pattern.compile("[a-z]+", Pattern.CASE_INSENSITIVE)
                    .matcher(validatorVar);


            if (matchCommand.matches()) {
                String[] arrayForVariables = validatorVar.split(" ");

                if (mapForVariables.containsKey(arrayForVariables[arrayForVariables.length - 1])) {
                    mapForVariables.put(arrayForVariables[0],mapForVariables.get(arrayForVariables[arrayForVariables.length - 1]));
                } else {
                    try {
                        mapForVariables.put(arrayForVariables[0], Integer.parseInt(arrayForVariables[arrayForVariables.length - 1]));
                    } catch (NumberFormatException ignored) {
                        System.out.println("Unknown variable");
                    }
                }

            } else if (invalidIdentifier.matches()) {
                System.out.println("The identifier is invalid");
            } else if (invalidAssignment.matches()) {
                System.out.println("The assignment is invalid");
            } else if (oneInput.matches()) {
                if (mapForVariables.containsKey(input)) {
                    System.out.println(mapForVariables.get(input));
                } else {
                    System.out.println("the input is wrong");
                }
            } else {
                try {
                    String[] fExp = validatorVar.split(" ");
                    for (Map.Entry<String, Integer> entry : mapForVariables.entrySet()) {
                        for (int i = 0; i < fExp.length; i++) {
                            if (fExp[i].equals(entry.getKey())) {
                                fExp[i] = String.valueOf(entry.getValue());
                            }
                        }
                    }
                    StringJoiner Join = new StringJoiner(" ");

                    for (String s : fExp) {
                        Join.add(s);
                    }

                    String finalJoin = Join.toString();
                    String finalResult = ReplaceRegex(finalJoin);

                    if (finalResult.isEmpty()) {
                        System.out.print("");
                    } else {
                        System.out.println(Stackk.evaluate(finalResult));
                    }

                } catch (NumberFormatException ignored) {
                }
            }
        }
    }


    private static String ReplaceRegex(String input) {
        // Removing the spaces
        String replaceSpaces = Pattern.compile("\\s+").matcher(input).replaceAll(" ");

        // If we have 2 or more +, we are gonna merge them to one single +
        String replacePluses = Pattern.compile("\\+{2,}").matcher(replaceSpaces).replaceAll("+");

        //If we have 3 or more -, we are gonna merge it into 1 single -,
        String mergeMinus = Pattern.compile("-{3,}").matcher(replacePluses).replaceAll("-");

        // If we have 2 -, they will be replaced with a + (-- in math=+)
        String replaceDoubleMin = Pattern.compile("-{2}").matcher(mergeMinus).replaceAll("+");

       // String refactoringZERO = Pattern.compile("\\b-.*").matcher(replaceDoubleMin).replaceAll("0 -");
        //Will replace - by 0 -.

        // If we have -+, replaced with -
        String result = Pattern.compile("(-\\+|\\+-)").matcher(replaceDoubleMin).replaceAll("-");
        return result;
    }

    private static String validator(String input) {
        // Input validation: for 123+, 2555555+
        Matcher endWithASign = Pattern.compile("(.*?\\+\\s*?|.*?-\\s*?)", Pattern.CASE_INSENSITIVE).matcher(input);
        // Wrong input for commands
        Matcher matchCommand = Pattern.compile("/[a-z]*", Pattern.CASE_INSENSITIVE).matcher(input);
        //if we don't have a sign between them
        boolean containSign = input.contains("+") || input.contains("-") || input.contains("=") || input.contains("*") || input.contains("/");
        boolean knownCommands = !(input.equals("/exit") || input.equals("/help") || input.equals(""));
        boolean isHigherThan = input.length() > 5;

        if(endWithASign.matches() && knownCommands){
            return "The expression is wrong, try again !";
        } else if (matchCommand.matches() && knownCommands){
            return "The command is wrong!";
        }
         else if (!containSign && knownCommands && isHigherThan){
            return "The expression is wrong, try again !";
        }
        else {
            return input;
        }
    }

    private static String checkVar(String userInput) {
        // Example: a=5 will be replaced by a = 5...
        Pattern checkEquals = Pattern.compile("([a-z]*=[0-9]+|[a-z]*=[a-z]*)", Pattern.CASE_INSENSITIVE);
        Matcher matchEquals = checkEquals.matcher(userInput);

        if (matchEquals.matches()){
            return userInput.replace("=", " = ").trim();
        } else {
            Pattern extraSpace = Pattern.compile("\\s+");
            Matcher matchSpace = extraSpace.matcher(userInput);
            //Will remove all spaces between the expression.
            return matchSpace.replaceAll(" ");
        }
    }
}
