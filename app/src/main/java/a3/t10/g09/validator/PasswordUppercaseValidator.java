package a3.t10.g09.validator;

public class PasswordUppercaseValidator implements Validator {
    @Override
    public String validate(String password) {
        if (password == null) {
            return "Password cannot be null.";
        }
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return null;
            }
        }
        return "Password must contain at least one uppercase letter.";
    }
}
