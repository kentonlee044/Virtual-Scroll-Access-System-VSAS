package a3.t10.g09.validator;

public class PhoneDigitValidator implements Validator {
    @Override
    public String validate(String phone) {
        if (phone == null) {
            return "Phone number cannot be null.";
        }
        for (char c : phone.toCharArray()) {
            if (!Character.isDigit(c)) {
                return "Phone number must contain only digits.";
            }
        }
        return null;
    }
}
