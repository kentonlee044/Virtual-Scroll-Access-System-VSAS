package a3.t10.g09.validator;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DomainDotValidatorTest{
    
    private DomainDotValidator validator;
    @BeforeEach
    void setup(){
        validator = new DomainDotValidator();
    }

    @Test   
    public void validate_validTest(){
        String expected = null;
        String actual = validator.validate("testing@gmail.com");
        assertTrue(expected == actual);
    }

    @Test
    public void validate_invalidTest_noDot(){
        String expected = "Domain part must contain '.' and it cannot be at the start or end.";
        String actual = validator.validate("testing@gmailcom");
        assertTrue(expected.equals(actual));
    }

    @Test
    public void validate_nullTest(){
        String expected = "Email cannot be null.";
        String actual = validator.validate(null);
        assertTrue(expected.equals(actual));
    }

    @Test
    public void validate_noAtTest(){
        String expected = "Email must contain '@'.";
        String actual = validator.validate("testinggmail.com");
        assertTrue(expected.equals(actual));
    }
}