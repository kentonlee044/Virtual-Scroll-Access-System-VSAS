package a3.t10.g09;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import a3.t10.g09.Login.LoginCli;

class LoginCliTest {

    @Test
    void promptIdKeySkipsBlankInput() throws Exception {
        Scanner scanner = new Scanner(new ByteArrayInputStream("\n\nuser123\n".getBytes(StandardCharsets.UTF_8)));
        LoginCli cli = new LoginCli(scanner);
        Method method = LoginCli.class.getDeclaredMethod("promptIdKey");
        method.setAccessible(true);

        final Object[] resultHolder = new Object[1];
        captureStdOut(() -> resultHolder[0] = invoke(method, cli));

        assertEquals("user123", resultHolder[0]);
    }

    @Test
    void promptIdKeyReturnsNullWhenNoInput() throws Exception {
        Scanner scanner = new Scanner(new ByteArrayInputStream(new byte[0]));
        LoginCli cli = new LoginCli(scanner);
        Method method = LoginCli.class.getDeclaredMethod("promptIdKey");
        method.setAccessible(true);

        final Object[] resultHolder = new Object[1];
        captureStdOut(() -> resultHolder[0] = invoke(method, cli));

        assertNull(resultHolder[0]);
    }

    // @Test
    // void promptPasswordUsesScannerWhenConsoleMissing() throws Exception {
    // LoginCli cli = newCli("\n\nsecret\n");
    // Method method = LoginCli.class.getDeclaredMethod("promptPassword",
    // java.io.Console.class, String.class);
    // method.setAccessible(true);

    // final Object[] resultHolder = new Object[1];
    // captureStdOut(() -> resultHolder[0] = invoke(method, cli, null, "user123"));

    // assertEquals("secret", resultHolder[0]);
    // }

    @Test
    void promptPasswordReturnsNullWhenInputMissing() throws Exception {
        LoginCli cli = newCli("");
        Method method = LoginCli.class.getDeclaredMethod("promptPassword", java.io.Console.class, String.class);
        method.setAccessible(true);

        final Object[] resultHolder = new Object[1];
        captureStdOut(() -> resultHolder[0] = invoke(method, cli, null, "user123"));

        assertNull(resultHolder[0]);
    }

    @Test
    void waitForEnterConsumesOneLine() throws Exception {
        Scanner scanner = new Scanner(new ByteArrayInputStream("first\nsecond\n".getBytes(StandardCharsets.UTF_8)));
        LoginCli cli = new LoginCli(scanner);
        Method method = LoginCli.class.getDeclaredMethod("waitForEnter");
        method.setAccessible(true);

        captureStdOut(() -> invoke(method, cli));
        assertEquals("second", scanner.nextLine());
    }

    @Test
    void renderFormOutputsStatusAndHint() throws Exception {
        LoginCli cli = newCli("");
        Method method = LoginCli.class.getDeclaredMethod("renderForm", String.class, String.class, String.class,
                String.class);
        method.setAccessible(true);

        String output = captureStdOut(() -> invoke(method, cli, "Status message", "Hint text", "user123", "secret"));

        assertTrue(output.contains("Login"));
        assertTrue(output.contains("Status message"));
        assertTrue(output.contains("Hint text"));
        assertTrue(output.contains("user123"));
        assertTrue(output.contains("*"));
    }

    @Test
    void printBorderProducesFullWidthLine() throws Exception {
        LoginCli cli = newCli("");
        Method method = LoginCli.class.getDeclaredMethod("printBorder", char.class, char.class, char.class);
        method.setAccessible(true);

        String output = captureStdOut(() -> invoke(method, cli, '┌', '─', '┐'));

        assertEquals("┌" + "─".repeat(70) + "┐" + System.lineSeparator(), output);
    }

    @Test
    void centerRowTruncatesAndCenters() throws Exception {
        LoginCli cli = newCli("");
        Method method = LoginCli.class.getDeclaredMethod("centerRow", String.class);
        method.setAccessible(true);

        String result = (String) invoke(method, cli, "x".repeat(200));

        assertTrue(result.startsWith("│"));
        assertTrue(result.endsWith("│"));
        assertEquals(72, result.length());
        assertTrue(result.contains("…"));
    }

    @Test
    void rowTruncatesLongContent() throws Exception {
        LoginCli cli = newCli("");
        Method method = LoginCli.class.getDeclaredMethod("row", String.class);
        method.setAccessible(true);

        String result = (String) invoke(method, cli, "y".repeat(200));

        assertTrue(result.startsWith("│"));
        assertTrue(result.endsWith("│"));
        assertEquals(72, result.length());
        assertTrue(result.contains("…"));
    }

    @Test
    void fieldLineMasksPasswordWhenRequested() throws Exception {
        LoginCli cli = newCli("");
        Method method = LoginCli.class.getDeclaredMethod("fieldLine", String.class, String.class, boolean.class);
        method.setAccessible(true);

        String result = (String) invoke(method, cli, "Password", "secret", true);

        assertTrue(result.startsWith("│"));
        assertTrue(result.contains("Password"));
        assertTrue(result.contains("*"));
        assertEquals(72, result.length());
    }

    @Test
    void padValueHandlesMaskingAndTruncation() throws Exception {
        LoginCli cli = newCli("");
        Method method = LoginCli.class.getDeclaredMethod("padValue", String.class, boolean.class);
        method.setAccessible(true);

        String masked = (String) invoke(method, cli, "secret", true);
        assertTrue(masked.startsWith("******"));
        assertEquals(42, masked.length());

        String truncated = (String) invoke(method, cli, "z".repeat(100), false);
        assertTrue(truncated.endsWith("…"));
        assertEquals(42, truncated.length());

        String blanks = (String) invoke(method, cli, null, false);
        assertTrue(blanks.trim().isEmpty());
        assertEquals(42, blanks.length());
    }

    private LoginCli newCli(String input) {
        return new LoginCli(new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))));
    }

    private Object invoke(Method method, LoginCli cli, Object... args) throws Exception {
        try {
            return method.invoke(cli, args);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof Exception) {
                throw (Exception) cause;
            }
            throw new RuntimeException(cause);
        }
    }

    private String captureStdOut(ThrowingRunnable action) throws Exception {
        PrintStream original = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        try {
            action.run();
        } finally {
            System.setOut(original);
        }
        return new String(baos.toByteArray(), StandardCharsets.UTF_8);
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }
}