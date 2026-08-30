
        package mu.rekolt.service;

import mu.rekolt.model.CashCropProduce;
import mu.rekolt.model.CerealProduce;
import mu.rekolt.model.Delivery;
import mu.rekolt.model.Grade;
import mu.rekolt.model.PerishableProduce;
import mu.rekolt.model.Produce;

import java.util.Scanner;

public class DeliveryService {

    private final Scanner scanner;
    private final SeasonService seasonService;

    public DeliveryService(
            Scanner scanner,
            SeasonService seasonService) {

        this.scanner = scanner;
        this.seasonService = seasonService;
    }

    // Records a new delivery
    public void recordDelivery() {

        System.out.println();
        System.out.println("----- RECORD DELIVERY -----");

        String deliveryId = generateDeliveryId();
        String memberId = readMemberId();
        String memberName = readMemberName();
        String produceCode = readProduceCode();
        double massKg = readMass();
        int qualityScore = readQualityScore();
        int week = readWeek();

        Grade grade = determineGrade(qualityScore);
        Produce produce = createProduce(produceCode);

        Delivery delivery = new Delivery(
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

    // Generates delivery ID
    private String generateDeliveryId() {

        return String.format(
                "D-%04d",
                seasonService.getDeliveryCount() + 1
        );
    }

    // Reads member ID
    private String readMemberId() {

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

    // Reads member name
    private String readMemberName() {

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

    // Reads produce code
    private String readProduceCode() {

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

    // Reads mass
    private double readMass() {

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

    // Reads quality score
    private int readQualityScore() {

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

    // Reads delivery week
    private int readWeek() {

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

    // Creates the correct Produce object
    private Produce createProduce(
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

    // Determines the grade
    private Grade determineGrade(
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

    // Displays payment information
    private void displayDeliveryPayment(
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
}

