package models;

/**
 *
 * @author Shahin Mokhtar
 */
public class EducationForm {

    private String educationId, dateFrom, dateTill, name, profession, description;

    public EducationForm() {

    }

    public EducationForm(String educationId, String dateFrom, String dateTill,
            String profession, String description) {
        this.educationId = educationId;
        this.dateFrom = dateFrom;
        this.dateTill = dateTill;
        this.name = name;
        this.profession = profession;
        this.description = description;
    }

    /**
     * @return the educationId
     */
    public String getEducationId() {
        return educationId;
    }

    /**
     * @param educationId the educationId to set
     */
    public void setEducationId(String educationId) {
        this.educationId = educationId;
    }

    /**
     * @return the dateFrom
     */
    public String getDateFrom() {
        return dateFrom;
    }

    /**
     * @param dateFrom the dateFrom to set
     */
    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * @return the dateTill
     */
    public String getDateTill() {
        return dateTill;
    }

    /**
     * @param dateTill the dateTill to set
     */
    public void setDateTill(String dateTill) {
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

    
}
