# Rekolt_Planter
Programming 2 first Java complete UML project. A produce tracking system for planters 

Structure

src/main/java/mu/rekolt/
├── app/       Main.java — console menu loop, entry point
├── model/     Produce (abstract) + CerealProduce/PerishableProduce/CashCropProduce,
│              Grade (enum), Payable/Reportable (interfaces), Member, Delivery
├── service/   ProduceCatalog, GradingService, PaymentService, SeasonTracker,
│              ReportService, ReportGenerationException
└── util/      InputValidator, MoneyFormatter