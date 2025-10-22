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

    // Helpers copied/adapted from RegisterCli for consistent UI
    private static void printBorder(char left, char fill, char right) {
        System.out.println(left + String.valueOf(fill).repeat(BOX_WIDTH) + right);
    }

    private static String centerRow(String text) {
        String content = text == null ? "" : text;
        if (content.length() > BOX_WIDTH) {
            content = content.substring(0, BOX_WIDTH - 1) + "…";
        }
        int padding = Math.max(0, BOX_WIDTH - content.length());
        int leftPad = padding / 2;
        int rightPad = padding - leftPad;
        return "│" + " ".repeat(leftPad) + content + " ".repeat(rightPad) + "│";
    }

    private static String row(String text) {
        String content = text == null ? "" : text;
        if (content.length() > BOX_WIDTH) {
            content = content.substring(0, BOX_WIDTH - 1) + "…";
        }
        return "│" + String.format("%-" + BOX_WIDTH + "s", content) + "│";
    }

    private static String padValue(String value) {
        String content = value == null ? "" : value;
        if (content.length() > FIELD_WIDTH) {
            content = content.substring(0, FIELD_WIDTH - 1) + "…";
        }
        return String.format("%-" + FIELD_WIDTH + "s", content);
    }

    private static String fieldLine(String label, String value) {
        String paddedValue = padValue(value);
        String content = String.format("%-" + LABEL_WIDTH + "s [%s]", label, paddedValue);
        return row(content);
    }

    private static void renderFormUpload(String status,
                                         String hint,
                                         String filename,
                                         String categorizationId) {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
        printBorder('┌', '─', '┐');
        System.out.println(centerRow("Upload New Scroll"));
        printBorder('├', '─', '┤');
        System.out.println(fieldLine("Filename", filename));
        System.out.println(fieldLine("Categorization ID", categorizationId));
        printBorder('├', '─', '┤');
        System.out.println(row(status));
        System.out.println(row(hint));
        printBorder('└', '─', '┘');
    }

    private static void renderFormReplace(String status,
                                          String hint,
                                          String ownerID,
                                          String oldFilename,
                                          String newFilename) {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
        printBorder('┌', '─', '┐');
        System.out.println(centerRow("Replace Existing Scroll"));
        printBorder('├', '─', '┤');
        System.out.println(fieldLine("ID", ownerID));
        System.out.println(fieldLine("Old Filename", oldFilename));
        System.out.println(fieldLine("New Filename", newFilename));
        printBorder('├', '─', '┤');
        System.out.println(row(status));
        System.out.println(row(hint));
        printBorder('└', '─', '┘');
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
                filename = candidate;
                break;
            }
            status = error;
        }

        // Prompt categorization ID (optional)
        status = "Enter categorization ID (optional):";
        candidate = categorizationId;
        // prepare uniqueness validator against current data
        ScrollList existingScrolls = ScrollJSONHandler.loadFromJson();
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
                categorizationId = "";
                break;
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
                break;
            }
            String error = firstError(filenameValidator.validate(candidate));
            if (error == null) {
                newFileName = candidate;
                break;
            }
            status = error;
        }

        // Review
        renderFormReplace("Review replacement", SUBMIT_HINT, ownerID, oldFileName, newFileName);
        if (!waitForLine()) {
            return;
        }

        // Replace and save the scroll
        ScrollList scrolls = ScrollJSONHandler.loadFromJson();
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
