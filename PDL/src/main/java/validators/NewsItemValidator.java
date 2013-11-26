package validators;

/**
 *
 * @author wesley
 */
import java.util.ArrayList;
import java.util.List;
import models.CourseForm;
import models.NewsItemForm;
import validators.GenericValidator.Regex;

public class NewsItemValidator {

    List<String> errors = new ArrayList<String>();

    public List<String> validate(NewsItemForm newsItemForm) {
        if (GenericValidator.isEmpty(newsItemForm.getTitle())) {
            errors.add("Title may not be empty. ");
        } else if (!GenericValidator.isValid(newsItemForm.getTitle(), Regex.REGULAR)) {
            errors.add("Title must be 1-100 characters in size. ");
        }
        if (GenericValidator.isEmpty(newsItemForm.getDescription())) {
            errors.add("Description may not be empty. ");
        } else if (!GenericValidator.isValid(newsItemForm.getDescription(), Regex.REGULAR)) {
            errors.add("Description must be 1-100 characters in size. ");
        }
        if (GenericValidator.isEmpty(newsItemForm.getUpdated())) {
            errors.add("Date may not be empty. ");
        }
        else if (!GenericValidator.dateIsValid(newsItemForm.getUpdated())){
            errors.add("Date is not in valid format (year-month-date). ");
        }
        if (GenericValidator.isEmpty(newsItemForm.getEditedBy())) {
            errors.add("Editor must be chosen. ");
        }

        return errors;
    }
}
