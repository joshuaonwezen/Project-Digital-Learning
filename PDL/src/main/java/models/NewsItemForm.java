package models;

/**
 *
 * @author wesley
 */
public class NewsItemForm {
    private String newsId, title, description, updated, editedBy;
    
    public NewsItemForm(){
        
    }
    
    public NewsItemForm(String newsId, String title, String description, String updated, String editedBy){
        this.newsId = newsId;
        this.title = title;
        this.description = description;
        this.updated = updated;
        this.editedBy = editedBy;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getEditedBy() {
        return editedBy;
    }

    public void setEditedBy(String editedBy) {
        this.editedBy = editedBy;
    }
}