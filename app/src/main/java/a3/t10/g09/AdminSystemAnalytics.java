package a3.t10.g09;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AdminSystemAnalytics {
    private static final String HORIZONTAL_LINE = "┌────────────────────────────────────────────────────────┐";
    private static final String BOTTOM_LINE = "└───────────────────────────┴────────┴──────────┴──────────┘";
    private static final String HEADER = "│ %-54s │";
    private static final String ROW_HEADER = "│ %-25s │ %-6s │ %-8s │ %-8s │";
    private static final String ROW_FORMAT = "│ %-25s │ %6d │ %8d │ %-8s │";
    
    private ScrollList scrollList;
    private final Scanner scanner;
    private static final String SCROLLS_JSON_PATH = "src/main/java/a3/t10/g09/data/scrolls.json";

    public AdminSystemAnalytics() {
        this.scanner = new Scanner(System.in);
        this.scrollList = new ScrollList();
    }
    
    private void ensureScrollsLoaded() {
        if (scrollList == null || scrollList.getAllScrolls().isEmpty()) {
            this.scrollList = ScrollJSONHandler.loadFromJson();
        }
    }

    public void displayAnalytics() {
        boolean running = true;
        
        while (running) {
            System.out.println("\n" + HORIZONTAL_LINE);
            System.out.println(String.format(HEADER, "ADMIN SYSTEM ANALYTICS"));
            System.out.println(String.format(HEADER, ""));
            
            // Display filter options
            System.out.println(String.format(HEADER, "1. Show all scrolls"));
            System.out.println(String.format(HEADER, "2. Filter by category ID"));
            System.out.println(String.format(HEADER, "0. Exit"));
            System.out.println(BOTTOM_LINE);
            
            System.out.print("\nSelect an option: ");
            String input = scanner.nextLine().trim();
            
            switch (input) {
                case "1":
                    displayAllScrolls();
                    break;
                case "2":
                    filterByCategory();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("\nInvalid option. Please try again.");
            }
        }
    }
    
    public void displayAllScrolls() {
        ensureScrollsLoaded();
        displayScrolls(scrollList.getAllScrolls(), "ALL SCROLLS");
    }
    
    private void filterByCategory() {
        ensureScrollsLoaded();
        System.out.print("\nEnter category ID to filter (or leave empty to cancel): ");
        String categoryId = scanner.nextLine().trim();
        
        if (categoryId.isEmpty()) {
            return;
        }
        
        List<Scroll> filteredScrolls = scrollList.getAllScrolls().stream()
                .filter(scroll -> categoryId.equalsIgnoreCase(scroll.getCategorizationId()))
                .collect(Collectors.toList());
                
        if (filteredScrolls.isEmpty()) {
            System.out.println("\nNo scrolls found with category ID: " + categoryId);
        } else {
            displayScrolls(filteredScrolls, "CATEGORY: " + categoryId);
        }
    }
    
    private void displayScrolls(List<Scroll> scrollsToDisplay, String title) {
        if (scrollsToDisplay == null || scrollsToDisplay.isEmpty()) {
            System.out.println("\nNo scrolls found.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }
        System.out.println("\n" + HORIZONTAL_LINE);
        System.out.println(String.format(HEADER, title));
        System.out.println("├───────────────────────────┼────────┼──────────┼──────────┤");
        System.out.println(String.format(ROW_HEADER, "SCROLL NAME", "UPLOADS", "DOWNLOADS", "CATEGORY"));
        System.out.println("├───────────────────────────┼────────┼──────────┼──────────┤");
        
        for (Scroll scroll : scrollsToDisplay) {
            String category = scroll.getCategorizationId();
            if (category == null || category.isEmpty()) {
                category = "N/A";
            }
            System.out.println(String.format(ROW_FORMAT, 
                    truncate(scroll.getFilename(), 25),
                    scroll.getNumberOfUploads(),
                    scroll.getNumberOfDownloads(),
                    truncate(category, 8)));
        }
        
        System.out.println(BOTTOM_LINE);
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private String truncate(String str, int maxLength) {
        if (str == null) {
            return "";
        }
        return str.length() > maxLength ? str.substring(0, maxLength - 3) + "..." : str;
    }
}
