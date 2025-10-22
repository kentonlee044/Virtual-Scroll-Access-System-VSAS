package a3.t10.g09;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientTest {
    Client client;

    @BeforeEach
    public void setUp() {
        client = new Client();
    }
    // @Test
    // public void testGetters() {
    // assertEquals("N/A", client.getUsername());
    // assertEquals(ClientStatus.ANONYMOUS, client.getUserType());
    // client.getAvailableCommands();
    // client.getCurrentUser();
    // }
}
