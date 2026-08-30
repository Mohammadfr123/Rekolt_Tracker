package mu.rekolt.model;

public class CerealProduce extends Produce {

    public CerealProduce(
            String code,
            String name,
            double pricePerKg) {

        super(code, name, pricePerKg);
    }

    @Override
    public double getCategoryMultiplier() {
        return 1.00;
    }
}