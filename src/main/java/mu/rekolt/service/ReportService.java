package mu.rekolt.service;

import mu.rekolt.model.Delivery;
import mu.rekolt.model.Grade;

import org.apache.poi.xwpf.usermodel.*;
import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

public class ReportService {

    private static final String OUTPUT_FOLDER = "output";
    private static final String REPORT_FILE =
            "output/season-report.docx";
    private static final String LOG_FILE =
            "output/run-log.txt";

    private final SeasonService seasonService;

    public ReportService(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    // Generates the complete season report
    public void generateSeasonReport() {

        System.out.println();
        System.out.println(
                "Writing output/season-report.docx ..."
        );

        try {

            Files.createDirectories(
                    Path.of(OUTPUT_FOLDER)
            );

            writeReport();
            writeLog(
                    "Season report generated successfully: "
                            + REPORT_FILE
            );

            System.out.println(
                    "Season report generated successfully."
            );

        } catch (IOException e) {

            System.out.println(
                    "Unable to write the season report. "
                            + "Check that the output folder is writable "
                            + "and that the report is not open."
            );
        }
    }

    // Creates the Word document
    private void writeReport() throws IOException {

        try (
                XWPFDocument document =
                        new XWPFDocument();

                FileOutputStream output =
                        new FileOutputStream(REPORT_FILE)
        ) {

            addTitle(document);

            Map<String, List<Delivery>> members =
                    groupDeliveries();

            boolean firstMember = true;

            for (Map.Entry<String, List<Delivery>> entry :
                    members.entrySet()) {

                if (!firstMember) {
                    addPageBreak(document);
                }

                firstMember = false;

                addMemberSection(
                        document,
                        entry.getKey(),
                        entry.getValue()
                );
            }

            addSeasonTotals(
                    document,
                    members
            );

            document.write(output);
        }
    }

    // Groups deliveries by member
    private Map<String, List<Delivery>> groupDeliveries() {

        Map<String, List<Delivery>> members =
                new LinkedHashMap<>();

        for (Delivery delivery :
                seasonService.getDeliveries()) {

            members.computeIfAbsent(
                    delivery.getMemberId(),
                    key -> new ArrayList<>()
            ).add(delivery);
        }

        return members;
    }

    // Adds report title
    private void addTitle(
            XWPFDocument document) {

        XWPFParagraph title =
                document.createParagraph();

        title.setAlignment(
                ParagraphAlignment.CENTER
        );

        XWPFRun titleRun =
                title.createRun();

        titleRun.setBold(true);
        titleRun.setFontSize(20);
        titleRun.setText(
                "REKOLT PRODUCE TRACKER"
        );

        XWPFParagraph subtitle =
                document.createParagraph();

        subtitle.setAlignment(
                ParagraphAlignment.CENTER
        );

        XWPFRun subtitleRun =
                subtitle.createRun();

        subtitleRun.setBold(true);
        subtitleRun.setFontSize(14);
        subtitleRun.setText(
                "Season 2026 Report"
        );
    }

    // Adds a page break
    private void addPageBreak(
            XWPFDocument document) {

        XWPFParagraph paragraph =
                document.createParagraph();

        paragraph.createRun().addBreak(
                BreakType.PAGE
        );
    }

    // Creates a member section
    private void addMemberSection(
            XWPFDocument document,
            String memberId,
            List<Delivery> deliveries) {

        Delivery firstDelivery =
                deliveries.get(0);

        addHeading(
                document,
                "Member Statement"
        );

        addText(
                document,
                "Member ID: " + memberId
        );

        addText(
                document,
                "Member Name: "
                        + firstDelivery.getMemberName()
        );

        addText(
                document,
                "Number of deliveries: "
                        + deliveries.size()
        );

        addDeliveryTable(
                document,
                deliveries
        );

        addPaymentSummary(
                document,
                deliveries
        );

        addSignatureLine(document);
    }

    // Adds a heading
    private void addHeading(
            XWPFDocument document,
            String text) {

        XWPFRun run =
                document.createParagraph()
                        .createRun();

        run.setBold(true);
        run.setFontSize(16);
        run.setText(text);
    }

    // Adds normal text
    private void addText(
            XWPFDocument document,
            String text) {

        document.createParagraph()
                .createRun()
                .setText(text);
    }

    // Creates the delivery table
    private void addDeliveryTable(
            XWPFDocument document,
            List<Delivery> deliveries) {

        String[] headers = {
                "Delivery ID",
                "Produce",
                "Mass (kg)",
                "Grade",
                "Quality",
                "Net Payable (MUR)"
        };

        XWPFTable table =
                document.createTable(
                        deliveries.size() + 1,
                        headers.length
                );

        XWPFTableRow header =
                table.getRow(0);

        for (int i = 0; i < headers.length; i++) {

            setCell(
                    header.getCell(i),
                    headers[i],
                    true
            );
        }

        for (int i = 0; i < deliveries.size(); i++) {

            Delivery delivery =
                    deliveries.get(i);

            XWPFTableRow row =
                    table.getRow(i + 1);

            setCell(
                    row.getCell(0),
                    delivery.getDeliveryId(),
                    false
            );

            setCell(
                    row.getCell(1),
                    delivery.getProduceCode(),
                    false
            );

            setCell(
                    row.getCell(2),
                    String.format(
                            "%.2f",
                            delivery.getMassKg()
                    ),
                    false
            );

            setCell(
                    row.getCell(3),
                    delivery.getGrade().toString(),
                    false
            );

            setCell(
                    row.getCell(4),
                    String.valueOf(
                            delivery.getQualityScore()
                    ),
                    false
            );

            setCell(
                    row.getCell(5),
                    String.format(
                            "%.2f",
                            delivery.calculateNetPayable()
                    ),
                    false
            );
        }
    }

    // Adds payment information
    private void addPaymentSummary(
            XWPFDocument document,
            List<Delivery> deliveries) {

        double commission = 0;
        double transport = 0;
        double total = 0;

        for (Delivery delivery : deliveries) {

            if (delivery.getGrade() != Grade.REJECT) {

                double value =
                        delivery.getMassKg()
                                * delivery.getProduce()
                                .getPricePerKg()
                                * delivery.getGrade()
                                .getMultiplier()
                                * delivery.getProduce()
                                .getCategoryMultiplier();

                commission += value * 0.05;
                transport += delivery.getMassKg() * 2;
            }

            total +=
                    delivery.calculateNetPayable();
        }

        addText(
                document,
                String.format(
                        "Commission (5%%): %.2f MUR",
                        commission
                )
        );

        addText(
                document,
                String.format(
                        "Transport levy: %.2f MUR",
                        transport
                )
        );

        XWPFRun run =
                document.createParagraph()
                        .createRun();

        run.setBold(true);
        run.setFontSize(13);

        run.setText(
                String.format(
                        "NET PAYABLE: %.2f MUR",
                        total
                )
        );
    }

    // Adds signature line
    private void addSignatureLine(
            XWPFDocument document) {

        addText(
                document,
                "Signature: ______________________________"
        );
    }

    // Adds final season totals
    private void addSeasonTotals(
            XWPFDocument document,
            Map<String, List<Delivery>> members) {

        addPageBreak(document);

        addHeading(
                document,
                "Season Totals"
        );

        int totalDeliveries = 0;
        double totalMass = 0;
        double totalPayment = 0;

        for (List<Delivery> deliveries :
                members.values()) {

            totalDeliveries +=
                    deliveries.size();

            for (Delivery delivery : deliveries) {

                totalMass +=
                        delivery.getMassKg();

                totalPayment +=
                        delivery.calculateNetPayable();
            }
        }

        addText(
                document,
                "Total members: "
                        + members.size()
        );

        addText(
                document,
                "Total deliveries: "
                        + totalDeliveries
        );

        addText(
                document,
                String.format(
                        "Total mass: %.2f kg",
                        totalMass
                )
        );

        XWPFRun run =
                document.createParagraph()
                        .createRun();

        run.setBold(true);
        run.setFontSize(14);

        run.setText(
                String.format(
                        "TOTAL SEASON PAYMENT: %.2f MUR",
                        totalPayment
                )
        );
    }

    // Sets text inside a table cell
    private void setCell(
            XWPFTableCell cell,
            String text,
            boolean bold) {

        cell.removeParagraph(0);

        XWPFRun run =
                cell.addParagraph()
                        .createRun();

        run.setBold(bold);
        run.setText(text);
    }

    // Writes to run-log.txt
    private void writeLog(
            String message) throws IOException {

        String timestamp =
                LocalDateTime.now()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "yyyy-MM-dd HH:mm:ss"
                                )
                        );

        Files.writeString(
                Path.of(LOG_FILE),
                timestamp
                        + " - "
                        + message
                        + System.lineSeparator(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }
}
