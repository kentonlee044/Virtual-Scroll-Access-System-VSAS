package a3.t10.g09;

import java.util.Scanner;

public class ManageScrollFiltering {
    private final Scanner scanner;
    private final AdminSystemAnalytics analytics; // will be used later

    public ManageScrollFiltering(Scanner scanner) {
        this.scanner = scanner;
        this.analytics = new AdminSystemAnalytics();
    }

    // Prints the filtering menu in a box
    private void PrintMenu() {
        // Use the same UI helpers used elsewhere for consistency
        System.out.println("\n");
        DisplayUIContent.printBorder('┌', '─', '┐');
        System.out.println(DisplayUIContent.centerRow("Filter Scrolls"));
        DisplayUIContent.printBorder('├', '─', '┤');
        System.out.println(DisplayUIContent.row("1) Filter by Date"));
        System.out.println(DisplayUIContent.row("2) Filter by Uploader ID"));
        System.out.println(DisplayUIContent.row("3) Filter by Name"));
        System.out.println(DisplayUIContent.row("4) Filter by Category"));
        System.out.println(DisplayUIContent.row("0) Back"));
        DisplayUIContent.printBorder('└', '─', '┘');
        System.out.print("Select an option: ");
    }

    public void run() {
        boolean running = true;
        while (running) {
            PrintMenu();
            if (!scanner.hasNextLine()) {
                System.out.println("No input detected. Exiting.");
                return;
            }
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    analytics.filterByDate();
                    break;
                case "2":
                    analytics.filterByOwner();
                    break;
                case "3":
                    analytics.filterByFilename();
                    break;
                case "4":
                    analytics.filterByCategory();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
