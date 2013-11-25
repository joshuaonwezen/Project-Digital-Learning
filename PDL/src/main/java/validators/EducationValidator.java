package validators;

import java.util.ArrayList;
import java.util.List;
import models.EducationForm;
import validators.GenericValidator.Regex;

/**
 *
 * @author Shahin Mokhtar
 */
public class EducationValidator {

    List<String> errors = new ArrayList<String>();

    public List<String> validate(EducationForm EducationForm) {
        if (GenericValidator.isEmpty(EducationForm.getName())) {
            errors.add("Name may not be empty. ");
        } else if (!GenericValidator.isValid(EducationForm.getName(), Regex.BASE)) {
            errors.add("Name must be 1-25 characters in size");
        }
        
        if (GenericValidator.isEmpty(EducationForm.getProfession())) {
            errors.add("Profession may not be empty. ");
        } else if (!GenericValidator.isValid(EducationForm.getProfession(), Regex.BASE)) {
            errors.add("Profession must be 1-25 characters in size");
        }
        
        if (GenericValidator.isEmpty(EducationForm.getDescription())) {
            errors.add("Description may not be empty. ");
        } else if (!GenericValidator.isValid(EducationForm.getDescription(), Regex.DESC)) {
            errors.add("Description must be 1-300 characters in size");
        }

        return errors;
    }
}
