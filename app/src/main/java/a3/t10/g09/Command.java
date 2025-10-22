package a3.t10.g09;

import a3.t10.g09.Login.LoginCli;
import a3.t10.g09.Registration.RegisterCli;

import java.util.EnumSet;
import java.util.Set;
import java.util.Scanner;

public enum Command {

    LOGIN(EnumSet.of(ClientStatus.ANONYMOUS), "Log in") {
        @Override
        public void execute(Scanner scanner, Client client) {
            // if successful, set clientStatus = ClientStatus.GENERIC_USER or ClientStatus.ADMIN
            User loggedIn = new LoginCli(scanner).run();
            if (loggedIn != null) {
                switch(loggedIn.getRole()) {
                    case "admin":
                        client.setStatus(ClientStatus.ADMIN);
                        break;
                    case "user":
                        client.setStatus(ClientStatus.GENERIC_USER);
                        break;
                }
                client.setUser(loggedIn);
            }
        }
    },
    REGISTER(EnumSet.of(ClientStatus.ANONYMOUS), "Register") {
        @Override
        public void execute(Scanner scanner, Client client) {
            new RegisterCli(scanner).run();
        }
    },
    VIEW_SCROLLS(EnumSet.allOf(ClientStatus.class), "View the scrolls in the database.") {
        @Override
        public void execute(Scanner scanner, Client client) {
            new AdminSystemAnalytics().displayAllScrolls();
        }
    },
    LOG_OUT(EnumSet.of(ClientStatus.GENERIC_USER, ClientStatus.ADMIN), "Log out") {
        @Override
        public void execute(Scanner scanner, Client client) {
            client.setStatus(ClientStatus.ANONYMOUS);
            client.removeUser();
        }
    },
    CHANGE_DETAILS(EnumSet.of(ClientStatus.GENERIC_USER, ClientStatus.ADMIN), "Update user profile") {
        @Override
        public void execute(Scanner scanner, Client client) {
            new CommandMenu(scanner).launchProfileUpdater(client.getCurrentUser());
        }
    },
    MANAGE_USERS(EnumSet.of(ClientStatus.ADMIN), "Manage Users") {
        @Override
        public void execute(Scanner scanner, Client client) {
            new AdminUserManagement(scanner).run(client.getCurrentUser());
        }
    },
//    // maybe a separate command for creating a new admin account is needed?
//    ADD_USER(EnumSet.of(ClientStatus.ADMIN), "Add a user to the system") {
//        @Override
//        public void execute(Scanner scanner, Client client) {
//
//        }
//    },
//    REMOVE_USER(EnumSet.of(ClientStatus.ADMIN), "Remove a user from the system") {
//        @Override
//        public void execute(Scanner scanner, Client client) {
//
//        }
//    },
    EXIT(EnumSet.allOf(ClientStatus.class), "Exit") {
        @Override
        public void execute(Scanner scanner, Client client) {
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

    public abstract void execute(Scanner scanner, Client client);
}