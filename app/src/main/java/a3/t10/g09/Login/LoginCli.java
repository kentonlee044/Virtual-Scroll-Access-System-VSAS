package a3.t10.g09.Login;

import java.io.Console;
import java.util.Scanner;

import a3.t10.g09.User;

public class LoginCli {
    private static final String CLEAR_SCREEN = "\033[H\033[2J";
    private static final int BOX_WIDTH = 70;
    private static final int LABEL_WIDTH = 16;
    private static final int FIELD_WIDTH = 42;
    private static final String INPUT_HINT = "Press Enter to confirm · Ctrl+C to cancel";

    private final UserLogin login = new UserLogin();

    public User run() {
        Console console = System.console();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String idKey = promptIdKey(scanner);
                if (idKey == null) {
                    return null;
                }

                String password = promptPassword(scanner, console, idKey);
                if (password == null) {
                    return null;
                }

                UserLogin.AuthenticationResult result = login.authenticate(idKey, password);

                if (!result.isSuccess()) {
                    renderForm(result.getMessage(), "Press Enter to try again", idKey, "");
                    waitForEnter(scanner);
                    continue;
                }

                renderForm(result.getMessage(), "Press Enter to continue", idKey, password);
                waitForEnter(scanner);
                return result.getUser();
            }
        }
    }

    private String promptIdKey(Scanner scanner) {
        while (true) {
            renderForm("Enter ID key:", INPUT_HINT, "", "");
            System.out.print("> ");
            if (!scanner.hasNextLine()) {
                System.out.println("No input detected. Exiting login.");
                return null;
            }
            String idKey = scanner.nextLine().trim();
            if (!idKey.isBlank()) {
                return idKey;
            }
            renderForm("ID key cannot be empty.", "Press Enter to try again", "", "");
            waitForEnter(scanner);
        }
    }

    private String promptPassword(Scanner scanner, Console console, String idKey) {
        while (true) {
            renderForm("Enter password:", INPUT_HINT, idKey, "");
            String password;
            if (console != null) {
                char[] chars = console.readPassword("> ");
                password = (chars == null) ? "" : new String(chars);
            } else {
                System.out.print("> ");
                if (!scanner.hasNextLine()) {
                    System.out.println("No input detected. Exiting login.");
                    return null;
                }
                password = scanner.nextLine();
            }
            if (!password.isEmpty()) {
                return password;
            }
            renderForm("Password cannot be empty.", "Press Enter to try again", idKey, "");
            waitForEnter(scanner);
        }
    }

    private void waitForEnter(Scanner scanner) {
        System.out.print("> ");
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
    }

    private void renderForm(String status, String hint, String idKey, String password) {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
        printBorder('┌', '─', '┐');
        System.out.println(centerRow("Login"));
        printBorder('├', '─', '┤');
        System.out.println(fieldLine("ID key", idKey, false));
        System.out.println(fieldLine("Password", password, true));
        printBorder('├', '─', '┤');
        System.out.println(row(status));
        System.out.println(row(hint));
        printBorder('└', '─', '┘');
    }

    private void printBorder(char left, char fill, char right) {
        System.out.println(left + String.valueOf(fill).repeat(BOX_WIDTH) + right);
    }

    private String centerRow(String text) {
        String content = text == null ? "" : text;
        if (content.length() > BOX_WIDTH) {
            content = content.substring(0, BOX_WIDTH - 1) + "…";
        }
        int padding = Math.max(0, BOX_WIDTH - content.length());
        int leftPad = padding / 2;
        int rightPad = padding - leftPad;
        return "│" + " ".repeat(leftPad) + content + " ".repeat(rightPad) + "│";
    }

    private String row(String text) {
        String content = text == null ? "" : text;
        if (content.length() > BOX_WIDTH) {
            content = content.substring(0, BOX_WIDTH - 1) + "…";
        }
        return "│" + String.format("%-" + BOX_WIDTH + "s", content) + "│";
    }

    private String fieldLine(String label, String value, boolean mask) {
        String paddedValue = padValue(value, mask);
        String content = String.format("%-" + LABEL_WIDTH + "s [%s]", label, paddedValue);
        return row(content);
    }

    private String padValue(String value, boolean mask) {
        String content = value == null ? "" : value;
        if (mask) {
            content = "*".repeat(Math.min(content.length(), FIELD_WIDTH));
        }
        if (content.length() > FIELD_WIDTH) {
            content = content.substring(0, FIELD_WIDTH - 1) + "…";
        }
        return String.format("%-" + FIELD_WIDTH + "s", content);
    }
}