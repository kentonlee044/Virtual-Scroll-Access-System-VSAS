package a3.t10.g09;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import a3.t10.g09.Login.UserLogin;
import a3.t10.g09.Registration.UserRegistration;

public class UserLoginTest {
    private UserList userList;
    private UserLogin login;

    @BeforeEach
    public void setUp() {
        userList = new UserList();
        UserRegistration rego = new UserRegistration();

        userList.addUser(new User("tester1", "Test User1", "1111111111", "test1@gmail.com",
                rego.hashPassword("password1")));
        userList.addUser(new User("tester2", "Test User2", "2222222222", "test2@gmail.com",
                rego.hashPassword("password2")));
        userList.addUser(new User("tester3", "Test User3", "3333333333", "test3@gmail.com",
                rego.hashPassword("password3")));
        userList.addUser(new User("tester4", "Test Admin4", "4444444444", "test4@gmail.com",
                rego.hashPassword("password4")));
        userList.addUser(new User("tester5", "Test random5", "5555555555", "test5@gmail.com",
                rego.hashPassword("password5")));
    }

    @Test
    public void testGetUserData() throws Exception {
        Path tempFile = Files.createTempFile("test-users", ".json");
        tempFile.toFile().deleteOnExit();

        String json = """
                {
                    "users": [
                        {
                            "idkey": "tester1",
                            "fullname": "Test User1",
                            "phone": "1111111111",
                            "email": "test1@gmail.com",
                            "password": "hashedpass1"
                        }
                    ]
                }
                """;

        Files.writeString(tempFile, json);

        UserLogin login = new UserLogin(tempFile.toString());

        UserList result = login.getUserData();
        assertNotNull(result);
        assertEquals(1, result.getUsers().size());
        assertEquals("tester1", result.getUsers().get(0).getIdkey());
    }

    @Test
    public void testGetUserData_FileNotFoundReturnsEmptyList() {
        UserLogin login = new UserLogin("nonexistent/path/users.json");

        UserList result = login.getUserData();

        assertNotNull(result);
        assertTrue(result.getUsers().isEmpty(), "Expected empty list when file is missing");
    }

    @Test
    public void testIsValidIdKey() {
        login = new UserLogin();
        assertTrue(login.isValidIdKey(userList, "tester2"));
    }

    @Test
    public void testGetSuccessfulLoginMessage() {
        login = new UserLogin();
        assertEquals("User Login Successful! Directing to Dashboard...", login.getSuccessfulLoginMessage());
    }

    @Test
    public void testGetUnsuccessfulIdKey() {
        login = new UserLogin();
        assertEquals("ID key not found. Please check your credentials and try again.", login.getUnsuccessfulIdKey());
    }

    @Test
    public void testGetUnsuccessfulPassword() {
        login = new UserLogin();
        assertEquals("Incorrect password. Please try again.", login.getUnsuccessfulPassword());
    }

    @Test
    public void testInputIDKey() {
        InputStream originalIn = System.in;
        try {
            String simulatedInput = "ABC123\n";
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

            login = new UserLogin();
            assertEquals("ABC123", login.inputIDKey());
        } finally {
            System.setIn(originalIn);
        }
    }

    @Test
    public void testInputPassword() {
        InputStream originalIn = System.in;
        try {
            String simulatedPW = "ILoveCS\n";
            System.setIn(new ByteArrayInputStream(simulatedPW.getBytes()));

            login = new UserLogin();
            assertEquals("ILoveCS", login.inputPassword());
        } finally {
            System.setIn(originalIn);
        }
    }

    @Test
    public void testAuthenticateUserInvalidIdKey() {
        login = new UserLogin();
        assertFalse(login.authenticateUser(userList, "wronguser", "password3"));
    }

    @Test
    public void testAuthenticateUserInvalidPassword() {
        login = new UserLogin();
        assertFalse(login.authenticateUser(userList, "tester3", "wrongpassword"));
    }

    @Test
    public void testAuthenticateUserSuccess() {
        login = new UserLogin();
        assertTrue(login.authenticateUser(userList, "tester3", "password3"));
    }
}