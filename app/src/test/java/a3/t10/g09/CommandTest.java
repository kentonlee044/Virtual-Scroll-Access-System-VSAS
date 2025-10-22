package a3.t10.g09;

import org.junit.jupiter.api.*;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class CommandTest {
    // @Test
    // public void testExecute() {
    // for (Command c : Command.values()) {
    // c.execute(new Scanner(System.in), new Client());
    // }
    // }

    @Test
    public void testIsAllowed() {
        for (Command c : Command.values()) {
            c.isAllowed(ClientStatus.ANONYMOUS);
        }
    }

    @Test
    public void testGetDescription() {
        for (Command c : Command.values()) {
            assertNotNull(c.getDescription());
        }
    }
}
