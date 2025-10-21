package a3.t10.g09.Registration;

import java.io.Console;
import java.util.Scanner;

import a3.t10.g09.UserList;
import a3.t10.g09.Validator.AtSymbolValidator;
import a3.t10.g09.Validator.DomainDotValidator;
import a3.t10.g09.Validator.EmailUniqueValidator;
import a3.t10.g09.Validator.IDKeyFormatValidator;
import a3.t10.g09.Validator.IDKeyUniqueValidator;
import a3.t10.g09.Validator.NameValidator;
import a3.t10.g09.Validator.PasswordLengthValidator;
import a3.t10.g09.Validator.PasswordSpecialCharValidator;
import a3.t10.g09.Validator.PhoneDigitValidator;
import a3.t10.g09.Validator.PhoneLengthValidator;

public class RegisterCli {

    private static final String CLEAR_SCREEN = "\033[H\033[2J";
    private static final int BOX_WIDTH = 70;
    private static final int LABEL_WIDTH = 16;
    private static final int FIELD_WIDTH = 42;
    private static final String INPUT_HINT = "Press Enter to confirm · Ctrl+C to cancel";
    private static final String SUBMIT_HINT = "Press Enter to submit · Ctrl+C to cancel";

    private final UserRegistration registration;
    private final Scanner scanner;
    private final Console console;

    public RegisterCli(Scanner scanner) {
        this(scanner, System.console());
    }

    RegisterCli(Scanner scanner, Console console) {
        this.registration = new UserRegistration();
        this.scanner = scanner;
        this.console = console;
    }

    public void run() {
        String fullName = "";
        String email = "";
        String idKey = "";
        String phone = "";
        String password = "";

        fullName = promptName(fullName, email, idKey, phone, password);
        if (fullName == null) {
            return;
        }

        email = promptEmail(fullName, email, idKey, phone, password);
        if (email == null) {
            return;
        }

        idKey = promptIdKey(fullName, email, idKey, phone, password);
        if (idKey == null) {
            return;
        }

        phone = promptPhone(fullName, email, idKey, phone, password);
        if (phone == null) {
            return;
        }

        password = promptPassword(fullName, email, idKey, phone, password);
        if (password == null) {
            return;
        }

        renderForm("Review details", SUBMIT_HINT, fullName, email, idKey, phone, password);
        if (!waitForLine()) {
            return;
        }

        var result = registration.register(fullName, email, phone, idKey, password);

        if (result.isSuccess()) {
            renderForm(result.getMessages().get(0), "Press Enter to continue", fullName, email, idKey, phone, password);
            waitForLine();
        } else {
            renderForm("Registration failed", "See issues below", fullName, email, idKey, phone, password);
            result.getMessages().forEach(msg -> System.out.println("- " + msg));
            System.out.println("\nPress Enter to return to the menu...");
            waitForLine();
        }
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            new RegisterCli(scanner).run();
        }
    }

    private String promptName(String fullName,
            String email,
            String idKey,
            String phone,
            String password) {
        String status = "Enter full name:";
        String candidate = fullName;
        var validator = new NameValidator();
        while (true) {
            renderForm(status, INPUT_HINT, candidate, email, idKey, phone, password);
            System.out.print("> ");
            if (!scanner.hasNextLine()) {
                System.out.println("No input detected. Exiting registration.");
                return null;
            }
            candidate = scanner.nextLine().trim();
            String error = validator.validate(candidate);
            if (error == null) {
                return candidate;
            }
            status = error;
        }
    }

    private String promptEmail(String fullName,
            String email,
            String idKey,
            String phone,
            String password) {
        String status = "Enter email address:";
        String candidate = email;
        while (true) {
            renderForm(status, INPUT_HINT, fullName, candidate, idKey, phone, password);
            System.out.print("> ");
            if (!scanner.hasNextLine()) {
                System.out.println("No input detected. Exiting registration.");
                return null;
            }
            candidate = scanner.nextLine().trim();
            UserList users = registration.currentUsers();
            String error = firstError(
                    new AtSymbolValidator().validate(candidate),
                    new DomainDotValidator().validate(candidate),
                    new EmailUniqueValidator(users).validate(candidate));
            if (error == null) {
                return candidate;
            }
            status = error;
        }
    }

    private String promptIdKey(String fullName,
            String email,
            String idKey,
            String phone,
            String password) {
        String status = "Create a unique ID key:";
        String candidate = idKey;
        while (true) {
            renderForm(status, INPUT_HINT, fullName, email, candidate, phone, password);
            System.out.print("> ");
            if (!scanner.hasNextLine()) {
                System.out.println("No input detected. Exiting registration.");
                return null;
            }
            candidate = scanner.nextLine().trim();
            UserList users = registration.currentUsers();
            String error = firstError(
                    new IDKeyFormatValidator().validate(candidate),
                    new IDKeyUniqueValidator(users).validate(candidate));
            if (error == null) {
                return candidate;
            }
            status = error;
        }
    }

    private String promptPhone(String fullName,
            String email,
            String idKey,
            String phone,
            String password) {
        String status = "Enter phone number:";
        String candidate = phone;
        while (true) {
            renderForm(status, INPUT_HINT, fullName, email, idKey, candidate, password);
            System.out.print("> ");
            if (!scanner.hasNextLine()) {
                System.out.println("No input detected. Exiting registration.");
                return null;
            }
            candidate = scanner.nextLine().trim();
            String error = firstError(
                    new PhoneLengthValidator().validate(candidate),
                    new PhoneDigitValidator().validate(candidate));
            if (error == null) {
                return candidate;
            }
            status = error;
        }
    }

    private String promptPassword(String fullName,
            String email,
            String idKey,
            String phone,
            String password) {
        String status = "Create a password (9+ chars, 1 special):";
        String candidate = password;
        while (true) {
            renderForm(status, INPUT_HINT, fullName, email, idKey, phone, candidate);
            if (console != null) {
                char[] chars = console.readPassword("> ");
                if (chars == null) {
                    System.out.println("No input detected. Exiting registration.");
                    return null;
                }
                candidate = new String(chars);
            } else {
                System.out.print("> ");
                if (!scanner.hasNextLine()) {
                    System.out.println("No input detected. Exiting registration.");
                    return null;
                }
                candidate = scanner.nextLine();
            }
            String error = firstError(
                    new PasswordLengthValidator().validate(candidate),
                    new PasswordSpecialCharValidator().validate(candidate));
            if (error == null) {
                return candidate;
            }
            status = error;
        }
    }

    private boolean waitForLine() {
        if (!scanner.hasNextLine()) {
            System.out.println("No input detected. Exiting registration.");
            return false;
        }
        scanner.nextLine();
        return true;
    }

    private static String firstError(String... messages) {
        for (String message : messages) {
            if (message != null && !message.isBlank()) {
                return message;
            }
        }
        return null;
    }

    private static void renderForm(String status,
            String hint,
            String fullName,
            String email,
            String idKey,
            String phone,
            String password) {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
        printBorder('┌', '─', '┐');
        System.out.println(centerRow("New User"));
        printBorder('├', '─', '┤');
        System.out.println(fieldLine("Full name", fullName, false));
        System.out.println(fieldLine("Email", email, false));
        System.out.println(fieldLine("ID key", idKey, false));
        System.out.println(fieldLine("Phone", phone, false));
        System.out.println(fieldLine("Password", password, true));
        printBorder('├', '─', '┤');
        System.out.println(row(status));
        System.out.println(row(hint));
        printBorder('└', '─', '┘');
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

    private static String fieldLine(String label, String value, boolean mask) {
        String paddedValue = padValue(value, mask);
        String content = String.format("%-" + LABEL_WIDTH + "s [%s]", label, paddedValue);
        return row(content);
    }

    private static String padValue(String value, boolean mask) {
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