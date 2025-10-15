package a3.t10.g09.Login;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.mindrot.jbcrypt.BCrypt;
import a3.t10.g09.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class UserLogin {

    private static final String DEFAULT_USER_DATA = "src/main/java/a3/t10/g09/data/users.json";

    private final String userDataPath;
    private final Gson gson = new Gson();
    private final Scanner scanner;

    public UserLogin() {
        this(DEFAULT_USER_DATA, new Scanner(System.in));
    }

    public UserLogin(String userDataPath) {
        this(userDataPath, new Scanner(System.in));
    }

    UserLogin(String userDataPath, Scanner scanner) {
        this.userDataPath = userDataPath;
        this.scanner = scanner;
    }

    public UserList getUserData() {
        try (JsonReader reader = new JsonReader(new FileReader(userDataPath))) {
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

    public String inputIDKey() {
        System.out.print("Please enter your ID key: ");
        return scanner.nextLine().trim();
    }

    public String inputPassword() {
        System.out.print("Please enter password: ");
        return scanner.nextLine();
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

    public AuthenticationResult authenticate(String idKey, String password) {
        UserList users = getUserData();

        if (!isValidIdKey(users, idKey)) {
            return AuthenticationResult.failed(getUnsuccessfulIdKey());
        }
        if (!isValidUser(users, idKey, password)) {
            return AuthenticationResult.failed(getUnsuccessfulPassword());
        }
        return AuthenticationResult.success(getSuccessfulLoginMessage());
    }

    public boolean authenticateUser(UserList userList, String idKey, String password) {
        if (!isValidIdKey(userList, idKey)) {
            System.out.println(getUnsuccessfulIdKey());
            return false;
        }
        if (!isValidUser(userList, idKey, password)) {
            System.out.println(getUnsuccessfulPassword());
            return false;
        }

        System.out.println(getSuccessfulLoginMessage());
        System.out.println("Logged in as: " + idKey);
        return true;
    }

    public static final class AuthenticationResult {
        private final boolean success;
        private final String message;

        private AuthenticationResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public static AuthenticationResult success(String message) {
            return new AuthenticationResult(true, message);
        }

        public static AuthenticationResult failed(String message) {
            return new AuthenticationResult(false, message);
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}