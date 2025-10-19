package a3.t10.g09.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AtSymbolValidatorTest{
    
    private AtSymbolValidator validator;

    @BeforeEach
    public void setup(){
        validator = new AtSymbolValidator();
    }

    @Test
    public void testValidate(){
        String expected = null;
        String actual = validator.validate("testing@gmail.com");
        assertEquals(expected, actual);
    }

    @Test
    public void testValidate_atSymbolAtStart(){
        String expected = "Email must contain '@' and it cannot be at the start or end.";
        String actual = validator.validate("@gmail.com");
        assertEquals(expected, actual);
    }

    @Test
    public void testValidate_nullEmail(){
        String expected = "Email cannot be null.";
        String actual = validator.validate(null);
        assertEquals(expected, actual);
    }
}