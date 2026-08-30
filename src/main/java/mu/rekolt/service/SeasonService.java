package mu.rekolt.service;

import mu.rekolt.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mu.rekolt.model.Payable;
public class SeasonService implements Payable, Reportable {

    private final List<Delivery> deliveries;

    public SeasonService() {
        deliveries = new ArrayList<>();
    }

    // Adds a delivery to the current season
    public void addDelivery(Delivery delivery) {

        if (delivery == null) {
            throw new IllegalArgumentException(
                    "Delivery cannot be null."
            );
        }

        deliveries.add(delivery);
    }

    // Returns all deliveries
    public List<Delivery> getDeliveries() {
        return new ArrayList<>(deliveries);
    }

    // Returns all distinct member IDs
    public Set<String> getDistinctMemberIds() {

        Set<String> memberIds = new HashSet<>();

        for (Delivery delivery : deliveries) {
            memberIds.add(delivery.getMemberId());
        }

        return memberIds;
    }

    // Groups deliveries by member
    public Map<String, List<Delivery>> getDeliveriesPerMember() {

        Map<String, List<Delivery>> result =
                new HashMap<>();

        for (Delivery delivery : deliveries) {

            result.computeIfAbsent(
                    delivery.getMemberId(),
                    key -> new ArrayList<>()
            ).add(delivery);
        }

        return result;
    }

    // Calculates total payment for every member
    public Map<String, Double> calculatePaymentPerMember() {

        Map<String, Double> payments =
                new HashMap<>();

        for (Delivery delivery : deliveries) {

            payments.merge(
                    delivery.getMemberId(),
                    delivery.calculateNetPayable(),
                    Double::sum
            );
        }

        return payments;
    }

    // Returns the total number of deliveries
    public int getDeliveryCount() {
        return deliveries.size();
    }

    // Returns total mass delivered
    public double getTotalMass() {

        double total = 0.0;

        for (Delivery delivery : deliveries) {
            total += delivery.getMassKg();
        }

        return total;
    }

    // Returns total season payment
    public double getTotalSeasonPayment() {

        double total = 0.0;

        for (Delivery delivery : deliveries) {
            total += delivery.calculateNetPayable();
        }

        return total;
    }

    // Finds a delivery by its ID
    public Delivery findDeliveryById(String deliveryId) {

        for (Delivery delivery : deliveries) {

            if (delivery.getDeliveryId().equals(deliveryId)) {
                return delivery;
            }
        }

        return null;
    }

    // Removes rejected deliveries
    public int removeRejectedDeliveries() {

        int before = deliveries.size();

        deliveries.removeIf(
                delivery -> delivery.getGrade() == Grade.REJECT
        );

        return before - deliveries.size();
    }

    // Creates sample deliveries for testing
    public void loadSampleDeliveries() {

        deliveries.clear();

        Produce beans =
                new CerealProduce(
                        "BNS",
                        "Beans",
                        90.00
                );

        Produce maize =
                new CerealProduce(
                        "MZE",
                        "Maize",
                        30.00
                );

        Produce potatoes =
                new PerishableProduce(
                        "POT",
                        "Potatoes",
                        45.00
                );

        Produce tea =
                new CashCropProduce(
                        "TEA",
                        "Green Tea Leaf",
                        25.00
                );

        addDelivery(
                new Delivery(
                        "D-1001",
                        "M-0042",
                        "Devi Ramjaun",
                        beans,
                        236.0,
                        91,
                        3,
                        Grade.A
                )
        );

        addDelivery(
                new Delivery(
                        "D-1002",
                        "M-0015",
                        "Ahmed Ali",
                        maize,
                        150.0,
                        75,
                        5,
                        Grade.B
                )
        );

        addDelivery(
                new Delivery(
                        "D-1003",
                        "M-0042",
                        "Devi Ramjaun",
                        potatoes,
                        100.0,
                        60,
                        7,
                        Grade.C
                )
        );

        addDelivery(
                new Delivery(
                        "D-1004",
                        "M-0088",
                        "Sophie Jean",
                        tea,
                        80.0,
                        90,
                        10,
                        Grade.A
                )
        );

        addDelivery(
                new Delivery(
                        "D-1005",
                        "M-0015",
                        "Ahmed Ali",
                        beans,
                        50.0,
                        40,
                        12,
                        Grade.REJECT
                )
        );
    }

    // Provides a simple report line for the service
    @Override
    public String getReportLine() {

        return String.format(
                "Season: %d deliveries | %.2f kg | %.2f MUR",
                getDeliveryCount(),
                getTotalMass(),
                getTotalSeasonPayment()
        );
    }

    @Override
    public double calculateNetPayable() {

        double total = 0.0;

        for (Delivery delivery : deliveries) {
            total += delivery.calculateNetPayable();
        }

        return total;
    }

    public List<Produce> getAvailableProduce() {

        List<Produce> produces =
                new ArrayList<>();

        produces.add(
                new CerealProduce(
                        "MZE",
                        "Maize",
                        30.00
                )
        );

        produces.add(
                new CerealProduce(
                        "BNS",
                        "Beans",
                        90.00
                )
        );

        produces.add(
                new PerishableProduce(
                        "POT",
                        "Potatoes",
                        45.00
                )
        );

        produces.add(
                new CashCropProduce(
                        "TEA",
                        "Green Tea Leaf",
                        25.00
                )
        );

        return produces;
    }

    public void displayProduceMultipliers() {

        List<Produce> produces =
                getAvailableProduce();

        for (Produce produce : produces) {

            System.out.printf(
                    "%s - %s -> x %.2f%n",
                    produce.getCode(),
                    produce.getName(),
                    produce.getCategoryMultiplier()
            );
        }
    }

    public void displaySeasonFigures() {

        System.out.println();
        System.out.println(
                "===== DELIVERIES PER MEMBER ====="
        );

        Map<String, List<Delivery>> deliveriesPerMember =
                getDeliveriesPerMember();

        for (Map.Entry<String, List<Delivery>> entry :
                deliveriesPerMember.entrySet()) {

            System.out.println();
            System.out.println(
                    "Member: " + entry.getKey()
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
                calculatePaymentPerMember();

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
                        getDeliveryCount()
        );

        System.out.printf(
                "Total mass: %.2f kg%n",
                getTotalMass()
        );

        System.out.printf(
                "Total season payment: %.2f MUR%n",
                getTotalSeasonPayment()
        );
    }


}