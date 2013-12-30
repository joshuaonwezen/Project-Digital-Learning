package models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.swing.ImageIcon;

/**
 *
 * @author wesley
 * 
 * Shows when the i18n values where last updated and by whom
 */
@Entity
public class Internationalization implements Serializable {
    
    @Id
    @GeneratedValue
    private int i18nId; // there should only be one entry in the database
    @ManyToOne
    private User updatedBy; // the user that edited the translations
    @ManyToOne
    private User appliedBy; // the user that applied (restarted the server) the hashes
    private boolean needsUpdate; // whether there are edited translations that need to be applied to the server
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastUpdated; // when the translations where edited
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastAppliedToSystem; // when the updates where last applied to the system
    
    public Internationalization(){
        
    }

    public Internationalization(int i18nId, User updatedBy, User appliedBy, boolean needsUpdate, Date lastUpdated, Date lastAppliedToSystem) {
        this.i18nId = i18nId;
        this.updatedBy = updatedBy;
        this.appliedBy = appliedBy;
        this.needsUpdate = needsUpdate;
        this.lastUpdated = lastUpdated;
        this.lastAppliedToSystem = lastAppliedToSystem;
    }

    public int getI18nId() {
        return i18nId;
    }

    public void setI18nId(int i18nId) {
        this.i18nId = i18nId;
    }

    public User getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
    }

    public User getAppliedBy() {
        return appliedBy;
    }

    public void setAppliedBy(User appliedBy) {
        this.appliedBy = appliedBy;
    }

    public boolean isNeedsUpdate() {
        return needsUpdate;
    }

    public void setNeedsUpdate(boolean needsUpdate) {
        this.needsUpdate = needsUpdate;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Date getLastAppliedToSystem() {
        return lastAppliedToSystem;
    }

    public void setLastAppliedToSystem(Date lastAppliedToSystem) {
        this.lastAppliedToSystem = lastAppliedToSystem;
    }
    
}