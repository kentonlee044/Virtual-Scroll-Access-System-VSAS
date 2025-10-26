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

class ScrollReplacementCliTest {

    @Test
    void runReturnsImmediatelyWhenUserNull() {
        ScrollReplacementCli cli = new ScrollReplacementCli(new Scanner(new ByteArrayInputStream(new byte[0])), null);
        cli.run();
    }

    @Test
    void printBorderProducesFullWidthLine() throws Exception {
        String output = captureOut(() -> {
            Method method = accessStatic("printBorder", char.class, char.class, char.class);
            method.invoke(null, '┌', '─', '┐');
            return null;
        });
        assertEquals("┌" + "─".repeat(70) + "┐" + System.lineSeparator(), output);
    }

    @Test
    void centerRowTrimsAndCentersLongText() throws Exception {
        Method method = accessStatic("centerRow", String.class);
        String result = (String) method.invoke(null, "x".repeat(200));
        assertTrue(result.startsWith("│"));
        assertTrue(result.endsWith("│"));
        assertTrue(result.contains("…"));
        assertEquals(72, result.length());
    }

    @Test
    void rowPadsAndTruncatesContent() throws Exception {
        Method method = accessStatic("row", String.class);
        String result = (String) method.invoke(null, "y".repeat(200));
        assertTrue(result.startsWith("│"));
        assertTrue(result.endsWith("│"));
        assertTrue(result.contains("…"));
        assertEquals(72, result.length());
    }

    @Test
    void fieldLineFormatsLabelAndValue() throws Exception {
        Method method = accessStatic("fieldLine", String.class, String.class);
        String result = (String) method.invoke(null, "Label", "value");
        assertTrue(result.contains("Label"));
        assertTrue(result.contains("[value"));
        assertEquals(72, result.length());
    }

    @Test
    void padValueHandlesNullAndLength() throws Exception {
        Method method = accessStatic("padValue", String.class);
        String blank = (String) method.invoke(null, new Object[] { null });
        assertTrue(blank.isBlank());
        assertEquals(42, blank.length());

        String truncated = (String) method.invoke(null, "z".repeat(100));
        assertEquals(42, truncated.length());
        assertTrue(truncated.endsWith("…"));
    }

    @Test
    void safeReturnsPlaceholderForBlank() throws Exception {
        Method method = accessStatic("safe", String.class);
        assertEquals("—", method.invoke(null, new Object[] { null }));
        assertEquals("—", method.invoke(null, " "));
        assertEquals("value", method.invoke(null, "value"));
    }

    @Test
    void waitForEnterTrueWhenInputPresent() throws Exception {
        Method method = accessInstance("waitForEnter");
        ScrollReplacementCli cli = new ScrollReplacementCli(
                new Scanner(new ByteArrayInputStream("line\n".getBytes(StandardCharsets.UTF_8))), null);
        assertTrue((boolean) method.invoke(cli));
    }

    @Test
    void waitForEnterFalseWhenNoInput() throws Exception {
        Method method = accessInstance("waitForEnter");
        ScrollReplacementCli cli = new ScrollReplacementCli(
                new Scanner(new ByteArrayInputStream(new byte[0])), null);
        assertFalse((boolean) method.invoke(cli));
    }

    @Test
    void promptScrollSelectionReturnsValidIndex() throws Exception {
        Method method = accessInstance("promptScrollSelection", List.class);
        String input = "2\n";
        ScrollReplacementCli cli = new ScrollReplacementCli(
                new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))), null);
        int result = (int) method.invoke(cli, List.of(
                new Scroll("first.txt", "owner"),
                new Scroll("second.txt", "owner")));
        assertEquals(1, result);
    }

    @Test
    void promptScrollSelectionReturnsNegativeOnEmptyInput() throws Exception {
        Method method = accessInstance("promptScrollSelection", List.class);
        String input = "\n";
        ScrollReplacementCli cli = new ScrollReplacementCli(
                new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))), null);
        int result = (int) method.invoke(cli, List.of(new Scroll("only.txt", "owner")));
        assertEquals(-1, result);
    }

    @Test
    void promptNewBinaryPathReturnsNullOnEmpty() throws Exception {
        Method method = accessInstance("promptNewBinaryPath", Scroll.class);
        String input = "\n";
        ScrollReplacementCli cli = new ScrollReplacementCli(
                new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))), null);
        Path path = (Path) method.invoke(cli, new Scroll("file.txt", "owner"));
        assertNull(path);
    }

    @Test
    void promptNewBinaryPathAcceptsValidFile() throws Exception {
        Path tempFile = Files.createTempFile("scroll-test", ".bin");
        try {
            String input = "missing.file\n" + tempFile.toString() + "\n";
            ScrollReplacementCli cli = new ScrollReplacementCli(
                    new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))), null);
            Method method = accessInstance("promptNewBinaryPath", Scroll.class);
            Path result = (Path) method.invoke(cli, new Scroll("file.txt", "owner"));
            assertEquals(tempFile.toAbsolutePath().normalize(), result);
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    void ensureStorageDirCreatesDirectory() throws Exception {
        Method method = accessInstance("ensureStorageDir");
        ScrollReplacementCli cli = new ScrollReplacementCli(
                new Scanner(new ByteArrayInputStream(new byte[0])), null);
        method.invoke(cli);
        Path storageDir = Path.of("src/main/java/a3/t10/g09/data/scroll_files");
        assertTrue(Files.exists(storageDir));
    }

    @Test
    void renderScrollMenuOutputsEntries() throws Exception {
        Method method = accessInstance("renderScrollMenu", String.class, String.class, List.class);
        String output = captureOut(() -> {
            ScrollReplacementCli cli = new ScrollReplacementCli(
                    new Scanner(new ByteArrayInputStream(new byte[0])), null);
            method.invoke(cli, "Status", "Hint", List.of(
                    new Scroll("fileA.txt", "owner"),
                    new Scroll("fileB.txt", "owner")));
            return null;
        });
        assertTrue(output.contains("Replace Scroll Binary"));
        assertTrue(output.contains("fileA.txt"));
        assertTrue(output.contains("Status"));
    }

    @Test
    void renderFilePromptShowsFilenameAndPath() throws Exception {
        Method method = accessInstance("renderFilePrompt", String.class, String.class, String.class, String.class);
        String output = captureOut(() -> {
            ScrollReplacementCli cli = new ScrollReplacementCli(
                    new Scanner(new ByteArrayInputStream(new byte[0])), null);
            method.invoke(cli, "Provide file", "Hint", "scroll.txt", "/tmp/path");
            return null;
        });
        assertTrue(output.contains("Provide Replacement File"));
        assertTrue(output.contains("scroll.txt"));
        assertTrue(output.contains("/tmp/path"));
    }

    @Test
    void renderMessageDisplaysStatus() throws Exception {
        Method method = accessInstance("renderMessage", String.class, String.class);
        String output = captureOut(() -> {
            ScrollReplacementCli cli = new ScrollReplacementCli(
                    new Scanner(new ByteArrayInputStream(new byte[0])), null);
            method.invoke(cli, "No scrolls", "Press Enter");
            return null;
        });
        assertTrue(output.contains("Replace Scroll Binary"));
        assertTrue(output.contains("No scrolls"));
    }

    @Test
    void renderConfirmationShowsStoredPath() throws Exception {
        Method method = accessInstance("renderConfirmation", String.class, String.class, String.class);
        String output = captureOut(() -> {
            ScrollReplacementCli cli = new ScrollReplacementCli(
                    new Scanner(new ByteArrayInputStream(new byte[0])), null);
            method.invoke(cli, "Done", "scroll.txt", "/tmp/scroll.txt");
            return null;
        });
        assertTrue(output.contains("Replacement Complete"));
        assertTrue(output.contains("scroll.txt"));
        assertTrue(output.contains("Done"));
    }

    private static Method accessStatic(String name, Class<?>... parameterTypes) throws NoSuchMethodException {
        Method method = ScrollReplacementCli.class.getDeclaredMethod(name, parameterTypes);
        method.setAccessible(true);
        return method;
    }

    private static Method accessInstance(String name, Class<?>... parameterTypes) throws NoSuchMethodException {
        Method method = ScrollReplacementCli.class.getDeclaredMethod(name, parameterTypes);
        method.setAccessible(true);
        return method;
    }

    private static String captureOut(Callable<Void> action) throws Exception {
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