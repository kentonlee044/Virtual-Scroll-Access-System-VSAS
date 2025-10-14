package a3.t10.g09;

import java.util.Scanner;

public class UserLogin {

    Scanner scanner = new Scanner(System.in);

    // Retrieve info from JSON file
    private String userData = "data/users.json";

    public String getUserData() {
        // TODO Reads the JSON file and returns its content , may need to change type
        return "";
    }

    // Methods that interact with the JSON file 
    public boolean isValidUsername(String username) {
        // Checks if username is existing inside the json file
        // TODO check if username exists in JSON file
        return false;
    }

    public boolean isValidUser(String username, String password) {
        // Checks if user inputted password matches the usernames' hashed passwords inside the json file
        // TODO check if password matches the username in JSON file
        return false;
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
    public String inputIDKey(){
        System.out.println("Please enter your ID key: ");
        String IDkey = scanner.next();
        return IDkey;
    }

    public String inputPassword(){
        System.out.println("Please enter password: ");
        String password = scanner.next();
        return password;
    }
    public boolean authenticateUser(String username, String password) {
        // Authenticate user against stored credentials

        String data = this.getUserData(); 
        String inputUsername = this.inputIDKey();
        String inputPassword = this.inputPassword();

        if(!this.isValidUsername(inputUsername)){
            this.getUnsuccessfulUsername();
            return false;
        }
        else if(!this.isValidUser(inputUsername, inputPassword)){
            this.getUnsuccessfulPassword();
            return false;
        }
        else{
            this.getSuccessfulLoginMessage();
            return true;
        }
        return false;
    }
}