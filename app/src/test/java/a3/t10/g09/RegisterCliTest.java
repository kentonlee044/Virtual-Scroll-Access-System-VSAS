package a3.t10.g09;

import a3.t10.g09.Registration.*;
import a3.t10.g09.User;
import a3.t10.g09.UserList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class RegisterCliTest {

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
    void runCompletesSuccessfulFlow() throws IOException {
        Scanner scanner = scannerFromLines(
                "Alice Example",
                "alice@example.com",
                "ALICE1",
                "0123456789",
                "Strong!Pass1",
                "",
                "");
        RegisterCli cli = new RegisterCli(scanner);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));
        try {
            cli.run();
        } finally {
            System.setOut(originalOut);
            scanner.close();
        }

        String console = out.toString();
        assertTrue(console.contains("Account created successfully."));

        UserList users = new UserRegistration().currentUsers();
        assertEquals(1, users.getUsers().size());
        User saved = users.getUsers().get(0);
        assertEquals("Alice Example", saved.getFullname());
        assertEquals("alice@example.com", saved.getEmail());
        assertEquals("ALICE1", saved.getIdkey());
    }

    @Test
    void runStopsWhenInputMissing() throws IOException {
        Scanner scanner = scannerFromLines();
        RegisterCli cli = new RegisterCli(scanner);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));
        try {
            cli.run();
        } finally {
            System.setOut(originalOut);
            scanner.close();
        }

        String console = out.toString();
        assertTrue(console.contains("No input detected. Exiting registration."));
        assertTrue(new UserRegistration().currentUsers().getUsers().isEmpty());
    }

    private Scanner scannerFromLines(String... lines) {
        String input = String.join(System.lineSeparator(), lines);
        if (!input.isEmpty()) {
            input += System.lineSeparator();
        }
        return new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
    }
}