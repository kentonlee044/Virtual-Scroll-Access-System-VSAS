package a3.t10.g09;

import org.junit.jupiter.api.Test;

public class ProfileUpdateControllerTest {
    ProfileUpdateController p = new ProfileUpdateController(new User("a", "b", "c", "d", "e"), new UserList());
    @Test
    public void testPrintMenu() {
        p.printMenu();
    }

}
