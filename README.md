# Rekolt_Planter
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

### Objective 3
Collections, searching, sorting and statistics.

### Objective 4
System design and UML documentation.

### Objective 5
Abstraction, inheritance, interfaces and polymorphism.

### Objective 6
Microsoft Word season report generation.

## How to Run

1. Clone the repository.
2. Open the project in an IDE.
3. Ensure JDK 17 or later is configured.
4. Run:
