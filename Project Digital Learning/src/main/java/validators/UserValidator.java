package validators;

/**
 *
 * @author wesley
 */
import java.util.ArrayList;
import java.util.List;
import models.UserForm;
import validators.GenericValidator.Patterns;

public class UserValidator {

    List<String> errors = new ArrayList<String>();

    public List<String> validate(UserForm userForm) {
        if (GenericValidator.isEmpty(userForm.getUsername())){
            errors.add("Name may not be empty");
        }
        else{
            if (!GenericValidator.isValid(userForm.getUsername(), Patterns.REGULAR_TEXT)){
                errors.add("Name: must contain at least one character");
            }
        }
        if (GenericValidator.isEmpty(userForm.getFirstname())){
            errors.add("First Name may not be empty");
        }
        else{
            if (!GenericValidator.isValid(userForm.getFirstname(), Patterns.REGULAR_TEXT)){
                errors.add("First Name: must contain at least one character");
            }
        }
        if (GenericValidator.isEmpty(userForm.getLastname())){
            errors.add("Last Name may not be empty");
        }
        else{
            if (!GenericValidator.isValid(userForm.getLastname(), Patterns.REGULAR_TEXT)){
                errors.add("Last Name: must contain at least one character");
            }
        }
        if (!GenericValidator.isValidEmailAddress(userForm.getEmailAddress())){
            errors.add("E-mail address is not valid");
        }
        if (GenericValidator.isEmpty(userForm.getPosition())){
            errors.add("Position may not be empty");
        }
        else{
            if (!GenericValidator.isValid(userForm.getPosition(), Patterns.REGULAR_TEXT)){
                errors.add("Position: must contain at least one character");
            }
        }
        return errors;
    }
}