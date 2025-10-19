package a3.t10.g09;

import org.junit.jupiter.api.*;

public class ProfileUpdateUITest {
    ProfileUpdateUI p = new ProfileUpdateUI(new User("a", "b", "c", "d", "e"), new UserList());

    @Test
    public void testUpdaters() {
        p.updatePhoneNumber();
        p.updateEmail();
        p.updateName();
        p.updateIDKey();
        p.updatePassword();
    }
}
