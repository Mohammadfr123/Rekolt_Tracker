package mu.rekolt;

import mu.rekolt.model.Delivery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SeasonService {

    // =========================================================
    // 1. REQUIRED ARRAYS
    // =========================================================

    private static final String[] PRODUCE_CODES = {
            "MZE",
            "BNS",
            "POT",
            "TEA"
    };

    private static final double[] PRICES = {
            30.00,
            90.00,
            45.00,
            25.00
    };


    // =========================================================
    // 2. ARRAYLIST
    // =========================================================
    /*  Main collection containing all deliveries.*/
    private ArrayList<Delivery> deliveries;


    // =========================================================
    // 3. CONSTRUCTOR
    // =========================================================

    public SeasonService() {

        deliveries = new ArrayList<>();
    }


    // =========================================================
    // 4. ARRAYLIST METHODS
    // =========================================================

    /*
     * Add a delivery to the ArrayList.
     */
    public void addDelivery(Delivery delivery) {

        deliveries.add(delivery);
    }


    /*
     * Return all deliveries.
     */
    public ArrayList<Delivery> getDeliveries() {

        return deliveries;
    }


    // =========================================================
    // 5. PRICE LOOKUP USING ARRAYS
    // =========================================================

    private double getPrice(String produceCode) {

        for (int i = 0; i < PRODUCE_CODES.length; i++) {

            if (PRODUCE_CODES[i]
                    .equalsIgnoreCase(produceCode)) {

                return PRICES[i];
            }
        }

        return 0.00;
    }


    // =========================================================
    // 6. QUALITY GRADING
    // =========================================================

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


    // =========================================================
    // 7. GRADE MULTIPLIER
    // =========================================================

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


    // =========================================================
    // 8. CATEGORY MULTIPLIER
    // =========================================================

    private double getCategoryMultiplier(
            String produceCode) {

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


    // =========================================================
    // 9. NET PAYMENT CALCULATION
    // =========================================================

    private double calculateNetPayable(
            double massKg,
            String produceCode,
            int qualityScore) {

        String grade =
                determineGrade(qualityScore);

        /*
         * Rejected deliveries are still recorded,
         * but receive zero payment.
         */
        if (grade.equals("REJECT")) {

            return 0.00;
        }

        double price =
                getPrice(produceCode);

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

        /*
         * 5% commission.
         */
        double commission =
                categoryValue * 0.05;

        /*
         * Transport levy of 2 MUR per kg.
         */
        double transportLevy =
                massKg * 2.00;

        return categoryValue
                - commission
                - transportLevy;
    }


    // =========================================================
    // 10. ADD SAMPLE DELIVERY
    // =========================================================

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


    // =========================================================
    // 11. LOAD 12 SAMPLE DELIVERIES
    // =========================================================

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


    // =========================================================
    // 12. HASHMAP
    // MEMBER ID -> TOTAL PAYMENT
    // =========================================================

    public Map<String, Double>
    calculatePaymentPerMember() {

        Map<String, Double> paymentPerMember =
                new HashMap<>();

        for (Delivery delivery : deliveries) {

            String memberId =
                    delivery.getMemberId();

            double currentTotal =
                    paymentPerMember.getOrDefault(
                            memberId,
                            0.00
                    );

            paymentPerMember.put(
                    memberId,
                    currentTotal
                            + delivery.getNetPayable()
            );
        }

        return paymentPerMember;
    }


    // =========================================================
    // 13. MAP OF LISTS
    // MEMBER ID -> THEIR DELIVERIES
    // =========================================================

    public Map<String, List<Delivery>>
    getDeliveriesPerMember() {

        Map<String, List<Delivery>>
                deliveriesPerMember =
                new HashMap<>();

        for (Delivery delivery : deliveries) {

            String memberId =
                    delivery.getMemberId();

            deliveriesPerMember
                    .computeIfAbsent(
                            memberId,
                            key -> new ArrayList<>()
                    )
                    .add(delivery);
        }

        return deliveriesPerMember;
    }


    // =========================================================
    // 14. HASHSET
    // DISTINCT MEMBER IDS
    // =========================================================

    public Set<String>
    getDistinctMemberIds() {

        Set<String> memberIds =
                new HashSet<>();

        for (Delivery delivery : deliveries) {

            memberIds.add(
                    delivery.getMemberId()
            );
        }

        return memberIds;
    }


    // =========================================================
    // 15. SEARCH BY DELIVERY ID
    // =========================================================

    public Delivery findDeliveryById(
            String deliveryId) {

        for (Delivery delivery : deliveries) {

            if (delivery.getDeliveryId()
                    .equalsIgnoreCase(deliveryId)) {

                return delivery;
            }
        }

        /*
         * null represents an absent result.
         */
        return null;
    }


    // =========================================================
    // 16. COMPARABLE
    // NATURAL ORDERING
    // =========================================================

    public void sortByDeliveryId() {

        Collections.sort(deliveries);
    }


    // =========================================================
    // 17. COMPARATOR
    // SORT BY NET PAYMENT DESCENDING
    // =========================================================

    public void sortByPaymentDescending() {

        deliveries.sort(
                Comparator.comparingDouble(
                        Delivery::getNetPayable
                ).reversed()
        );
    }


    // =========================================================
    // 18. TOP FIVE DELIVERIES
    // =========================================================

    public List<Delivery> getTopFiveDeliveries() {

        /*
         * Create a copy so that this method does not
         * permanently change the main ArrayList order.
         */
        List<Delivery> sortedDeliveries =
                new ArrayList<>(deliveries);

        sortedDeliveries.sort(
                Comparator.comparingDouble(
                        Delivery::getNetPayable
                ).reversed()
        );

        int numberToReturn =
                Math.min(5, sortedDeliveries.size());

        return new ArrayList<>(
                sortedDeliveries.subList(
                        0,
                        numberToReturn
                )
        );
    }


    // =========================================================
    // 19. ITERATOR
    // REMOVE REJECTED DELIVERIES
    // =========================================================

    public int removeRejectedDeliveries() {

        int removed = 0;

        Iterator<Delivery> iterator =
                deliveries.iterator();

        while (iterator.hasNext()) {

            Delivery delivery =
                    iterator.next();

            if (delivery.getGrade()
                    .equalsIgnoreCase("REJECT")) {

                iterator.remove();

                removed++;
            }
        }

        return removed;
    }
}