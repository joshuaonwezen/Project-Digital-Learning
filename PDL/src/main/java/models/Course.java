package models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.swing.ImageIcon;
import org.json.simple.JSONValue;

/**
 *
 * @author martijn
 * @todo skills toevoegen aan een course
 * @todo teachers toevoegen aan een course
 * @todo afbeelding toevoegen aan een course
 */
@Entity
public class Course implements Serializable{
    
    public static enum Level{
        Beginner("Beginner"),
        Intermediate("Intermediate"),
        Advanced("Advanced");
        
        private String level;
        
        Level(String level){
            this.level = level;
        }
        public String getLevel(){
            return level;
        }
    }
    
    @Id
    @GeneratedValue
    private int courseId;
    private String name, description, courseKey;
    private ImageIcon image;
    private Level level;
    private boolean isVisible;
    @ManyToOne
    private User owner;
    @ManyToMany
    private List<User> enrolledUsers;
    @ManyToMany(cascade = {CascadeType.ALL})
    private List<Skill> skills;
    
    public Course(){
        
    }
            
    public Course(int courseId, String courseKey, String name, String description, ImageIcon image, Level level, boolean isVisible, User owner, List<User> enrolledUsers, List<Skill> skills){
        this.courseId = courseId;
        this.courseKey = courseKey;
        this.name = name;
        this.description = description;
        this.image = image;
        this.level = level;
        this.isVisible = isVisible;
        this.owner = owner;
        this.enrolledUsers = enrolledUsers;
        this.skills = skills;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImageIcon getImage() {
        return image;
    }

    public String getCourseKey() {
        return UUID.randomUUID().toString();
    }

    public void setCourseKey(String courseKey) {
        this.courseKey = courseKey;
    }

    public void setImage(ImageIcon image) {
        this.image = image;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }    
    
    /**
     * Returns the skills in the following format: Skill1,Skill2,Skill3
     */
    public String getSkillsSeperatedByComma(){
        String skillsSeperatedByComma="";
        for (int i=0;i<skills.size();i++){
            skillsSeperatedByComma += skills.get(i).getName();
            if (i!=skills.size()-1){
                skillsSeperatedByComma += ",";
            }
        }
        System.out.println("returning: " + skillsSeperatedByComma);
        return skillsSeperatedByComma;
    }
    
    public boolean isIsVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public List<User> getEnrolledUsers() {
        return enrolledUsers;
    }

    public void setEnrolledUsers(List<User> enrolledUsers) {
        this.enrolledUsers = enrolledUsers;
    }
    
}