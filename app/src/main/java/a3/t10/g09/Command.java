package a3.t10.g09;

import java.util.EnumSet;
import java.util.Set;

public enum Command {
    VIEW_SCROLLS(EnumSet.allOf(ClientStatus.class), "View the scrolls in the database.") {
        @Override
        public void execute() {
            
        }
    },
    LOGIN(EnumSet.of(ClientStatus.ANONYMOUS), "Log in with a valid username and password.") {
        @Override
        public void execute() {
            // if successful, set clientStatus = ClientStatus.LOGGED_IN or ClientStatus.ADMIN
        }
    },
    SIGN_OUT(EnumSet.of(ClientStatus.LOGGED_IN, ClientStatus.ADMIN), "Sign out of the active profile.") {
        @Override
        public void execute() {
            
        }
    },
    CHANGE_DETAILS(EnumSet.of(ClientStatus.LOGGED_IN, ClientStatus.ADMIN), "Change the active profile's details including password") {
        @Override
        public void execute() {
            return;
        }
    },
    VIEW_USERS(EnumSet.of(ClientStatus.ADMIN), "View all users and their profiles") {
        @Override
        public void execute() {
            
        }
    },
    // maybe a separate command for creating a new admin account is needed?
    ADD_USER(EnumSet.of(ClientStatus.ADMIN), "Add a user to the system") {
        @Override
        public void execute() {
            
        }
    },
    REMOVE_USER(EnumSet.of(ClientStatus.ADMIN), "Remove a user from the system") {
        @Override
        public void execute() {
            
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

    public abstract void execute();
}