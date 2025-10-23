package a3.t10.g09;

public final class DisplayUITable {
    static final int minimumBoxWidth = 53;

    private DisplayUITable(){}


    public static void printMenu(Command[] commands, MenuTitle title, Client client) {
        int longest = findLongestLine(commands, title);

        if (longest < minimumBoxWidth) {
            longest = minimumBoxWidth;
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
}
