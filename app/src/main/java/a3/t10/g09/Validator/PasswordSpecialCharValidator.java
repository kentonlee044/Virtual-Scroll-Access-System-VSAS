package a3.t10.g09.Validator;

import java.util.regex.Pattern;

public class PasswordSpecialCharValidator implements Validator {
    private static final Pattern SPECIAL =
            Pattern.compile(".*[!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>/?`~|\\\\].*");

    @Override
    public String validate(String input) {
        if (input == null || input.isBlank()) {
            return "Password cannot be empty.";
        }
        return SPECIAL.matcher(input).matches()
                ? null
                : "Password must contain at least one special character.";
    }
}