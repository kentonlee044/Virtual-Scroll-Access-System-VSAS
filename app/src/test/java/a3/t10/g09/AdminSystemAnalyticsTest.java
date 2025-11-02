package a3.t10.g09;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class AdminSystemAnalyticsTest {

    private static final Path SCROLLS_JSON = Path.of("src/main/java/a3/t10/g09/data/scrolls.json");
    private String originalContent;

    @BeforeEach
    void setup() throws IOException {
        if (Files.exists(SCROLLS_JSON)) {
            originalContent = Files.readString(SCROLLS_JSON);
        } else {
            if (SCROLLS_JSON.getParent() != null && !Files.exists(SCROLLS_JSON.getParent())) {
                Files.createDirectories(SCROLLS_JSON.getParent());
            }
            originalContent = null;
        }
        // Minimal, valid dataset with one scroll
        String data = """
                [
                  {
                    "filename": "alpha.pdf",
                    "numberOfUploads": 1,
                    "numberOfDownloads": 2,
                    "categorizationId": "catA",
                    "ownerId": "owner-123",
                    "uploadDate": "2025-10-29T12:00:00"
                  }
                ]
                """;
        Files.writeString(SCROLLS_JSON, data, StandardCharsets.UTF_8);
    }

    @AfterEach
    void teardown() throws IOException {
        if (originalContent != null) {
            Files.writeString(SCROLLS_JSON, originalContent, StandardCharsets.UTF_8);
        } else {
            Files.deleteIfExists(SCROLLS_JSON);
        }
    }

    @Test
    void displayAnalytics_showAll_thenExit() {
        // 1 -> show all, then Enter to return, then 0 -> exit
        String input = "1\n\n0\n";
        AdminSystemAnalytics analytics = new AdminSystemAnalytics(
                new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))));
        assertDoesNotThrow(analytics::displayAnalytics);
    }

    @Test
    void displayAllScrolls_nonEmpty_returnsOnEnter() {
        // displayAllScrolls will show table and wait for Enter to return
        String input = "\n";
        AdminSystemAnalytics analytics = new AdminSystemAnalytics(
                new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))));
        assertDoesNotThrow(analytics::displayAllScrolls);
    }

    @Test
    void filterByCategory_match_thenReturn() {
        // Provide matching category, then Enter to return from table
        String input = "catA\n\n";
        AdminSystemAnalytics analytics = new AdminSystemAnalytics(
                new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))));
        assertDoesNotThrow(analytics::filterByCategory);
    }

    @Test
    void filterByDate_match_thenReturn() {
        // Date prefix matches uploadDate "2025-10-29..."
        String input = "2025-10-29\n\n";
        AdminSystemAnalytics analytics = new AdminSystemAnalytics(
                new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))));
        assertDoesNotThrow(analytics::filterByDate);
    }

    @Test
    void filterByOwner_match_thenReturn() {
        String input = "owner-123\n\n";
        AdminSystemAnalytics analytics = new AdminSystemAnalytics(
                new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))));
        assertDoesNotThrow(analytics::filterByOwner);
    }

    @Test
    void filterByFilename_downloadFlow_thenReturn() {
        // Match filename, choose "download", provide filename, then Enter to return
        String input = "alpha.pdf\ndownload\nalpha.pdf\n\n";
        AdminSystemAnalytics analytics = new AdminSystemAnalytics(
                new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))));
        assertDoesNotThrow(analytics::filterByFilename);
    }
}