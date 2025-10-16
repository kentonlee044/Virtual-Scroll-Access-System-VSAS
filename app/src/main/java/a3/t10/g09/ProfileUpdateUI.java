package a3.t10.g09;

import java.util.Scanner;

public class ProfileUpdateUI {
    private final Scanner scanner = new Scanner(System.in);
    private final ProfileUpdateHandler updateHandler;
    private final User user;
    private final UserList userList;

    public ProfileUpdateUI(User user, UserList userList) {
        this.user = user;
        this.userList = userList;
        this.updateHandler = new ProfileUpdateHandler(user, userList);
    }

    
    private String displayBox(String title, String prompt, String currentValue, boolean showError) {
        // Create a line of dashes for the box border
        String border = "+" + "-".repeat(38) + "+";
        
        System.out.println("\n" + border);
        
        // Center the title
        int padding = (36 - title.length()) / 2;
        String centeredTitle = " ".repeat(Math.max(0, padding)) + title + " ".repeat(Math.max(0, 36 - title.length() - padding));
        System.out.println("| " + centeredTitle + " |");
        System.out.println(border);
        
        // Display current value if provided
        if (currentValue != null && !currentValue.isEmpty()) {
            System.out.println("| Current: " + currentValue + " ".repeat(28 - currentValue.length()) + " |");
            System.out.println(border);
        }
        
        // Show prompt and get input
        System.out.print("| " + prompt + " ");
        String input = scanner.nextLine().trim();
        
        // Close the box
        System.out.println(border);
        
        return input;
    }

    public void updatePhoneNumber() {
        String errorMessage = null;
        String currentPhone = user.getPhone();
        
        do {
            if (errorMessage != null) {
                System.out.println("\u001B[31m" + errorMessage + "\u001B[0m");
            }
            
            String newPhone = displayBox("UPDATE PHONE NUMBER", "Enter new phone number:", currentPhone, errorMessage != null);
            
            // Call the handler and get the result
            errorMessage = updateHandler.updatePhoneNumber(newPhone);
            
            if (errorMessage == null) {
                System.out.println("\nPhone number updated successfully.");
                return;
            }
        } while (true);
    }

    public void updateEmail() {
        String errorMessage = null;
        String currentEmail = user.getEmail();
        
        do {
            if (errorMessage != null) {
                System.out.println("\u001B[31m" + errorMessage + "\u001B[0m");
            }
            
            String newEmail = displayBox("UPDATE EMAIL", "Enter new email:", currentEmail, errorMessage != null);
            
            // Call the handler and get the result
            errorMessage = updateHandler.updateEmail(newEmail);
            
            if (errorMessage == null) {
                System.out.println("\nEmail updated successfully.");
                return;
            }
        } while (true);
    }

    public void updateName() {
        String errorMessage = null;
        String currentName = user.getFullname();
        
        do {
            if (errorMessage != null) {
                System.out.println("\u001B[31m" + errorMessage + "\u001B[0m");
            }
            
            String newName = displayBox("UPDATE NAME", "Enter new name:", currentName, errorMessage != null);
            
            // Call the handler and get the result
            errorMessage = updateHandler.updateName(newName);
            
            if (errorMessage == null) {
                System.out.println("\nName updated successfully.");
                return;
            }
        } while (true);
    }

    public void updateIDKey() {
        String errorMessage = null;
        String currentIDKey = user.getIdkey();
        
        do {
            if (errorMessage != null) {
                System.out.println("\u001B[31m" + errorMessage + "\u001B[0m");
            }
            
            String newIDKey = displayBox("UPDATE ID KEY", "Enter new ID key:", currentIDKey, errorMessage != null);
            
            // Call the handler and get the result
            String result = updateHandler.updateIDKey(newIDKey);
            
            if (result == null) {
                System.out.println("\nID key updated successfully.");
                return;
            }

            errorMessage = result;
        } while (true);
    }

    public void updatePassword() {
        String errorMessage = null;
        
        do {
            if (errorMessage != null) {
                System.out.println("\u001B[31m" + errorMessage + "\u001B[0m");
            }
            
            // First, get current password
            String currentPassword = displayBox("UPDATE PASSWORD", "Enter current password:", "", errorMessage != null && errorMessage.startsWith("Current password"));
            
            // If we have an error and it's not about the current password, continue to new password input
            if (errorMessage != null && !errorMessage.startsWith("Current password")) {
                // Get the new password (only once)
                String newPassword = displayBox("UPDATE PASSWORD", "Enter new password:", "", true);
                
                // Call the handler with current and new password (no confirm password)
                errorMessage = updateHandler.updatePassword(currentPassword, newPassword);
                
                if (errorMessage == null) {
                    System.out.println("\nPassword updated successfully.");
                    return;
                }
                continue;
            }
            
            // Get new password
            String newPassword = displayBox("UPDATE PASSWORD", "Enter new password:", "", errorMessage != null && !errorMessage.startsWith("Current password"));
            
            // Show error message if any (for new password validation)
            if (errorMessage != null && !errorMessage.startsWith("Current password")) {
                System.out.println("\u001B[31m" + errorMessage + "\u001B[0m");
                
                // Get confirmation again
                String confirmPassword = displayBox("UPDATE PASSWORD", "Confirm new password:", "", false);
                
                // Check if passwords match
                if (!newPassword.equals(confirmPassword)) {
                    errorMessage = "Error: Passwords do not match";
                    continue;
                }
            } else {
                // Get confirmation if no error yet
                String confirmPassword = displayBox("UPDATE PASSWORD", "Confirm new password:", "", false);
                
                // Check if passwords match
                if (!newPassword.equals(confirmPassword)) {
                    errorMessage = "Error: Passwords do not match";
                    continue;
                }
            }
            
            // Call the handler and get the result
            errorMessage = updateHandler.updatePassword(currentPassword, newPassword);
            
            if (errorMessage == null) {
                System.out.println("\nPassword updated successfully.");
                return;
            }
        } while (true);
    }
}
