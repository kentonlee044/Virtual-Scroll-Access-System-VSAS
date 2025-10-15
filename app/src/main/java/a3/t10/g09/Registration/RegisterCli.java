package a3.t10.g09.Registration;

import java.io.Console;
import java.util.Scanner;

public class RegisterCli {
    public static void main(String[] args) {
        var reg = new UserRegistration();
        var sc = new Scanner(System.in);

        System.out.println("=== USER REGISTRATION ===");
        System.out.print("Full name: ");
        String name = sc.nextLine().trim();
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        System.out.print("Phone: ");
        String phone = sc.nextLine().trim();
        System.out.print("ID key: ");
        String idKey = sc.nextLine().trim();

        String password;
        Console console = System.console();
        if (console != null) {
            password = new String(console.readPassword("Password (8+ chars w/ special): "));
        } else {
            System.out.print("Password (8+ chars w/ special): ");
            password = sc.nextLine();
        }

        var result = reg.register(name, email, phone, idKey, password);

        if (result.isSuccess()) {
            System.out.println(result.getMessages().get(0));
        } else {
            System.out.println("Registration failed. Please fix these issues:");
            result.getMessages().forEach(m -> System.out.println("- " + m));
        }
    }
}
