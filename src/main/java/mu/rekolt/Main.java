package mu.rekolt;

import java.util.Scanner;

public class Main {

    // Scanner used for reading user input
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        boolean running = true;

        System.out.println("======================================");
        System.out.println("     REKOLT PRODUCE TRACKER");
        System.out.println("            Season 2026");
        System.out.println("======================================");

        while (running) {
            displayMenu();
            int choice = readMenuChoice();
            switch (choice) {

                case 1:
                    recordDelivery();
                    break;

                case 2:
                    showSeasonFigures();
                    break;

                case 3:
                    System.out.println();
                    System.out.println("Season report generation will be added in Objective 6.");
                    break;

                case 4:
                    running = false;
                    System.out.println();
                    System.out.println("Goodbye.");
                    break;
            }
        }

        scanner.close();
    }

    // Displays the main menu
    public static void displayMenu() {

        System.out.println();
        System.out.println("1. Record a delivery");
        System.out.println("2. Season figures on screen");
        System.out.println("3. Generate the season report");
        System.out.println("4. Exit");
    }

    // Reads and validates the menu choice
    public static int readMenuChoice() {

        while (true) {

            System.out.print("Choose an option: ");

            String input = scanner.nextLine().trim();

            try {

                int choice = Integer.parseInt(input);

                if (choice >= 1 && choice <= 4) {
                    return choice;
                }

                System.out.println(
                        "Invalid option. Please enter a number from 1 to 4."
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid option. Please enter a number from 1 to 4."
                );
            }
        }
    }

    // Records one delivery
    public static void recordDelivery() {

        System.out.println();
        System.out.println("----- RECORD DELIVERY -----");

        String memberId = readMemberId();

        String memberName = readMemberName();

        String produceCode = readProduceCode();

        double massKg = readMass();

        int qualityScore = readQualityScore();

        int week = readWeek();

        String grade = determineGrade(qualityScore);

        double gradeMultiplier = getGradeMultiplier(grade);

        double pricePerKg = getPrice(produceCode);

        double categoryMultiplier = getCategoryMultiplier(produceCode);

        calculateAndDisplayPayment(
                memberId,
                memberName,
                produceCode,
                massKg,
                qualityScore,
                week,
                grade,
                gradeMultiplier,
                pricePerKg,
                categoryMultiplier
        );
    }

    // Reads and validates the member ID
    public static String readMemberId() {

        while (true) {

            System.out.print("Member identifier: ");

            String memberId = scanner.nextLine().trim();

            if (memberId.matches("M-\\d{4}")) {
                return memberId;
            }

            System.out.println(
                    "Invalid member identifier. Use the format M-0000."
            );
        }
    }

    // Reads and validates the member name
    public static String readMemberName() {

        while (true) {

            System.out.print("Member name: ");

            String name = scanner.nextLine().trim();

            if (!name.isEmpty()) {
                return name;
            }

            System.out.println(
                    "Member name cannot be empty."
            );
        }
    }

    // Reads and validates the produce code
    public static String readProduceCode() {

        while (true) {

            System.out.print("Produce code (MZE/BNS/POT/TEA): ");

            String code = scanner.nextLine().trim().toUpperCase();

            if (code.equals("MZE")
                    || code.equals("BNS")
                    || code.equals("POT")
                    || code.equals("TEA")) {

                return code;
            }

            System.out.println(
                    "Invalid produce code. Use MZE, BNS, POT or TEA."
            );
        }
    }

    // Reads and validates mass
    public static double readMass() {
        while (true) {
            System.out.print("Mass in kg: ");
            String Mass = scanner.nextLine().trim();

            try {

                double mass = Double.parseDouble(Mass);

                if (mass > 0 && mass <= 5000) {
                    return mass;
                }

                System.out.println(
                        "Mass must be above 0 and not more than 5000."
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Mass must be a valid decimal number."
                );
            }
        }
    }

    // Reads and validates quality score
    public static int readQualityScore() {
        while (true) {
            System.out.print("Quality score (0-100): ");
            String input = scanner.nextLine().trim();

            try {

                int score = Integer.parseInt(input);
                if (score >= 0 && score <= 100) {
                    return score;
                }

                System.out.println(
                        "Quality score must be between 0 and 100."
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Quality score must be a whole number."
                );
            }
        }
    }

    // Reads and validates delivery week
    public static int readWeek() {
        while (true) {

            System.out.print("Week of delivery (1-20): ");
            String input = scanner.nextLine().trim();
            try {

                int week = Integer.parseInt(input);
                if (week >= 1 && week <= 20) {
                    return week;
                }

                System.out.println(
                        "Week must be between 1 and 20."
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Week must be a whole number."
                );
            }
        }
    }

    // Determines the grade
    public static String determineGrade(int qualityScore) {
        if (qualityScore >= 85) {
            return "A";

        } else if (qualityScore >= 70) {
            return "B";

        } else if (qualityScore >= 50) {
            return "C";

        } else {
            return "REJECT";
        }
    }

    // Determines the grade multiplier
    public static double getGradeMultiplier(String grade) {

        return switch (grade) {
            case "A" -> 1.15;
            case "B" -> 1.00;
            case "C" -> 0.85;
            case "REJECT" -> 0.00;
            default -> 0.00;
        };
    }

    // Returns the base price according to the produce
    public static double getPrice(String produceCode) {

        return switch (produceCode) {
            case "MZE" -> 30.00;
            case "BNS" -> 90.00;
            case "POT" -> 45.00;
            case "TEA" -> 25.00;
            default -> 0.00;
        };
    }

    // Returns the category multiplier
    public static double getCategoryMultiplier(String produceCode) {

        return switch (produceCode) {
            case "MZE", "BNS" -> 1.00;
            case "POT" -> 0.90;
            case "TEA" -> 1.10;
            default -> 0.00;
        };
    }

    // Calculates and displays the payment
    public static void calculateAndDisplayPayment(
            String memberId,
            String memberName,
            String produceCode,
            double massKg,
            int qualityScore,
            int week,
            String grade,
            double gradeMultiplier,
            double pricePerKg,
            double categoryMultiplier) {

        System.out.println();

        // Explicit cast required by Objective 1

        double baseValue =
                (double) massKg * pricePerKg;

        double gradeValue =
                baseValue * gradeMultiplier;

        double categoryValue =
                gradeValue * categoryMultiplier;

        double commission = 0.0;
        double transportLevy = 0.0;
        double netPayable;

        // REJECT deliveries have zero value and no deductions
        if (grade.equals("REJECT")) {

            netPayable = 0.0;

        } else {

            commission =
                    categoryValue * 0.05;

            transportLevy =
                    (double) massKg * 2.00;

            netPayable =
                    categoryValue
                            - commission
                            - transportLevy;
        }

        System.out.println("--------------------------------------");
        System.out.println("Delivery recorded");
        System.out.println("--------------------------------------");

        System.out.println("Member: " + memberId);
        System.out.println("Name: " + memberName);
        System.out.println("Produce: " + produceCode);
        System.out.println("Mass: " + massKg + " kg");
        System.out.println("Quality: " + qualityScore);
        System.out.println("Week: " + week);
        System.out.println("Grade: " + grade);

        System.out.println();

        System.out.printf(
                "Base value:        %.2f MUR%n",
                baseValue
        );

        System.out.printf(
                "Grade %s:            x %.2f = %.2f MUR%n",
                grade,
                gradeMultiplier,
                gradeValue
        );

        System.out.printf(
                "Category:            x %.2f = %.2f MUR%n",
                categoryMultiplier,
                categoryValue
        );

        if (grade.equals("REJECT")) {

            System.out.println(
                    "REJECT: value is zero and no deductions are taken."
            );

        } else {

            System.out.printf(
                    "Commission 5%%:     -%.2f MUR%n",
                    commission
            );

            System.out.printf(
                    "Transport levy:    -%.2f MUR%n",
                    transportLevy
            );
        }

        System.out.printf(
                "NET PAYABLE:         %.2f MUR%n",
                netPayable
        );
    }

    // Placeholder for Objective 3
    public static void showSeasonFigures() {

        System.out.println();
        System.out.println("Season figures will be implemented with collections in Objective 3.");
    }
}