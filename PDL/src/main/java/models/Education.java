package models;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Shahin Mokhtar
 */
@Entity
public class Education implements Serializable {

    @Id
    @GeneratedValue

    private long educationId;
    @Column(columnDefinition="int(2)")
    private int fromMonth;
    @Column(columnDefinition="int(2)")
    private int tillMonth;
    @Column(columnDefinition="int(4)")
    private int fromYear;
    @Column(columnDefinition="int(4)")
    private int tillYear;
    @Column(columnDefinition="varchar(25)")
    private String name;
    @Column(columnDefinition="varchar(25)")
    private String profession;
    @Column(columnDefinition="varchar(300)")
    private String description;

    @ManyToOne
    private User user;

    public Education() {
    }

    public Education(long educationId, int fromMonth, int tillMonth, int fromYear, int tillYear, String name, String profession, String description) {
        this.setEducationId(educationId);
        this.setFromMonth(fromMonth);
        this.setTillMonth(tillMonth);
        this.setFromYear(fromYear);
        this.setTillYear(tillYear);
        this.setName(name);
        this.setProfession(profession);
        this.setDescription(description);
    }

    /* Getters en setters voor de verschillende attributen van het Model */
    public long getEducationId() {
        return educationId;
    }

    public void setEducationId(long educationId) {
        if (educationId < 1) {
            throw new IllegalArgumentException(
                    "Education number may not be negative, value = " + educationId);
        }
        this.educationId = educationId;
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

    /**
     * @return the fromMonth
     */
    public int getFromMonth() {
        return fromMonth;
    }

    /**
     * @param fromMonth the fromMonth to set
     */
    public void setFromMonth(int fromMonth) {
        this.fromMonth = fromMonth;
    }

    /**
     * @return the tillMonth
     */
    public int getTillMonth() {
        return tillMonth;
    }

    /**
     * @param tillMonth the tillMonth to set
     */
    public void setTillMonth(int tillMonth) {
        this.tillMonth = tillMonth;
    }

}
