package a3.t10.g09;

import java.util.ArrayList;
import java.util.List;

public class UserList {
    private List<User> users;

    public UserList(){
        users = new ArrayList<User>();
    }

    public void addUser(User user){
        users.add(user);
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
