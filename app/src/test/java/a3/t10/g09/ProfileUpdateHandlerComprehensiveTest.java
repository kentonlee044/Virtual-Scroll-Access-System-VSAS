package a3.t10.g09;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.mindrot.jbcrypt.BCrypt;

public class ProfileUpdateHandlerComprehensiveTest {

    private static final Path USERS_JSON = Path.of("src/main/java/a3/t10/g09/data/users.json");
    private static final Path SCROLLS_JSON = Path.of("src/main/java/a3/t10/g09/data/scrolls.json");

    private String usersJsonBackup;
    private boolean usersJsonExisted;

    private String scrollsJsonBackup;
    private boolean scrollsJsonExisted;

    @BeforeEach
    void backupFiles() throws IOException {
        // Backup users.json
        usersJsonExisted = Files.exists(USERS_JSON);
        if (usersJsonExisted) {
            usersJsonBackup = Files.readString(USERS_JSON, StandardCharsets.UTF_8);
        } else {
            usersJsonBackup = null;
        }
        // Backup scrolls.json
        scrollsJsonExisted = Files.exists(SCROLLS_JSON);
        if (scrollsJsonExisted) {
            scrollsJsonBackup = Files.readString(SCROLLS_JSON, StandardCharsets.UTF_8);
        } else {
            scrollsJsonBackup = null;
        }
    }

    @AfterEach
    void restoreFiles() throws IOException {
        // Restore users.json
        if (usersJsonExisted) {
            Files.createDirectories(USERS_JSON.getParent());
            Files.writeString(USERS_JSON, usersJsonBackup, StandardCharsets.UTF_8);
        } else {
            // If it did not exist before, remove it to avoid side effects
            Files.deleteIfExists(USERS_JSON);
        }
        // Restore scrolls.json
        if (scrollsJsonExisted) {
            Files.createDirectories(SCROLLS_JSON.getParent());
            Files.writeString(SCROLLS_JSON, scrollsJsonBackup, StandardCharsets.UTF_8);
        } else {
            Files.deleteIfExists(SCROLLS_JSON);
        }
    }

    private User newUserWithPassword(String id, String full, String phone, String email, String rawPassword) {
        String hash = BCrypt.hashpw(rawPassword, BCrypt.gensalt(12));
        return new User(id, full, phone, email, hash);
    }

    // ========= Pure unit tests (no persistence) =========

    @Test
    void updatePhoneNumber_valid_whenNoUserList() {
        User u = newUserWithPassword("me001", "Alice", "0412345678", "alice@example.com", "OldP@ss1");
        ProfileUpdateHandler h = new ProfileUpdateHandler(u, null);
        String err = h.updatePhoneNumber("0499999999");
        assertNull(err);
        assertEquals("0499999999", u.getPhone());
    }

    @Test
    void updatePhoneNumber_invalid_returnsError() {
        User u = newUserWithPassword("me001", "Alice", "0412345678", "alice@example.com", "OldP@ss1");
        ProfileUpdateHandler h = new ProfileUpdateHandler(u, null);
        assertNotNull(h.updatePhoneNumber("04abc")); // non-digits
        assertNotNull(h.updatePhoneNumber("123"));   // too short
    }

    @Test
    void updateEmail_valid_and_invalid() {
        User u = newUserWithPassword("me001", "Alice", "0412345678", "alice@example.com", "OldP@ss1");
        ProfileUpdateHandler h = new ProfileUpdateHandler(u, null);
        assertNotNull(h.updateEmail("no-at.example.com"));
        assertNotNull(h.updateEmail("no-tld@domain"));
        assertNull(h.updateEmail("new.email@example.com"));
        assertEquals("new.email@example.com", u.getEmail());
    }

    @Test
    void updateName_valid_and_invalid() {
        User u = newUserWithPassword("me001", "Alice", "0412345678", "alice@example.com", "OldP@ss1");
        ProfileUpdateHandler h = new ProfileUpdateHandler(u, null);
        assertNotNull(h.updateName(""));
        assertNotNull(h.updateName("John$Doe"));
        assertNull(h.updateName("John Doe"));
        assertEquals("John Doe", u.getFullname());
    }

    @Test
    void updatePassword_incorrectCurrent_returnsError() {
        User u = newUserWithPassword("me001", "Alice", "0412345678", "alice@example.com", "OldP@ss1");
        ProfileUpdateHandler h = new ProfileUpdateHandler(u, null);
        String err = h.updatePassword("wrongCurrent", "NewPass1!");
        assertEquals("Current password is incorrect.", err);
    }

    @Test
    void updatePassword_valid_updatesHashedPassword() {
        User u = newUserWithPassword("me001", "Alice", "0412345678", "alice@example.com", "OldP@ss1");
        String oldHash = u.getPassword();
        ProfileUpdateHandler h = new ProfileUpdateHandler(u, null);
        String err = h.updatePassword("OldP@ss1", "NewPass1!");
        assertNull(err);
        assertNotEquals(oldHash, u.getPassword());
        assertTrue(BCrypt.checkpw("NewPass1!", u.getPassword()));
    }

    // ========= Light integration tests (with persistence/files) =========

    @Test
    void updateIDKey_notUnique_returnsError_withUserList() {
        // Prepare a user list containing a different user with the target ID
        User me = newUserWithPassword("me001", "Alice", "0412345678", "alice@example.com", "OldP@ss1");
        User other = newUserWithPassword("taken123", "Bob", "0410000000", "bob@example.com", "Another1!");
        UserList list = new UserList();
        list.addUser(me);
        list.addUser(other);

        ProfileUpdateHandler h = new ProfileUpdateHandler(me, list);
        String err = h.updateIDKey("taken123");
        assertNotNull(err); // should report not unique
        assertEquals("me001", me.getIdkey());
    }

    @Test
    void methods_returnUnableToLocate_whenUserIsNull() {
        ProfileUpdateHandler h = new ProfileUpdateHandler(null, null);
        assertEquals("Unable to locate user record.", h.updatePhoneNumber("0412345678"));
        assertEquals("Unable to locate user record.", h.updateEmail("a@b.com"));
        assertEquals("Unable to locate user record.", h.updateName("Alice"));
        assertEquals("Unable to locate user record.", h.updateIDKey("abc123"));
        assertEquals("Unable to locate user record.", h.updatePassword("x", "Y1!aaaa"));
    }

    @Test
    void updateIDKey_formatErrors_returnSpecificMessages() {
        User u = newUserWithPassword("me001", "Alice", "0412345678", "alice@example.com", "OldP@ss1");
        ProfileUpdateHandler h = new ProfileUpdateHandler(u, null);
        // null input
        assertEquals("ID key cannot be null.", h.updateIDKey(null));
        // too short / invalid format
        assertEquals("ID key must be alphanumeric and 6-12 characters long.", h.updateIDKey("abc"));
        // contains non-alphanumeric
        assertEquals("ID key must be alphanumeric and 6-12 characters long.", h.updateIDKey("abc$12"));
    }

    @Test
    void updatePassword_ruleFailures_areReported() {
        User u = newUserWithPassword("me001", "Alice", "0412345678", "alice@example.com", "OldP@ss1");
        ProfileUpdateHandler h = new ProfileUpdateHandler(u, null);
        // Correct current, but new password violates various validators
        assertNotNull(h.updatePassword("OldP@ss1", "NoDigit!"));        // missing digit
        assertNotNull(h.updatePassword("OldP@ss1", "Aa1!"));            // too short
        assertNotNull(h.updatePassword("OldP@ss1", "noupper1!"));       // missing uppercase
        assertNotNull(h.updatePassword("OldP@ss1", "NoSpecial1"));      // missing special char
    }

    @Test
    void updatePhoneNumber_tooLong_returnsError() {
        User u = newUserWithPassword("me001", "Alice", "0412345678", "alice@example.com", "OldP@ss1");
        ProfileUpdateHandler h = new ProfileUpdateHandler(u, null);
        String tooLong = "0412345678909832434238402383984734792384"; // clearly exceeds typical length
        assertNotNull(h.updatePhoneNumber(tooLong));
    }

    @Test
    void updateEmail_null_returnsSpecificMessage() {
        User u = newUserWithPassword("me001", "Alice", "0412345678", "alice@example.com", "OldP@ss1");
        ProfileUpdateHandler h = new ProfileUpdateHandler(u, null);
        assertEquals("Email cannot be null.", h.updateEmail(null));
    }

    @Test
    void updatePhone_null_returnsSpecificMessage() {
        User u = newUserWithPassword("me001", "Alice", "0412345678", "alice@example.com", "OldP@ss1");
        ProfileUpdateHandler h = new ProfileUpdateHandler(u, null);
        assertNotNull(h.updatePhoneNumber(null));
    }

    @Test
    void updatePassword_nullNew_returnsSpecificMessage() {
        User u = newUserWithPassword("me001", "Alice", "0412345678", "alice@example.com", "OldP@ss1");
        ProfileUpdateHandler h = new ProfileUpdateHandler(u, null);
        // Correct current password, but null new password should trigger validator message
        String msg = h.updatePassword("OldP@ss1", null);
        assertEquals("Password cannot be null.", msg);
    }

    @Test
    void persistAndSync_updatesSessionUserFromPersistedRecord() throws IOException {
        // Create distinct session and persisted user instances with same id
        User session = newUserWithPassword("sync01", "Sess Name", "0400000000", "sess@example.com", "OldP@ss1");
        User persisted = newUserWithPassword("sync01", "Persist Name", "0499999999", "persist@example.com", "Other1!");
        UserList list = new UserList();
        list.addUser(persisted);
        // Act: handler resolves persisted user and targets it; then sync should copy back to session
        ProfileUpdateHandler h = new ProfileUpdateHandler(session, list);
        String err = h.updateName("Updated Persist Name");
        assertNull(err);
        // After persistAndSync, session should reflect target (persisted) fields, including the updated name
        assertEquals("Updated Persist Name", session.getFullname());
        assertEquals(persisted.getEmail(), session.getEmail());
        assertEquals(persisted.getPhone(), session.getPhone());
        assertEquals(persisted.getIdkey(), session.getIdkey());
    }

    @Test
    void updateIDKey_withNullUserList_stillUpdates() {
        User me = newUserWithPassword("me001", "Alice", "0412345678", "alice@example.com", "OldP@ss1");
        ProfileUpdateHandler h = new ProfileUpdateHandler(me, null);
        String err = h.updateIDKey("abc123");
        assertNull(err);
        assertEquals("abc123", me.getIdkey());
    }
}
