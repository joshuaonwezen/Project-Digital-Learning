package models;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Work implements Serializable {

    @Id
    @GeneratedValue

    private long workNumber;
    @Column(columnDefinition="int(4)")
    private int fromYear;
    @Column(columnDefinition="int(4)")
    private int tillYear;
    @Column(columnDefinition="varchar(20)")
    private String name;
    @Column(columnDefinition="varchar(30)")
    private String profession;
    @Column(columnDefinition="varchar(300)")
    private String description;

    @ManyToOne
    private User user;

    public Work() {
    }

    public Work(long workNumber, int fromYear, int tillYear, String name, String profession, String description) {
        this.setWorkNumber(workNumber);
        this.setFromYear(fromYear);
        this.setTillYear(tillYear);
        this.setName(name);
        this.setProfession(profession);
        this.setDescription(description);
    }

    /* Getters en setters voor de verschillende attributen van het Model */
    public long getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(long workNumber) {
        if (workNumber < 1) {
            throw new IllegalArgumentException(
                    "Work number may not be negative, value = " + workNumber);
        }
        this.workNumber = workNumber;
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
