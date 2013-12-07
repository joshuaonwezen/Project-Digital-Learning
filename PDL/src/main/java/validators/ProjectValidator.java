package validators;

import java.util.ArrayList;
import java.util.List;
import models.ProjectForm;
import validators.GenericValidator.Regex;

/**
 *
 * @author Shahin Mokhtar
 */
public class ProjectValidator {

    List<String> errors = new ArrayList<String>();

    public List<String> validate(ProjectForm ProjectForm) {
        if (GenericValidator.isEmpty(ProjectForm.getName())) {
            errors.add("Name may not be empty. ");
        } else if (!GenericValidator.isValid(ProjectForm.getName(), Regex.BASE)) {
            errors.add("Name must be 1-25 characters in size");
        }
        
        if (GenericValidator.isEmpty(ProjectForm.getProfession())) {
            errors.add("Profession may not be empty. ");
        } else if (!GenericValidator.isValid(ProjectForm.getProfession(), Regex.BASE)) {
            errors.add("Profession must be 1-25 characters in size");
        }
        
        if (GenericValidator.isEmpty(ProjectForm.getDescription())) {
            errors.add("Description may not be empty. ");
        } else if (!GenericValidator.isValid(ProjectForm.getDescription(), Regex.DESC)) {
            errors.add("Description must be 1-500 characters in size");
        }

        return errors;
    }
}
