package mu.rekolt.model;

public class PerishableProduce extends Produce {

    public PerishableProduce(
            String code,
            String name,
            double pricePerKg) {

        super(code, name, pricePerKg);
    }

    @Override
    public double getCategoryMultiplier() {
        return 0.90;
    }
}
