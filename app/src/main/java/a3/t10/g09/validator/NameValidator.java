package a3.t10.g09.validator;

public class NameValidator implements Validator {
    @Override
    public String validate(String name) {
        if (name == null) {
            return "Name cannot be null.";
        }
        name = name.trim();
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
