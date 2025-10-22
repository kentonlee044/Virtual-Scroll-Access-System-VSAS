package a3.t10.g09;
import java.util.*;

public class Client {
    User currentUser;
    ClientStatus clientStatus;    

    Client() {
        clientStatus = ClientStatus.ANONYMOUS;
        currentUser = null;
    }

    public String getUsername() {
        if (currentUser == null) {
            return "N/A";
        }
        return currentUser.getIdkey();
    }

    public String getUserType() {
        switch(clientStatus) {
            case ANONYMOUS:
                return "Anonymous";
            case GENERIC_USER:
                return "Generic User"; // a.k.a a generic, logged-in user.
            case ADMIN:
                return "Administrator";
        }
        throw new IllegalArgumentException("Client's Status is not one of the prescribed status types. Check that Client.getUserType() evaluates all possible client statuses");
    }
    
    public Command[] getAvailableCommands() {
        ArrayList<Command> available = new ArrayList<>();
        for (Command cmd : Command.values()) {
            if (cmd.isAllowed(this.clientStatus)) {
                available.add(cmd);
            }
        }
        Command[] returnArray = available.toArray(new Command[0]);

        return returnArray;
    }


    public void setUser(User user) { this.currentUser = user;}
    public void removeUser() { this.currentUser = null;}
    public void setStatus(ClientStatus status) { this.clientStatus = status;}

    public User getCurrentUser() { return this.currentUser;}
    public ClientStatus getStatus() { return this.clientStatus;}
}