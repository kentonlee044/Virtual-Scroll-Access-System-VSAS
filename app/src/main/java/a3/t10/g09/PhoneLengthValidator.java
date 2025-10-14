package a3.t10.g09;

public class PhoneLengthValidator implements Validator {
    @Override
    public String validate(Object input) {
        if (input == null) {
            return "Phone number cannot be null.";
        }
        String phone = input.toString();
        if (phone.length() < 10 || phone.length() > 15) {
            return "Phone number must be between 10 and 15 digits.";
        }
        return null;
    }
}
