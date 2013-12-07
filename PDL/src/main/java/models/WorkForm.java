package models;

/**
 *
 * @author Shahin Mokhtar
 */
public class WorkForm {

    private String workId, dateFrom, dateTill, name, profession, description;

    public WorkForm() {

    }

    public WorkForm(String workId, String dateFrom, String dateTill,
            String profession, String description) {
        this.workId = workId;
        this.dateFrom = dateFrom;
        this.dateTill = dateTill;
        this.name = name;
        this.profession = profession;
        this.description = description;
    }

    /**
     * @return the workId
     */
    public String getWorkId() {
        return workId;
    }

    /**
     * @param workId the workId to set
     */
    public void setWorkId(String workId) {
        this.workId = workId;
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
