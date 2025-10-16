package a3.t10.g09.Registration;

import org.mindrot.jbcrypt.BCrypt;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import a3.t10.g09.User;
import a3.t10.g09.UserList;
import a3.t10.g09.validator.AtSymbolValidator;
import a3.t10.g09.validator.DomainDotValidator;
import a3.t10.g09.validator.EmailUniqueValidator;
import a3.t10.g09.validator.IDKeyFormatValidator;
import a3.t10.g09.validator.IDKeyUniqueValidator;
import a3.t10.g09.validator.NameValidator;
import a3.t10.g09.validator.PasswordLengthValidator;
import a3.t10.g09.validator.PasswordSpecialCharValidator;
import a3.t10.g09.validator.PhoneDigitValidator;
import a3.t10.g09.validator.PhoneLengthValidator;
import a3.t10.g09.validator.Validator;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class UserRegistration {
    private static final String USER_DATA = "src/main/java/a3/t10/g09/data/users.json";

    private final Gson gson = new Gson();

    public String hashPassword(String password) {
        String salt = BCrypt.gensalt(12);
        return BCrypt.hashpw(password, salt);
    }

    public RegistrationResult register(String fullName,
            String email,
            String phone,
            String idKey,
            String password) {
        UserList users = loadUsers();
        List<String> errors = new ArrayList<>();

        String name = safe(fullName);
        String normEmail = safe(email).toLowerCase();
        String normPhone = safe(phone);
        String normIdKey = safe(idKey);

        collectError(new NameValidator(), name, errors);

        collectError(new AtSymbolValidator(), normEmail, errors);
        collectError(new DomainDotValidator(), normEmail, errors);
        collectError(new EmailUniqueValidator(users), normEmail, errors);

        collectError(new PhoneLengthValidator(), normPhone, errors);
        collectError(new PhoneDigitValidator(), normPhone, errors);

        collectError(new IDKeyFormatValidator(), normIdKey, errors);
        collectError(new IDKeyUniqueValidator(users), normIdKey, errors);

        collectError(new PasswordLengthValidator(), password, errors);
        collectError(new PasswordSpecialCharValidator(), password, errors);

        if (!errors.isEmpty()) {
            return RegistrationResult.failed(errors);
        }

        String hashed = hashPassword(password);

        User newUser = new User(
                normIdKey,
                name,
                normPhone,
                normEmail,
                "user",
                hashed);

        users.addUser(newUser);
        saveUsers(users);

        return RegistrationResult.ok("Account created successfully.");
    }

    public UserList currentUsers() {
        return loadUsers();
    }

    private void collectError(Validator v, String input, List<String> errors) {
        String msg = v.validate(input);
        if (msg != null && !msg.isBlank()) {
            errors.add(msg);
        }
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }

    private UserList loadUsers() {
        try {
            ensureFile();
            try (JsonReader reader = new JsonReader(new FileReader(USER_DATA))) {
                UserList list = gson.fromJson(reader, UserList.class);
                return (list != null) ? list : new UserList();
            }
        } catch (IOException e) {
            System.err.println("Error loading user data: " + e.getMessage());
            return new UserList();
        }
    }

    public void saveUsers(UserList users) {
        try {
            ensureFile();
            Path target = Path.of(USER_DATA);
            Path tmp = target.resolveSibling(target.getFileName() + ".tmp");
            try (Writer writer = new FileWriter(tmp.toFile())) {
                gson.toJson(users, writer);
            }
            Files.move(tmp, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            System.err.println("Error saving user data: " + e.getMessage());
        }
    }

    private void ensureFile() throws IOException {
        Path path = Path.of(USER_DATA);
        if (path.getParent() != null && !Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        if (!Files.exists(path)) {
            try (Writer writer = new FileWriter(path.toFile())) {
                writer.write("{\"users\":[]}");
            }
        }
    }

    public static class RegistrationResult {
        private final boolean success;
        private final List<String> messages;

        private RegistrationResult(boolean success, List<String> messages) {
            this.success = success;
            this.messages = messages;
        }

        public static RegistrationResult ok(String msg) {
            return new RegistrationResult(true, List.of(msg));
        }

        public static RegistrationResult failed(List<String> errors) {
            return new RegistrationResult(false, errors);
        }

        public boolean isSuccess() {
            return success;
        }

        public List<String> getMessages() {
            return messages;
        }
    }
}