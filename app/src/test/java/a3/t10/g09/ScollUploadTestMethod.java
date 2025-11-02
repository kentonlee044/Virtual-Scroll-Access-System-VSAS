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

class ScollUploadTestMethod {

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
    void run_invalidThenValid_inputs_cover_loops() throws Exception {
        Path tmp = Files.createTempFile("upload-cover-", ".pdf");
        try {
            String input = "\n" + // empty filename -> error branch
                    "/definitely/not/here.pdf\n" + // not found -> error branch
                    tmp.toAbsolutePath() + "\n" + // valid file -> proceed
                    "\n" + // empty categorization -> error branch
                    "cat-001\n" + // valid categorization
                    "\n" + // confirm review
                    "\n"; // acknowledge success
            ScrollUpload upload = new ScrollUpload(
                    new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))),
                    "owner-cov");
            assertDoesNotThrow(upload::run);
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    @Test
    void replaceExisting_success_then_emptyNewName_then_valid_covers_more_branches() throws Exception {
        // Seed one owned scroll so owner and old filename checks pass
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

        String input = "owner-abc\n" + // owner id (ownedScrolls not empty)
                "old.pdf\n" + // old filename match
                "\n" + // new filename empty -> error branch
                "new.pdf\n" + // new filename valid + unique
                "\n" + // confirm review
                "\n"; // acknowledge message
        ScrollUpload upload = new ScrollUpload(
                new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))),
                "owner-abc");
        assertDoesNotThrow(upload::replaceExisting);
    }
}