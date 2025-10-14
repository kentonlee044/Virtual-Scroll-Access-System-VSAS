package a3.t10.g09.validator;

import a3.t10.g09.Validator;
public class PhoneDigitValidator implements Validator {
    @Override
    public String validate(Object input) {
        if (input == null) {
            return "Phone number cannot be null.";
        }
        String phone = input.toString();
        for (char c : phone.toCharArray()) {
            if (!Character.isDigit(c)) {
                return "Phone number must contain only digits.";
            }
        }
        return null;
    }
}
