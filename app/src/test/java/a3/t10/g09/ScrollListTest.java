package a3.t10.g09;
import org.junit.jupiter.api.*;

public class ScrollListTest {
    ScrollList s = new ScrollList();

    @Test
    public void testSetters() {
        s.addScroll(new Scroll("a", 2, 3, "b"));
        s.removeScroll("blah");
    }

    @Test
    public void testGetters() {
        s.getScroll("c");
        s.getAllScrolls();
        s.getScrollCount();
        s.getTotalDownloads();
        s.getTotalUploads();
        s.getPopularScrolls(1);
    }

    @Test
    public void testClear() {
        s.clear();
    }
}
