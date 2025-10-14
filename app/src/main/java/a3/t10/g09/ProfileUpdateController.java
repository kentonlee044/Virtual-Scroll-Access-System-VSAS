package a3.t10.g09;

import java.util.Scanner;

public class ProfileUpdateController {
	private final ProfileUpdateHandler handler;
	private final Scanner scanner;
    private UserList users;

	public ProfileUpdateController(User user, UserList users) {
        this.users = users;
		this.handler = new ProfileUpdateHandler(user);
		this.scanner = new Scanner(System.in);
	}

	public void displayMenu() {
		System.out.println("Profile Update Menu:");
		System.out.println("1. Update Email");
		System.out.println("2. Update Phone Number");
		System.out.println("3. Update Name");
		System.out.println("4. Update ID Key");
		System.out.println("5. Update Password");
		System.out.print("Enter the number of the option you want to update: ");
		int choice = scanner.nextInt();
		switch (choice) {
			case 1:
				handler.updateEmail();
				break;
			case 2:
				handler.updatePhoneNumber();
				break;
			case 3:
				handler.updateName();
				break;
			case 4:
				handler.updateIDKey(users);
				break;
			case 5:
				handler.updatePassword();
				break;
			default:
				System.out.println("Invalid option. Please try again.");
		}
	}
}

