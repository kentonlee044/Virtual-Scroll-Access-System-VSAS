package a3.t10.g09;

import a3.t10.g09.Registration.UserRegistration;
import a3.t10.g09.Registration.UserRegistration.RegistrationResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class UserRegistrationTest {

    private static final Path USER_DATA = Path.of("src/main/java/a3/t10/g09/data/users.json");
    private String originalContent;

    @BeforeEach
    void setUp() throws IOException {
        if (Files.exists(USER_DATA)) {
            originalContent = Files.readString(USER_DATA);
        } else {
            if (USER_DATA.getParent() != null && !Files.exists(USER_DATA.getParent())) {
                Files.createDirectories(USER_DATA.getParent());
            }
            originalContent = null;
        }
        Files.writeString(USER_DATA, "{\"users\":[]}");
    }

    @AfterEach
    void tearDown() throws IOException {
        if (originalContent != null) {
            Files.writeString(USER_DATA, originalContent);
        } else {
            Files.deleteIfExists(USER_DATA);
        }
    }

    @Test
    void registerSuccessAddsUserAndHashesPassword() {
        UserRegistration registration = new UserRegistration();

        RegistrationResult result = registration.register(
                " Alice Smith ",
                " Alice@Example.com ",
                "0123456789",
                "ABC123",
                "Secure!Pass1");

        assertTrue(result.isSuccess());
        UserList savedUsers = registration.currentUsers();
        assertEquals(1, savedUsers.getUsers().size());

        User savedUser = savedUsers.getUsers().get(0);
        assertEquals("Alice Smith", savedUser.getFullname());
        assertEquals("alice@example.com", savedUser.getEmail());
        assertEquals("0123456789", savedUser.getPhone());
        assertEquals("ABC123", savedUser.getIdkey());
        assertNotEquals("Secure!Pass1", savedUser.getPassword());
        assertFalse(savedUser.getPassword().isBlank());
    }

    @Test
    void registerWithInvalidDataFailsAndDoesNotPersist() {
        UserRegistration registration = new UserRegistration();

        RegistrationResult result = registration.register(
                null,
                "invalid-email",
                "12345abc",
                "123",
                "short");

        assertFalse(result.isSuccess());
        assertFalse(result.getMessages().isEmpty());
        assertTrue(registration.currentUsers().getUsers().isEmpty());
    }

    @Test
    void registerRejectsDuplicateEmailAndIdKey() {
        UserRegistration registration = new UserRegistration();

        RegistrationResult first = registration.register(
                "Bob Example",
                "bob@example.com",
                "0987654321",
                "BOB001",
                "Another!Pass1");
        assertTrue(first.isSuccess());

        RegistrationResult duplicate = registration.register(
                "Robert Example",
                "BOB@example.com",
                "0123456789",
                "BOB001",
                "Another!Pass1");

        assertFalse(duplicate.isSuccess());
        assertFalse(duplicate.getMessages().isEmpty());
    }
}