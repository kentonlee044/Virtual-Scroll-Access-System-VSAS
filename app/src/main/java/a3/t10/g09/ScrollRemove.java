package a3.t10.g09;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Scanner;

public class ScrollRemove {
    private static final String CLEAR_SCREEN = "\033[H\033[2J";
    private static final int BOX_WIDTH = 70;
    private static final int LABEL_WIDTH = 16;
    private static final int FIELD_WIDTH = 42;
    private static final String INPUT_HINT = "Press Enter to confirm · Ctrl+C to cancel";
    private static final String SUBMIT_HINT = "Press Enter to submit · Ctrl+C to cancel";
    private static final Path STORAGE_DIR = Path.of("src/main/java/a3/t10/g09/data/scroll_files");

    private final Scanner scanner;
    private final User currentUser;

    public ScrollRemove(Scanner scanner, User currentUser) {
        this.scanner = scanner;
        this.currentUser = currentUser;
    }

    public void run() {
        if (currentUser == null) {
            return;
        }

        ScrollList scrollList = ScrollJSONHandler.loadFromJson();
        List<Scroll> ownedScrolls;
        if (currentUser.getRole().equals("admin")) {
            ownedScrolls = scrollList.getAllScrolls();
        } else {
            ownedScrolls = scrollList.getScrollsByOwner(currentUser.getIdkey());
        }
        if (ownedScrolls.isEmpty()) {
            renderMessage("You have no scrolls to remove.", "Press Enter to return");
            waitForEnter();
            return;
        }

        int index = promptScrollSelection(ownedScrolls);
        if (index < 0) {
            return;
        }

        Scroll selected = ownedScrolls.get(index);
        if (selected == null) {
            return;
        }
        Path target = STORAGE_DIR.resolve(selected.getFilename());
        scrollList.removeScroll(selected.getFilename());
        selected.incrementUploads();
//
        if (ScrollJSONHandler.saveToJson(scrollList)) {
            renderConfirmation("✓ Scroll binary removed successfully!", selected.getFilename(),
                    target.toAbsolutePath().toString());
        } else {
            renderConfirmation("✗ Binary removed, but metadata failed to save.", selected.getFilename(),
                    target.toAbsolutePath().toString());
        }
        waitForEnter();
    }

    private int promptScrollSelection(List<Scroll> ownedScrolls) {
        String status = "Select a scroll number to remove.";
        while (true) {
            renderScrollMenu(status, SUBMIT_HINT, ownedScrolls);
            System.out.print("> ");
            if (!scanner.hasNextLine()) {
                System.out.println("No input detected. Exiting.");
                return -1;
            }
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return -1;
            }
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= ownedScrolls.size()) {
                    return choice - 1;
                }
            } catch (NumberFormatException ignored) {
            }
            status = "Invalid selection. Enter a number between 1 and " + ownedScrolls.size() + ".";
        }
    }

    private void ensureStorageDir() throws IOException {
        if (!Files.exists(STORAGE_DIR)) {
            Files.createDirectories(STORAGE_DIR);
        }
    }

    private void renderScrollMenu(String status, String hint, List<Scroll> scrolls) {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
        printBorder('┌', '─', '┐');
        System.out.println(centerRow("Remove Scroll Binary"));
        printBorder('├', '─', '┤');
        for (int i = 0; i < scrolls.size(); i++) {
            String entry = String.format("%d) %-45s ID: %s", i + 1, scrolls.get(i).getFilename(),
                    safe(scrolls.get(i).getCategorizationId()));
            System.out.println(row(entry));
        }
        printBorder('├', '─', '┤');
        System.out.println(row(status));
        System.out.println(row(hint));
        printBorder('└', '─', '┘');
    }

    private void renderMessage(String status, String hint) {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
        printBorder('┌', '─', '┐');
        System.out.println(centerRow("Replace Scroll Binary"));
        printBorder('├', '─', '┤');
        System.out.println(row(status));
        System.out.println(row(hint));
        printBorder('└', '─', '┘');
    }

    private void renderConfirmation(String status, String filename, String storedPath) {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
        printBorder('┌', '─', '┐');
        System.out.println(centerRow("Removal Complete"));
        printBorder('├', '─', '┤');
        System.out.println(fieldLine("Scroll", filename));
        System.out.println(fieldLine("Stored at", storedPath));
        printBorder('├', '─', '┤');
        System.out.println(row(status));
        System.out.println(row("Press Enter to continue"));
        printBorder('└', '─', '┘');
    }

    private boolean waitForEnter() {
        if (!scanner.hasNextLine()) {
            return false;
        }
        scanner.nextLine();
        return true;
    }

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

    private static String fieldLine(String label, String value) {
        String content = String.format("%-" + LABEL_WIDTH + "s [%s]", label, padValue(value));
        return row(content);
    }

    private static String padValue(String value) {
        String content = value == null ? "" : value;
        if (content.length() > FIELD_WIDTH) {
            content = content.substring(0, FIELD_WIDTH - 1) + "…";
        }
        return String.format("%-" + FIELD_WIDTH + "s", content);
    }

    private static String safe(String value) {
        return value == null || value.isBlank() ? "—" : value;
    }

}
