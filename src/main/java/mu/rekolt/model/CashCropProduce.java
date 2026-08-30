package mu.rekolt.model;

public class CashCropProduce extends Produce {

    public CashCropProduce(
            String code,
            String name,
            double pricePerKg) {

        super(code, name, pricePerKg);
    }

    @Override
    public double getCategoryMultiplier() {
        return 1.10;
    }
}