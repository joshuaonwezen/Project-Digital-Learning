package models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Skill implements Serializable {

    @Id
    @GeneratedValue

    private long skillNumber;
    private String name;
    private String level;
    private String description;

    @ManyToOne
    private User user;

    public Skill() {
    }

    public Skill(long projectNumber, String name, String level) {
        this.setSkillNumber(skillNumber);
        this.setName(name);
        this.setLevel(level);
    }

    /* Getters en setters voor de verschillende attributen van het Model */
    public long getSkillNumber() {
        return skillNumber;
    }

    public void setSkillNumber(long skillNumber) {
        if (skillNumber < 1) {
            throw new IllegalArgumentException(
                    "Work number may not be negative, value = " + skillNumber);
        }
        this.skillNumber = skillNumber;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the level
     */
    public String getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }
}
