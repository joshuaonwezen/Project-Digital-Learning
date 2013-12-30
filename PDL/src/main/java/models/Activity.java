package models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.swing.ImageIcon;


@Entity
public class Activity implements Serializable{
    
    
    @Id
    @GeneratedValue
    private int activityId;
    private String title;
    private String message;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date sent;
    @ManyToOne
    private User user;

    public Activity() {
    }
    
    public Activity(String title, String message, Date sent, User user){
      this.title=title;
      this.message=message;
      this.sent=sent;
      this.user=user;
    }
        
    public Activity(int activityId, String title, String message, Date sent){
      this.activityId=activityId;
      this.title=title;
      this.message=message;
      this.sent=sent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        if (activityId < 1) {
            throw new IllegalArgumentException(
                    "Activity number may not be negative, value = " + activityId);
        }
        this.activityId = activityId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
}