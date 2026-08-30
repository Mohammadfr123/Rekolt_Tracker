# Rekolt_Tracker
Programming 2 first Java complete UML project. A produce tracking system for planters

A Java console application for recording produce deliveries,
calculating member payments, viewing season statistics, and
generating a season payment report.

Structure

src/main/java/mu/rekolt/
├── app/       Main.java — console menu loop, entry point
├── model/     Produce (abstract) + CerealProduce/PerishableProduce/CashCropProduce,
│              Grade (enum), Payable/Reportable (interfaces), Member, Delivery
├── service/   ProduceCatalog, GradingService, PaymentService, SeasonTracker,
│              ReportService, ReportGenerationException
└── util/      InputValidator, MoneyFormatter

Project

This project is developed progressively across six objectives.

### Objective 1
Environment setup and payment calculation.

### Objective 2
Control flow, validation, loops, methods and version control.

<img width="372" height="314" alt="Screenshot 2026-08-21 093309" src="https://github.com/user-attachments/assets/a92b09f0-f2c9-4f0c-94ba-b02eeb1f6eab" />
<img width="388" height="210" alt="Screenshot 2026-08-21 093534" src="https://github.com/user-attachments/assets/22910d04-11ba-4ad4-9af3-b7a787ca11b3" />
<img width="374" height="269" alt="Screenshot 2026-08-21 093605" src="https://github.com/user-attachments/assets/cdf5ef77-c58c-4765-8419-dbfe56cc2e22" />
<img width="433" height="176" alt="Screenshot 2026-08-21 093625" src="https://github.com/user-attachments/assets/27ceb816-5c73-4fec-bde7-282e2c2c511e" />

### Objective 3
Collections, searching, sorting and statistics.

### Objective 4
System design and UML documentation.

### Objective 5
Abstraction, inheritance, interfaces and polymorphism.

### Objective 6
Microsoft Word season report generation.
<img width="804" height="394" alt="Screenshot 2026-08-30 235744" src="https://github.com/user-attachments/assets/f67e13be-b83b-40b0-bace-b3825e88cf84" />

## How to Run

1. Clone the repository.
2. Open the project in an IDE.
3. Ensure JDK 17 or later is configured.
4. Run:
