package a3.t10.g09.validator;

import a3.t10.g09.User;
import a3.t10.g09.UserList;

public class IDKeyUniqueValidator implements Validator {
    private final UserList userList;
    private final User currentUser;

    public IDKeyUniqueValidator(UserList userList) {
        this(userList, null);
    }

    public IDKeyUniqueValidator(UserList userList, User currentUser) {
        this.userList = userList;
        this.currentUser = currentUser;
    }

    @Override
    public String validate(String input) {
        if (input == null) {
            return "ID key cannot be null.";
        }
        if (userList == null || userList.getUsers() == null) {
            return null;
        }

        String idKey = input;
        boolean exists = userList.getUsers().stream()
                .filter(user -> currentUser == null || !user.getIdkey().equals(currentUser.getIdkey()))
                .anyMatch(user -> user.getIdkey().equals(idKey));

        return exists ? "ID key is already in use." : null;
    }
}