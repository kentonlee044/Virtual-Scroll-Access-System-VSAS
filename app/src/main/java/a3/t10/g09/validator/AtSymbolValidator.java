package a3.t10.g09.validator;

public class AtSymbolValidator implements Validator {
    @Override
    public String validate(String email) {
        if (email == null) {
            return "Email cannot be null.";
        }
        int atIndex = email.indexOf('@');
        if (atIndex <= 0 || atIndex == email.length() - 1) {
            return "Email must contain '@' and it cannot be at the start or end.";
        }
        return null;
    }
}
