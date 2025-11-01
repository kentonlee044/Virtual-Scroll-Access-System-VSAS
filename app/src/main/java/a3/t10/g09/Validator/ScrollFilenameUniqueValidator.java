package a3.t10.g09.Validator;

import a3.t10.g09.Scroll;
import a3.t10.g09.ScrollList;

public class ScrollFilenameUniqueValidator implements Validator {
    private final ScrollList scrolls;

    public ScrollFilenameUniqueValidator(ScrollList scrolls) {
        this.scrolls = scrolls;
    }

    @Override
    public String validate(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "Filename is required.";
        }
        String candidate = input.trim();
        // Only check among active scrolls
        for (Scroll scroll : scrolls.getAllScrolls()) {
            if (scroll.getFilename() != null && scroll.getFilename().equalsIgnoreCase(candidate)) {
                return "A scroll with this filename already exists.";
            }
        }
        return null;
    }
}
