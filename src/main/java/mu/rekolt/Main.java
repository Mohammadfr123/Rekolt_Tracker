package mu.rekolt;

public class Main {
  public static void main (String[] args) {


      System.out.println("======================================");
      System.out.println("     REKOLT PRODUCE TRACKER");
      System.out.println("======================================");

      System.out.println("Objective 1 - Payment Calculation");

      // Delivery information

      String memberId = "M-0042";
      String produceCode = "BNS";
      double massKg = 236.0;
      int qualityScore = 91;

      // Price and payment rules

      double pricePerKg = 90.00;
      double gradeMultiplier = 1.15;
      double categoryMultiplier = 1.00;
      double commissionRate = 0.05;
      double transportLevyPerKg = 2.00;

      //  Base value
      double baseValue = massKg * pricePerKg;

      // Grade multiplier
      double gradeValue = baseValue * gradeMultiplier;

      //  Category multiplier
      double categoryValue = gradeValue * categoryMultiplier;

      // Commission
      double commission = categoryValue * commissionRate;

      // Transport levy
      double transportLevy = massKg * transportLevyPerKg;

      // Final payment
      double netPayable =
              categoryValue - commission - transportLevy;


      // Display results
      System.out.println();
      System.out.println("Member: " + memberId);
      System.out.println("Produce: " + produceCode);
      System.out.println("Mass: " + massKg + " kg");
      System.out.println("Quality Score: " + qualityScore);

      System.out.println();
      System.out.printf("Base value:        %.2f MUR%n", baseValue);
      System.out.printf("Grade value:       %.2f MUR%n", gradeValue);
      System.out.printf("Category value:    %.2f MUR%n", categoryValue);
      System.out.printf("Commission:       -%.2f MUR%n", commission);
      System.out.printf("Transport levy:   -%.2f MUR%n", transportLevy);
      System.out.printf("NET PAYABLE:       %.2f MUR%n", netPayable); //Money is rounded to two decimals on display only


        }
}