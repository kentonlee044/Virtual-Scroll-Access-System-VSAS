package a3.t10.g09.Validator;

import a3.t10.g09.Scroll;
import a3.t10.g09.ScrollList;

public class ScrollCategorizationIdUniqueValidator implements Validator {
    private final ScrollList scrolls;

    public ScrollCategorizationIdUniqueValidator(ScrollList scrolls) {
        this.scrolls = scrolls;
    }

    @Override
    public String validate(String input) {
        if (input == null || input.trim().isEmpty()) {
            // Optional field: empty is allowed and treated as no-categorization
            return null;
        }
        String candidate = input.trim();
        for (Scroll scroll : scrolls.getAllScrolls()) {
            String existing = scroll.getCategorizationId();
            if (existing != null && existing.equals(candidate)) {
                return "Categorization ID already exists. Please choose a different one.";
            }
        }
        return null;
    }
}
