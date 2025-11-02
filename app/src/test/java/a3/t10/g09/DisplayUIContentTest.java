package a3.t10.g09;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class DisplayUIContentTest {

    @Test
    void centerRow_handlesNormalAndLongText() {
        String s1 = DisplayUIContent.centerRow("Hello");
        assertNotNull(s1);
        assertTrue(s1.startsWith("│") && s1.endsWith("│"));

        // Long text triggers truncation branch with ellipsis
        String longText = "X".repeat(200);
        String s2 = DisplayUIContent.centerRow(longText);
        assertNotNull(s2);
        assertTrue(s2.contains("…"));
        assertTrue(s2.startsWith("│") && s2.endsWith("│"));
    }

    @Test
    void row_handlesNullAndLongText() {
        String s1 = DisplayUIContent.row(null);
        assertNotNull(s1);
        assertTrue(s1.startsWith("│") && s1.endsWith("│"));

        String s2 = DisplayUIContent.row("Y".repeat(200));
        assertNotNull(s2);
        assertTrue(s2.contains("…"));
        assertTrue(s2.startsWith("│") && s2.endsWith("│"));
    }

    @Test
    void fieldLine_usesPadValueAndRowFormatting() {
        // Long value triggers padValue truncation
        String value = "Z".repeat(200);
        String line = DisplayUIContent.fieldLine("Label", value);
        assertNotNull(line);
        assertTrue(line.contains("Label"));
        assertTrue(line.contains("…"));
        assertTrue(line.startsWith("│") && line.endsWith("│"));
    }

    @Test
    void printBorder_printsExpectedFrame() {
        PrintStream original = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        try {
            DisplayUIContent.printBorder('┌', '─', '┐');
        } finally {
            System.setOut(original);
        }
        String printed = out.toString();
        assertTrue(printed.startsWith("┌"));
        assertTrue(printed.trim().endsWith("┐"));
    }
}