package a3.t10.g09;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;

import org.junit.jupiter.api.Test;

class ScrollRemoveTest {

    @Test
    void runReturnsImmediatelyWhenUserNull() {
        ScrollRemove remove = new ScrollRemove(new Scanner(new ByteArrayInputStream(new byte[0])), null);
        remove.run();
    }

    @Test
    void promptScrollSelectionReturnsIndexAfterValidInput() throws Exception {
        Method method = accessInstance("promptScrollSelection", List.class);
        String input = "2\n";
        ScrollRemove remove = new ScrollRemove(
                new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))), null);
        int index = (int) captureOut(() -> method.invoke(remove, List.of(
                new Scroll("one.txt", "owner"),
                new Scroll("two.txt", "owner"))));
        assertEquals(1, index);
    }

    @Test
    void promptScrollSelectionReturnsMinusOneOnEmptyInput() throws Exception {
        Method method = accessInstance("promptScrollSelection", List.class);
        ScrollRemove remove = new ScrollRemove(
                new Scanner(new ByteArrayInputStream("\n".getBytes(StandardCharsets.UTF_8))), null);
        int index = (int) captureOut(() -> method.invoke(remove, List.of(new Scroll("only.txt", "owner"))));
        assertEquals(-1, index);
    }

    @Test
    void promptScrollSelectionHandlesInvalidThenValid() throws Exception {
        Method method = accessInstance("promptScrollSelection", List.class);
        String input = "bad\n5\n1\n";
        ScrollRemove remove = new ScrollRemove(
                new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))), null);
        int index = (int) captureOut(() -> method.invoke(remove, List.of(
                new Scroll("first.txt", "owner"),
                new Scroll("second.txt", "owner"))));
        assertEquals(0, index);
    }

    @Test
    void ensureStorageDirCreatesDirectory() throws Exception {
        Method method = accessInstance("ensureStorageDir");
        ScrollRemove remove = new ScrollRemove(new Scanner(new ByteArrayInputStream(new byte[0])), null);
        method.invoke(remove);
        Path expected = Path.of("src/main/java/a3/t10/g09/data/scroll_files");
        assertTrue(Files.exists(expected));
    }

    @Test
    void renderScrollMenuOutputsFilenames() throws Exception {
        Method method = accessInstance("renderScrollMenu", String.class, String.class, List.class);
        String output = captureOutToString(() -> {
            ScrollRemove remove = new ScrollRemove(new Scanner(new ByteArrayInputStream(new byte[0])), null);
            method.invoke(remove, "Status", "Hint", List.of(
                    new Scroll("fileA.txt", "owner"),
                    new Scroll("fileB.txt", "owner")));
            return null;
        });
        assertTrue(output.contains("Remove Scroll Binary"));
        assertTrue(output.contains("fileA.txt"));
        assertTrue(output.contains("Status"));
    }

    @Test
    void renderMessageDisplaysText() throws Exception {
        Method method = accessInstance("renderMessage", String.class, String.class);
        String output = captureOutToString(() -> {
            ScrollRemove remove = new ScrollRemove(new Scanner(new ByteArrayInputStream(new byte[0])), null);
            method.invoke(remove, "No scrolls", "Press Enter");
            return null;
        });
        assertTrue(output.contains("Replace Scroll Binary"));
        assertTrue(output.contains("No scrolls"));
    }

    @Test
    void renderConfirmationShowsDetails() throws Exception {
        Method method = accessInstance("renderConfirmation", String.class, String.class, String.class);
        String output = captureOutToString(() -> {
            ScrollRemove remove = new ScrollRemove(new Scanner(new ByteArrayInputStream(new byte[0])), null);
            method.invoke(remove, "Done", "scroll.txt", "/tmp/scroll.txt");
            return null;
        });
        assertTrue(output.contains("Removal Complete"));
        assertTrue(output.contains("scroll.txt"));
        assertTrue(output.contains("Done"));
    }

    @Test
    void waitForEnterTrueWhenLinePresent() throws Exception {
        Method method = accessInstance("waitForEnter");
        ScrollRemove remove = new ScrollRemove(
                new Scanner(new ByteArrayInputStream("\n".getBytes(StandardCharsets.UTF_8))), null);
        assertTrue((boolean) method.invoke(remove));
    }

    @Test
    void waitForEnterFalseWhenNoLine() throws Exception {
        Method method = accessInstance("waitForEnter");
        ScrollRemove remove = new ScrollRemove(new Scanner(new ByteArrayInputStream(new byte[0])), null);
        assertFalse((boolean) method.invoke(remove));
    }

    // @Test
    // void printBorderProducesFullWidthLine() throws Exception {
    // String output = captureOutToString(() -> {
    // Method method = accessStatic("printBorder", char.class, char.class,
    // char.class);
    // method.invoke(null, '┌', '─', '┐');
    // return null;
    // });
    // assertEquals("┌" + "─".repeat(70) + "┐" + System.lineSeparator(), output);
    // }

    // @Test
    // void centerRowTruncatesAndCenters() throws Exception {
    // Method method = accessStatic("centerRow", String.class);
    // String result = (String) method.invoke(null, "x".repeat(200));
    // assertTrue(result.startsWith("│"));
    // assertTrue(result.endsWith("│"));
    // assertTrue(result.contains("…"));
    // assertEquals(72, result.length());
    // }

    // @Test
    // void rowTruncatesLongContent() throws Exception {
    // Method method = accessStatic("row", String.class);
    // String result = (String) method.invoke(null, "y".repeat(200));
    // assertTrue(result.startsWith("│"));
    // assertTrue(result.endsWith("│"));
    // assertTrue(result.contains("…"));
    // assertEquals(72, result.length());
    // }

    // @Test
    // void fieldLineFormatsLabelAndValue() throws Exception {
    // Method method = accessStatic("fieldLine", String.class, String.class);
    // String result = (String) method.invoke(null, "Label", "value");
    // assertTrue(result.contains("Label"));
    // assertTrue(result.contains("[value"));
    // assertEquals(72, result.length());
    // }

    // @Test
    // void padValueHandlesNullAndTruncation() throws Exception {
    // Method method = accessStatic("padValue", String.class);
    // String blank = (String) method.invoke(null, new Object[] { null });
    // assertTrue(blank.isBlank());
    // assertEquals(42, blank.length());

    // String truncated = (String) method.invoke(null, "z".repeat(100));
    // assertEquals(42, truncated.length());
    // assertTrue(truncated.endsWith("…"));
    // }

    @Test
    void safeReturnsDashForBlank() throws Exception {
        Method method = accessStatic("safe", String.class);
        assertEquals("—", method.invoke(null, new Object[] { null }));
        assertEquals("—", method.invoke(null, " "));
        assertEquals("value", method.invoke(null, "value"));
    }

    private static Method accessStatic(String name, Class<?>... parameterTypes) throws NoSuchMethodException {
        Method method = ScrollRemove.class.getDeclaredMethod(name, parameterTypes);
        method.setAccessible(true);
        return method;
    }

    private static Method accessInstance(String name, Class<?>... parameterTypes) throws NoSuchMethodException {
        Method method = ScrollRemove.class.getDeclaredMethod(name, parameterTypes);
        method.setAccessible(true);
        return method;
    }

    private static Object captureOut(Callable<Object> action) throws Exception {
        PrintStream original = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        try {
            return action.call();
        } finally {
            System.setOut(original);
        }
    }

    private static String captureOutToString(Callable<Void> action) throws Exception {
        PrintStream original = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        try {
            action.call();
        } finally {
            System.setOut(original);
        }
        return baos.toString(StandardCharsets.UTF_8);
    }
}