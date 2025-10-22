package a3.t10.g09;

import a3.t10.g09.Login.UserLogin;

import java.util.Scanner;

public final class SubMenuUIHandler {
    private SubMenuUIHandler(){}

    public static void launchUpdateHandler(Scanner scanner, User user) {
        UserList userList = new UserLogin(scanner).getUserData();
        ProfileUpdateController controller = new ProfileUpdateController(user, userList);
        controller.displayMenu();
    }

}
