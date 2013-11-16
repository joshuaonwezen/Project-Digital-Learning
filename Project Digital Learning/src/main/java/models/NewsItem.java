package models;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author wesley
 */
@Entity
public class NewsItem {

    @Id
    @GeneratedValue
    private int newsId;
    private String title, description;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date updated;
    @ManyToOne
    private User editedBy;
    
    public NewsItem(){
        
    }
    public NewsItem(String title, String description, Date updated, User editedBy){
        this.title = title;
        this.description = description;
        this.updated = updated;
        this.editedBy = editedBy;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
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

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public User getEditedBy() {
        return editedBy;
    }

    public void setEditedBy(User editedBy) {
        this.editedBy = editedBy;
    }
}