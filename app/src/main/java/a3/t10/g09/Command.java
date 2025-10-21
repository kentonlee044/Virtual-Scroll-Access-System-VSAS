package a3.t10.g09;

import a3.t10.g09.Login.LoginCli;
import a3.t10.g09.Registration.RegisterCli;

import java.util.EnumSet;
import java.util.Set;
import java.util.Scanner;

public enum Command {

    LOGIN(EnumSet.of(ClientStatus.ANONYMOUS), "Log in") {
        @Override
        public void execute(Scanner scanner) {
            // if successful, set clientStatus = ClientStatus.LOGGED_IN or ClientStatus.ADMIN
            User loggedIn = new LoginCli(scanner).run();
            if (loggedIn != null) {
                new CommandMenu(scanner).runFor(loggedIn);
            }
        }
    },
    REGISTER(EnumSet.of(ClientStatus.ANONYMOUS), "Register") {
        @Override
        public void execute(Scanner scanner) {
            new RegisterCli(scanner).run();
        }
    },
    VIEW_SCROLLS(EnumSet.allOf(ClientStatus.class), "View the scrolls in the database.") {
        @Override
        public void execute(Scanner scanner) {
            new AdminSystemAnalytics().displayAllScrolls();
        }
    },
    SIGN_OUT(EnumSet.of(ClientStatus.LOGGED_IN, ClientStatus.ADMIN), "Sign out of the active profile.") {
        @Override
        public void execute(Scanner scanner) {
            
        }
    },
    CHANGE_DETAILS(EnumSet.of(ClientStatus.LOGGED_IN, ClientStatus.ADMIN), "Change the active profile's details including password") {
        @Override
        public void execute(Scanner scanner) {
            return;
        }
    },
    VIEW_USERS(EnumSet.of(ClientStatus.ADMIN), "View all users and their profiles") {
        @Override
        public void execute(Scanner scanner) {
            
        }
    },
    // maybe a separate command for creating a new admin account is needed?
    ADD_USER(EnumSet.of(ClientStatus.ADMIN), "Add a user to the system") {
        @Override
        public void execute(Scanner scanner) {
            
        }
    },
    REMOVE_USER(EnumSet.of(ClientStatus.ADMIN), "Remove a user from the system") {
        @Override
        public void execute(Scanner scanner) {
            
        }
    },
    EXIT(EnumSet.allOf(ClientStatus.class), "Exit") {
        @Override
        public void execute(Scanner scanner) {
            System.out.println("Goodbye.");
          System.exit(0);
        }
    }
    ;
    private final Set<ClientStatus> allowedUsers;
    private final String description;


    Command(Set<ClientStatus> allowedUsers, String description) {
        this.allowedUsers = allowedUsers;
        this.description = description;
    }

    public boolean isAllowed(ClientStatus ClientStatus) {
        return this.allowedUsers.contains(ClientStatus);
    }

    public String getDescription() {
        return this.description;
    }

    public abstract void execute(Scanner scanner);
}