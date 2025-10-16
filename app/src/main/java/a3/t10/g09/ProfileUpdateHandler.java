package a3.t10.g09;

import a3.t10.g09.Registration.UserRegistration;
import a3.t10.g09.validator.AtSymbolValidator;
import a3.t10.g09.validator.DomainDotValidator;
import a3.t10.g09.validator.PhoneLengthValidator;
import a3.t10.g09.validator.PhoneDigitValidator;
import a3.t10.g09.validator.NameValidator;
import a3.t10.g09.validator.PasswordDigitValidator;
import a3.t10.g09.validator.PasswordLengthValidator;
import a3.t10.g09.validator.PasswordSpecialCharValidator;
import a3.t10.g09.validator.PasswordUppercaseValidator;
import a3.t10.g09.validator.IDKeyFormatValidator;
import a3.t10.g09.validator.IDKeyUniqueValidator;
import org.mindrot.jbcrypt.BCrypt;

public class ProfileUpdateHandler {
    private final User user;
    private final UserList userList;
    private final UserRegistration userRegistration;

    public ProfileUpdateHandler(User user, UserList userList) {
        this.user = user;
        this.userList = userList;
        this.userRegistration = new UserRegistration();
    }

    public String updatePhoneNumber(String newPhone) {
        PhoneLengthValidator lengthValidator = new PhoneLengthValidator();
        PhoneDigitValidator digitValidator = new PhoneDigitValidator();

        String error = lengthValidator.validate(newPhone);
        if (error != null) return error;
        
        error = digitValidator.validate(newPhone);
        if (error != null) return error;

        user.setPhone(newPhone);
        userRegistration.saveUsers(userList);
        return null; // Success
    }

    
    public String updateEmail(String newEmail) {
        AtSymbolValidator atSymbolValidator = new AtSymbolValidator();
        DomainDotValidator domainDotValidator = new DomainDotValidator();

        String error = atSymbolValidator.validate(newEmail);
        if (error != null) return error;
        
        error = domainDotValidator.validate(newEmail);
        if (error != null) return error;

        user.setEmail(newEmail);
        userRegistration.saveUsers(userList);
        return null; // Success
    }

    
    public String updateName(String newName) {
        NameValidator nameValidator = new NameValidator();
        String error = nameValidator.validate(newName);
        if (error != null) return error;
        
        user.setFullname(newName);
        userRegistration.saveUsers(userList);
        return null; // Success
    }

    
    public String updateIDKey(String newIDKey) {
        IDKeyFormatValidator formatValidator = new IDKeyFormatValidator();
        IDKeyUniqueValidator uniqueValidator = new IDKeyUniqueValidator(this.userList);

        String error = formatValidator.validate(newIDKey);
        if (error != null) return error;
        
        error = uniqueValidator.validate(newIDKey);
        if (error != null) return error;

        user.setIdkey(newIDKey);
        userRegistration.saveUsers(userList);
        return null; // Success
    }

    
    public String updatePassword(String currentPassword, String newPassword) {
        // Validate current password
        if (!BCrypt.checkpw(currentPassword, user.getPassword())) {
            return "Current password is incorrect.";
        }

        // Validate new password
        PasswordDigitValidator digitValidator = new PasswordDigitValidator();
        PasswordLengthValidator lengthValidator = new PasswordLengthValidator();
        PasswordUppercaseValidator uppercaseValidator = new PasswordUppercaseValidator();
        PasswordSpecialCharValidator specialCharValidator = new PasswordSpecialCharValidator();
        
        String error = digitValidator.validate(newPassword);
        if (error != null) return error;
        
        error = lengthValidator.validate(newPassword);
        if (error != null) return error;
        
        error = uppercaseValidator.validate(newPassword);
        if (error != null) return error;
        
        error = specialCharValidator.validate(newPassword);
        if (error != null) return error;

        // If all validations pass, update the password
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
        user.setPassword(hashedPassword);
        userRegistration.saveUsers(userList);
        return null; // Success
    }
}
