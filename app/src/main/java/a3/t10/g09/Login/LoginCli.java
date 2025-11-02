package a3.t10.g09.Login;

import java.io.Console;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;

import a3.t10.g09.User;

import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class LoginCli {
    private static final String CLEAR_SCREEN = "\033[H\033[2J";
    private static final int BOX_WIDTH = 70;
    private static final int LABEL_WIDTH = 16;
    private static final int FIELD_WIDTH = 42;
    private static final String INPUT_HINT = "Press Enter to confirm · Ctrl+C to cancel";

    private final UserLogin login;
    private final Scanner scanner;

    private final Terminal terminal;
    private final LineReader lineReader;

    public LoginCli(Scanner scanner) {
        this(scanner, new UserLogin());
    }

    public LoginCli() {
        this(new Scanner(System.in));
    }

    LoginCli(Scanner scanner, UserLogin login) {
        this.scanner = scanner;
        this.login = login;

        Terminal builtTerminal = null;
        LineReader builtReader = null;
        // Try normal system terminal first
        try {
            builtTerminal = TerminalBuilder.builder()
                    .system(true) // system:true
                    .jna(true) // jna:true
                    .build();
            builtReader = LineReaderBuilder.builder()
                    .terminal(builtTerminal)
                    .build();
        } catch (Exception ignored) {
            builtTerminal = null;
            builtReader = null;
        }
        // If unavailable (e.g., piped I/O), try binding to /dev/tty (macOS/Linux)
        if (builtReader == null) {
            try (FileInputStream ttyIn = new FileInputStream("/dev/tty");
                    FileOutputStream ttyOut = new FileOutputStream("/dev/tty")) {
                builtTerminal = TerminalBuilder.builder()
                        .system(false)
                        .streams(ttyIn, ttyOut)
                        .jna(true)
                        .build();
                builtReader = LineReaderBuilder.builder()
                        .terminal(builtTerminal)
                        .build();
            } catch (Exception ignored) {
                builtTerminal = null;
                builtReader = null;
            }
        }
        this.terminal = builtTerminal;
        this.lineReader = builtReader;
    }

    public User run() {
        Console console = System.console();
        while (true) {
            String idKey = promptIdKey();
            if (idKey == null)
                return null;

            String password = promptPassword(console, idKey);
            if (password == null)
                return null;

            UserLogin.AuthenticationResult result = login.authenticate(idKey, password);
            if (!result.isSuccess()) {
                renderForm(/* status: */ result.getMessage(), /* hint: */ "Press Enter to try again",
                        /* idKey: */ idKey, /* password: */ "");
                waitForEnter();
                continue;
            }

            renderForm(/* status: */ result.getMessage(), /* hint: */ "Press Enter to continue", /* idKey: */ idKey,
                    /* password: */ password);
            waitForEnter();
            return result.getUser();
        }
    }

    private String promptIdKey() {
        while (true) {
            renderForm(/* status: */ "Enter ID key:", /* hint: */ INPUT_HINT, /* idKey: */ "", /* password: */ "");
            System.out.print("> ");
            if (!scanner.hasNextLine()) {
                System.out.println("No input detected. Exiting login.");
                return null;
            }
            String idKey = scanner.nextLine().trim();
            if (!idKey.isBlank())
                return idKey;

            renderForm(/* status: */ "ID key cannot be empty.", /* hint: */ "Press Enter to try again", /* idKey: */ "",
                    /* password: */ "");
            waitForEnter();
        }
    }

    private String promptPassword(Console console, String idKey) {
        while (true) {
            renderForm(/* status: */ "Enter password:", /* hint: */ INPUT_HINT, /* idKey: */ idKey, /* password: */ "");
            String password;
            if (lineReader != null) {
                try {
                    password = lineReader.readLine(/* prompt: */ "> ", /* mask: */ '*');
                } catch (UserInterruptException | EndOfFileException e) {
                    System.out.println("No input detected. Exiting login.");
                    return null;
                }
            } else if (console != null) {
                char[] chars = console.readPassword(/* fmt: */ "> ");
                if (chars == null) {
                    System.out.println("No input detected. Exiting login.");
                    return null;
                }
                password = new String(chars);
            } else {
                System.out.print("> ");
                if (!scanner.hasNextLine()) {
                    System.out.println("No input detected. Exiting login.");
                    return null;
                }
                password = scanner.nextLine();
            }

            if (!password.isEmpty())
                return password;

            renderForm(/* status: */ "Password cannot be empty.", /* hint: */ "Press Enter to try again",
                    /* idKey: */ idKey, /* password: */ "");
            waitForEnter();
        }
    }

    private void waitForEnter() {
        System.out.print("> ");
        if (scanner.hasNextLine())
            scanner.nextLine();
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
        if (content.length() > BOX_WIDTH)
            content = content.substring(0, BOX_WIDTH - 1) + "…";
        int pad = Math.max(0, BOX_WIDTH - content.length());
        int left = pad / 2, right = pad - left;
        return "│" + " ".repeat(left) + content + " ".repeat(right) + "│";
    }

    private String row(String text) {
        String content = text == null ? "" : text;
        if (content.length() > BOX_WIDTH)
            content = content.substring(0, BOX_WIDTH - 1) + "…";
        return "│" + String.format("%-" + BOX_WIDTH + "s", content) + "│";
    }

    private String fieldLine(String label, String value, boolean mask) {
        String paddedValue = padValue(value, mask);
        String content = String.format("%-" + LABEL_WIDTH + "s [%s]", label, paddedValue);
        return row(content);
    }

    private String padValue(String value, boolean mask) {
        String content = value == null ? "" : value;
        if (mask)
            content = "*".repeat(Math.min(content.length(), FIELD_WIDTH));
        if (content.length() > FIELD_WIDTH)
            content = content.substring(0, FIELD_WIDTH - 1) + "…";
        return String.format("%-" + FIELD_WIDTH + "s", content);
    }
}