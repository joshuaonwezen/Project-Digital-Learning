package models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Project implements Serializable {

    @Id
    @GeneratedValue

    private long projectNumber;
    private int fromYear;
    private int tillYear;
    private String name;
    private String profession;
    private String description;

    @ManyToOne
    private User user;

    public Project() {
    }

    public Project(long projectNumber, int fromYear, int tillYear, String name, String profession, String description) {
        this.setProjectNumber(projectNumber);
        this.setFromYear(fromYear);
        this.setTillYear(tillYear);
        this.setName(name);
        this.setProfession(profession);
        this.setDescription(description);
    }

    /* Getters en setters voor de verschillende attributen van het Model */
    public long getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(long projectNumber) {
        if (projectNumber < 1) {
            throw new IllegalArgumentException(
                    "Work number may not be negative, value = " + projectNumber);
        }
        this.projectNumber = projectNumber;
    }

    /**
     * @return the fromYear
     */
    public int getFromYear() {
        return fromYear;
    }

    /**
     * @param fromYear the fromYear to set
     */
    public void setFromYear(int fromYear) {
        this.fromYear = fromYear;
    }

    /**
     * @return the tillYear
     */
    public int getTillYear() {
        return tillYear;
    }

    /**
     * @param tillYear the tillYear to set
     */
    public void setTillYear(int tillYear) {
        this.tillYear = tillYear;
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
     * @return the profession
     */
    public String getProfession() {
        return profession;
    }

    /**
     * @param profession the profession to set
     */
    public void setProfession(String profession) {
        this.profession = profession;
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
