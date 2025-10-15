package a3.t10.g09;

import java.util.Scanner;
import a3.t10.g09.Registration.RegisterCli;

public class MainMenu {

    public void run() {
        try (Scanner in = new Scanner(System.in)) {
            while (true) {
                printMenu();
                if (!in.hasNextLine()) {
                    System.out.println("No input detected. Exiting.");
                    return;
                }
                String line = in.nextLine().trim();
                int choice;
                try {
                    choice = Integer.parseInt(line);
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a number between 0 and 3.\n");
                    continue;
                }

                switch (choice) {
                    case 1: { // Log in
                        UserLogin login = new UserLogin();
                        UserList list = login.getUserData();
                        if (list == null) {
                            System.out.println("No user data found.\n");
                            break;
                        }
                        String id = login.inputIDKey();
                        String pw = login.inputPassword();
                        login.authenticateUser(list, id, pw);
                        break;
                    }
                    case 2: { // Register
                        RegisterCli.main(new String[0]);
                        break;
                    }
                    case 3: { // Guest
                        System.out.println("Continuing as Guest...\n");
                        // TODO: launch guest flow if needed
                        break;
                    }
                    case 0:
                        System.out.println("Goodbye.");
                        return;
                    default:
                        System.out.println("Please choose 0, 1, 2, or 3.\n");
                }
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