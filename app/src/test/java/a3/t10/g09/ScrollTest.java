package a3.t10.g09;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ScrollTest {

    @Test
    void primaryConstructorInitializesDefaults() {
        Scroll scroll = new Scroll("file.txt", "owner1");
        assertEquals("file.txt", scroll.getFilename());
        assertEquals(0, scroll.getNumberOfUploads());
        assertEquals(0, scroll.getNumberOfDownloads());
        assertEquals("", scroll.getCategorizationId());
        assertEquals("owner1", scroll.getOwnerId());
    }

    @Test
    void fullConstructorStoresProvidedValues() {
        Scroll scroll = new Scroll("report.pdf", 2, 3, "cat-42", "owner2");
        assertEquals("report.pdf", scroll.getFilename());
        assertEquals(2, scroll.getNumberOfUploads());
        assertEquals(3, scroll.getNumberOfDownloads());
        assertEquals("cat-42", scroll.getCategorizationId());
        assertEquals("owner2", scroll.getOwnerId());
    }

    @Test
    void mutatorsUpdateCountsAndMetadata() {
        Scroll scroll = new Scroll("notes.docx", "owner3");
        scroll.incrementUploads();
        scroll.incrementUploads();
        scroll.incrementDownloads();
        assertEquals(2, scroll.getNumberOfUploads());
        assertEquals(1, scroll.getNumberOfDownloads());

        scroll.resetUploads();
        scroll.resetDownloads();
        assertEquals(0, scroll.getNumberOfUploads());
        assertEquals(0, scroll.getNumberOfDownloads());

        scroll.setFilename("updated.docx");
        scroll.setCategorizationId("cat-77");
        scroll.setOwnerId("owner4");
        assertEquals("updated.docx", scroll.getFilename());
        assertEquals("cat-77", scroll.getCategorizationId());
        assertEquals("owner4", scroll.getOwnerId());
    }

    @Test
    void ownershipChecksRespectNullAndExactMatch() {
        Scroll scroll = new Scroll("data.csv", "initial");
        assertTrue(scroll.isOwnedBy("initial"));
        assertFalse(scroll.isOwnedBy("other"));

        scroll.setOwnerId(null);
        assertFalse(scroll.isOwnedBy("initial"));

        scroll.setOwnerId("final");
        assertTrue(scroll.isOwnedBy("final"));
    }
}