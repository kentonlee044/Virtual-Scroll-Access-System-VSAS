package a3.t10.g09.validator;

import a3.t10.g09.UserList;

public class EmailUniqueValidator implements Validator {
    private final UserList userList;

    public EmailUniqueValidator(UserList userList) {
        this.userList = userList;
    }

    @Override
    public String validate(String email) {
        if (email == null || email.isBlank()) {
            return "Email cannot be empty.";
        }
        boolean exists = userList.getUsers().stream()
                .anyMatch(u -> email.equalsIgnoreCase(u.getEmail()));
        return exists ? "Email address already registered." : null;
    }
}