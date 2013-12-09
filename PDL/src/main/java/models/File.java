package models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.Entity;

/**
 *
 * @author wesley
 */
@Entity
public class File implements Serializable{
    
    @Id
    @GeneratedValue
    private int fileId;
    private String name;
    @ManyToOne
    private User owner;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastEdited;
     @ManyToOne
    private Course course;
    private boolean visible;
    private long fileSize;
    
    public File(){
        
    }
    public File(int fileId, String name, User owner, Date lastEdited, Course course, boolean visible, long fileSize){
        this.fileId = fileId;
        this.name = name;
        this.owner = owner;
        this.lastEdited = lastEdited;
        this.course = course;
        this.visible = visible;
        this.fileSize = fileSize;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Date getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(Date lastEdited) {
        this.lastEdited = lastEdited;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}