  package mu.rekolt.app;

import mu.rekolt.service.DeliveryService;
import mu.rekolt.service.ReportService;
import mu.rekolt.service.SeasonService;

import java.util.Scanner;

public class Main {

    private static final Scanner scanner =
            new Scanner(System.in);

    private static final SeasonService seasonService =
            new SeasonService();

    private static final ReportService reportService =
            new ReportService(seasonService);

    public static void main(String[] args) {

        System.out.println(
                "*******************************************************************"
        );
        System.out.println(
                "   Welcome to The REKOLT Planter's Cooperative Produce Tracker"
        );
        System.out.println(
                "                    Season 2026"
        );
        System.out.println(
                "*******************************************************************"
        );

        seasonService.loadSampleDeliveries();

        boolean running = true;

        while (running) {

            displayMenu();

            int choice = readMenuChoice();

            switch (choice) {

                case 1:
                    recordDelivery();
                    break;

                case 2:
                    showSeasonFigures();
                    break;

                case 3:
                    reportService.generateSeasonReport();
                    break;

                case 4:
                    running = false;
                    System.out.println("\nGoodbye.");
                    break;
            }
        }

        scanner.close();
    }

    // Displays the menu
    private static void displayMenu() {

        System.out.println();
        System.out.println("1. Record a delivery");
        System.out.println("2. Season figures on screen");
        System.out.println("3. Generate the season report");
        System.out.println("4. Exit");
    }

    // Reads the menu choice
    private static int readMenuChoice() {

        while (true) {

            System.out.print("Choose an option: ");

            try {

                int choice =
                        Integer.parseInt(
                                scanner.nextLine().trim()
                        );

                if (choice >= 1 && choice <= 4) {
                    return choice;
                }

            } catch (NumberFormatException e) {
                // Invalid input
            }

            System.out.println(
                    "Invalid option. Please enter a number from 1 to 4."
            );
        }
    }

    // Calls the delivery function
    private static void recordDelivery() {

        DeliveryService deliveryService =
                new DeliveryService(
                        scanner,
                        seasonService
                );

        deliveryService.recordDelivery();
    }

    // Calls the season figures function
    private static void showSeasonFigures() {

        seasonService.displaySeasonFigures();
    }
}

