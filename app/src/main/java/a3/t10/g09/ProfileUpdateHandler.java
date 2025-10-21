package a3.t10.g09;

import a3.t10.g09.Registration.UserRegistration;
import a3.t10.g09.Validator.AtSymbolValidator;
import a3.t10.g09.Validator.DomainDotValidator;
import a3.t10.g09.Validator.PhoneLengthValidator;
import a3.t10.g09.Validator.PhoneDigitValidator;
import a3.t10.g09.Validator.NameValidator;
import a3.t10.g09.Validator.PasswordDigitValidator;
import a3.t10.g09.Validator.PasswordLengthValidator;
import a3.t10.g09.Validator.PasswordSpecialCharValidator;
import a3.t10.g09.Validator.PasswordUppercaseValidator;
import a3.t10.g09.Validator.IDKeyFormatValidator;
import a3.t10.g09.Validator.IDKeyUniqueValidator;
import org.mindrot.jbcrypt.BCrypt;

public class ProfileUpdateHandler {
    private final User sessionUser;
    private final User persistedUser;
    private final UserList userList;
    private final UserRegistration userRegistration;

    public ProfileUpdateHandler(User user, UserList userList) {
        this.sessionUser = user;
        this.userList = userList;
        this.userRegistration = new UserRegistration();
        this.persistedUser = resolvePersistedUser(user, userList);
    }

    private User resolvePersistedUser(User user, UserList list) {
        if (user == null) {
            return null;
        }
        if (list == null || list.getUsers() == null) {
            return user;
        }
        return list.getUsers().stream()
                .filter(u -> u.getIdkey().equals(user.getIdkey()))
                .findFirst()
                .orElse(user);
    }

    private User targetUser() {
        return persistedUser != null ? persistedUser : sessionUser;
    }

    private void persistAndSync() {
        if (userList != null) {
            userRegistration.saveUsers(userList);
        }
        if (sessionUser != null && targetUser() != null && sessionUser != targetUser()) {
            sessionUser.setIdkey(targetUser().getIdkey());
            sessionUser.setFullname(targetUser().getFullname());
            sessionUser.setPhone(targetUser().getPhone());
            sessionUser.setEmail(targetUser().getEmail());
            sessionUser.setPassword(targetUser().getPassword());
            sessionUser.setRole(targetUser().getRole());
        }
    }

    public String updatePhoneNumber(String newPhone) {
        PhoneLengthValidator lengthValidator = new PhoneLengthValidator();
        PhoneDigitValidator digitValidator = new PhoneDigitValidator();

        String error = lengthValidator.validate(newPhone);
        if (error != null)
            return error;

        error = digitValidator.validate(newPhone);
        if (error != null)
            return error;

        User target = targetUser();
        if (target == null) {
            return "Unable to locate user record.";
        }

        target.setPhone(newPhone);
        persistAndSync();
        return null;
    }

    public String updateEmail(String newEmail) {
        AtSymbolValidator atSymbolValidator = new AtSymbolValidator();
        DomainDotValidator domainDotValidator = new DomainDotValidator();

        String error = atSymbolValidator.validate(newEmail);
        if (error != null)
            return error;

        error = domainDotValidator.validate(newEmail);
        if (error != null)
            return error;

        User target = targetUser();
        if (target == null) {
            return "Unable to locate user record.";
        }

        target.setEmail(newEmail);
        persistAndSync();
        return null;
    }

    public String updateName(String newName) {
        NameValidator nameValidator = new NameValidator();
        String error = nameValidator.validate(newName);
        if (error != null)
            return error;

        User target = targetUser();
        if (target == null) {
            return "Unable to locate user record.";
        }

        target.setFullname(newName);
        persistAndSync();
        return null;
    }

    public String updateIDKey(String newIDKey) {
        IDKeyFormatValidator formatValidator = new IDKeyFormatValidator();
        IDKeyUniqueValidator uniqueValidator = new IDKeyUniqueValidator(this.userList, targetUser());

        String error = formatValidator.validate(newIDKey);
        if (error != null)
            return error;

        error = uniqueValidator.validate(newIDKey);
        if (error != null)
            return error;

        User target = targetUser();
        if (target == null) {
            return "Unable to locate user record.";
        }

        target.setIdkey(newIDKey);
        persistAndSync();
        return null;
    }

    public String updatePassword(String currentPassword, String newPassword) {
        User target = targetUser();
        if (target == null) {
            return "Unable to locate user record.";
        }

        if (!BCrypt.checkpw(currentPassword, target.getPassword())) {
            return "Current password is incorrect.";
        }

        PasswordDigitValidator digitValidator = new PasswordDigitValidator();
        PasswordLengthValidator lengthValidator = new PasswordLengthValidator();
        PasswordUppercaseValidator uppercaseValidator = new PasswordUppercaseValidator();
        PasswordSpecialCharValidator specialCharValidator = new PasswordSpecialCharValidator();

        String error = digitValidator.validate(newPassword);
        if (error != null)
            return error;

        error = lengthValidator.validate(newPassword);
        if (error != null)
            return error;

        error = uppercaseValidator.validate(newPassword);
        if (error != null)
            return error;

        error = specialCharValidator.validate(newPassword);
        if (error != null)
            return error;

        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
        target.setPassword(hashedPassword);
        persistAndSync();
        return null;
    }
}