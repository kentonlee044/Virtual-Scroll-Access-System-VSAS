package a3.t10.g09;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AdminSystemAnalytics {
    // Simple ASCII table formatting (no box-drawing characters)
    private static final String TABLE_TITLE_FORMAT = "%-120s";
    private static final String TABLE_HEADER_FORMAT = "%-25s | %-16s | %-36s | %8s | %10s | %-25s";
    private static final String TABLE_ROW_FORMAT    = "%-25s | %-16s | %-36s | %8d | %10d | %-25s";
    private static final String TABLE_SEPARATOR     =
            "--------------------------+------------------+--------------------------------------+----------+------------+-------------------------";
    
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
            System.out.println();
            System.out.println("ADMIN SYSTEM ANALYTICS");
            System.out.println(TABLE_SEPARATOR);
            // Display filter options
            System.out.println("1. Show all scrolls");
            System.out.println("2. Filter by category ID");
            System.out.println("0. Exit");
            System.out.println(TABLE_SEPARATOR);
            
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
    
    public void filterByCategory() {
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

    public void filterByDate() {
        ensureScrollsLoaded();
        System.out.print("\nEnter date (YYYY-MM-DD) to filter (or leave empty to cancel): ");
        String dateStr = scanner.nextLine().trim();

        if (dateStr.isEmpty()) {
            return;
        }

        // Match by ISO-8601 date prefix (YYYY-MM-DD) stored in uploadDate
        List<Scroll> filteredScrolls = scrollList.getAllScrolls().stream()
                .filter(scroll -> scroll.getUploadDate() != null && scroll.getUploadDate().startsWith(dateStr))
                .collect(Collectors.toList());

        if (filteredScrolls.isEmpty()) {
            System.out.println("\nNo scrolls found with upload date: " + dateStr);
        } else {
            displayScrolls(filteredScrolls, "DATE: " + dateStr);
        }
    }
    
    public void filterByOwner() {
        ensureScrollsLoaded();
        System.out.print("\nEnter owner ID to filter (or leave empty to cancel): ");
        String ownerId = scanner.nextLine().trim();

        if (ownerId.isEmpty()) {
            return;
        }

        List<Scroll> filteredScrolls = scrollList.getAllScrolls().stream()
                .filter(scroll -> scroll.getOwnerId() != null && ownerId.equalsIgnoreCase(scroll.getOwnerId()))
                .collect(Collectors.toList());

        if (filteredScrolls.isEmpty()) {
            System.out.println("\nNo scrolls found for owner ID: " + ownerId);
        } else {
            displayScrolls(filteredScrolls, "OWNER: " + ownerId);
        }
    }

    public void filterByFilename() {
        ensureScrollsLoaded();
        System.out.print("\nEnter filename to filter (or leave empty to cancel): ");
        String filename = scanner.nextLine().trim();

        if (filename.isEmpty()) {
            return;
        }

        List<Scroll> filteredScrolls = scrollList.getAllScrolls().stream()
                .filter(scroll -> scroll.getFilename() != null && filename.equalsIgnoreCase(scroll.getFilename()))
                .collect(Collectors.toList());

        if (filteredScrolls.isEmpty()) {
            System.out.println("\nNo scrolls found with filename: " + filename);
        } else {
            displayScrolls(filteredScrolls, "FILENAME: " + filename);
        }
    }
    
    private void displayScrolls(List<Scroll> scrollsToDisplay, String title) {
        while(true){

            if (scrollsToDisplay == null || scrollsToDisplay.isEmpty()) {
                System.out.println("\nNo scrolls found.");
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
                return;
            }
            System.out.println();
            System.out.println(String.format(TABLE_TITLE_FORMAT, title));
            System.out.println(TABLE_SEPARATOR);
            System.out.println(String.format(TABLE_HEADER_FORMAT, "OWNER ID", "CATEGORY ID", "FILENAME", "UPLOADS", "DOWNLOADS", "UPLOAD DATE"));
            System.out.println(TABLE_SEPARATOR);
            
            for (Scroll scroll : scrollsToDisplay) {
                String owner = scroll.getOwnerId() == null ? "" : scroll.getOwnerId();
                String category = scroll.getCategorizationId();
                if (category == null || category.isEmpty()) {
                    category = "N/A";
                }
                String filename = scroll.getFilename() == null ? "" : scroll.getFilename();
                String date = scroll.getUploadDate() == null || scroll.getUploadDate().isEmpty() ? "N/A" : scroll.getUploadDate();
                System.out.println(String.format(TABLE_ROW_FORMAT,
                        truncate(owner, 25),
                        truncate(category, 16),
                        truncate(filename, 36),
                        scroll.getNumberOfUploads(),
                        scroll.getNumberOfDownloads(),
                        truncate(date, 25)));
            }
            System.out.println(TABLE_SEPARATOR);
            System.out.println("\nEnter download or preview to proceed, or press Enter to return: ");
            String token = scanner.nextLine().trim();
            if(token.equals("")){
                break;
            }
            else if(token.equalsIgnoreCase("preview")){
                System.out.println("Enter scroll name to preview: ");
                String scrollToPreview = scanner.nextLine().trim();
                String content = ScrollJSONHandler.previewScroll(scrollToPreview);
                
                // Check if local has filename 
                if(content == null){
                    System.out.println("Scroll does not exist. Please enter an existing scroll name");
                } else if(content.isEmpty()){
                    System.out.println("Scroll content is empty");
                }
                else{
                    System.out.println("\n--- Scroll Content Start ---\n");
                    System.out.println(content);
                    System.out.println("\n--- Scroll Content End ---\n");
                    System.out.println("Press Enter to continue...");
                    if (!scanner.hasNextLine()) {
                        System.out.println("No input detected. Exiting.");
                        break;
                    }
                }
                scanner.nextLine();
            } else{
                System.out.println("Please enter a valid command.");
                scanner.nextLine();
            }
        }
    }
    
    private String truncate(String str, int maxLength) {
        if (str == null) {
            return "";
        }
        return str.length() > maxLength ? str.substring(0, maxLength - 3) + "..." : str;
    }
}
