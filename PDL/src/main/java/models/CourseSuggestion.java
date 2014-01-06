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
import javax.persistence.ManyToOne;

/**
 *
 * @author Joshua
 */
@Entity
public class CourseSuggestion implements Serializable {

    @Id
    @GeneratedValue
    private int suggestedCourseId;
    @ManyToOne
    private Course course;
    @ManyToOne
    private User user;
    
    public CourseSuggestion(){
    }

    public CourseSuggestion(Course course, User user){
        this.course = course;
        this.user = user;
        
    }
    public int getSuggestedCourseId() {
        return suggestedCourseId;
    }

    public void setSuggestedCourseId(int suggestedCourseId) {
        this.suggestedCourseId = suggestedCourseId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



    
}
