package validators;

import java.util.ArrayList;
import java.util.List;
import models.WorkForm;
import validators.GenericValidator.Regex;

/**
 *
 * @author Shahin Mokhtar
 */
public class WorkValidator {

    List<String> errors = new ArrayList<String>();

    public List<String> validate(WorkForm WorkForm) {
        if (GenericValidator.isEmpty(WorkForm.getName())) {
            errors.add("Name may not be empty. ");
        } else if (!GenericValidator.isValid(WorkForm.getName(), Regex.BASE)) {
            errors.add("Name must be 1-25 characters in size");
        }
        
        if (GenericValidator.isEmpty(WorkForm.getProfession())) {
            errors.add("Profession may not be empty. ");
        } else if (!GenericValidator.isValid(WorkForm.getProfession(), Regex.BASE)) {
            errors.add("Profession must be 1-25 characters in size");
        }
        
        if (GenericValidator.isEmpty(WorkForm.getDescription())) {
            errors.add("Description may not be empty. ");
        } else if (!GenericValidator.isValid(WorkForm.getDescription(), Regex.DESC)) {
            errors.add("Description must be 1-300 characters in size");
        }

        return errors;
    }
}
