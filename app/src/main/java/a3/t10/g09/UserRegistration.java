package a3.t10.g09;

import org.mindrot.jbcrypt.BCrypt;

public class UserRegistration {
    // Placeholder for user registration logic

    // Method to hash passwords using BCrypt
    public String hashPassword(String password){
        String salt = BCrypt.gensalt(12);
        String hashedPassword = BCrypt.hashpw(password, salt);
        
        return hashedPassword;
    }
}