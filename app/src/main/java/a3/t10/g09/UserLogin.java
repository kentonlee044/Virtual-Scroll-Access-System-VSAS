package a3.t10.g09;

import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.mindrot.jbcrypt.BCrypt;
import java.io.FileReader;
import java.io.IOException;

public class UserLogin {

    Scanner scanner = new Scanner(System.in);

    // Retrieve info from JSON file
    String userData = "data/users.json";

    public UserList getUserData() {
        try (JsonReader reader = new JsonReader(new FileReader(userData))) {
            Gson gson = new Gson();
            return gson.fromJson(reader, UserList.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getUserRole(UserList userList, String username) {
        return userList.getUsers().stream()
                .filter(user -> user.getUsername().equals(username))
                .map(User::getRole)
                .findFirst()
                .orElse("guest");
    }

    // Methods that interact with the JSON file
    public boolean isValidUsername(UserList userList, String username) {
        return userList.getUsers().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    public boolean isValidUser(UserList userList, String username, String password) {
        return userList.getUsers().stream()
                .filter(user -> user.getUsername().equals(username))
                .anyMatch(user -> BCrypt.checkpw(password, user.getPassword()));
    }

    // Feedback messages
    public String getSuccessfulLoginMessage() {
        return "User Login Successful! Directing to Dashboard...";
    }

    public String getUnsuccessfulUsername() {
        return "ID key not found. Please check your credentials and try again.";
    }

    public String getUnsuccessfulPassword() {
        return "Incorrect password. Please try again.";
    }

    // User input methods
    public String inputIDKey() {
        System.out.println("Please enter your ID key: ");
        String IDkey = scanner.next();
        return IDkey;
    }

    public String inputPassword() {
        System.out.println("Please enter password: ");
        String password = scanner.next();
        return password;
    }

    public boolean authenticateUser(UserList userList, String username, String password) {
        // Authenticate user against stored credentials

        if (!this.isValidUsername(userList, username)) {
            System.out.println(this.getUnsuccessfulUsername());
            return false;
        }
        if (!this.isValidUser(userList, username, password)) {
            System.out.println(this.getUnsuccessfulPassword());
            return false;
        }

        String role = getUserRole(userList, username);
        System.out.println(getSuccessfulLoginMessage());
        System.out.println("Logged in as: " + username + role);

        // Role-based access control logic
        switch (role.toLowerCase()) {
            case "admin":
                System.out.println("Access granted to admin dashboard.");
                break;
            case "user":
                System.out.println("Access granted to user homepage.");
                break;
            default:
                System.out.println("Guest access. Limited access");
        }

        return true;
    }
}