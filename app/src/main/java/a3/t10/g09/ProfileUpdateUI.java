package a3.t10.g09;

import java.util.Scanner;

public class ProfileUpdateUI {
    private final Scanner scanner = new Scanner(System.in);
    private final ProfileUpdateHandler updateHandler;
    private final User user;
    private final UserList userList;

    public ProfileUpdateUI(User user, UserList userList) {
        this.user = user;
        this.userList = userList;
        this.updateHandler = new ProfileUpdateHandler(user, userList);
    }

    // UI helpers adapted from RegisterCli/ScrollUpload for consistent look
    private static final String CLEAR_SCREEN = "\033[H\033[2J";
    private static final int BOX_WIDTH = 70;
    private static final int LABEL_WIDTH = 16;
    private static final int FIELD_WIDTH = 42;
    private static final String INPUT_HINT = "Press Enter to confirm · Ctrl+C to cancel";

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

    private static String fieldLine(String label, String value, boolean mask) {
        String paddedValue = padValue(value, mask);
        String content = String.format("%-" + LABEL_WIDTH + "s [%s]", label, paddedValue);
        return row(content);
    }

    private static void renderProfileForm(String title,
                                          String status,
                                          String hint,
                                          String fullName,
                                          String email,
                                          String idKey,
                                          String phone) {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
        printBorder('┌', '─', '┐');
        System.out.println(centerRow(title));
        printBorder('├', '─', '┤');
        System.out.println(fieldLine("Full name", fullName, false));
        System.out.println(fieldLine("Email", email, false));
        System.out.println(fieldLine("ID key", idKey, false));
        System.out.println(fieldLine("Phone", phone, false));
        printBorder('├', '─', '┤');
        System.out.println(row(status));
        System.out.println(row(hint));
        printBorder('└', '─', '┘');
    }

    private static void renderPasswordForm(String title,
                                           String status,
                                           String hint,
                                           String currentPassword,
                                           String newPassword,
                                           String confirmPassword) {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
        printBorder('┌', '─', '┐');
        System.out.println(centerRow(title));
        printBorder('├', '─', '┤');
        System.out.println(fieldLine("Current password", currentPassword, true));
        System.out.println(fieldLine("New password", newPassword, true));
        System.out.println(fieldLine("Confirm password", confirmPassword, true));
        printBorder('├', '─', '┤');
        System.out.println(row(status));
        System.out.println(row(hint));
        printBorder('└', '─', '┘');
    }

    public void updatePhoneNumber() {
        String status = "Enter new phone number:";
        String candidate = user.getPhone();
        while (true) {
            renderProfileForm("Update Phone Number", status, INPUT_HINT, user.getFullname(), user.getEmail(), user.getIdkey(), candidate);
            System.out.print("> ");
            if (!scanner.hasNextLine()) {
                System.out.println("No input detected. Exiting.");
                return;
            }
            candidate = scanner.nextLine().trim();
            String error = updateHandler.updatePhoneNumber(candidate);
            if (error == null) {
                renderProfileForm("Update Phone Number", "✓ Phone number updated successfully.", "Press Enter to continue", user.getFullname(), user.getEmail(), user.getIdkey(), candidate);
                if (scanner.hasNextLine()) scanner.nextLine();
                return;
            }
            status = error;
        }
    }

    public void updateEmail() {
        String status = "Enter new email:";
        String candidate = user.getEmail();
        while (true) {
            renderProfileForm("Update Email", status, INPUT_HINT, user.getFullname(), candidate, user.getIdkey(), user.getPhone());
            System.out.print("> ");
            if (!scanner.hasNextLine()) {
                System.out.println("No input detected. Exiting.");
                return;
            }
            candidate = scanner.nextLine().trim();
            String error = updateHandler.updateEmail(candidate);
            if (error == null) {
                renderProfileForm("Update Email", "✓ Email updated successfully.", "Press Enter to continue", user.getFullname(), candidate, user.getIdkey(), user.getPhone());
                if (scanner.hasNextLine()) scanner.nextLine();
                return;
            }
            status = error;
        }
    }

    public void updateName() {
        String status = "Enter new name:";
        String candidate = user.getFullname();
        while (true) {
            renderProfileForm("Update Name", status, INPUT_HINT, candidate, user.getEmail(), user.getIdkey(), user.getPhone());
            System.out.print("> ");
            if (!scanner.hasNextLine()) {
                System.out.println("No input detected. Exiting.");
                return;
            }
            candidate = scanner.nextLine().trim();
            String error = updateHandler.updateName(candidate);
            if (error == null) {
                renderProfileForm("Update Name", "✓ Name updated successfully.", "Press Enter to continue", candidate, user.getEmail(), user.getIdkey(), user.getPhone());
                if (scanner.hasNextLine()) scanner.nextLine();
                return;
            }
            status = error;
        }
    }

    public void updateIDKey() {
        String status = "Enter new ID key:";
        String candidate = user.getIdkey();
        while (true) {
            renderProfileForm("Update ID Key", status, INPUT_HINT, user.getFullname(), user.getEmail(), candidate, user.getPhone());
            System.out.print("> ");
            if (!scanner.hasNextLine()) {
                System.out.println("No input detected. Exiting.");
                return;
            }
            candidate = scanner.nextLine().trim();
            String error = updateHandler.updateIDKey(candidate);
            if (error == null) {
                renderProfileForm("Update ID Key", "✓ ID key updated successfully.", "Press Enter to continue", user.getFullname(), user.getEmail(), candidate, user.getPhone());
                if (scanner.hasNextLine()) scanner.nextLine();
                return;
            }
            status = error;
        }
    }

    public void updatePassword() {
        String status = "Enter current password:";
        String current = "";
        String next = "";
        String confirm = "";

        // Current password
        while (true) {
            renderPasswordForm("Update Password", status, INPUT_HINT, current, "", "");
            System.out.print("> ");
            if (!scanner.hasNextLine()) {
                System.out.println("No input detected. Exiting.");
                return;
            }
            current = scanner.nextLine();
            // proceed to next step regardless; handler will validate in final step
            break;
        }

        // New password
        status = "Enter new password:";
        while (true) {
            renderPasswordForm("Update Password", status, INPUT_HINT, current, next, "");
            System.out.print("> ");
            if (!scanner.hasNextLine()) {
                System.out.println("No input detected. Exiting.");
                return;
            }
            next = scanner.nextLine();
            if (next.isEmpty()) {
                status = "Error: New password cannot be empty";
                continue;
            }
            break;
        }

        // Confirm password
        status = "Confirm new password:";
        while (true) {
            renderPasswordForm("Update Password", status, INPUT_HINT, current, next, confirm);
            System.out.print("> ");
            if (!scanner.hasNextLine()) {
                System.out.println("No input detected. Exiting.");
                return;
            }
            confirm = scanner.nextLine();
            if (!next.equals(confirm)) {
                status = "Error: Passwords do not match";
                continue;
            }
            break;
        }

        String error = updateHandler.updatePassword(current, next);
        if (error == null) {
            renderPasswordForm("Update Password", "✓ Password updated successfully.", "Press Enter to continue", current, next, confirm);
            if (scanner.hasNextLine()) scanner.nextLine();
        } else {
            renderPasswordForm("Update Password", error, "Press Enter to retry", current, next, confirm);
            if (scanner.hasNextLine()) scanner.nextLine();
        }
    }
}
