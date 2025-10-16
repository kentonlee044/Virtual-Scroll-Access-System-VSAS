package a3.t10.g09;

import java.util.Scanner;
import a3.t10.g09.Login.LoginCli;
import a3.t10.g09.Registration.RegisterCli;

public class MainMenu {
    private final Scanner scanner;

    public MainMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public MainMenu() {
        this(new Scanner(System.in));
    }

    public void run() {
        while (true) {
            printMenu();
            if (!scanner.hasNextLine()) {
                System.out.println("No input detected. Exiting.");
                return;
            }
            String line = scanner.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number between 0 and 3.\n");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    User loggedIn = new LoginCli(scanner).run();
                    if (loggedIn != null) {
                        new CommandMenu(scanner).runFor(loggedIn);
                    }
                }
                case 2 -> new RegisterCli(scanner).run();
                case 3 -> new CommandMenu(scanner).runGuest();
                case 0 -> {
                    System.out.println("Goodbye.");
                    return;
                }
                default -> System.out.println("Please choose 0, 1, 2, or 3.\n");
            }
        }
    }

    private void printMenu() {
        System.out.println("┌────────────────────────────────────────────────────────┐");
        System.out.println("│   🔑  Virtual Scroll Access System                     │");
        System.out.println("├────────────────────────────────────────────────────────┤");
        System.out.println("│  1) Log in                                             │");
        System.out.println("│  2) Register                                           │");
        System.out.println("│  3) Continue as Guest                                  │");
        System.out.println("│  0) Exit                                               │");
        System.out.println("└────────────────────────────────────────────────────────┘");
        System.out.print("Select an option (0-3): ");
    }
}
