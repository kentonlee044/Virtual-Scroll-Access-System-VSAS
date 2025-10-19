package a3.t10.g09.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class IDKeyFormatValidatorTest {
    
    private IDKeyFormatValidator validator;
    @BeforeEach
    public void setUp() {
        validator = new IDKeyFormatValidator();
    }

    @Test
    public void testValidIDKey() {
        String result = validator.validate("Abc123");
        assertNull(result);
    }

    @Test
    public void testInvalidIDKeyTooShort() {
        String result = validator.validate("Abc1");
        assertEquals("ID key must be alphanumeric and 6-12 characters long.", result);
    }
    @Test
    public void testnullIDKey() {
        String result = validator.validate(null);
        assertEquals("ID key cannot be null.", result);
    }
}