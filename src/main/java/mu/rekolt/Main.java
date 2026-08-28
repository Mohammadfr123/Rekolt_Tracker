 package mu.rekolt;

import mu.rekolt.model.Delivery;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {

    // Scanner used for reading user input
    private static final Scanner scanner = new Scanner(System.in);

    // One SeasonService used throughout the application
    private static final SeasonService seasonService =
            new SeasonService();

    public static void main(String[] args) {

        boolean running = true;

        // Load the sample deliveries once when the program starts
        seasonService.loadSampleDeliveries();

        System.out.println("***********************************");
        System.out.println("     REKOLT PRODUCE TRACKER");
        System.out.println("            Season 2026");
        System.out.println("***********************************");

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


    // =========================================================
    // MAIN MENU
    // =========================================================

    public static void displayMenu() {

        System.out.println();
        System.out.println("===================================");
        System.out.println("             MAIN MENU");
        System.out.println("===================================");
        System.out.println("1. Record a delivery");
        System.out.println("2. Season figures on screen");
        System.out.println("3. Generate the season report");
        System.out.println("4. Exit");
        System.out.println("===================================");
    }


    // =========================================================
    // MENU VALIDATION
    // =========================================================

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


    // =========================================================
    // OBJECTIVE 2
    // RECORD DELIVERY
    // =========================================================

    public static void recordDelivery() {

        System.out.println();
        System.out.println("----- RECORD DELIVERY -----");

        String memberId = readMemberId();

        String memberName = readMemberName();

        String produceCode = readProduceCode();

        double massKg = readMass();

        int qualityScore = readQualityScore();

        int week = readWeek();

        // Determine grade
        String grade =
                determineGrade(qualityScore);

        // Calculate multipliers and price
        double gradeMultiplier =
                getGradeMultiplier(grade);

        double pricePerKg =
                getPrice(produceCode);

        double categoryMultiplier =
                getCategoryMultiplier(produceCode);


        // =====================================================
        // CALCULATE PAYMENT
        // =====================================================

        double baseValue =
                (double) massKg * pricePerKg;

        double gradeValue =
                baseValue * gradeMultiplier;

        double categoryValue =
                gradeValue * categoryMultiplier;

        double commission = 0.0;

        double transportLevy = 0.0;

        double netPayable;


        // Rejected deliveries receive zero payment
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


        // =====================================================
        // CREATE DELIVERY OBJECT
        // =====================================================

        /*
         * Generate a new delivery ID.
         *
         * The current number of deliveries is used to
         * create the next ID.
         */
        int nextNumber =
                seasonService.getDeliveries().size()
                        + 1001;

        String deliveryId =
                String.format(
                        "D-%04d",
                        nextNumber
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


        // =====================================================
        // ADD DELIVERY TO ARRAYLIST
        // =====================================================

        seasonService.addDelivery(delivery);


        // =====================================================
        // DISPLAY RESULT
        // =====================================================

        System.out.println();

        System.out.println(
                "--------------------------------------"
        );

        System.out.println(
                "Delivery recorded successfully"
        );

        System.out.println(
                "--------------------------------------"
        );

        System.out.println(
                "Delivery ID: " + deliveryId
        );

        System.out.println(
                "Member: " + memberId
        );

        System.out.println(
                "Name: " + memberName
        );

        System.out.println(
                "Produce: " + produceCode
        );

        System.out.println(
                "Mass: " + massKg + " kg"
        );

        System.out.println(
                "Quality: " + qualityScore
        );

        System.out.println(
                "Week: " + week
        );

        System.out.println(
                "Grade: " + grade
        );

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

        System.out.println(
                "--------------------------------------"
        );

        System.out.println(
                "Delivery has been added to the ArrayList."
        );

        System.out.println(
                "Total deliveries: "
                        + seasonService
                        .getDeliveries()
                        .size()
        );
    }


    // =========================================================
    // MEMBER ID
    // =========================================================

    public static String readMemberId() {

        while (true) {

            System.out.print(
                    "Member identifier: "
            );

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


    // =========================================================
    // MEMBER NAME
    // =========================================================

    public static String readMemberName() {

        while (true) {

            System.out.print(
                    "Member name: "
            );

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


    // =========================================================
    // PRODUCE CODE
    // =========================================================

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


    // =========================================================
    // MASS
    // =========================================================

    public static double readMass() {

        while (true) {

            System.out.print(
                    "Mass in kg: "
            );

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


    // =========================================================
    // QUALITY SCORE
    // =========================================================

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


    // =========================================================
    // WEEK
    // =========================================================

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


    // =========================================================
    // GRADE
    // =========================================================

    public static String determineGrade(
            int qualityScore) {

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
    // GRADE MULTIPLIER
    // =========================================================

    public static double getGradeMultiplier(
            String grade) {

        return switch (grade) {

            case "A" -> 1.15;

            case "B" -> 1.00;

            case "C" -> 0.85;

            case "REJECT" -> 0.00;

            default -> 0.00;
        };
    }


    // =========================================================
    // PRICE
    // =========================================================

    public static double getPrice(
            String produceCode) {

        return switch (produceCode) {

            case "MZE" -> 30.00;

            case "BNS" -> 90.00;

            case "POT" -> 45.00;

            case "TEA" -> 25.00;

            default -> 0.00;
        };
    }


    // =========================================================
    // CATEGORY MULTIPLIER
    // =========================================================

    public static double getCategoryMultiplier(
            String produceCode) {

        return switch (produceCode) {

            case "MZE", "BNS" -> 1.00;

            case "POT" -> 0.90;

            case "TEA" -> 1.10;

            default -> 0.00;
        };
    }


    // =========================================================
    // PAYMENT CALCULATION
    // =========================================================

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

        // REJECT deliveries have zero value
        // and no deductions
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


        System.out.println(
                "--------------------------------------"
        );

        System.out.println(
                "Delivery recorded"
        );

        System.out.println(
                "--------------------------------------"
        );

        System.out.println(
                "Member: " + memberId
        );

        System.out.println(
                "Name: " + memberName
        );

        System.out.println(
                "Produce: " + produceCode
        );

        System.out.println(
                "Mass: " + massKg + " kg"
        );

        System.out.println(
                "Quality: " + qualityScore
        );

        System.out.println(
                "Week: " + week
        );

        System.out.println(
                "Grade: " + grade
        );

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

        System.out.println(
                "--------------------------------------"
        );
    }


    // =========================================================
    // OBJECTIVE 3
    // SEASON FIGURES
    // =========================================================

    public static void showSeasonFigures() {

        boolean back = false;

        while (!back) {

            System.out.println();
            System.out.println(
                    "======================================"
            );
            System.out.println(
                    "       OBJECTIVE 3 - COLLECTIONS"
            );
            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "1. Show all deliveries"
            );

            System.out.println(
                    "2. Show payment per member"
            );

            System.out.println(
                    "3. Show deliveries grouped by member"
            );

            System.out.println(
                    "4. Show distinct member IDs"
            );

            System.out.println(
                    "5. Search for a delivery"
            );

            System.out.println(
                    "6. Sort by Delivery ID (Comparable)"
            );

            System.out.println(
                    "7. Sort by Net Payment (Comparator)"
            );

            System.out.println(
                    "8. Show Top 5 deliveries"
            );

            System.out.println(
                    "9. Remove rejected deliveries (Iterator)"
            );

            System.out.println(
                    "10. Back to main menu"
            );

            System.out.println(
                    "======================================"
            );

            System.out.print(
                    "Choose an option: "
            );

            String input =
                    scanner.nextLine().trim();

            int choice;

            try {

                choice =
                        Integer.parseInt(input);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number."
                );

                continue;
            }


            switch (choice) {

                case 1:
                    showAllDeliveries();
                    break;

                case 2:
                    showPaymentPerMember();
                    break;

                case 3:
                    showDeliveriesPerMember();
                    break;

                case 4:
                    showDistinctMembers();
                    break;

                case 5:
                    searchDelivery();
                    break;

                case 6:
                    sortUsingComparable();
                    break;

                case 7:
                    sortUsingComparator();
                    break;

                case 8:
                    showTopFive();
                    break;

                case 9:
                    removeRejected();
                    break;

                case 10:
                    back = true;
                    break;

                default:
                    System.out.println(
                            "Invalid option."
                    );
            }
        }
    }


    // =========================================================
    // OBJECTIVE 3 - ARRAYLIST
    // =========================================================

    public static void showAllDeliveries() {

        System.out.println();
        System.out.println(
                "===== ARRAYLIST - ALL DELIVERIES ====="
        );

        List<Delivery> deliveries =
                seasonService.getDeliveries();

        for (Delivery delivery : deliveries) {

            System.out.println(delivery);
        }

        System.out.println();
        System.out.println(
                "Total deliveries: "
                        + deliveries.size()
        );
    }


    // =========================================================
    // OBJECTIVE 3 - HASHMAP
    // MEMBER -> PAYMENT
    // =========================================================

    public static void showPaymentPerMember() {

        System.out.println();
        System.out.println(
                "===== HASHMAP - PAYMENT PER MEMBER ====="
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
    }


    // =========================================================
    // OBJECTIVE 3 - MAP OF LISTS
    // MEMBER -> DELIVERIES
    // =========================================================

    public static void showDeliveriesPerMember() {

        System.out.println();
        System.out.println(
                "===== MAP OF LISTS ====="
        );

        Map<String, List<Delivery>>
                deliveriesPerMember =
                seasonService
                        .getDeliveriesPerMember();

        for (Map.Entry<String, List<Delivery>>
                entry :
                deliveriesPerMember.entrySet()) {

            System.out.println();
            System.out.println(
                    "Member: "
                            + entry.getKey()
            );

            for (Delivery delivery :
                    entry.getValue()) {

                System.out.println(
                        "  " + delivery
                );
            }

            System.out.println(
                    "Number of deliveries: "
                            + entry.getValue().size()
            );
        }
    }


    // =========================================================
    // OBJECTIVE 3 - HASHSET
    // =========================================================

    public static void showDistinctMembers() {

        System.out.println();
        System.out.println(
                "===== HASHSET - DISTINCT MEMBERS ====="
        );

        Set<String> memberIds =
                seasonService
                        .getDistinctMemberIds();

        for (String memberId : memberIds) {

            System.out.println(memberId);
        }

        System.out.println();

        System.out.println(
                "Number of distinct members: "
                        + memberIds.size()
        );
    }


    // =========================================================
    // OBJECTIVE 3 - SEARCH
    // =========================================================

    public static void searchDelivery() {

        System.out.println();

        System.out.println(
                "===== SEARCH DELIVERY ====="
        );

        System.out.print(
                "Enter delivery ID (example D-1001): "
        );

        String deliveryId =
                scanner.nextLine()
                        .trim()
                        .toUpperCase();

        Delivery delivery =
                seasonService
                        .findDeliveryById(
                                deliveryId
                        );

        if (delivery != null) {

            System.out.println();
            System.out.println(
                    "Delivery found:"
            );

            System.out.println(
                    delivery
            );

        } else {

            System.out.println();
            System.out.println(
                    "Delivery not found."
            );
        }
    }


    // =========================================================
    // OBJECTIVE 3 - COMPARABLE
    // =========================================================

    public static void sortUsingComparable() {

        System.out.println();
        System.out.println(
                "===== COMPARABLE - DELIVERY ID ====="
        );

        seasonService.sortByDeliveryId();

        for (Delivery delivery :
                seasonService.getDeliveries()) {

            System.out.println(
                    delivery
            );
        }
    }


    // =========================================================
    // OBJECTIVE 3 - COMPARATOR
    // =========================================================

    public static void sortUsingComparator() {

        System.out.println();
        System.out.println(
                "===== COMPARATOR - PAYMENT DESCENDING ====="
        );

        seasonService.sortByPaymentDescending();

        for (Delivery delivery :
                seasonService.getDeliveries()) {

            System.out.println(
                    delivery
            );
        }
    }


    // =========================================================
    // OBJECTIVE 3 - TOP FIVE
    // =========================================================

    public static void showTopFive() {

        System.out.println();
        System.out.println(
                "===== TOP FIVE DELIVERIES ====="
        );

        List<Delivery> topFive =
                seasonService
                        .getTopFiveDeliveries();

        int position = 1;

        for (Delivery delivery :
                topFive) {

            System.out.println(
                    position
                            + ". "
                            + delivery
            );

            position++;
        }
    }


    // =========================================================
    // OBJECTIVE 3 - ITERATOR
    // =========================================================

    public static void removeRejected() {

        System.out.println();
        System.out.println(
                "===== ITERATOR - REMOVE REJECTED ====="
        );

        int before =
                seasonService
                        .getDeliveries()
                        .size();

        int removed =
                seasonService
                        .removeRejectedDeliveries();

        int after =
                seasonService
                        .getDeliveries()
                        .size();

        System.out.println(
                "Deliveries before removal: "
                        + before
        );

        System.out.println(
                "Rejected deliveries removed: "
                        + removed
        );

        System.out.println(
                "Deliveries after removal: "
                        + after
        );
    }
}
