package a3.t10.g09;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;

public class UserLoginTest{
    private UserList userList;
    private UserLogin login;

    @BeforeEach
    public void setUp() {
        userList = new UserList();
        UserRegistration rego = new UserRegistration();
        User user1 = new User("1", "tester1", "test1@gmail.com", "Test User1", "1111111111", "user", rego.hashPassword("password1"));

        User user2 = new User("2", "tester2", "test2@gmail.com", "Test User2", "2222222222", "user", rego.hashPassword("password2"));

        User user3 = new User("3", "tester3", "test3@gmail.com", "Test User3", "3333333333", "user", rego.hashPassword("password3"));

        User admin1 = new User("4", "tester4", "test4@gmail.com", "Test Admin4", "4444444444", "admin", rego.hashPassword("password4"));

        User random1 = new User("5", "tester5", "test5@gmail.com", "Test random5", "5555555555", "random", rego.hashPassword("password5"));
        
        userList.addUser(user1);
        userList.addUser(user2);
        userList.addUser(user3);
        userList.addUser(admin1);
        userList.addUser(random1);
    }
    // TODO users.json currently has no users, so this test will fail
    @Test
    public void testGetUserData() throws IOException {
        File tempFile = File.createTempFile("test-users", ".json");
        tempFile.deleteOnExit();

        String json = """
            {
                "users": [
                    {
                        "idkey": "1",
                        "username": "tester1",
                        "email": "test1@gmail.com",
                        "fullname": "Test User1",
                        "phoneNumber": "1111111111",
                        "role": "user",
                        "password": "hashedpass1"
                    }
                ]
            }
            """;

        Files.writeString(tempFile.toPath(), json);

        // Subclass or inject the path
        UserLogin login = new UserLogin() {{
            userData = tempFile.getAbsolutePath();
        }};

        UserList result = login.getUserData();
        assertNotNull(result);
        assertEquals("tester1", result.getUsers().get(0).getUsername());
    }

    @Test
    public void testGetUserData_FileNotFoundTriggersCatch() {
        UserLogin login = new UserLogin() {{
            userData = "nonexistent/path/users.json"; // This file doesn't exist
        }};

        // Call the method and expect it to return null due to IOException
        UserList result = login.getUserData();

        assertNull(result, "Expected null when file is missing");
    }

    @Test
    public void testGetUserRole(){
        login = new UserLogin();
        String role = login.getUserRole(userList, "tester1");
        assertEquals("user", role);
    }

    @Test
    public void testIsValidUsername(){
        login = new UserLogin();
        boolean isValid = login.isValidUsername(userList, "tester2");
        assertEquals(true, isValid);
    }

    @Test
    public void testGetSuccessfulLoginMessage(){
        login = new UserLogin();
        String message = login.getSuccessfulLoginMessage();
        assertEquals("User Login Successful! Directing to Dashboard...", message);
    }

    @Test
    public void testGetUnsuccessfulUsername(){
        login = new UserLogin();
        String message = login.getUnsuccessfulUsername();
        assertEquals("ID key not found. Please check your credentials and try again.", message);
    }

    @Test
    public void testGetUnsuccessfulPassword(){
        login = new UserLogin();
        String message = login.getUnsuccessfulPassword();
        assertEquals("Incorrect password. Please try again.", message);
    }

    @Test
    public void testInputIDKey() {
        // Simulate user input: "ABC123\n"
        String simulatedInput = "ABC123\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream); // Redirect System.in

        login = new UserLogin(); 
        String result = login.inputIDKey();

        assertEquals("ABC123", result);

        System.setIn(System.in); // Reset System.in to its original state
    }

    @Test
    public void testInputPassword(){
        String simulatedPW = "ILoveCS\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedPW.getBytes());
        System.setIn(inputStream); // Redirect System.in

        login = new UserLogin(); 
        String result = login.inputPassword();

        assertEquals("ILoveCS", result);

        System.setIn(System.in); // Reset System.in to its original state
    }

    @Test 
    public void testAuthenticateUserInvalidUsername(){
        login = new UserLogin();
        boolean isAuthenticated = login.authenticateUser(userList, "wronguser", "password3");
        assertEquals(false, isAuthenticated);
    }

    @Test
    public void testAuthenticateUserInvalidPassword(){
        login = new UserLogin();
        boolean isAuthenticated = login.authenticateUser(userList, "tester3", "wrongpassword");
        assertEquals(false, isAuthenticated);
    }

    @Test
    public void testAuthenticateUserRole(){
        login = new UserLogin();
        boolean isAuthenticated = login.authenticateUser(userList, "tester3", "password3");
        assertEquals(true, isAuthenticated);
    }
    
    @Test
    public void testAuthenticateAdminRole(){
        login = new UserLogin();
        boolean isAuthenticated = login.authenticateUser(userList, "tester4", "password4");
        assertEquals(true, isAuthenticated);
    }
    
    @Test
    public void testInvalidRole(){
        login = new UserLogin();
        boolean isAuthenticated = login.authenticateUser(userList, "tester5", "password5");
        assertEquals(true, isAuthenticated);
    }
}