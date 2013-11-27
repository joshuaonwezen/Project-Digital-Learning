package models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Shahin Mokhtar
 */
@Entity
public class Education implements Serializable {

    @Id
    @GeneratedValue

    private long educationId;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateFrom;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateTill;
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

    public Education(long educationId, Date dateFrom, Date dateTill, String name, String profession, String description) {
        this.setEducationId(educationId);
        this.setDateFrom(dateFrom);
        this.setDateTill(dateTill);
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
     * @return the dateFrom
     */
    public Date getDateFrom() {
        return dateFrom;
    }
    
    public String getDateFromFormatted() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = "";

        formattedDate = sdf.format(dateFrom);

        System.out.println("RETURING: " + formattedDate);
        return formattedDate;
    }

    /**
     * @param dateFrom the dateFrom to set
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * @return the dateTill
     */
    public Date getDateTill() {
        return dateTill;
    }
    
    public String getDateTillFormatted() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = "";

        formattedDate = sdf.format(dateTill);

        System.out.println("RETURING: " + formattedDate);
        return formattedDate;
    }

    /**
     * @param dateTill the dateTill to set
     */
    public void setDateTill(Date dateTill) {
        this.dateTill = dateTill;
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
