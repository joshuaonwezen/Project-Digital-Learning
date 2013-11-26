package validators;

import java.util.ArrayList;
import java.util.List;
import models.SkillForm;
import validators.GenericValidator.Regex;

/**
 *
 * @author Shahin Mokhtar
 */
public class SkillValidator {

    List<String> errors = new ArrayList<String>();

    public List<String> validate(SkillForm SkillForm) {
        if (GenericValidator.isEmpty(SkillForm.getName())) {
            errors.add("Name may not be empty. ");
        } else if (!GenericValidator.isValid(SkillForm.getName(), Regex.BASE)) {
            errors.add("Name must be 1-25 characters in size");
        }
        return errors;
    }
}
