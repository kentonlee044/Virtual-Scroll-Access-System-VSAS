package a3.t10.g09;

public class NameValidator implements Validator {
    @Override
    public String validate(Object input) {
        if (input == null) {
            return "Name cannot be null.";
        }
        String name = input.toString().trim();
        if (name.isEmpty()) {
            return "Name cannot be empty.";
        }
        for (char c : name.toCharArray()) {
            if (!Character.isLetter(c) && c != ' ') {
                return "Name must contain only letters and spaces.";
            }
        }
        return null;
    }
}
