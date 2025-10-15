package a3.t10.g09;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import a3.t10.g09.Registration.UserRegistration;

class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        UserRegistration userReg = new UserRegistration();
        String hashedPassword = userReg.hashPassword("SOFT2412");

        user = new User("1", "Thomas Shelby", "1234567890", "thomas@gmail.com", hashedPassword);
    }

    @Test
    public void getIdkeyTest() {
        assertEquals("1", user.getIdkey());
    }

    @Test
    public void getFullnameTest() {
        assertEquals("Thomas Shelby", user.getFullname());
    }

    @Test
    public void getPhoneTest() {
        assertEquals("1234567890", user.getPhone());
    }

    @Test
    public void getEmailTest() {
        assertEquals("thomas@gmail.com", user.getEmail());
    }

    @Test
    public void getPasswordTest() {
        assertTrue(BCrypt.checkpw("SOFT2412", user.getPassword()));
    }
}