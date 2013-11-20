package validators;

/**
 *
 * @author Martijn
 */
import java.util.ArrayList;
import java.util.List;
import models.CourseForm;
import validators.GenericValidator.Regex;

public class CourseValidator {

    List<String> errors = new ArrayList<String>();

    public List<String> validate(CourseForm courseForm) {
        if (GenericValidator.isEmpty(courseForm.getName())){
            errors.add("Name may not be empty");
        }
        else{
            if (!GenericValidator.isValid(courseForm.getName(), Regex.REGULAR)){
                errors.add("Name: must contain at least one character");
            }
        }
        if (GenericValidator.isEmpty(courseForm.getDescription())){
            errors.add("Description may not be empty");
        }
        else{
            if (!GenericValidator.isValid(courseForm.getDescription(), Regex.REGULAR)){
                errors.add("Description: must contain at least one character");
            }
        }
        if (GenericValidator.isEmpty(courseForm.getLevel())){
            errors.add("Level may not be empty");
        }
        else{
            if (!GenericValidator.isValid(courseForm.getLevel(), Regex.REGULAR)){
                errors.add("Username: must contain at least one character and no spaces or special characters are allowed");
            }
        }
        if (GenericValidator.isEmpty(courseForm.getOwner())){
            errors.add("Owner may not be empty");
        }
        else{
            if (!GenericValidator.isValid(courseForm.getOwner(), Regex.REGULAR)){
                errors.add("Username: must contain at least one character and no spaces or special characters are allowed");
            }
        }
        return errors;
    }
}