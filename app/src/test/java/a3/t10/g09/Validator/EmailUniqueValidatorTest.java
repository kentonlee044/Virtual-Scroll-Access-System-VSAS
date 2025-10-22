package a3.t10.g09.Validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import a3.t10.g09.Registration.UserRegistration;
import a3.t10.g09.User;
import a3.t10.g09.UserList;

public class EmailUniqueValidatorTest {
    // Test code to be implemented
    private EmailUniqueValidator validator;
    private User user;
    private UserList userList;

    @BeforeEach
    void setup() {
        // Mock UserList or use a test instance
        userList = new UserList();
        // Add test users to mockUserList if necessary
        validator = new EmailUniqueValidator(userList);
        UserRegistration userReg = new UserRegistration();
        String hashedPassword = userReg.hashPassword("SOFT2412");
        user = new User("1", "Thomas Shelby", "1234567890", "thomas@gmail.com", hashedPassword);
        userList.addUser(user);
    }

    @Test
    public void validate_validEmailTest(){
        String expected = null;
        String actual = validator.validate("test@gmail.com");
        assertEquals(expected, actual);
    }

    @Test
    public void validate_invalidEmailTest(){
        String expected = "Email cannot be empty.";
        String actual = validator.validate(null);
        assertEquals(expected, actual);
    }

    @Test
    public void validate_takenEmailTest(){
        String expected = "Email address already registered.";
        String actual = validator.validate("thomas@gmail.com");
        assertEquals(expected, actual);
    }

    @Test 
    public void validate_emptyEmailTest(){
        String expected = "Email cannot be empty.";
        String actual = validator.validate("   ");
        assertEquals(expected, actual);
    }
}