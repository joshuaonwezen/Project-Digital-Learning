/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

/**
 *
 * @author Martijn
 */
public class CourseForm {
    
    private String courseId, courseName, courseDiscription, courseLevel, courseSkills;
    
    
    public CourseForm(){
        
    }
    
    public CourseForm(String courseId, String courseName, String courseDiscription, String courseLevel, String courseSkills){
        this.courseId=courseId;
        this.courseName=courseName;
        this.courseDiscription=courseDiscription;
        this.courseLevel=courseLevel;
        this.courseSkills=courseSkills;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDiscription() {
        return courseDiscription;
    }

    public void setCourseDiscription(String courseDiscription) {
        this.courseDiscription = courseDiscription;
    }

    public String getCourseLevel() {
        return courseLevel;
    }

    public void setCourseLevel(String courseLevel) {
        this.courseLevel = courseLevel;
    }

    public String getCourseSkills() {
        return courseSkills;
    }

    public void setCourseSkills(String courseSkills) {
        this.courseSkills = courseSkills;
    }
    
    
    
    
}
