/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author martijn
 */
@Entity
public class Course implements Serializable{
    
    @Id
    @GeneratedValue
    private int courseId;
    private String courseName, courseDiscription, courseLevel, courseSkills;
    
    
    public Course(){
        
    }
    
    public Course(String courseName, String courseDiscription, String courseLevel, String courseSkills){
        this.courseName=courseName;
        this.courseDiscription=courseDiscription;
        this.courseLevel=courseLevel;
        this.courseSkills=courseSkills;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
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
