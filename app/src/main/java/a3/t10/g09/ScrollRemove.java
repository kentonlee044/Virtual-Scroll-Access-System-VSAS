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
        DisplayUIContent.printBorder('┌', '─', '┐');
        System.out.println(DisplayUIContent.centerRow("Remove Scroll Binary"));
        DisplayUIContent.printBorder('├', '─', '┤');
        for (int i = 0; i < scrolls.size(); i++) {
            String entry = String.format("%d) %-45s ID: %s", i + 1, scrolls.get(i).getFilename(),
                    safe(scrolls.get(i).getCategorizationId()));
            System.out.println(DisplayUIContent.row(entry));
        }
        DisplayUIContent.printBorder('├', '─', '┤');
        System.out.println(DisplayUIContent.row(status));
        System.out.println(DisplayUIContent.row(hint));
        DisplayUIContent.printBorder('└', '─', '┘');
    }

    private void renderMessage(String status, String hint) {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
        DisplayUIContent.printBorder('┌', '─', '┐');
        System.out.println(DisplayUIContent.centerRow("Replace Scroll Binary"));
        DisplayUIContent.printBorder('├', '─', '┤');
        System.out.println(DisplayUIContent.row(status));
        System.out.println(DisplayUIContent.row(hint));
        DisplayUIContent.printBorder('└', '─', '┘');
    }

    private void renderConfirmation(String status, String filename, String storedPath) {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
        DisplayUIContent.printBorder('┌', '─', '┐');
        System.out.println(DisplayUIContent.centerRow("Removal Complete"));
        DisplayUIContent.printBorder('├', '─', '┤');
        System.out.println(DisplayUIContent.fieldLine("Scroll", filename));
        System.out.println(DisplayUIContent.fieldLine("Stored at", storedPath));
        DisplayUIContent.printBorder('├', '─', '┤');
        System.out.println(DisplayUIContent.row(status));
        System.out.println(DisplayUIContent.row("Press Enter to continue"));
        DisplayUIContent.printBorder('└', '─', '┘');
    }

    private boolean waitForEnter() {
        if (!scanner.hasNextLine()) {
            return false;
        }
        scanner.nextLine();
        return true;
    }

    private static String safe(String value) {
        return value == null || value.isBlank() ? "—" : value;
    }

}
