package mu.rekolt.model;

import java.util.Objects;

public class Delivery
        implements Comparable<Delivery>, Payable, Reportable {

    private final String deliveryId;
    private final String memberId;
    private final String memberName;
    private final Produce produce;
    private final double massKg;
    private final int qualityScore;
    private final int week;
    private final Grade grade;

    public Delivery(
            String deliveryId,
            String memberId,
            String memberName,
            Produce produce,
            double massKg,
            int qualityScore,
            int week,
            Grade grade) {

        if (deliveryId == null || deliveryId.isBlank()) {
            throw new IllegalArgumentException(
                    "Delivery ID cannot be empty."
            );
        }

        if (memberId == null || memberId.isBlank()) {
            throw new IllegalArgumentException(
                    "Member ID cannot be empty."
            );
        }

        if (memberName == null || memberName.isBlank()) {
            throw new IllegalArgumentException(
                    "Member name cannot be empty."
            );
        }

        if (produce == null) {
            throw new IllegalArgumentException(
                    "Produce cannot be null."
            );
        }

        if (massKg <= 0 || massKg > 5000) {
            throw new IllegalArgumentException(
                    "Mass must be greater than 0 and not more than 5000 kg."
            );
        }

        if (qualityScore < 0 || qualityScore > 100) {
            throw new IllegalArgumentException(
                    "Quality score must be between 0 and 100."
            );
        }

        if (week < 1 || week > 20) {
            throw new IllegalArgumentException(
                    "Week must be between 1 and 20."
            );
        }

        if (grade == null) {
            throw new IllegalArgumentException(
                    "Grade cannot be null."
            );
        }

        this.deliveryId = deliveryId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.produce = produce;
        this.massKg = massKg;
        this.qualityScore = qualityScore;
        this.week = week;
        this.grade = grade;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public Produce getProduce() {
        return produce;
    }

    public String getProduceCode() {
        return produce.getCode();
    }

    public double getMassKg() {
        return massKg;
    }

    public int getQualityScore() {
        return qualityScore;
    }

    public int getWeek() {
        return week;
    }

    public Grade getGrade() {
        return grade;
    }

    @Override
    public double calculateNetPayable() {

        if (grade == Grade.REJECT) {
            return 0.0;
        }

        double baseValue =
                massKg * produce.getPricePerKg();

        double gradeValue =
                baseValue * grade.getMultiplier();

        double categoryValue =
                gradeValue * produce.getCategoryMultiplier();

        double commission =
                categoryValue * 0.05;

        double transportLevy =
                massKg * 2.00;

        return categoryValue
                - commission
                - transportLevy;
    }

    @Override
    public String getReportLine() {

        return String.format(
                "%s | %s | %s | %.1f kg | Grade %s | %.2f MUR",
                deliveryId,
                memberId,
                produce.getCode(),
                massKg,
                grade,
                calculateNetPayable()
        );
    }

    @Override
    public int compareTo(Delivery other) {
        return this.deliveryId.compareTo(other.deliveryId);
    }

    @Override
    public String toString() {
        return getReportLine();
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Delivery)) {
            return false;
        }

        Delivery other = (Delivery) obj;

        return deliveryId.equals(other.deliveryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deliveryId);
    }
}
