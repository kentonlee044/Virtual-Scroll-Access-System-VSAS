package a3.t10.g09.Validator;

public class DomainDotValidator implements Validator {
    @Override
    public String validate(String email) {
        if (email == null) {
            return "Email cannot be null.";
        }
        int atIndex = email.indexOf('@');
        if (atIndex == -1) {
            return "Email must contain '@'.";
        }
        String domain = email.substring(atIndex + 1);
        int dotIndex = domain.indexOf('.');
        if (dotIndex <= 0 || dotIndex == domain.length() - 1) {
            return "Domain part must contain '.' and it cannot be at the start or end.";
        }
        return null;
    }
}
