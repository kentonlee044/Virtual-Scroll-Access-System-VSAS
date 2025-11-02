package a3.t10.g09;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class AdminSystemAnalyticsExtendedTest {

    private static final Path SCROLLS_JSON = Path.of("src/main/java/a3/t10/g09/data/scrolls.json");
    private String originalContent;

    @BeforeEach
    void setup() throws Exception {
        if (Files.exists(SCROLLS_JSON)) {
            originalContent = Files.readString(SCROLLS_JSON);
        } else {
            if (SCROLLS_JSON.getParent() != null && !Files.exists(SCROLLS_JSON.getParent())) {
                Files.createDirectories(SCROLLS_JSON.getParent());
            }
            originalContent = null;
        }
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
    void teardown() throws Exception {
        if (originalContent != null) {
            Files.writeString(SCROLLS_JSON, originalContent, StandardCharsets.UTF_8);
        } else {
            Files.deleteIfExists(SCROLLS_JSON);
        }
    }

    @Test
    void displayAllScrolls_previewNotFound_thenExit() {
        // preview -> enter missing name -> two Enters to continue -> Enter to exit
        // table
        String input = "preview\nnotthere.pdf\n\n\n\n";
        AdminSystemAnalytics analytics = new AdminSystemAnalytics(
                new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))));
        assertDoesNotThrow(analytics::displayAllScrolls);
    }

    @Test
    void displayAllScrolls_downloadNotFound_thenExit() {
        // download unknown scroll -> Enter to exit
        String input = "download\nunknown.pdf\n\n";
        AdminSystemAnalytics analytics = new AdminSystemAnalytics(
                new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))));
        assertDoesNotThrow(analytics::displayAllScrolls);
    }

    @Test
    void displayAllScrolls_invalidCommand_thenExit() {
        // invalid token -> extra Enter to consume -> Enter to exit
        String input = "bogus\n\n\n";
        AdminSystemAnalytics analytics = new AdminSystemAnalytics(
                new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))));
        assertDoesNotThrow(analytics::displayAllScrolls);
    }

    @Test
    void filterByFilename_downloadFlow_found_thenExit() {
        // match filename, choose download, provide filename, then Enter to exit
        String input = "alpha.pdf\ndownload\nalpha.pdf\n\n";
        AdminSystemAnalytics analytics = new AdminSystemAnalytics(
                new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))));
        assertDoesNotThrow(analytics::filterByFilename);
    }

    @Test
    void filterByFilename_noMatch_returns() {
        String input = "zzz\n";
        AdminSystemAnalytics analytics = new AdminSystemAnalytics(
                new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))));
        assertDoesNotThrow(analytics::filterByFilename);
    }

    @Test
    void filterByOwner_noMatch_returns() {
        String input = "nobody\n";
        AdminSystemAnalytics analytics = new AdminSystemAnalytics(
                new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))));
        assertDoesNotThrow(analytics::filterByOwner);
    }

    @Test
    void filterByDate_noMatch_returns() {
        String input = "1999-01-01\n";
        AdminSystemAnalytics analytics = new AdminSystemAnalytics(
                new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))));
        assertDoesNotThrow(analytics::filterByDate);
    }

    @Test
    void filterByCategory_noMatch_returns() {
        String input = "none\n";
        AdminSystemAnalytics analytics = new AdminSystemAnalytics(
                new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))));
        assertDoesNotThrow(analytics::filterByCategory);
    }

    @Test
    void displayAllScrolls_truncatesLongColumns_thenExit() throws Exception {
        // Overwrite with long fields to exercise truncate()
        String longOwner = "owner-" + "x".repeat(80);
        String longCat = "cat-" + "y".repeat(80);
        String longFile = "file-" + "z".repeat(80) + ".pdf";
        String longDate = "2025-10-29T12:00:00+11:00-extra-long-suffix";
        String data = """
                [
                  {
                    "filename": "%s",
                    "numberOfUploads": 5,
                    "numberOfDownloads": 9,
                    "categorizationId": "%s",
                    "ownerId": "%s",
                    "uploadDate": "%s"
                  }
                ]
                """.formatted(longFile, longCat, longOwner, longDate);
        Files.writeString(SCROLLS_JSON, data, StandardCharsets.UTF_8);

        // Just press Enter to exit table after render
        String input = "\n";
        AdminSystemAnalytics analytics = new AdminSystemAnalytics(
                new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))));
        assertDoesNotThrow(analytics::displayAllScrolls);
    }
}