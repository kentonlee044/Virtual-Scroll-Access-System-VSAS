package a3.t10.g09;

import java.util.Scanner;

import a3.t10.g09.Login.UserLogin;

//
public class CommandMenu {
    private final Scanner scanner;
    private final UserLogin userLogin;

    public CommandMenu(Scanner scanner) {
        this(scanner, new UserLogin(scanner));
    }

    CommandMenu(Scanner scanner, UserLogin userLogin) {
        this.scanner = scanner;
        this.userLogin = userLogin;
    }

    public void runGuest() {
        boolean running = true;
        while (running) {
            printGuestMenu();
            String choice = readChoice();
            if (choice == null) {
                return;
            }
            switch (choice) {
                case "1" -> System.out.println("Guests cannot update profiles.");
                case "2" -> running = false;
                default -> System.out.println("Please choose 1 or 2.");
            }
        }
    }

    public void runFor(User user) {
        if (user == null) {
            runGuest();
            return;
        }
        boolean isAdmin = "admin".equalsIgnoreCase(user.getRole());
        boolean running = true;
        while (running) {
            if (isAdmin) {
                printAdminMenu(user);
            } else {
                printUserMenu(user);
            }
            String choice = readChoice();
            if (choice == null)
                return;
            switch (choice) {
                case "1" -> launchProfileUpdater(user);
                case "2" -> running = false;
                case "3" -> {
                    if (isAdmin) {
                        showUserManagement();
                    } else {
                        System.out.println("Invalid option.");
                    }
                }
                case "4" -> {
                    if (isAdmin) {
                        showScrollStats();
                    } else {
                        System.out.println("Invalid option.");
                    }
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void launchProfileUpdater(User user) {
        UserList userList = userLogin.getUserData();
        ProfileUpdateController controller = new ProfileUpdateController(user, userList);
        controller.displayMenu();
    }

    private void showUserManagement() {
        boolean managing = true;
        while (managing) {
            System.out.println("\n┌──────── User Management ────────┐");
            System.out.println("│ 1) View users                    │");
            System.out.println("│ 2) Add/Delete users (stub)       │");
            System.out.println("│ 3) Promote/Demote users (stub)   │");
            System.out.println("│ 0) Back                          │");
            System.out.println("└──────────────────────────────────┘");
            System.out.print("Select an option: ");
            String choice = readChoice();
            if (choice == null)
                return;
            switch (choice) {
                case "1" -> System.out.println("TODO: list users.");
                case "2" -> System.out.println("TODO: add/delete users.");
                case "3" -> System.out.println("TODO: promote/demote users.");
                case "0" -> managing = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void showScrollStats() {
        System.out.println("TODO: display scroll statistics.");
    }

    private void printGuestMenu() {
        System.out.println("\n┌──────── Guest Commands ─────────┐");
        System.out.println("│ 1) Update user profile           │");
        System.out.println("│ 2) Logout                        │");
        System.out.println("└──────────────────────────────────┘");
        System.out.print("Select an option: ");
    }

    private void printUserMenu(User user) {
        System.out.println("\n┌──────── User Commands ──────────┐");
        System.out.println("│ Logged in as: " + user.getFullname());
        System.out.println("│ 1) Update user profile           │");
        System.out.println("│ 2) Logout                        │");
        System.out.println("└──────────────────────────────────┘");
        System.out.print("Select an option: ");
    }

    private void printAdminMenu(User user) {
        System.out.println("\n┌──────── Admin Commands ─────────┐");
        System.out.println("│ Logged in as: " + user.getFullname());
        System.out.println("│ 1) Update user profile           │");
        System.out.println("│ 2) Logout                        │");
        System.out.println("│ 3) User management               │");
        System.out.println("│ 4) Scroll statistics             │");
        System.out.println("└──────────────────────────────────┘");
        System.out.print("Select an option: ");
    }

    private String readChoice() {
        if (!scanner.hasNextLine()) {
            System.out.println("No input detected. Exiting.");
            return null;
        }
        return scanner.nextLine().trim();
    }
}