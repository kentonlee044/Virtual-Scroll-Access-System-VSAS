package a3.t10.g09.validator;

public class PasswordDigitValidator implements Validator {
    @Override
    public String validate(String password) {
        if (password == null) {
            return "Password cannot be null.";
        }
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                return null;
            }
        }
        return "Password must contain at least one digit.";
    }
}
