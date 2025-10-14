package a3.t10.g09.validator;
import a3.t10.g09.UserList;

public class IDKeyUniqueValidator implements Validator {
    private final UserList userList;

    public IDKeyUniqueValidator(UserList userList) {
        this.userList = userList;
    }

    @Override
    public String validate(String input) {
        if (input == null) {
            return "ID key cannot be null.";
        }
        String idKey = input.toString();
        if (userList.getUsers().stream().anyMatch(user -> user.getIdkey().equals(idKey))) {
            return "ID key is already in use.";
        }
        return null;
    }
}
