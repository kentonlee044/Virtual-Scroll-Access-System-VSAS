package a3.t10.g09;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

class UserTest{

    private User user;
    
    @BeforeEach
    public void setUp(){

        UserRegistration userReg1 = new UserRegistration();
        String password = userReg1.hashPassword("SOFT2412");

        user = new User("1", "Thomas", "thomas@gmail.com", "Thomas Shelby", "1234567890", "admin", password); 
    }

    @Test 
    public void getIdkeyTest(){
        assertEquals("1", user.getIdkey());
    }

    @Test 
    public void getUsernameTest(){
        assertEquals("Thomas", user.getUsername());
    }

    @Test
    public void getEmailTest(){
        assertEquals("thomas@gmail.com", user.getEmail());
    }

    @Test
    public void getFullnameTest(){
        assertEquals("Thomas Shelby", user.getFullname());
    }

    @Test
    public void getPhoneNumberTest(){
        assertEquals("1234567890", user.getPhoneNumber());
    }

    @Test
    public void getRoleTest(){
        assertEquals("admin", user.getRole());
    }

    @Test
    public void getPasswordTest(){
        String password = "SOFT2412";
        boolean hashedPassword = BCrypt.checkpw(password, user.getPassword());
        assertTrue(hashedPassword);

    }
}