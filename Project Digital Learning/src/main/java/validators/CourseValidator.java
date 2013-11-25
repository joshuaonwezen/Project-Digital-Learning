package validators;

/**
 *
 * @author wesley
 */
import java.util.ArrayList;
import java.util.List;
import models.CourseForm;
import validators.GenericValidator.Regex;

public class CourseValidator {

    List<String> errors = new ArrayList<String>();

    public List<String> validate(CourseForm courseForm) {
        if (GenericValidator.isEmpty(courseForm.getName())) {
            errors.add("Name may not be empty. ");
        } else if (!GenericValidator.isValid(courseForm.getName(), Regex.REGULAR)) {
            errors.add("Name must be 1-100 characters in size. ");
        }
        if (GenericValidator.isEmpty(courseForm.getDescription())) {
            errors.add("Description may not be empty. ");
        } else if (!GenericValidator.isValid(courseForm.getDescription(), Regex.REGULAR)) {
            errors.add("Description must be 1-100 characters in size. ");
        }
        if (GenericValidator.isEmpty(courseForm.getLevel())) {
            errors.add("Level must be chosen. ");
        }
        if (GenericValidator.isEmpty(courseForm.getOwner())) {
            errors.add("Owner must be chosen. ");
        }
        if (GenericValidator.isEmpty(courseForm.getSkills())) {
            errors.add("Please choose at least one skill. ");
        }

        return errors;
    }
}
