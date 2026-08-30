package mu.rekolt.app;

import mu.rekolt.model.CashCropProduce;
import mu.rekolt.model.CerealProduce;
import mu.rekolt.model.Delivery;
import mu.rekolt.model.Grade;
import mu.rekolt.model.PerishableProduce;
import mu.rekolt.model.Produce;
import mu.rekolt.service.SeasonService;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner =
            new Scanner(System.in);

    private static final SeasonService seasonService =
            new SeasonService();

    public static void main(String[] args) {

        boolean running = true;

        System.out.println("***********************************");
        System.out.println("     REKOLT PRODUCE TRACKER");
        System.out.println("            Season 2026");
        System.out.println("***********************************");

        // Load sample deliveries for testing
        seasonService.loadSampleDeliveries();

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
                    System.out.println(
                            "Season report generation will be added in Objective 6."
                    );
                    break;

                case 4:
                    running = false;

                    System.out.println();
                    System.out.println("Goodbye.");
                    break;

                default:
                    System.out.println(
                            "Invalid option."
                    );
            }
        }

        scanner.close();
    }

    // --------------------------------------------------
    // MAIN MENU
    // --------------------------------------------------

    public static void displayMenu() {

        System.out.println();
        System.out.println("1. Record a delivery");
        System.out.println("2. Season figures on screen");
        System.out.println("3. Generate the season report");
        System.out.println("4. Exit");
    }

    public static int readMenuChoice() {

        while (true) {

            System.out.print("Choose an option: ");

            String input =
                    scanner.nextLine().trim();

            try {

                int choice =
                        Integer.parseInt(input);

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

    // --------------------------------------------------
    // RECORD DELIVERY
    // --------------------------------------------------

    public static void recordDelivery() {

        System.out.println();
        System.out.println("----- RECORD DELIVERY -----");

        String deliveryId =
                generateDeliveryId();

        String memberId =
                readMemberId();

        String memberName =
                readMemberName();

        String produceCode =
                readProduceCode();

        double massKg =
                readMass();

        int qualityScore =
                readQualityScore();

        int week =
                readWeek();

        Grade grade =
                determineGrade(qualityScore);

        Produce produce =
                createProduce(produceCode);

        Delivery delivery =
                new Delivery(
                        deliveryId,
                        memberId,
                        memberName,
                        produce,
                        massKg,
                        qualityScore,
                        week,
                        grade
                );

        seasonService.addDelivery(delivery);

        displayDeliveryPayment(delivery);
    }

    // Generates a simple delivery ID
    public static String generateDeliveryId() {

        return String.format(
                "D-%04d",
                seasonService.getDeliveryCount() + 1
        );
    }

    // --------------------------------------------------
    // INPUT VALIDATION
    // --------------------------------------------------

    public static String readMemberId() {

        while (true) {

            System.out.print("Member identifier: ");

            String memberId =
                    scanner.nextLine().trim();

            if (memberId.matches("M-\\d{4}")) {
                return memberId;
            }

            System.out.println(
                    "Invalid member identifier. Use the format M-0000."
            );
        }
    }

    public static String readMemberName() {

        while (true) {

            System.out.print("Member name: ");

            String name =
                    scanner.nextLine().trim();

            if (!name.isEmpty()) {
                return name;
            }

            System.out.println(
                    "Member name cannot be empty."
            );
        }
    }

    public static String readProduceCode() {

        while (true) {

            System.out.print(
                    "Produce code (MZE/BNS/POT/TEA): "
            );

            String code =
                    scanner.nextLine()
                            .trim()
                            .toUpperCase();

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

    public static double readMass() {

        while (true) {

            System.out.print("Mass in kg: ");

            String input =
                    scanner.nextLine().trim();

            try {

                double mass =
                        Double.parseDouble(input);

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

    public static int readQualityScore() {

        while (true) {

            System.out.print(
                    "Quality score (0-100): "
            );

            String input =
                    scanner.nextLine().trim();

            try {

                int score =
                        Integer.parseInt(input);

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

    public static int readWeek() {

        while (true) {

            System.out.print(
                    "Week of delivery (1-20): "
            );

            String input =
                    scanner.nextLine().trim();

            try {

                int week =
                        Integer.parseInt(input);

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

    // --------------------------------------------------
    // PRODUCE CREATION
    // --------------------------------------------------

    public static Produce createProduce(
            String produceCode) {

        return switch (produceCode) {

            case "MZE" ->
                    new CerealProduce(
                            "MZE",
                            "Maize",
                            30.00
                    );

            case "BNS" ->
                    new CerealProduce(
                            "BNS",
                            "Beans",
                            90.00
                    );

            case "POT" ->
                    new PerishableProduce(
                            "POT",
                            "Potatoes",
                            45.00
                    );

            case "TEA" ->
                    new CashCropProduce(
                            "TEA",
                            "Green Tea Leaf",
                            25.00
                    );

            default ->
                    throw new IllegalArgumentException(
                            "Unknown produce code."
                    );
        };
    }

    // --------------------------------------------------
    // GRADE
    // --------------------------------------------------

    public static Grade determineGrade(
            int qualityScore) {

        if (qualityScore >= 85) {
            return Grade.A;

        } else if (qualityScore >= 70) {
            return Grade.B;

        } else if (qualityScore >= 50) {
            return Grade.C;

        } else {
            return Grade.REJECT;
        }
    }

    // --------------------------------------------------
    // PAYMENT DISPLAY
    // --------------------------------------------------

    public static void displayDeliveryPayment(
            Delivery delivery) {

        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println("Delivery recorded");
        System.out.println("--------------------------------------");

        System.out.println(
                "Delivery ID: " +
                        delivery.getDeliveryId()
        );

        System.out.println(
                "Member: " +
                        delivery.getMemberId()
        );

        System.out.println(
                "Name: " +
                        delivery.getMemberName()
        );

        System.out.println(
                "Produce: " +
                        delivery.getProduceCode()
        );

        System.out.println(
                "Mass: " +
                        delivery.getMassKg() +
                        " kg"
        );

        System.out.println(
                "Quality: " +
                        delivery.getQualityScore()
        );

        System.out.println(
                "Week: " +
                        delivery.getWeek()
        );

        System.out.println(
                "Grade: " +
                        delivery.getGrade()
        );

        System.out.printf(
                "Price per kg: %.2f MUR%n",
                delivery.getProduce()
                        .getPricePerKg()
        );

        System.out.printf(
                "Category multiplier: x %.2f%n",
                delivery.getProduce()
                        .getCategoryMultiplier()
        );

        System.out.printf(
                "Grade multiplier: x %.2f%n",
                delivery.getGrade()
                        .getMultiplier()
        );

        System.out.printf(
                "NET PAYABLE: %.2f MUR%n",
                delivery.calculateNetPayable()
        );

        System.out.println("--------------------------------------");
    }

    // --------------------------------------------------
    // OBJECTIVE 3 — SEASON FIGURES
    // --------------------------------------------------

    public static void showSeasonFigures() {

        System.out.println();
        System.out.println(
                "===== DELIVERIES PER MEMBER ====="
        );

        Map<String, List<Delivery>>
                deliveriesPerMember =
                seasonService.getDeliveriesPerMember();

        for (Map.Entry<String, List<Delivery>> entry :
                deliveriesPerMember.entrySet()) {

            System.out.println();
            System.out.println(
                    "Member: " +
                            entry.getKey()
            );

            for (Delivery delivery :
                    entry.getValue()) {

                System.out.println(
                        "  " + delivery
                );
            }
        }

        System.out.println();
        System.out.println(
                "===== PAYMENT PER MEMBER ====="
        );

        Map<String, Double> payments =
                seasonService.calculatePaymentPerMember();

        for (Map.Entry<String, Double> entry :
                payments.entrySet()) {

            System.out.printf(
                    "%s -> %.2f MUR%n",
                    entry.getKey(),
                    entry.getValue()
            );
        }

        System.out.println();
        System.out.println(
                "===== SEASON TOTALS ====="
        );

        System.out.println(
                "Total deliveries: " +
                        seasonService.getDeliveryCount()
        );

        System.out.printf(
                "Total mass: %.2f kg%n",
                seasonService.getTotalMass()
        );

        System.out.printf(
                "Total season payment: %.2f MUR%n",
                seasonService.getTotalSeasonPayment()
        );
    }
}