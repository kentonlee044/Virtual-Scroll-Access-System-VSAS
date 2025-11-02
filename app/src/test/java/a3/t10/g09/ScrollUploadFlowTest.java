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

class ScrollUploadFlowTest {

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
        // Default to an empty list for uniqueness checks
        Files.writeString(SCROLLS_JSON, "[]", StandardCharsets.UTF_8);
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
    void run_happyPath_withExistingFile_andValidCategory() throws Exception {
        // Create a real temp file so the file existence check passes
        Path tmp = Files.createTempFile("upload-test-", ".pdf");
        try {
            String input = tmp.toAbsolutePath() + "\n" // filename (absolute path)
                    + "catA\n" // categorization ID
                    + "\n" // confirm review
                    + "\n"; // acknowledge success
            ScrollUpload upload = new ScrollUpload(
                    new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))),
                    "owner-xyz");
            assertDoesNotThrow(upload::run);
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    @Test
    void run_fileNotFound_thenExitGracefully() {
        // Provide a non-existent file path, then end input to trigger graceful exit
        String input = "/definitely/not/there.pdf\n";
        ScrollUpload upload = new ScrollUpload(
                new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))),
                "owner-xyz");
        assertDoesNotThrow(upload::run);
    }

    @Test
    void replaceExisting_ownerNotFound_thenExitGracefully() {
        // No scrolls for provided owner -> loop once, then end input => exits
        // gracefully
        String input = "unknown-owner\n";
        ScrollUpload upload = new ScrollUpload(
                new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))),
                "owner-xyz");
        assertDoesNotThrow(upload::replaceExisting);
    }

    @Test
    void replaceExisting_ownerFound_wrongOldFilename_thenExitGracefully() throws Exception {
        // Seed scrolls.json with one owned scroll so owner step succeeds
        String data = """
                [
                  {
                    "filename": "old.pdf",
                    "numberOfUploads": 1,
                    "numberOfDownloads": 0,
                    "categorizationId": "catA",
                    "ownerId": "owner-abc",
                    "uploadDate": "2025-10-29T12:00:00"
                  }
                ]
                """;
        Files.writeString(SCROLLS_JSON, data, StandardCharsets.UTF_8);

        // Provide matching owner, then a wrong old filename, then terminate input to
        // exit
        String input = "owner-abc\nnope.pdf\n";
        ScrollUpload upload = new ScrollUpload(
                new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))),
                "owner-abc");
        assertDoesNotThrow(upload::replaceExisting);
    }
}