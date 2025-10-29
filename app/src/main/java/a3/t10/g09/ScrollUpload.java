package a3.t10.g09;

import java.util.List;
import java.util.Scanner;

import a3.t10.g09.Validator.ScrollCategorizationIdUniqueValidator;
import a3.t10.g09.Validator.ScrollCategorizationIdValidator;
import a3.t10.g09.Validator.ScrollFilenameValidator;
import a3.t10.g09.Validator.Validator;

import java.io.File;

public class ScrollUpload {
    private final Scanner scanner;
    private final String ownerId;
    private final Validator filenameValidator;
    private final Validator categorizationIdValidator;

    public ScrollUpload(Scanner scanner, String ownerId) {
        this.scanner = scanner;
        this.ownerId = ownerId;
        this.filenameValidator = new ScrollFilenameValidator();
        this.categorizationIdValidator = new ScrollCategorizationIdValidator();
    }

    private static final String CLEAR_SCREEN = "\033[H\033[2J";
    private static final int BOX_WIDTH = 70;
    private static final int LABEL_WIDTH = 16;
    private static final int FIELD_WIDTH = 42;
    private static final String INPUT_HINT = "Press Enter to confirm · Ctrl+C to cancel";
    private static final String SUBMIT_HINT = "Press Enter to submit · Ctrl+C to cancel";

    private static void renderFormUpload(String status,
                                         String hint,
                                         String filename,
                                         String categorizationId) {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
        DisplayUIContent.printBorder('┌', '─', '┐');
        System.out.println(DisplayUIContent.centerRow("Upload New Scroll"));
        DisplayUIContent.printBorder('├', '─', '┤');
        System.out.println(DisplayUIContent.fieldLine("Filename", filename));
        System.out.println(DisplayUIContent.fieldLine("Categorization ID", categorizationId));
        DisplayUIContent.printBorder('├', '─', '┤');
        System.out.println(DisplayUIContent.row(status));
        System.out.println(DisplayUIContent.row(hint));
        DisplayUIContent.printBorder('└', '─', '┘');
    }

    private static void renderFormReplace(String status,
                                          String hint,
                                          String ownerID,
                                          String oldFilename,
                                          String newFilename) {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
        DisplayUIContent.printBorder('┌', '─', '┐');
        System.out.println(DisplayUIContent.centerRow("Replace Existing Scroll"));
        DisplayUIContent.printBorder('├', '─', '┤');
        System.out.println(DisplayUIContent.fieldLine("ID", ownerID));
        System.out.println(DisplayUIContent.fieldLine("Old Filename", oldFilename));
        System.out.println(DisplayUIContent.fieldLine("New Filename", newFilename));
        DisplayUIContent.printBorder('├', '─', '┤');
        System.out.println(DisplayUIContent.row(status));
        System.out.println(DisplayUIContent.row(hint));
        DisplayUIContent.printBorder('└', '─', '┘');
    }

    private static String firstError(String... messages) {
        for (String message : messages) {
            if (message != null && !message.isBlank()) {
                return message;
            }
        }
        return null;
    }

    private boolean waitForLine() {
        if (!scanner.hasNextLine()) {
            System.out.println("No input detected. Exiting.");
            return false;
        }
        scanner.nextLine();
        return true;
    }

    public void run() {
        String filename = "";
        String categorizationId = "";

        // Prompt filename (required)
        String status = "Enter filename:";
        String candidate = filename;
        // Load existing scrolls once for validations (filename uniqueness and categorization ID uniqueness)
        ScrollList existingScrolls = ScrollJSONHandler.loadFromJson();
        while (true) {
            renderFormUpload(status, INPUT_HINT, candidate, categorizationId);
            System.out.print("> ");
            if (!scanner.hasNextLine()) {
                System.out.println("No input detected. Exiting.");
                return;
            }
            candidate = scanner.nextLine().trim();
            String error = filenameValidator.validate(candidate);
            if (error == null) {
                // Check that the provided path exists and is a file
                File file = new File(candidate);
                if (!file.exists() || !file.isFile()) {
                    status = "File not found at path: " + candidate;
                    continue;
                }
                // If filename already exists, increment and normalize uploads for all matching scrolls
                ScrollList matches = existingScrolls.getScrolls(candidate);
                if (matches != null) {
                    int currentMax = 0;
                    for (Scroll s : matches.getAllScrolls()) {
                        if (s.getNumberOfUploads() > currentMax) {
                            currentMax = s.getNumberOfUploads();
                        }
                    }
                    int targetUploads = currentMax + 1;
                    for (Scroll s : matches.getAllScrolls()) {
                        // Normalize to the same upload count
                        s.resetUploads();
                        for (int i = 0; i < targetUploads; i++) {
                            s.incrementUploads();
                        }
                    }
                    ScrollJSONHandler.saveToJson(matches);
                }
                filename = candidate;
                break;
            }
            status = error;
        }

        // Prompt categorization ID
        status = "Enter categorization ID:";
        candidate = categorizationId;
        // prepare uniqueness validator against current data
        ScrollCategorizationIdUniqueValidator uniqueValidator = new ScrollCategorizationIdUniqueValidator(existingScrolls);
        while (true) {
            renderFormUpload(status, INPUT_HINT, filename, candidate);
            System.out.print("> ");
            if (!scanner.hasNextLine()) {
                System.out.println("No input detected. Exiting.");
                return;
            }
            candidate = scanner.nextLine().trim();
            if (candidate.isEmpty()) {
                status = "Categorization ID is required.";
                continue;
            }
            String error = firstError(
                    categorizationIdValidator.validate(candidate),
                    uniqueValidator.validate(candidate)
            );
            if (error == null) {
                categorizationId = candidate;
                break;
            }
            status = error;
        }

        // Review
        renderFormUpload("Review upload", SUBMIT_HINT, filename, categorizationId);
        if (!waitForLine()) {
            return;
        }

        // Create and save the scroll
        Scroll newScroll = new Scroll(filename, categorizationId, ownerId);
        Scroll scroll_same_filename = existingScrolls.getScroll(filename);
        //set # of uploads to the same as any scroll with the same filename
        if(scroll_same_filename != null){
            for(int i=0; i<scroll_same_filename.getNumberOfUploads(); i++){
                newScroll.incrementUploads();
            }
        }else{
            newScroll.incrementUploads();
        }
        ScrollList scrolls = ScrollJSONHandler.loadFromJson();
        scrolls.addScroll(newScroll);
        if (ScrollJSONHandler.saveToJson(scrolls)) {
            renderFormUpload("✓ Scroll uploaded successfully!", "Press Enter to continue", filename, categorizationId);
            waitForLine();
        } else {
            renderFormUpload("✗ Error: Failed to save scroll.", "Press Enter to return", filename, categorizationId);
            waitForLine();
        }
    }

    public void replaceExisting() {
        // Similar to run(), but replaces an existing scroll
        // Implementation would be similar to run(), with adjustments for replacement logic
        String oldFileName = "";
        String newFileName = "";
        String ownerID = "";
        List<Scroll> ownedScrolls;

        // Prompt filename (required)
        String status = "Enter ID key:";
        String candidate = ownerID;
        while(true){
            renderFormReplace(status, 
                              INPUT_HINT,
                              candidate,
                              oldFileName,
                              newFileName);
            System.out.print("> ");
            if (!scanner.hasNextLine()) {
                System.out.println("No input detected. Exiting.");
                return;
            }
            candidate = scanner.nextLine().trim();
            ownedScrolls = ScrollJSONHandler.loadFromJson().getScrollsByOwner(candidate);

            if (!ownedScrolls.isEmpty()) {
                ownerID = candidate;
                break;
            }
            status = "No scrolls found for the given owner ID. Please try again.";
        }

        status = "Enter old filename:";
        candidate = oldFileName;
        while (true) {
            renderFormReplace(status, 
                              INPUT_HINT,
                              ownerID,
                              candidate,
                              newFileName);
            System.out.print("> ");
            if (!scanner.hasNextLine()) {
                System.out.println("No input detected. Exiting.");
                return;
            }
            candidate = scanner.nextLine().trim();
            boolean found = false;
            for (Scroll scroll : ownedScrolls) {
                if (scroll.getFilename().equals(candidate) && scroll.isOwnedBy(ownerID)) {
                    found = true;
                    break;
                }
            }
            if (found) {
                oldFileName = candidate;
                break;
            }
            status = "Filename not found among owner's scrolls. Please try again.";
        }
        // Prompt new filename (required)
        status = "Enter new filename:";
        candidate = newFileName;
        ScrollList scrolls = ScrollJSONHandler.loadFromJson();
        while (true) {
            renderFormReplace(status, 
                              INPUT_HINT,
                              ownerID,
                              oldFileName,
                              candidate);
            System.out.print("> ");
            if (!scanner.hasNextLine()) {
                System.out.println("No input detected. Exiting.");
                return;
            }
        
            candidate = scanner.nextLine().trim();
            if(candidate.isEmpty()){
                status = "Filename cannot be empty.";
                continue;
            }

            String error = firstError(filenameValidator.validate(candidate));
            
            if (scrolls.getScroll(candidate) != null) {
                    status = "A scroll with this filename already exists. Please choose a different name.";
                    continue;
            }
            if (error == null) {
                newFileName = candidate;
                break;
            }
            status = error;
        }

        // Review
        renderFormReplace(status, SUBMIT_HINT, ownerID, oldFileName, newFileName);
        if (!waitForLine()) {
            return;
        }

        // Replace and save the scroll
        boolean replaced = scrolls.replaceExistingScroll(oldFileName, newFileName, ownerID);
        
        if (replaced && ScrollJSONHandler.saveToJson(scrolls)) {
            renderFormReplace("✓ Scroll replaced successfully!", "Press Enter to continue", ownerID, oldFileName, newFileName);
            waitForLine();
        } else {
            renderFormReplace("✗ Error: Failed to replace scroll.", "Press Enter to return", ownerID, oldFileName, newFileName);
            waitForLine();
        }
    }
}
