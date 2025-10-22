package a3.t10.g09;

import a3.t10.g09.Validator.ScrollCategorizationIdValidator;
import a3.t10.g09.Validator.ScrollFilenameValidator;
import a3.t10.g09.Validator.Validator;

import java.util.Scanner;

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
                filename = candidate;
                break;
            }
            status = error;
        }

        // Prompt categorization ID (optional)
        status = "Enter categorization ID (optional):";
        candidate = categorizationId;
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
            String error = categorizationIdValidator.validate(candidate);
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
}
