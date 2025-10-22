package a3.t10.g09.Validator;

public class PhoneLengthValidator implements Validator {
    @Override
    public String validate(String phone) {
        if (phone == null) {
            return "Phone number cannot be null.";
        }
        if (phone.length() < 10 || phone.length() > 15) {
            return "Phone number must be between 10 and 15 digits.";
        }
        return null;
    }
}
