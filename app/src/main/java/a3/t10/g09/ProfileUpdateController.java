package a3.t10.g09;

import java.util.Scanner;
import a3.t10.g09.Registration.UserRegistration;

public class ProfileUpdateController {
	private final Scanner scanner;
	private UserList users;
	private ProfileUpdateUI ui;

	public ProfileUpdateController(User user, UserList users) {
		this.users = users;
		this.ui = new ProfileUpdateUI(user, users);
		this.scanner = new Scanner(System.in);
	}

	public void printMenu(){
		System.out.println("┌────────────────────────────────────────────────────────┐");
		System.out.println("│   User Profile Update Menu                             │");
		System.out.println("├────────────────────────────────────────────────────────┤");
		System.out.println("│  1) Update Email                                       │");
		System.out.println("│  2) Update Phone Number                                │");
		System.out.println("│  3) Update Name                                        │");
		System.out.println("│  4) Update ID Key                                      │");
		System.out.println("│  5) Update Password                                    │");
		System.out.println("│  0) Exit                                               │");
		System.out.println("└────────────────────────────────────────────────────────┘");
		System.out.print("Select an option (0-5): ");
	}

	public void displayMenu() {
		printMenu();
		if (!scanner.hasNextLine()) {
			System.out.println("No input detected. Exiting profile update menu.");
			return;
		}
		String input = scanner.nextLine().trim();
		int choice;
		try {
			choice = Integer.parseInt(input);
		} catch (NumberFormatException e) {
			System.out.println("Invalid option. Please try again.");
			return;
		}
		switch (choice) {
			case 1:
				ui.updateEmail();
				break;
			case 2:
				ui.updatePhoneNumber();
				break;
			case 3:
				ui.updateName();
				break;
			case 4:
				ui.updateIDKey();
				break;
			case 5:
				ui.updatePassword();
				break;
			case 0:
				return;
			default:
				System.out.println("Invalid option. Please try again.");
		}
		//finally, save the updated user list to the JSON file
		UserRegistration registration = new UserRegistration();
		registration.saveUsers(users);
	}
}
