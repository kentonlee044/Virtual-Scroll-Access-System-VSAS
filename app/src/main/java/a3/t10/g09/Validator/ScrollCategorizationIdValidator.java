package a3.t10.g09.Validator;

public class ScrollCategorizationIdValidator implements Validator {
    private static final int MAX_LENGTH = 20;
    private static final String ALLOWED_CHARS = "[A-Za-z0-9_-]+";
    
    @Override
    public String validate(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        
        if (input.length() > MAX_LENGTH) {
            return String.format("Categorization ID cannot exceed %d characters.", MAX_LENGTH);
        }
        
        if (!input.matches(ALLOWED_CHARS)) {
            return "Categorization ID can only contain letters, numbers, hyphens, and underscores.";
        }
        
        return null;
    }
}
