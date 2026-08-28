package mu.rekolt;

import mu.rekolt.model.Delivery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class SeasonService {

    // Produce codes
    private static final String[] PRODUCE_CODES = {
            "MZE",
            "BNS",
            "POT",
            "TEA"
    };

    // Price per kilogram
    private static final double[] PRICES = {
            30.00,
            90.00,
            45.00,
            25.00
    };
    // Main collection for Objective 3
    private ArrayList<Delivery> deliveries;

    // Constructor
    public SeasonService() {
        deliveries = new ArrayList<>();
    }

    // Add a delivery to the ArrayList
    public void addDelivery(Delivery delivery) {
        deliveries.add(delivery);
    }

    // Return all deliveries
    public ArrayList<Delivery> getDeliveries() {
        return deliveries;
    }

    // -----------------------------
    // QUALITY GRADING
    // -----------------------------

    private String determineGrade(int qualityScore) {

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

    // -----------------------------
    // GRADE MULTIPLIER
    // -----------------------------

    private double getGradeMultiplier(String grade) {

        switch (grade) {

            case "A":
                return 1.15;

            case "B":
                return 1.00;

            case "C":
                return 0.85;

            case "REJECT":
                return 0.00;

            default:
                return 0.00;
        }
    }

    // -----------------------------
    // CATEGORY MULTIPLIER
    // -----------------------------

    private double getCategoryMultiplier(String produceCode) {

        switch (produceCode) {

            case "MZE":
            case "BNS":
                return 1.00;

            case "POT":
                return 0.90;

            case "TEA":
                return 1.10;

            default:
                return 0.00;
        }
    }

    // -----------------------------
    // PRICE
    // -----------------------------

    private double getPrice(String produceCode) {

        for (int i = 0; i < PRODUCE_CODES.length; i++) {

            if (PRODUCE_CODES[i].equalsIgnoreCase(produceCode)) {
                return PRICES[i];
            }
        }

        return 0.00;
    }

    // -----------------------------
    // PAYMENT CALCULATION
    // -----------------------------

    private double calculateNetPayable(
            double massKg,
            String produceCode,
            int qualityScore) {

        String grade = determineGrade(qualityScore);

        // Rejected deliveries receive no payment
        if (grade.equals("REJECT")) {
            return 0.0;
        }

        double price = getPrice(produceCode);

        double gradeMultiplier =
                getGradeMultiplier(grade);

        double categoryMultiplier =
                getCategoryMultiplier(produceCode);

        double baseValue =
                massKg * price;

        double gradeValue =
                baseValue * gradeMultiplier;

        double categoryValue =
                gradeValue * categoryMultiplier;

        double commission =
                categoryValue * 0.05;

        double transportLevy =
                massKg * 2.00;

        return categoryValue
                - commission
                - transportLevy;
    }

    // -----------------------------
    // CREATE SAMPLE DELIVERY
    // -----------------------------

    private void addSampleDelivery(
            String deliveryId,
            String memberId,
            String memberName,
            String produceCode,
            double massKg,
            int qualityScore,
            int week) {

        String grade =
                determineGrade(qualityScore);

        double netPayable =
                calculateNetPayable(
                        massKg,
                        produceCode,
                        qualityScore
                );

        Delivery delivery =
                new Delivery(
                        deliveryId,
                        memberId,
                        memberName,
                        produceCode,
                        massKg,
                        qualityScore,
                        week,
                        grade,
                        netPayable
                );

        addDelivery(delivery);
    }

    // -----------------------------
    // LOAD 12 SAMPLE DELIVERIES
    // -----------------------------

    public void loadSampleDeliveries() {

        addSampleDelivery(
                "D-1001",
                "M-0042",
                "Devi Ramjaun",
                "BNS",
                236.0,
                91,
                1
        );

        addSampleDelivery(
                "D-1002",
                "M-0117",
                "Jean Ah-Kine",
                "MZE",
                412.5,
                78,
                1
        );

        addSampleDelivery(
                "D-1003",
                "M-0088",
                "Anisha Beeharry",
                "POT",
                150.0,
                65,
                1
        );

        addSampleDelivery(
                "D-1004",
                "M-0042",
                "Devi Ramjaun",
                "TEA",
                88.3,
                90,
                1
        );

        addSampleDelivery(
                "D-1005",
                "M-0117",
                "Jean Ah-Kine",
                "BNS",
                390.5,
                72,
                2
        );

        addSampleDelivery(
                "D-1006",
                "M-0088",
                "Anisha Beeharry",
                "MZE",
                180.0,
                88,
                2
        );

        addSampleDelivery(
                "D-1007",
                "M-0042",
                "Devi Ramjaun",
                "POT",
                150.0,
                55,
                2
        );

        addSampleDelivery(
                "D-1008",
                "M-0135",
                "Rahul Mootoosamy",
                "TEA",
                95.0,
                67,
                2
        );

        addSampleDelivery(
                "D-1009",
                "M-0135",
                "Rahul Mootoosamy",
                "MZE",
                300.0,
                92,
                3
        );

        addSampleDelivery(
                "D-1010",
                "M-0042",
                "Devi Ramjaun",
                "BNS",
                200.0,
                48,
                3
        );

        addSampleDelivery(
                "D-1011",
                "M-0088",
                "Anisha Beeharry",
                "POT",
                125.5,
                82,
                3
        );

        addSampleDelivery(
                "D-1012",
                "M-0117",
                "Jean Ah-Kine",
                "TEA",
                110.0,
                86,
                3
        );
    }
    public Map<String, Double> calculatePaymentPerMember() {

        Map<String, Double> paymentPerMember =
                new HashMap<>();

        for (Delivery delivery : deliveries) {

            String memberId =
                    delivery.getMemberId();

            double currentTotal =
                    paymentPerMember.getOrDefault(
                            memberId,
                            0.0
                    );

            paymentPerMember.put(
                    memberId,
                    currentTotal + delivery.getNetPayable()
            );
        }

        return paymentPerMember;
    }
}