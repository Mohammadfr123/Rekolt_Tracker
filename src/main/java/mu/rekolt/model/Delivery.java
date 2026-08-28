package mu.rekolt.model;

public class Delivery implements Comparable<Delivery> {

    private String deliveryId;
    private String memberId;
    private String memberName;
    private String produceCode;
    private double massKg;
    private int qualityScore;
    private int week;
    private String grade;
    private double netPayable;

    public Delivery(
            String deliveryId,
            String memberId,
            String memberName,
            String produceCode,
            double massKg,
            int qualityScore,
            int week,
            String grade,
            double netPayable) {

        this.deliveryId = deliveryId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.produceCode = produceCode;
        this.massKg = massKg;
        this.qualityScore = qualityScore;
        this.week = week;
        this.grade = grade;
        this.netPayable = netPayable;
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

    public String getProduceCode() {
        return produceCode;
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

    public String getGrade() {
        return grade;
    }

    public double getNetPayable() {
        return netPayable;
    }

    /*
     * Comparable:
     * Natural ordering of Delivery objects is by delivery ID.
     */
    @Override
    public int compareTo(Delivery other) {

        return this.deliveryId.compareTo(
                other.deliveryId
        );
    }

    @Override
    public String toString() {

        return String.format(
                "%s | %s | %s | %.1f kg | Quality %d | Grade %s | %.2f MUR",
                deliveryId,
                memberId,
                produceCode,
                massKg,
                qualityScore,
                grade,
                netPayable
        );
    }
}