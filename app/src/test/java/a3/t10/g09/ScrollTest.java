package a3.t10.g09;

import a3.t10.g09.Registration.UserRegistration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScrollTest {
    Scroll scroll;
    @BeforeEach
    public void setUp() {
        scroll = new Scroll("freddo", 2, 3, "123");
    }

    @Test
    public void testGetters() {
        assertEquals("freddo", scroll.getFilename());
        assertEquals(2, scroll.getNumberOfUploads());
        assertEquals(3, scroll.getNumberOfDownloads());
        assertEquals("123", scroll.getCategorizationId());
    }

    @Test
    public void testSetters() {
        scroll.setFilename("frog");
        assertEquals("frog", scroll.getFilename());
        scroll.incrementUploads();
        assertEquals(3, scroll.getNumberOfUploads());
        scroll.incrementDownloads();
        assertEquals(4, scroll.getNumberOfDownloads());
        scroll.setCategorizationId("456");
        assertEquals("456", scroll.getCategorizationId());
    }
}
