package mu.rekolt.model;

import java.util.Objects;

public abstract class Produce {

    private final String code;
    private final String name;
    private final double pricePerKg;

    public Produce(
            String code,
            String name,
            double pricePerKg) {

        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException(
                    "Produce code cannot be empty."
            );
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Produce name cannot be empty."
            );
        }

        if (pricePerKg <= 0) {
            throw new IllegalArgumentException(
                    "Price per kg must be greater than zero."
            );
        }

        this.code = code;
        this.name = name;
        this.pricePerKg = pricePerKg;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public double getPricePerKg() {
        return pricePerKg;
    }

    public abstract double getCategoryMultiplier();

    @Override
    public String toString() {

        return String.format(
                "%s (%s) - %.2f MUR/kg",
                code,
                name,
                pricePerKg
        );
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Produce)) {
            return false;
        }

        Produce other = (Produce) obj;

        return code.equals(other.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}