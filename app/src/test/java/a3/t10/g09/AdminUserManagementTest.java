package a3.t10.g09;

import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class AdminUserManagementTest {

    private static final Path USERS_JSON = Path.of("src/main/java/a3/t10/g09/data/users.json");

    private String usersJsonBackup;
    private boolean usersJsonExisted;

    private a3.t10.g09.Registration.UserRegistration reg;

    @BeforeEach
    void backup() throws IOException {
        usersJsonExisted = Files.exists(USERS_JSON);
        if (usersJsonExisted) {
            usersJsonBackup = Files.readString(USERS_JSON, StandardCharsets.UTF_8);
        } else {
            usersJsonBackup = null;
        }
        reg = new a3.t10.g09.Registration.UserRegistration();
    }

    @AfterEach
    void restore() throws IOException {
        if (usersJsonExisted) {
            Files.createDirectories(USERS_JSON.getParent());
            Files.writeString(USERS_JSON, usersJsonBackup, StandardCharsets.UTF_8);
        } else {
            Files.deleteIfExists(USERS_JSON);
        }
    }

    private Scanner scannerFrom(String s) {
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        return new Scanner(in);
    }

    private User user(String id, String name, String phone, String email, String role, String passwordHash) {
        return new User(id, name, phone, email, role, passwordHash);
    }

    private String hash(String raw) {
        return org.mindrot.jbcrypt.BCrypt.hashpw(raw, org.mindrot.jbcrypt.BCrypt.gensalt(12));
    }

    private void writeUsers(User... users) {
        UserList list = new UserList();
        for (User u : users) list.addUser(u);
        reg.saveUsers(list);
    }

    @Test
    void run_exitsOnNoInput() {
        // Scanner with no lines triggers readLine -> hasNextLine=false path
        AdminUserManagement m = new AdminUserManagement(scannerFrom(""));
        m.run(null);
        // no assertion needed; just covering branch
    }

    @Test
    void listUsers_showsEmptyAndNonEmpty() {
        // Empty list case
        writeUsers();
        AdminUserManagement m1 = new AdminUserManagement(scannerFrom("1\n0\n"));
        m1.run(null);
        // Non-empty list case
        writeUsers(
                user("u1", "Alice", "0412345678", "alice@example.com", "user", hash("A1!aaaaa")),
                user("u2", "Bob", "0410000000", "bob@example.com", "admin", hash("B1!bbbbb"))
        );
        AdminUserManagement m2 = new AdminUserManagement(scannerFrom("1\n0\n"));
        m2.run(null);
    }

    @Test
    void addUser_cancelAtPrompts_and_invalidRegistration() {
        writeUsers();
        // Cancel at fullname
        new AdminUserManagement(scannerFrom("2\n\n0\n")).run(null);
        // Cancel at email
        new AdminUserManagement(scannerFrom("2\nAlice\n\n0\n")).run(null);
        // Cancel at idkey
        new AdminUserManagement(scannerFrom("2\nAlice\nalice@example.com\n\n0\n")).run(null);
        // Cancel at phone
        new AdminUserManagement(scannerFrom("2\nAlice\nalice@example.com\nabc123\n\n0\n")).run(null);
        // Cancel at password
        new AdminUserManagement(scannerFrom("2\nAlice\nalice@example.com\nabc123\n0412345678\n\n0\n")).run(null);
        // Invalid registration (bad email) hits failure path
        new AdminUserManagement(scannerFrom("2\nAlice\nbademail\n0412345678\nabc123\nPassw0rd!\n0\n")).run(null);
    }

    @Test
    void deleteUser_cancel_self_notFound_and_success() {
        // Prepare users
        User self = user("self01", "Self User", "0400000000", "self@example.com", "admin", hash("S1!aaaaa"));
        User other = user("victim", "Victim User", "0411111111", "victim@example.com", "user", hash("V1!bbbbb"));
        writeUsers(self, other);
        // Cancel
        new AdminUserManagement(scannerFrom("3\n\n0\n")).run(self);
        // Cannot delete self
        new AdminUserManagement(scannerFrom("3\nself01\n0\n")).run(self);
        // Not found
        new AdminUserManagement(scannerFrom("3\nunknown\n0\n")).run(self);
        // Success
        new AdminUserManagement(scannerFrom("3\nvictim\n0\n")).run(self);
        // Validate victim removed
        UserList after = reg.currentUsers();
        assertTrue(after.getUsers().stream().noneMatch(u -> "victim".equals(u.getIdkey())));
        assertTrue(after.getUsers().stream().anyMatch(u -> "self01".equals(u.getIdkey())));
    }

    @Test
    void toggleRole_cancel_notFound_self_and_toggleBothWays() {
        User u1 = user("u1", "Alice", "0412345678", "alice@example.com", "user", hash("A1!aaaaa"));
        User u2 = user("u2", "Bob", "0410000000", "bob@example.com", "admin", hash("B1!bbbbb"));
        writeUsers(u1, u2);
        // Cancel
        new AdminUserManagement(scannerFrom("4\n\n0\n")).run(u1);
        // Not found
        new AdminUserManagement(scannerFrom("4\nunknown\n0\n")).run(u1);
        // Cannot change own role
        new AdminUserManagement(scannerFrom("4\nu1\n0\n")).run(u1);
        // Toggle u2 from admin -> user
        new AdminUserManagement(scannerFrom("4\nu2\n0\n")).run(u1);
        UserList after1 = reg.currentUsers();
        User updated2 = after1.getUsers().stream().filter(x -> x.getIdkey().equals("u2")).findFirst().orElse(null);
        assertNotNull(updated2);
        assertEquals("user", updated2.getRole());
        // Toggle u2 back to admin
        new AdminUserManagement(scannerFrom("4\nu2\n0\n")).run(u1);
        UserList after2 = reg.currentUsers();
        User updated2b = after2.getUsers().stream().filter(x -> x.getIdkey().equals("u2")).findFirst().orElse(null);
        assertNotNull(updated2b);
        assertEquals("admin", updated2b.getRole());
    }
}
