package a3.t10.g09;

import java.util.Scanner;

public class UserLogin {

    Scanner scanner = new Scanner(System.in);

    // Methods that interact with the JSON file 
    public boolean isValidUsername(String username) {
        // Checks if username is existing inside the json file
        return false;
    }

    public boolean isValidUser(String username, String password) {
        // Checks if pw matches the hashed pw and username inside the json file
        return false;
    }

    // Feedback messages
    public String getSuccessfulLoginMessage() {
        return "";
    }

    public String getUnsuccessfulUsername() {
        return "";
    }

    public String getUnsuccessfulPassword() {
        return "";
    }

    // User input methods
    public String inputIDKey(){
        return "";
    }

    public String inputPassword(){
        return "";
    }
    public boolean authenticateUser(String username, String password) {
        // Authenticate user against stored credentials
        return false;
    }
}