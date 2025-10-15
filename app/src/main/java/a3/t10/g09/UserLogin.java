package a3.t10.g09;

import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.mindrot.jbcrypt.BCrypt;

import java.io.FileReader;
import java.io.IOException;

public class UserLogin {

    private final Scanner scanner;
    private final String userDataPath;

    public UserLogin() {
        this("data/users.json", new Scanner(System.in));
    }

    UserLogin(String userDataPath) {
        this(userDataPath, new Scanner(System.in));
    }

    UserLogin(String userDataPath, Scanner scanner) {
        this.userDataPath = userDataPath;
        this.scanner = scanner;
    }

    public UserList getUserData() {
        try (JsonReader reader = new JsonReader(new FileReader(userDataPath))) {
            Gson gson = new Gson();
            UserList list = gson.fromJson(reader, UserList.class);
            return (list != null) ? list : new UserList();
        } catch (IOException e) {
            e.printStackTrace();
            return new UserList();
        }
    }

    public boolean isValidIdKey(UserList userList, String idKey) {
        return userList.getUsers().stream()
                .anyMatch(user -> user.getIdkey().equals(idKey));
    }

    public boolean isValidUser(UserList userList, String idKey, String password) {
        return userList.getUsers().stream()
                .filter(user -> user.getIdkey().equals(idKey))
                .anyMatch(user -> BCrypt.checkpw(password, user.getPassword()));
    }

    public String getSuccessfulLoginMessage() {
        return "User Login Successful! Directing to Dashboard...";
    }

    public String getUnsuccessfulIdKey() {
        return "ID key not found. Please check your credentials and try again.";
    }

    public String getUnsuccessfulPassword() {
        return "Incorrect password. Please try again.";
    }

    public String inputIDKey() {
        System.out.print("Please enter your ID key: ");
        return scanner.nextLine().trim();
    }

    public String inputPassword() {
        System.out.print("Please enter password: ");
        return scanner.nextLine();
    }

    public boolean authenticateUser(UserList userList, String idKey, String password) {
        if (!this.isValidIdKey(userList, idKey)) {
            System.out.println(this.getUnsuccessfulIdKey());
            return false;
        }
        if (!this.isValidUser(userList, idKey, password)) {
            System.out.println(this.getUnsuccessfulPassword());
            return false;
        }

        System.out.println(getSuccessfulLoginMessage());
        System.out.println("Logged in as: " + idKey);
        return true;
    }
}