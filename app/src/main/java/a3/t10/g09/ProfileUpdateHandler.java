package a3.t10.g09;
import a3.t10.g09.validator.AtSymbolValidator;
import a3.t10.g09.validator.DomainDotValidator;
import a3.t10.g09.validator.PhoneLengthValidator;
import a3.t10.g09.validator.PhoneDigitValidator;
import a3.t10.g09.validator.NameValidator;
import a3.t10.g09.validator.IDKeyFormatValidator;
import a3.t10.g09.validator.IDKeyUniqueValidator;
import java.util.Scanner;

public class ProfileUpdateHandler {
    private final Scanner scanner = new Scanner(System.in);
    private User user;

    public ProfileUpdateHandler(User user){
        this.user = user;
    }

    public void updatePhoneNumber(){
        PhoneLengthValidator lengthValidator = new PhoneLengthValidator();
        PhoneDigitValidator digitValidator = new PhoneDigitValidator();

        System.out.print("Enter new phone number: ");
        String newPhone = scanner.nextLine();

        String result_1 = lengthValidator.validate(newPhone);
        String result_2 = digitValidator.validate(newPhone);
        if(result_1 != null){
            System.out.println(result_1);
            return;
        }
        if(result_2 != null){
            System.out.println(result_2);
            return;
        }
        //TODO
        // If all validators pass
        System.out.println("Phone number is valid. (Storage update not implemented yet)");

    }

    public void updateEmail(){
        AtSymbolValidator atSymbolValidator = new AtSymbolValidator();
        DomainDotValidator domainDotValidator = new DomainDotValidator();

        System.out.print("Enter new email: ");
        String newEmail = scanner.nextLine();

        String result_1 = atSymbolValidator.validate(newEmail);
        String result_2 = domainDotValidator.validate(newEmail);
        if(result_1 != null){
            System.out.println(result_1);
            return;
        }
        if(result_2 != null){
            System.out.println(result_2);
            return;
        }
        //TODO
        // If all validators pass
        System.out.println("Email is valid. (Storage update not implemented yet)");
    }

    public void updateName(){
        NameValidator nameValidator = new NameValidator();
        
        System.out.print("Enter new name: ");
        String newName = scanner.nextLine();

        String result = nameValidator.validate(newName);
        if(result != null){
            System.out.println(result);
            return;
        }
        //TODO
        System.out.println("Name is valid. (Storage update not implemented yet)");
    }

    public void updateIDKey(UserList userList) {
        IDKeyFormatValidator formatValidator = new IDKeyFormatValidator();
        IDKeyUniqueValidator uniqueValidator = new IDKeyUniqueValidator(userList);

        System.out.print("Enter new ID key: ");
        String newIDKey = scanner.nextLine();

        String result_1 = formatValidator.validate(newIDKey);
        String result_2 = uniqueValidator.validate(newIDKey);
        if(result_1 != null){
            System.out.println(result_1);
            return;
        }
        if(result_2 != null){
            System.out.println(result_2);
            return;
        }
        //TODO
        System.out.println("ID key is valid. (Storage update not implemented yet)");
    }

    public void updatePassword() {
        //TODO
    }
}
