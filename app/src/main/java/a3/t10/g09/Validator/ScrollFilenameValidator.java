package a3.t10.g09.Validator;

public class ScrollFilenameValidator implements Validator {
    private static final int MAX_LENGTH = 100;
    private static final String INVALID_CHARS = "[\\/*?:<>|]+";
    
    @Override
    public String validate(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "Filename cannot be empty.";
        }
        
        if (input.length() > MAX_LENGTH) {
            return String.format("Filename cannot exceed %d characters.", MAX_LENGTH);
        }
        
        if (input.matches(".*" + INVALID_CHARS + ".*")) {
            return "Filename contains invalid characters: \\ / * ? : " + 
                   "< > | are not allowed.";
        }
        
        if (input.trim().equals(input)) {
            return null;
        }
        
        return "Filename cannot start or end with whitespace.";
    }
}
