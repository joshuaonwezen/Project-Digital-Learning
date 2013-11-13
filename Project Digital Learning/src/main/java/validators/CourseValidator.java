/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
        if (GenericValidator.isEmpty(courseForm.getCourseName())){
            errors.add("Name may not be empty");
        }
        else{
            if (!GenericValidator.isValid(courseForm.getCourseName(), Regex.ABC)){
                errors.add("Name: must contain at least one character");
            }
        }
        if (GenericValidator.isEmpty(courseForm.getCourseDiscription())){
            errors.add("Course Discription may not be empty");
        }
        else{
            if (!GenericValidator.isValid(courseForm.getCourseDiscription(), Regex.ABC)){
                errors.add("Course Discription: must contain at least one character");
            }
        }
        if (GenericValidator.isEmpty(courseForm.getCourseLevel())){
            errors.add("Course Level may not be empty");
        }
        else{
            if (!GenericValidator.isValid(courseForm.getCourseLevel(), Regex.ABC)){
                errors.add("Course Level: must contain at least one character");
            }
        }
    
        
        if (GenericValidator.isEmpty(courseForm.getCourseSkills())){
            errors.add("Course Skills may not be empty");
        }
        else{
            if (!GenericValidator.isValid(courseForm.getCourseSkills(), Regex.ABC_SPACING_ALLOWED)){
                errors.add("Course Skills: must contain at least one character");
            }
        }
        return errors;
    }
}