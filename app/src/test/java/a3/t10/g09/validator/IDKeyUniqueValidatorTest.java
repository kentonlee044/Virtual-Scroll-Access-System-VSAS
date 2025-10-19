package a3.t10.g09.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import a3.t10.g09.Registration.UserRegistration;
import a3.t10.g09.User;
import a3.t10.g09.UserList;

public class IDKeyUniqueValidatorTest {
    // Test code to be implemented
    private IDKeyUniqueValidator validator;
    private User user;
    private UserList userList;

    @BeforeEach
    void setup() {
        // Mock UserList or use a test instance
        userList = new UserList();
        // Add test users to mockUserList if necessary
        validator = new IDKeyUniqueValidator(userList);
        UserRegistration userReg = new UserRegistration();
        String hashedPassword = userReg.hashPassword("SOFT2412");
        user = new User("1", "Thomas Shelby", "1234567890", "thomas@gmail.com", hashedPassword);
        userList.addUser(user);
    }

    @Test
    public void validate_validIDKey(){
        String expected = null;
        String actual = validator.validate("tesingValid input");
        assertEquals(expected, actual);
    }

    @Test
    public void validate_invalidIDKeyTest(){
        String expected = "ID key is already in use.";
        String actual = validator.validate("1");
        assertEquals(expected, actual);
    }

    @Test
    public void validate_nullInput(){
        String expected = "ID key cannot be null.";
        String actual = validator.validate(null);
        assertEquals(expected, actual);
    }

    @Test 
    public void validate_nullUserList(){
        userList = null;
        String expected = null;
        String actual = validator.validate("someIDKey");
        assertEquals(expected, actual);
    }

    @Test
    public void validate_idKeyMatchesCurrentUserOwnID() {
        validator = new IDKeyUniqueValidator(userList, user); // currentUser is set
        String expected = null;
        String actual = validator.validate("1"); // same as currentUser.getIdkey()
        assertEquals(expected, actual);
    }

    @Test
    public void validate_idKeyMatchesAnotherUser() {
        User anotherUser = new User("2", "Arthur Shelby", "0987654321", "arthur@gmail.com", "hashedPass");
        userList.addUser(anotherUser);

        validator = new IDKeyUniqueValidator(userList, user); // currentUser is '1'
        String expected = "ID key is already in use.";
        String actual = validator.validate("2"); // matches anotherUser
        assertEquals(expected, actual);
    }

    @Test
    public void validate_userListUsersIsNull() {
        userList = new UserList();
        userList.setUsers(null); // assuming you have a setter or can manipulate directly
        validator = new IDKeyUniqueValidator(userList);

        String expected = null;
        String actual = validator.validate("someIDKey");
        assertEquals(expected, actual);
    }

}