package a3.t10.g09;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ClientCoverageTest {

    @Test
    void defaultsAndAnonymousCommands() {
        Client client = new Client();

        // Default username and type when no user
        assertEquals("N/A", client.getUsername());
        assertEquals(ClientStatus.ANONYMOUS, client.getStatus());
        assertEquals("Anonymous", client.getUserType());

        // Available commands returned are all allowed for the current status
        Command[] cmds = client.getAvailableCommands();
        assertNotNull(cmds);
        for (Command c : cmds) {
            assertTrue(c.isAllowed(client.getStatus()));
        }

        // getCurrentUser is null by default
        assertNull(client.getCurrentUser());
    }

    @Test
    void setStatusAndCommandsPerRole() {
        Client client = new Client();

        client.setStatus(ClientStatus.GENERIC_USER);
        assertEquals(ClientStatus.GENERIC_USER, client.getStatus());
        assertEquals("Generic User", client.getUserType());
        for (Command c : client.getAvailableCommands()) {
            assertTrue(c.isAllowed(client.getStatus()));
        }

        client.setStatus(ClientStatus.ADMIN);
        assertEquals(ClientStatus.ADMIN, client.getStatus());
        assertEquals("Administrator", client.getUserType());
        for (Command c : client.getAvailableCommands()) {
            assertTrue(c.isAllowed(client.getStatus()));
        }
    }

    @Test
    void setUserRemoveUserAndGetUsernameBranches() throws Exception {
        Client client = new Client();

        // getUsername when no user
        assertEquals("N/A", client.getUsername());

        // Create a User instance regardless of constructor signature and set idkey
        Object user = newUserWithId("user123");
        client.setUser((User) user);

        assertNotNull(client.getCurrentUser());
        assertEquals("user123", client.getUsername());

        // Remove user path
        client.removeUser();
        assertNull(client.getCurrentUser());
        assertEquals("N/A", client.getUsername());
    }

    // Helper: construct a User instance even if constructor signature differs, then
    // set idkey field
    private static Object newUserWithId(String id) throws Exception {
        Class<?> userClass = Class.forName("a3.t10.g09.User");
        // Try to create with any available constructor using Strings
        Object instance = null;
        for (Constructor<?> ctor : userClass.getDeclaredConstructors()) {
            Class<?>[] types = ctor.getParameterTypes();
            Object[] args = Arrays.stream(types)
                    .map(t -> t == String.class ? "x" : defaultFor(t))
                    .toArray();
            try {
                ctor.setAccessible(true);
                instance = ctor.newInstance(args);
                break;
            } catch (ReflectiveOperationException ignored) {
            }
        }
        if (instance == null) {
            // As a last resort try no-args
            instance = userClass.getDeclaredConstructor().newInstance();
        }
        // Set private field idkey
        Field f = userClass.getDeclaredField("idkey");
        f.setAccessible(true);
        f.set(instance, id);
        return instance;
    }

    private static Object defaultFor(Class<?> t) {
        if (!t.isPrimitive())
            return null;
        if (t == boolean.class)
            return false;
        if (t == byte.class)
            return (byte) 0;
        if (t == short.class)
            return (short) 0;
        if (t == int.class)
            return 0;
        if (t == long.class)
            return 0L;
        if (t == float.class)
            return 0f;
        if (t == double.class)
            return 0d;
        if (t == char.class)
            return '\0';
        return null;
    }
}