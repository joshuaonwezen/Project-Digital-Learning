package validators;

/**
 *
 * @author wesley
 */
import java.util.ArrayList;
import java.util.List;
import models.UserForm;
import validators.GenericValidator.Regex;

public class UserValidator {

    List<String> errors = new ArrayList<String>();

    public List<String> validate(UserForm userForm) {
        if (GenericValidator.isEmpty(userForm.getUsername())) {
            errors.add("Username may not be empty. ");
        } else if (!GenericValidator.isValid(userForm.getUsername(), Regex.USERNAME)) {
            errors.add("Username must be 1-100 characters in size and no spaces or special characters are allowed. ");
        }

        if (GenericValidator.isEmpty(userForm.getFirstname())) {
            errors.add("First Name may not be empty. ");
        } else if (!GenericValidator.isValid(userForm.getFirstname(), Regex.REGULAR)) {
            errors.add("First Name must be 1-100 characters in size. ");
        }

        if (GenericValidator.isEmpty(userForm.getLastname())) {
            errors.add("Last Name may not be empty. ");
        } else if (!GenericValidator.isValid(userForm.getLastname(), Regex.REGULAR)) {
            errors.add("Last Name must be 1-100 characters in size. ");
        }
        if (GenericValidator.isEmpty(userForm.getEmailAddress())) {
            errors.add("Email address may not be empty ");
        } else if (!GenericValidator.isValid(userForm.getEmailAddress(), Regex.EMAIL_ADDRESS)) {
            errors.add("Email address is not valid.  ");
        }

        if (GenericValidator.isEmpty(userForm.getPosition())) {
            errors.add("Position may not be empty. ");
        } else if (!GenericValidator.isValid(userForm.getPosition(), Regex.REGULAR)) {
            errors.add("Position must be 1-100 characters in size. ");
        }

        if (GenericValidator.isEmpty(userForm.getPassword())) {
            errors.add("Password may not be empty. ");
        } else if (!GenericValidator.isValid(userForm.getPassword(), Regex.PASSWORD)) {
            errors.add("Password must be 7-100 characters in size. ");
        }

        return errors;
    }
}
