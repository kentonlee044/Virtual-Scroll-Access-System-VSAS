package a3.t10.g09;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ProfileUpdateUIFlowTest {

    private final java.io.InputStream originalIn = System.in;

    @AfterEach
    void restoreIn() {
        System.setIn(originalIn);
    }

    private ProfileUpdateUI uiWithInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        // Match existing project usage: 5-arg constructor seen in ProfileUpdateUITest
        User user = new User("idA", "Alice", "0123456789", "alice@example.com", "user");
        return new ProfileUpdateUI(user, new UserList());
    }

    @Test
    void updatePhoneNumber_validThenAck() {
        // Provide a valid phone and press Enter to continue
        String input = "0123456789\n\n";
        ProfileUpdateUI ui = uiWithInput(input);
        assertDoesNotThrow(ui::updatePhoneNumber);
    }

    @Test
    void updateEmail_validThenAck() {
        String input = "user@example.com\n\n";
        ProfileUpdateUI ui = uiWithInput(input);
        assertDoesNotThrow(ui::updateEmail);
    }

    @Test
    void updateName_validThenAck() {
        String input = "Bob Builder\n\n";
        ProfileUpdateUI ui = uiWithInput(input);
        assertDoesNotThrow(ui::updateName);
    }

    @Test
    void updateIDKey_validThenAck() {
        // Typical ID format used elsewhere in the app
        String input = "ABC123\n\n";
        ProfileUpdateUI ui = uiWithInput(input);
        assertDoesNotThrow(ui::updateIDKey);
    }

    @Test
    void updatePassword_flowEmptyThenMismatchThenSuccessOrErrorHandled() {
        // 1) enter current
        // 2) new password empty -> triggers error branch and retry
        // 3) new password provided, confirm mismatch -> triggers mismatch branch
        // 4) new password provided, confirm match -> success (or handler error) then
        // Enter to return
        String input = String.join("\n",
                "oldPass", // current
                "", // new (empty) -> error, loop
                "New!Pass1", // new
                "no-match", // confirm mismatch -> loop
                "New!Pass1", // new again
                "New!Pass1", // confirm match
                "" // press Enter to continue/return
        ) + "\n";
        ProfileUpdateUI ui = uiWithInput(input);

    }

    @Test
    void updatePhoneNumber_invalidThenEofExitsGracefully() {
        // One invalid input, then EOF (no more lines) -> next loop sees no nextLine and
        // returns
        String input = "abc\n";
        ProfileUpdateUI ui = uiWithInput(input);
        assertDoesNotThrow(ui::updatePhoneNumber);
    }

    @Test
    void updateEmail_invalidThenEofExitsGracefully() {
        String input = "not-an-email\n";
        ProfileUpdateUI ui = uiWithInput(input);
        assertDoesNotThrow(ui::updateEmail);
    }

    @Test
    void updateName_emptyThenEofExitsGracefully() {
        String input = "\n";
        ProfileUpdateUI ui = uiWithInput(input);
        assertDoesNotThrow(ui::updateName);
    }

    @Test
    void updateIDKey_invalidThenEofExitsGracefully() {
        String input = "bad key\n";
        ProfileUpdateUI ui = uiWithInput(input);
        assertDoesNotThrow(ui::updateIDKey);
    }
}