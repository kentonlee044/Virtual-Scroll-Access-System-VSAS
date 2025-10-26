package a3.t10.g09;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScrollListTest {

    private ScrollList list;

    @BeforeEach
    void setUp() {
        list = new ScrollList();
    }

    @Test
    void addAndGetScrollWorks() {
        Scroll scroll = new Scroll("file.txt", 2, 3, "cat-1", "ownerA");
        list.addScroll(scroll);
        assertEquals(scroll, list.getScroll("file.txt"));
        assertNull(list.getScroll("missing.txt"));
        assertEquals(1, list.getScrollCount());
    }

    @Test
    void replaceExistingScrollUpdatesNameAndResetsCounts() {
        Scroll scroll = new Scroll("old.txt", 5, 7, "cat-2", "ownerB");
        list.addScroll(scroll);
        assertTrue(list.replaceExistingScroll("old.txt", "new.txt", "ownerB"));
        Scroll updated = list.getScroll("new.txt");
        assertNotNull(updated);
        assertEquals("new.txt", updated.getFilename());
        assertEquals(0, updated.getNumberOfDownloads());
        assertEquals(0, updated.getNumberOfUploads());
        assertFalse(list.replaceExistingScroll("missing.txt", "noop.txt", "ownerB"));
    }

    @Test
    void getAllScrollsReturnsCopy() {
        list.addScroll(new Scroll("a.txt", "owner"));
        List<Scroll> snapshot = list.getAllScrolls();
        snapshot.clear();
        assertEquals(1, list.getScrollCount());
    }

    @Test
    void getScrollsByOwnerFiltersCorrectly() {
        list.addScroll(new Scroll("a.txt", "owner1"));
        list.addScroll(new Scroll("b.txt", "owner2"));
        list.addScroll(new Scroll("c.txt", "owner1"));
        List<Scroll> owner1 = list.getScrollsByOwner("owner1");
        assertEquals(2, owner1.size());
        assertTrue(owner1.stream().allMatch(s -> s.isOwnedBy("owner1")));
        assertTrue(list.getScrollsByOwner("unknown").isEmpty());
    }

    @Test
    void removeScrollRemovesMatchingEntries() {
        list.addScroll(new Scroll("keep.txt", "owner"));
        list.addScroll(new Scroll("remove.txt", "owner"));
        assertTrue(list.removeScroll("remove.txt"));
        assertNull(list.getScroll("remove.txt"));
        assertFalse(list.removeScroll("missing.txt"));
        assertEquals(1, list.getScrollCount());
    }

    @Test
    void totalsAggregateUploadsAndDownloads() {
        list.addScroll(new Scroll("one.txt", 1, 2, "cat", "owner"));
        list.addScroll(new Scroll("two.txt", 3, 4, "cat", "owner"));
        assertEquals(2, list.getScrollCount());
        assertEquals(4, list.getTotalUploads());
        assertEquals(6, list.getTotalDownloads());
    }

    @Test
    void getPopularScrollsReturnsThresholdMatches() {
        list.addScroll(new Scroll("low.txt", 0, 1, "cat", "owner"));
        list.addScroll(new Scroll("high.txt", 0, 5, "cat", "owner"));
        List<Scroll> popular = list.getPopularScrolls(3);
        assertEquals(1, popular.size());
        assertEquals("high.txt", popular.get(0).getFilename());
    }

    @Test
    void clearRemovesAllScrolls() {
        list.addScroll(new Scroll("a.txt", "owner"));
        list.addScroll(new Scroll("b.txt", "owner"));
        list.clear();
        assertEquals(0, list.getScrollCount());
        assertTrue(list.getAllScrolls().isEmpty());
    }
}
