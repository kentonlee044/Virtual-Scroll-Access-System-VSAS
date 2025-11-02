package a3.t10.g09;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.Callable;

import org.junit.jupiter.api.Test;

public class ScrollUploadTest {

    @Test
    public void firstErrorReturnsNullWhenAllBlank() throws Exception {
        Method method = ScrollUpload.class.getDeclaredMethod("firstError", String[].class);
        method.setAccessible(true);
        String result = (String) method.invoke(null, new Object[] { new String[] { null, "   ", "" } });
        assertNull(result);
    }

    @Test
    public void firstErrorReturnsFirstNonBlank() throws Exception {
        Method method = ScrollUpload.class.getDeclaredMethod("firstError", String[].class);
        method.setAccessible(true);
        String result = (String) method.invoke(null, new Object[] { new String[] { "", "error message", "later" } });
        assertEquals("error message", result);
    }

    // @Test
    // public void padValueTruncatesAndAddsEllipsis() throws Exception {
    // Method method = ScrollUpload.class.getDeclaredMethod("padValue",
    // String.class);
    // method.setAccessible(true);
    // String longValue =
    // "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    // String result = (String) method.invoke(null, longValue);
    // assertEquals(42, result.length());
    // assertTrue(result.endsWith("…"));
    // }

    // @Test
    // public void padValueHandlesNull() throws Exception {
    // Method method = accessMethod("padValue", String.class);
    // String result = (String) method.invoke(null, new Object[] { null });
    // assertEquals(42, result.length());
    // assertTrue(result.isBlank());
    // }

    // @Test
    // public void centerRowTruncatesLongContent() throws Exception {
    // Method method = accessMethod("centerRow", String.class);
    // String result = (String) method.invoke(null, "x".repeat(100));
    // assertTrue(result.contains("…"));
    // assertTrue(result.startsWith("│"));
    // assertTrue(result.endsWith("│"));
    // assertEquals(72, result.length());
    // }

    // @Test
    // public void rowTruncatesLongContent() throws Exception {
    // Method method = accessMethod("row", String.class);
    // String result = (String) method.invoke(null, "y".repeat(100));
    // assertTrue(result.contains("…"));
    // assertTrue(result.startsWith("│"));
    // assertTrue(result.endsWith("│"));
    // assertEquals(72, result.length());
    // }

    // @Test
    // public void fieldLineFormatsLabelAndValue() throws Exception {
    // Method method = accessMethod("fieldLine", String.class, String.class);
    // String result = (String) method.invoke(null, "Name", "value");
    // assertTrue(result.contains("Name"));
    // assertTrue(result.contains("[value"));
    // assertEquals(72, result.length());
    // }

    // @Test
    // public void printBorderPrintsFullWidthLine() throws Exception {
    // String output = captureOut(() -> {
    // Method method = accessMethod("printBorder", char.class, char.class,
    // char.class);
    // method.invoke(null, '┌', '─', '┐');
    // return null;
    // });
    // String expected = "┌" + "─".repeat(70) + "┐" + System.lineSeparator();
    // assertEquals(expected, output);
    // }

    @Test
    public void renderFormUploadOutputsKeySections() throws Exception {
        String output = captureOut(() -> {
            Method method = accessMethod("renderFormUpload", String.class, String.class, String.class, String.class);
            method.invoke(null, "Ready", "Hint", "file.txt", "cat42");
            return null;
        });
        assertTrue(output.contains("Upload New Scroll"));
        assertTrue(output.contains("Filename"));
        assertTrue(output.contains("Categorization ID"));
    }

    @Test
    public void renderFormReplaceOutputsKeySections() throws Exception {
        String output = captureOut(() -> {
            Method method = accessMethod("renderFormReplace", String.class, String.class, String.class, String.class,
                    String.class);
            method.invoke(null, "Ready", "Hint", "owner", "old.txt", "new.txt");
            return null;
        });
        assertTrue(output.contains("Replace Existing Scroll"));
        assertTrue(output.contains("Old Filename"));
        assertTrue(output.contains("New Filename"));
    }

    @Test
    public void waitForLineReturnsTrueWhenInputAvailable() throws Exception {
        Method method = ScrollUpload.class.getDeclaredMethod("waitForLine");
        method.setAccessible(true);
        ScrollUpload upload = new ScrollUpload(
                new Scanner(new ByteArrayInputStream("line\n".getBytes(StandardCharsets.UTF_8))),
                "owner");
        boolean result = (boolean) method.invoke(upload);
        assertTrue(result);
    }

    @Test
    public void waitForLineReturnsFalseWhenNoInput() throws Exception {
        Method method = ScrollUpload.class.getDeclaredMethod("waitForLine");
        method.setAccessible(true);
        ScrollUpload upload = new ScrollUpload(
                new Scanner(new ByteArrayInputStream(new byte[0])),
                "owner");
        boolean result = (boolean) method.invoke(upload);
        assertFalse(result);
    }

    @Test
    public void runExitsWhenNoInputProvided() throws Exception {
        ScrollUpload upload = new ScrollUpload(
                new Scanner(new ByteArrayInputStream(new byte[0])),
                "owner");
        String output = captureOut(() -> {
            upload.run();
            return null;
        });
        assertTrue(output.contains("No input detected. Exiting."));
    }

    @Test
    public void replaceExistingExitsWhenNoInputProvided() throws Exception {
        ScrollUpload upload = new ScrollUpload(
                new Scanner(new ByteArrayInputStream(new byte[0])),
                "owner");
        String output = captureOut(() -> {
            upload.replaceExisting();
            return null;
        });
        assertTrue(output.contains("No input detected. Exiting."));
    }

    private static Method accessMethod(String name, Class<?>... parameterTypes) throws NoSuchMethodException {
        Method method = ScrollUpload.class.getDeclaredMethod(name, parameterTypes);
        method.setAccessible(true);
        return method;
    }

    private static String captureOut(Callable<Void> action) throws Exception {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        try {
            action.call();
        } finally {
            System.setOut(originalOut);
        }
        return baos.toString(StandardCharsets.UTF_8);
    }
}