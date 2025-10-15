package a3.t10.g09.validator;

public class PasswordLengthValidator implements Validator {
    @Override
    public String validate(String password) {
        if (password == null) {
            return "Password cannot be null.";
        }
        if (password.length() < 8) {
            return "Password must be at least 8 characters long.";
        }
        return null;
    }
}
