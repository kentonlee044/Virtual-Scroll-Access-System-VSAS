package a3.t10.g09;

import java.util.EnumSet;
import java.util.Scanner;
import java.util.Set;

import a3.t10.g09.Login.LoginCli;
import a3.t10.g09.Registration.RegisterCli;

public enum Command {

    LOGIN(EnumSet.of(ClientStatus.ANONYMOUS), "Log in") {
        @Override
        public void execute(Scanner scanner, Client client) {
            // if successful, set clientStatus = ClientStatus.GENERIC_USER or
            // ClientStatus.ADMIN
            User loggedIn = new LoginCli(scanner).run();
            if (loggedIn != null) {
                switch (loggedIn.getRole()) {
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
    FILTER_SCROLLS(EnumSet.allOf(ClientStatus.class), "Filter scrolls") {
        @Override
        public void execute(Scanner scanner, Client client) {
            new ManageScrollFiltering(scanner).run();
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
            SubMenuUIHandler.launchUpdateHandler(scanner, client.getCurrentUser());
        }
    },
    MANAGE_USERS(EnumSet.of(ClientStatus.ADMIN), "Manage Users") {
        @Override
        public void execute(Scanner scanner, Client client) {
            new AdminUserManagement(scanner).run(client.getCurrentUser());
        }
    },
    NEW_SCROLL(EnumSet.of(ClientStatus.GENERIC_USER, ClientStatus.ADMIN), "Upload a new scroll") {
        @Override
        public void execute(Scanner scanner, Client client) {
            new ScrollUpload(scanner, client.getCurrentUser().getIdkey()).run();
        }
    },
    UPLOAD_REPLACEMENT_SCROLL_content(EnumSet.of(ClientStatus.GENERIC_USER, ClientStatus.ADMIN),
            "Update existing scroll content") {
        @Override
        public void execute(Scanner scanner, Client client) {
            User current = client.getCurrentUser();
            if (current == null) {
                System.out.println("No user session detected. Please log in first.");
                return;
            }
            new ScrollReplacementCli(scanner, current).run();
        }
    },
    UPLOAD_REPLACEMENT_SCROLL(EnumSet.of(ClientStatus.GENERIC_USER, ClientStatus.ADMIN),
            "Update existing scroll name") {
        @Override
        public void execute(Scanner scanner, Client client) {
            new ScrollUpload(scanner, client.getCurrentUser().getIdkey()).replaceExisting();
        }
    },
    REMOVE_SCROLL(EnumSet.of(ClientStatus.GENERIC_USER, ClientStatus.ADMIN), "Remove a scroll") {
        @Override
        public void execute(Scanner scanner, Client client) {
            new ScrollRemove(scanner, client.getCurrentUser()).run();
        }
    },
    EXIT(EnumSet.allOf(ClientStatus.class), "Exit") {
        @Override
        public void execute(Scanner scanner, Client client) {
            System.out.println("Goodbye.");
            System.exit(0);
        }
    };

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