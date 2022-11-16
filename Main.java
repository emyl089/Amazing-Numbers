package numbers;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to Amazing Numbers! \n");
        System.out.println("""
                Supported requests:\s
                - enter a natural number to know its properties;\s
                - enter two natural numbers to obtain the properties of the list:\s
                  * the first parameter represents a starting number;\s
                  * the second parameter shows how many consecutive numbers are to be printed;\s
                - two natural numbers and properties to search for;\s
                - a property preceded by minus must not be present in numbers;\s
                - separate the parameters with one space;\s
                - enter 0 to exit.\s
                """);

        enterNumber();
    }

    private static void enterNumber() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter a request: ");
        String input = scanner.nextLine();

        String[] inputParts = input.split(" ");

        switch (inputParts.length) {
            case 1 -> checkNumber(Long.parseLong(inputParts[0]));
            case 2 -> checkNumber(Long.parseLong(inputParts[0]), Integer.parseInt(inputParts[1]));
            default -> {
                if (inputParts.length > 2) {
                    String[] insertedProperties = new String[inputParts.length - 2];
                    System.arraycopy(inputParts, 2, insertedProperties, 0, inputParts.length - 2);
                    checkNumber(Long.parseLong(inputParts[0]), Integer.parseInt(inputParts[1]), insertedProperties);
                } else System.out.println("Wrong input!");
            }
        }

        enterNumber();
    }

    public static ArrayList<String> propertiesList = new ArrayList<>(Arrays.asList("EVEN", "ODD", "BUZZ", "DUCK", "PALINDROMIC", "GAPFUL", "SPY", "SQUARE", "SUNNY", "JUMPING", "HAPPY", "SAD", "-EVEN", "-ODD", "-BUZZ", "-DUCK", "-PALINDROMIC", "-GAPFUL", "-SPY", "-SQUARE", "-SUNNY", "-JUMPING", "-HAPPY", "-SAD"));

    private enum NumberProperties {
        EVEN(1),
        ODD(1),
        BUZZ(2),
        DUCK(3),
        PALINDROMIC(4),
        GAPFUL(5),
        SPY(3),
        SQUARE(6),
        SUNNY(6),
        JUMPING(7),
        HAPPY(8),
        SAD(8);

        private int value;
        NumberProperties(int value) {
            this.value = value;
        }

        private void toggleValue() {
            this.value *= -1;
        }
    }

    private static void checkNumber(long firstParameter) {
        if (firstParameter > 0) {
            System.out.println("Properties of " + firstParameter);
            System.out.println("even: " + checkParity(firstParameter));
            System.out.println("odd: " + !checkParity(firstParameter));
            System.out.println("buzz: " + checkBuzz(firstParameter));
            System.out.println("duck: " + checkDuck(firstParameter));
            System.out.println("palindromic: " + checkPalindromic(firstParameter));
            System.out.println("gapful: " + checkGapful(firstParameter));
            System.out.println("spy: " + checkSpy(firstParameter));
            System.out.println("square: " + checkSquare(firstParameter));
            System.out.println("sunny: " + checkSunny(firstParameter));
            System.out.println("jumping: " + checkJumping(firstParameter));
            System.out.println("happy: " + checkHappySad(firstParameter));
            System.out.println("sad: " + !checkHappySad(firstParameter));
        } else if (firstParameter == 0) {
            exit();
        } else {
            System.out.println("The first parameter should be a natural number or zero.");
            enterNumber();
        }
    }

    private static void checkNumber(long firstParameter, int secondParameter) {
        if (firstParameter > 0) {
            if (secondParameter > 0) {
                ArrayList<Long> longs = new ArrayList<>();
                long tempLong = firstParameter;
                for (int i = 0; i < secondParameter; i++) {
                    longs.add(tempLong);
                    tempLong++;
                }

                for (Long number :
                        longs) {
                    printProperties(number);
                }
            } else {
                System.out.println("The second parameter should be a natural number.");
                enterNumber();
            }
        } else if (firstParameter== 0) {
            exit();
        } else {
            System.out.println("The first parameter should be a natural number or zero.");
            enterNumber();
        }
    }

    private static void checkNumber(long firstParameter, int secondParameter, String[] multipleParameters) {
        int counter = 0;
        ArrayList<String> wrongProperties = new ArrayList<>();

        if (firstParameter > 0) {
            if (secondParameter > 0) {
                //Check validity of the parameters
                for (String parameter : multipleParameters) {
                    if (!checkValidity(parameter.toUpperCase())) {
                        wrongProperties.add(parameter);
                    }
                }

                //If there are wrong parameters, print a message
                if (!wrongProperties.isEmpty()) {
                    if (wrongProperties.size() < 2) {
                        System.out.printf("The property [" + wrongProperties + "] is wrong.\n" +
                                "Available properties: [EVEN, ODD, BUZZ, DUCK, PALINDROMIC, GAPFUL, SPY, SQUARE, SUNNY, JUMPING, HAPPY, SAD]");
                        enterNumber();
                    } else {
                        System.out.printf("The properties [" + wrongProperties + "] are wrong.\n" +
                                "Available properties: [EVEN, ODD, BUZZ, DUCK, PALINDROMIC, GAPFUL, SPY, SQUARE, SUNNY, JUMPING, HAPPY, SAD]");
                        enterNumber();
                    }
                }

                //Check mutual exclusive between parameters
                for (int i = 0; i < multipleParameters.length; i++) {
                    if (multipleParameters[i].contains("-")  && NumberProperties.valueOf(multipleParameters[i].toUpperCase().replace("-","")).value > 0) NumberProperties.valueOf(multipleParameters[i].toUpperCase().replace("-","")).toggleValue();
                    for (int j = i + 1; j < multipleParameters.length; j++) {
                        if (multipleParameters[j].contains("-") && NumberProperties.valueOf(multipleParameters[j].toUpperCase().replace("-","")).value > 0) NumberProperties.valueOf(multipleParameters[j].toUpperCase().replace("-","")).toggleValue();
                        if (mutuallyExclusive(multipleParameters[i], multipleParameters[j])) {
                            System.out.printf("The request contains mutually exclusive properties: [%s, %s]\n" +
                                    "There are no numbers with these properties.", multipleParameters[i].toUpperCase(), multipleParameters[j].toUpperCase());
                            enterNumber();
                        }
                    }
                }

                //Print numbers that have all entered parameters
                while (counter < secondParameter) {
                    if (parameterCheck(firstParameter, multipleParameters)) {
                        printProperties(firstParameter);
                        counter++;
                    }

                    firstParameter++;
                }
            } else {
                System.out.println("The second parameter should be a natural number.");
                enterNumber();
            }
        } else if(firstParameter == 0) {
            exit();
        } else {
            System.out.println("The first parameter should be a natural number or zero.");
            enterNumber();
        }
    }

    private static void printProperties(long number) {
        StringJoiner stringJoiner = new StringJoiner(", ", number + " is ", "");

        if (checkParity(number)) {
            stringJoiner.add("even");
        } else stringJoiner.add("odd");
        if (checkBuzz(number)) {
            stringJoiner.add("buzz");
        }
        if (checkDuck(number)) {
            stringJoiner.add("duck");
        }
        if (checkPalindromic(number)) {
            stringJoiner.add("palindromic");
        }
        if (checkGapful(number)) {
            stringJoiner.add("gapful");
        }
        if (checkSpy(number)) {
            stringJoiner.add("spy");
        }
        if (checkSquare(number)) {
            stringJoiner.add("square");
        }
        if (checkSunny(number)) {
            stringJoiner.add("sunny");
        }
        if (checkJumping(number)) {
            stringJoiner.add("jumping");
        }
        if (checkHappySad(number)) {
            stringJoiner.add("happy");
        } else stringJoiner.add("sad");

        System.out.println(stringJoiner);
    }

    private static boolean checkParity(long number) {
        return number % 2 == 0;
    }

    private static boolean checkBuzz(long number) {
        boolean divisible = false;
        boolean lastDigit = false;

        if (number % 7 == 0)
            divisible = true;

        if (number % 10 == 7)
            lastDigit = true;

        return (divisible || lastDigit);
    }

    private static boolean checkDuck(long number) {
        long temp = number;

        while (temp > 0) {
            long digit = temp % 10;

            if (digit == 0)
                return true;

            temp /= 10;
            }

        return false;
    }

    private static boolean checkPalindromic(long number) {
        StringBuilder str2 = new StringBuilder(Long.toString(number));
        str2.reverse();

        return Long.toString(number).equals(str2.toString());
    }

    private static boolean checkGapful(long number) {
        String str = String.valueOf(number);

        if (str.length() > 2) {
            String endsOfNumberString = str.charAt(0) + "" + str.charAt(str.length() - 1);
            int endsOfNumber = Integer.parseInt(endsOfNumberString);

            return number % endsOfNumber == 0;
        } else {
            return false;
        }
    }

    private static boolean checkSpy(long number) {
        StringBuilder sb = new StringBuilder(Long.toString(number));

        int sum = 0;
        int prod = 1;

        for (int i = 0; i < sb.length(); i++) {
            sum += Integer.parseInt(String.valueOf(sb.charAt(i)));
            prod *= Integer.parseInt(String.valueOf(sb.charAt(i)));
        }

        return sum == prod;
    }

    private static boolean checkSquare(long number) {
        int n = (int) Math.sqrt(number);

        return Math.pow(n, 2) == number;
    }

    private static boolean checkSunny(long number) {
        return checkSquare(number + 1);
    }

    private static boolean checkJumping(long number) {
        boolean flag = false;

        if (number < 10) {
            return true;
        } else {
            LinkedList<Long> stack = new LinkedList<>();

            while (number > 0) {
                stack.push(number % 10);
                number /= 10;
            }

            long nr1 = stack.pop();

            while (!stack.isEmpty()) {
                long nr2 = stack.pop();

                if (Math.abs(nr1 - nr2) != 1) {
                    flag = false;
                    break;
                } else {
                    flag = true;
                    nr1 = nr2;
                }
            }
        }

        return flag;
    }

    private static boolean checkHappySad(long number) {
        long storage = number;
        long temp = 0;

        while (number != 1) {
            while (number > 0) {
                temp += Math.pow(number % 10, 2);
                number /= 10;
            }
            if (temp == storage || temp == 4L) {
                return false;
            }
            number = temp;
            temp = 0;
        }

        return true;
    }

    private static boolean parameterCheck(long firstParameter, String[] inputParameters) {
        boolean allParameters = false;

        for (String parameter : inputParameters) {
            allParameters = false;

            switch (parameter.toUpperCase()) {
                case "BUZZ" -> {
                    if (checkBuzz(firstParameter)) {
                        allParameters = true;
                    }
                }
                case "DUCK" -> {
                    if (checkDuck(firstParameter)) {
                        allParameters = true;
                    }
                }
                case "PALINDROMIC" -> {
                    if (checkPalindromic(firstParameter)) {
                        allParameters = true;
                    }
                }
                case "GAPFUL" -> {
                    if (checkGapful(firstParameter)) {
                        allParameters = true;
                    }
                }
                case "SPY" -> {
                    if (checkSpy(firstParameter)) {
                        allParameters = true;
                    }
                }
                case "SQUARE" -> {
                    if (checkSquare(firstParameter)) {
                        allParameters = true;
                    }
                }
                case "SUNNY" -> {
                    if (checkSunny(firstParameter)) {
                        allParameters = true;
                    }
                }
                case "JUMPING" -> {
                    if (checkJumping(firstParameter)) {
                        allParameters = true;
                    }
                }
                case "HAPPY", "-SAD" -> {
                    if (checkHappySad(firstParameter)) {
                        allParameters = true;
                    }
                }
                case "SAD", "-HAPPY" -> {
                    if (!checkHappySad(firstParameter)) {
                        allParameters = true;
                    }
                }
                case "EVEN", "-ODD" -> {
                    if (checkParity(firstParameter)) {
                        allParameters = true;
                    }
                }
                case "ODD", "-EVEN" -> {
                    if (!checkParity(firstParameter)) {
                        allParameters = true;
                    }
                }
                case "-BUZZ" -> {
                    if (!checkBuzz(firstParameter)) {
                        allParameters = true;
                    }
                }
                case "-DUCK" -> {
                    if (!checkDuck(firstParameter)) {
                        allParameters = true;
                    }
                }
                case "-PALINDROMIC" -> {
                    if (!checkPalindromic(firstParameter)) {
                        allParameters = true;
                    }
                }
                case "-GAPFUL" -> {
                    if (!checkGapful(firstParameter)) {
                        allParameters = true;
                    }
                }
                case "-SPY" -> {
                    if (!checkSpy(firstParameter)) {
                        allParameters = true;
                    }
                }
                case "-SQUARE" -> {
                    if (!checkSquare(firstParameter)) {
                        allParameters = true;
                    }
                }
                case "-SUNNY" -> {
                    if (!checkSunny(firstParameter)) {
                        allParameters = true;
                    }
                }
                case "-JUMPING" -> {
                    if (!checkJumping(firstParameter)) {
                        allParameters = true;
                    }
                }
                default -> {
                    System.out.printf("The property [%s] is wrong.\n" +
                            "Available properties: [EVEN, ODD, BUZZ, DUCK, PALINDROMIC, GAPFUL, SPY, SQUARE, SUNNY]", parameter.toUpperCase());
                    enterNumber();
                }
            }

            if (!allParameters) break;
        }
        return allParameters;
    }

    private static boolean checkValidity(String proprieties) {
        return propertiesList.contains(proprieties);
    }

    private static boolean mutuallyExclusive(String firstProperty, String secondProperty) {
        return !firstProperty.equalsIgnoreCase(secondProperty) && NumberProperties.valueOf(firstProperty.toUpperCase().replace("-","")).value == (NumberProperties.valueOf(secondProperty.toUpperCase().replace("-",""))).value;
    }

    private static void exit() {
        System.out.println("Goodbye!");
        System.exit(0);
    }
}
