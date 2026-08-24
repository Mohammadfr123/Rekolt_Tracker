package mu.rekolt.service;

import mu.rekolt.model.Delivery;

import java.util.ArrayList;

public class SeasonService {

    private ArrayList<Delivery> deliveries;

    public SeasonService() {
        deliveries = new ArrayList<>();
    }

    public void addDelivery(Delivery delivery) {
        deliveries.add(delivery);
    }

    public ArrayList<Delivery> getDeliveries() {
        return deliveries;
    }
}