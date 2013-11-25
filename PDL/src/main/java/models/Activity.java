package models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.swing.ImageIcon;


@Entity
public class Activity implements Serializable{
    
    
    @Id
    @GeneratedValue
    private int activityId;
    private String sender;
    private String message;
    private boolean activityOpened;
    @ManyToOne
    private User user;

    public Activity() {
    }
    
    public Activity(int activityId, String sender, String message, boolean activityOpened){
      this.activityId=activityId;
      this.sender=sender;
      this.message=message;
      this.activityOpened=activityOpened;
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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isActivityOpened() {
        return activityOpened;
    }

    public void setActivityOpened(boolean activityOpened) {
        this.activityOpened = activityOpened;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
}