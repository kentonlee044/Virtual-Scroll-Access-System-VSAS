package a3.t10.g09;

public final class DisplayUIContent {
    static final int MIN_MAIN_MENU_SCROLL_OPERATION_BOX_WIDTH = 53;
    private static final int SCROLL_OPERATION_BOX_WIDTH = 70;
    private static final int LABEL_WIDTH = 16;
    private static final int FIELD_WIDTH = 42;
    private static final String INPUT_HINT = "Press Enter to confirm · Ctrl+C to cancel";
    private static final String SUBMIT_HINT = "Press Enter to submit · Ctrl+C to cancel";


    private DisplayUIContent(){}


    public static void printMainMenu(Command[] commands, MenuTitle title, Client client) {
        int longest = findLongestLine(commands, title);

        if (longest < MIN_MAIN_MENU_SCROLL_OPERATION_BOX_WIDTH) {
            longest = MIN_MAIN_MENU_SCROLL_OPERATION_BOX_WIDTH;
        }
        // print the title box
        System.out.println("┌──" + "─".repeat(longest) + "┐");
        System.out.println("│  " + " " + title.getTitle() +
                " ".repeat(longest - title.getTitle().length() - 1) + "|");
        System.out.println("├──" + "─".repeat(longest + 1) + "┤");
        // print the user and their status
        switch (client.getStatus()) {
            case ClientStatus.ANONYMOUS:
                // print "You are logged in as a guest"
                break;
            case ClientStatus.GENERIC_USER:
                // print "You are logged in as the generic user '<user id>'
            case ClientStatus.ADMIN:
                // print "You are logged in as the administrator '<user id>."
        }

        // print each row of available commands
        for (int i = 0; i < commands.length; i++) {
            System.out.println("│  " + (i+1) + ") " + commands[i].getDescription() +
                    " ".repeat(longest - commands[i].getDescription().length() - 3) + "│");
        }

        // print bottom edge of the box
        System.out.println("└──" + "─".repeat(longest) + "┘");
        System.out.print("Select an option (1-" + commands.length + "): ");
    }

    private static int findLongestLine(Command[] commands, MenuTitle title) {
        int longest = title.getTitle().length() + 1; // there is one whitespace before the title
        for (int i = 0; i < commands.length; i++) {
            int curr = commands[i].getDescription().length() + 3; // there is "i) " before each command
            if (curr > longest) {
                longest = curr;
            }
        }
        return longest;
    }

    public static void printBorder(char left, char fill, char right) {
        System.out.println(left + String.valueOf(fill).repeat(SCROLL_OPERATION_BOX_WIDTH) + right);
    }

    public static String centerRow(String text) {
        String content = text == null ? "" : text;
        if (content.length() > SCROLL_OPERATION_BOX_WIDTH) {
            content = content.substring(0, SCROLL_OPERATION_BOX_WIDTH - 1) + "…";
        }
        int padding = Math.max(0, SCROLL_OPERATION_BOX_WIDTH - content.length());
        int leftPad = padding / 2;
        int rightPad = padding - leftPad;
        return "│" + " ".repeat(leftPad) + content + " ".repeat(rightPad) + "│";
    }

    public static String row(String text) {
        String content = text == null ? "" : text;
        if (content.length() > SCROLL_OPERATION_BOX_WIDTH) {
            content = content.substring(0, SCROLL_OPERATION_BOX_WIDTH - 1) + "…";
        }
        return "│" + String.format("%-" + SCROLL_OPERATION_BOX_WIDTH + "s", content) + "│";
    }

    public static String fieldLine(String label, String value) {
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
}
