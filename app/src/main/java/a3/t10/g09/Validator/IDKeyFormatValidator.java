package a3.t10.g09.Validator;

public class IDKeyFormatValidator implements Validator {
    @Override
    public String validate(String input) {
        if (input == null) {
            return "ID key cannot be null.";
        }
        String idKey = input.toString();
        if (!idKey.matches("^[A-Za-z0-9]{6,12}$")) {
            return "ID key must be alphanumeric and 6-12 characters long.";
        }
        return null;
    }
}
