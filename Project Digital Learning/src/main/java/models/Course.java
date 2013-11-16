package models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.swing.ImageIcon;

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
    private String name, description;
    private ImageIcon image;
    private Level level;
    @ManyToOne
    private User owner;
    
    public Course(){
        
    }
            
    public Course(int courseId, String name, String description, ImageIcon image, Level level, User owner){
        this.courseId = courseId;
        this.name = name;
        this.description = description;
        this.image = image;
        this.level = level;
        this.owner = owner;
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
}