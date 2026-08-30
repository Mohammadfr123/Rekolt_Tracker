package mu.rekolt.model;

public enum Grade {

    A(1.15),
    B(1.00),
    C(0.85),
    REJECT(0.00);

    private final double multiplier;

    Grade(double multiplier) {
        this.multiplier = multiplier;
    }

    public double getMultiplier() {
        return multiplier;
    }
}