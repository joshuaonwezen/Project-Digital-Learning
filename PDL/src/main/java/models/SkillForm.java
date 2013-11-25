package models;

/**
 *
 * @author Shahin Mokhtar
 */
public class SkillForm {

    private String skillId, name, level, description;

    public SkillForm() {

    }

    public SkillForm(String skillId, String name, String level, String description) {
        this.skillId = skillId;
        this.name = name;
        this.level = level;
        this.description = description;
    }

    /**
     * @return the skillId
     */
    public String getSkillId() {
        return skillId;
    }

    /**
     * @param skillId the skillId to set
     */
    public void setSkillId(String skillId) {
        this.skillId = skillId;
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

    
}
