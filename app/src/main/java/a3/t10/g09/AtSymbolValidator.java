package a3.t10.g09;

public class AtSymbolValidator implements Validator {
    @Override
    public String validate(Object input) {
        if (input == null) {
            return "Email cannot be null.";
        }
        String email = input.toString();
        int atIndex = email.indexOf('@');
        if (atIndex <= 0 || atIndex == email.length() - 1) {
            return "Email must contain '@' and it cannot be at the start or end.";
        }
        return null;
    }
}
