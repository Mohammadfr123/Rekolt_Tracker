package mu.rekolt.service;

import mu.rekolt.model.Delivery;
import mu.rekolt.model.Grade;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReportService {

    private static final String OUTPUT_DIRECTORY = "output";
    private static final String REPORT_FILE =
            "output/season-report.docx";
    private static final String LOG_FILE =
            "output/run-log.txt";

    private final SeasonService seasonService;

    public ReportService(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    /**
     * Generates the complete season report.
     */
    public void generateSeasonReport() {

        System.out.println();
        System.out.println(
                "Writing output/season-report.docx ..."
        );

        try {

            createOutputDirectory();

            writeWordDocument();

            writeRunLog(
                    "Season report generated successfully: "
                            + REPORT_FILE
            );

            System.out.println(
                    "Season report generated successfully."
            );

        } catch (FileNotFoundException e) {

            System.out.println(
                    "Unable to write the season report. "
                            + "Please check that the output folder exists "
                            + "and that the report file is not open."
            );

        } catch (IOException e) {

            System.out.println(
                    "Unable to write the season report. "
                            + "Please check your file permissions "
                            + "and make sure the output folder is writable."
            );
        }
    }

    /**
     * Creates the output directory if it does not exist.
     */
    private void createOutputDirectory()
            throws IOException {

        Files.createDirectories(
                Path.of(OUTPUT_DIRECTORY)
        );
    }

    /**
     * Creates the Word document.
     */
    private void writeWordDocument()
            throws IOException {

        /*
         * try-with-resources automatically closes the
         * Word document and output stream.
         */
        try (
                XWPFDocument document =
                        new XWPFDocument();

                FileOutputStream outputStream =
                        new FileOutputStream(REPORT_FILE)
        ) {

            addReportTitle(document);

            Map<String, List<Delivery>>
                    deliveriesPerMember =
                    groupDeliveriesByMember();

            boolean firstMember = true;

            for (
                    Map.Entry<String, List<Delivery>> entry :
                    deliveriesPerMember.entrySet()
            ) {

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
                    deliveriesPerMember
            );

            document.write(outputStream);
        }
    }

    /**
     * Groups all deliveries by member.
     */
    private Map<String, List<Delivery>>
    groupDeliveriesByMember() {

        Map<String, List<Delivery>>
                grouped =
                new LinkedHashMap<>();

        for (Delivery delivery :
                seasonService.getDeliveries()) {

            grouped.computeIfAbsent(
                    delivery.getMemberId(),
                    key -> new ArrayList<>()
            ).add(delivery);
        }

        return grouped;
    }

    /**
     * Adds the main report title.
     */
    private void addReportTitle(
            XWPFDocument document) {

        XWPFParagraph paragraph =
                document.createParagraph();

        paragraph.setAlignment(
                org.apache.poi.xwpf.usermodel
                        .ParagraphAlignment.CENTER
        );

        XWPFRun run =
                paragraph.createRun();

        run.setBold(true);
        run.setFontSize(20);

        run.setText(
                "REKOLT PRODUCE TRACKER"
        );

        XWPFParagraph subtitle =
                document.createParagraph();

        subtitle.setAlignment(
                org.apache.poi.xwpf.usermodel
                        .ParagraphAlignment.CENTER
        );

        XWPFRun subtitleRun =
                subtitle.createRun();

        subtitleRun.setBold(true);
        subtitleRun.setFontSize(14);

        subtitleRun.setText(
                "Season 2026 Report"
        );
    }

    /**
     * Adds a page break before the next member.
     */
    private void addPageBreak(
            XWPFDocument document) {

        XWPFParagraph paragraph =
                document.createParagraph();

        XWPFRun run =
                paragraph.createRun();

        run.addBreak(
                org.apache.poi.xwpf.usermodel.BreakType.PAGE
        );
    }

    /**
     * Creates one complete section for a member.
     */
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

        addMemberPaymentSummary(
                document,
                deliveries
        );

        addSignatureLine(document);
    }

    /**
     * Adds a heading.
     */
    private void addHeading(
            XWPFDocument document,
            String text) {

        XWPFParagraph paragraph =
                document.createParagraph();

        XWPFRun run =
                paragraph.createRun();

        run.setBold(true);
        run.setFontSize(16);
        run.setText(text);
    }

    /**
     * Adds normal text.
     */
    private void addText(
            XWPFDocument document,
            String text) {

        XWPFParagraph paragraph =
                document.createParagraph();

        XWPFRun run =
                paragraph.createRun();

        run.setText(text);
    }

    /**
     * Creates the delivery table for a member.
     */
    private void addDeliveryTable(
            XWPFDocument document,
            List<Delivery> deliveries) {

        XWPFTable table =
                document.createTable(
                        deliveries.size() + 1,
                        6
                );

        String[] headers = {
                "Delivery ID",
                "Produce",
                "Mass (kg)",
                "Grade",
                "Quality",
                "Net Payable (MUR)"
        };

        XWPFTableRow headerRow =
                table.getRow(0);

        for (int i = 0;
             i < headers.length;
             i++) {

            setCellText(
                    headerRow.getCell(i),
                    headers[i],
                    true
            );
        }

        for (int i = 0;
             i < deliveries.size();
             i++) {

            Delivery delivery =
                    deliveries.get(i);

            XWPFTableRow row =
                    table.getRow(i + 1);

            setCellText(
                    row.getCell(0),
                    delivery.getDeliveryId(),
                    false
            );

            setCellText(
                    row.getCell(1),
                    delivery.getProduceCode(),
                    false
            );

            setCellText(
                    row.getCell(2),
                    String.format(
                            "%.2f",
                            delivery.getMassKg()
                    ),
                    false
            );

            setCellText(
                    row.getCell(3),
                    delivery.getGrade().toString(),
                    false
            );

            setCellText(
                    row.getCell(4),
                    String.valueOf(
                            delivery.getQualityScore()
                    ),
                    false
            );

            setCellText(
                    row.getCell(5),
                    String.format(
                            "%.2f",
                            delivery.calculateNetPayable()
                    ),
                    false
            );
        }
    }

    /**
     * Adds commission, levy and net payable.
     */
    private void addMemberPaymentSummary(
            XWPFDocument document,
            List<Delivery> deliveries) {

        double totalCommission = 0.0;
        double totalTransportLevy = 0.0;
        double totalNetPayable = 0.0;

        for (Delivery delivery :
                deliveries) {

            if (delivery.getGrade()
                    != Grade.REJECT) {

                double valueAfterCategory =
                        delivery.getMassKg()
                                * delivery.getProduce()
                                .getPricePerKg()
                                * delivery.getGrade()
                                .getMultiplier()
                                * delivery.getProduce()
                                .getCategoryMultiplier();

                double commission =
                        valueAfterCategory * 0.05;

                double transportLevy =
                        delivery.getMassKg() * 2.00;

                totalCommission += commission;
                totalTransportLevy += transportLevy;
            }

            totalNetPayable +=
                    delivery.calculateNetPayable();
        }

        addText(
                document,
                String.format(
                        "Commission (5%%): %.2f MUR",
                        totalCommission
                )
        );

        addText(
                document,
                String.format(
                        "Transport levy: %.2f MUR",
                        totalTransportLevy
                )
        );

        XWPFParagraph paragraph =
                document.createParagraph();

        XWPFRun run =
                paragraph.createRun();

        run.setBold(true);
        run.setFontSize(13);

        run.setText(
                String.format(
                        "NET PAYABLE: %.2f MUR",
                        totalNetPayable
                )
        );
    }

    /**
     * Adds the member signature line.
     */
    private void addSignatureLine(
            XWPFDocument document) {

        XWPFParagraph paragraph =
                document.createParagraph();

        paragraph.createRun().setText(
                "Signature: ______________________________"
        );
    }

    /**
     * Adds the final season totals.
     */
    private void addSeasonTotals(
            XWPFDocument document,
            Map<String, List<Delivery>>
                    deliveriesPerMember) {

        addPageBreak(document);

        addHeading(
                document,
                "Season Totals"
        );

        int totalDeliveries = 0;
        double totalMass = 0.0;
        double totalPayment = 0.0;

        for (
                List<Delivery> deliveries :
                deliveriesPerMember.values()
        ) {

            totalDeliveries +=
                    deliveries.size();

            for (Delivery delivery :
                    deliveries) {

                totalMass +=
                        delivery.getMassKg();

                totalPayment +=
                        delivery.calculateNetPayable();
            }
        }

        addText(
                document,
                "Total members: "
                        + deliveriesPerMember.size()
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

        XWPFParagraph paragraph =
                document.createParagraph();

        XWPFRun run =
                paragraph.createRun();

        run.setBold(true);
        run.setFontSize(14);

        run.setText(
                String.format(
                        "TOTAL SEASON PAYMENT: %.2f MUR",
                        totalPayment
                )
        );
    }

    /**
     * Sets text inside a Word table cell.
     */
    private void setCellText(
            XWPFTableCell cell,
            String text,
            boolean bold) {

        cell.removeParagraph(
                0
        );

        XWPFParagraph paragraph =
                cell.addParagraph();

        XWPFRun run =
                paragraph.createRun();

        run.setBold(bold);
        run.setText(text);
    }

    /**
     * Appends a timestamped line to run-log.txt.
     */
    private void writeRunLog(
            String message)
            throws IOException {

        String timestamp =
                LocalDateTime.now()
                        .format(
                                DateTimeFormatter
                                        .ofPattern(
                                                "yyyy-MM-dd HH:mm:ss"
                                        )
                        );

        String line =
                timestamp
                        + " - "
                        + message
                        + System.lineSeparator();

        Files.writeString(
                Path.of(LOG_FILE),
                line,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }
}
