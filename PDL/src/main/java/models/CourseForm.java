package models;

/**
 *
 * @author martijn
 */
public class CourseForm{
    
    private String courseId, name, description, image, level, owner, skills, isVisible;
    
    public CourseForm(){
        
    }
            
    public CourseForm(String courseId, String name, String description, String image, String level, String isVisible, String owner, String skills){
        this.courseId = courseId;
        this.name = name;
        this.description = description;
        this.image = image;
        this.level = level;
        this.isVisible = isVisible;
        this.owner = owner;
        this.skills = skills;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(String isVisible) {
        this.isVisible = isVisible;
    }
}