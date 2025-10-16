package a3.t10.g09;

import java.util.Scanner;

import a3.t10.g09.Login.UserLogin;
import a3.t10.g09.Registration.UserRegistration;

public class AdminUserManagement {
    private final Scanner scanner;
    private final UserLogin userLogin;
    private final UserRegistration registration;

    public AdminUserManagement(Scanner scanner) {
        this.scanner = scanner;
        this.userLogin = new UserLogin(scanner);
        this.registration = new UserRegistration();
    }

    public void run(User currentUser) {
        boolean managing = true;
        while (managing) {
            System.out.println("\n┌──────── User Management ────────┐");
            System.out.println("│ 1) View users                    │");
            System.out.println("│ 2) Add user                      │");
            System.out.println("│ 3) Delete user                   │");
            System.out.println("│ 4) Promote/Demote user           │");
            System.out.println("│ 0) Back                          │");
            System.out.println("└──────────────────────────────────┘");
            System.out.print("Select an option: ");
            String choice = readLine();
            if (choice == null)
                return;
            switch (choice) {
                case "1" -> listUsers();
                case "2" -> addUser();
                case "3" -> deleteUser(currentUser);
                case "4" -> toggleRole(currentUser);
                case "0" -> managing = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void listUsers() {
        UserList list = userLogin.getUserData();
        System.out.println("\n┌──────── All Users ──────────────┐");
        if (list.getUsers().isEmpty()) {
            System.out.println("│ No users found.                  │");
        } else {
            for (User u : list.getUsers()) {
                System.out.printf("│ idkey=%s | name=%s | email=%s | phone=%s | role=%s%n",
                        safe(u.getIdkey()), safe(u.getFullname()), safe(u.getEmail()),
                        safe(u.getPhone()), safe(u.getRole()));
            }
        }
        System.out.println("└──────────────────────────────────┘");
    }

    private void addUser() {
        System.out.println("\nAdd new user (leave blank to cancel):");
        String fullname = prompt("Full name: ");
        if (blank(fullname))
            return;
        String email = prompt("Email: ");
        if (blank(email))
            return;
        String idkey = prompt("ID key: ");
        if (blank(idkey))
            return;
        String phone = prompt("Phone: ");
        if (blank(phone))
            return;
        String password = prompt("Password: ");
        if (blank(password))
            return;

        var result = registration.register(fullname, email, phone, idkey, password);
        if (result.isSuccess()) {
            System.out.println("User added successfully.");
        } else {
            System.out.println("Failed to add user:");
            result.getMessages().forEach(m -> System.out.println("- " + m));
        }
    }

    private void deleteUser(User currentUser) {
        UserList list = userLogin.getUserData();
        String idkey = prompt("Enter ID key to delete (blank to cancel): ");
        if (blank(idkey))
            return;

        if (currentUser != null && idkey.equals(currentUser.getIdkey())) {
            System.out.println("You cannot delete yourself.");
            return;
        }

        User target = list.getUsers().stream()
                .filter(u -> idkey.equals(u.getIdkey()))
                .findFirst().orElse(null);

        if (target == null) {
            System.out.println("User not found.");
            return;
        }

        list.getUsers().remove(target);
        registration.saveUsers(list);
        System.out.println("User deleted.");
    }

    private void toggleRole(User currentUser) {
        UserList list = userLogin.getUserData();
        String idkey = prompt("Enter ID key to promote/demote (blank to cancel): ");
        if (blank(idkey))
            return;

        User target = list.getUsers().stream()
                .filter(u -> idkey.equals(u.getIdkey()))
                .findFirst().orElse(null);

        if (target == null) {
            System.out.println("User not found.");
            return;
        }
        if (currentUser != null && idkey.equals(currentUser.getIdkey())) {
            System.out.println("You cannot change your own role here.");
            return;
        }

        String newRole = "admin".equalsIgnoreCase(target.getRole()) ? "user" : "admin";
        target.setRole(newRole);
        registration.saveUsers(list);
        System.out.println("Role updated: " + idkey + " -> " + newRole);
    }

    private String prompt(String label) {
        System.out.print(label);
        return readLine();
    }

    private String readLine() {
        if (!scanner.hasNextLine()) {
            System.out.println("No input detected. Exiting.");
            return null;
        }
        return scanner.nextLine().trim();
    }

    private boolean blank(String s) {
        return s == null || s.isBlank();
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}