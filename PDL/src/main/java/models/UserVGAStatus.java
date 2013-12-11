package models;

/**
 *
 * @author wesley
 * 
 * This class is used for temporary displaying results from the vga to a user (no database interaction)
 */
public class UserVGAStatus {
    
    private User user;
    private String status;
    private String statusDescription;
    
    public UserVGAStatus(User user, String status, String statusDescription){
        this.user = user;
        this.status = status;
        this.statusDescription = statusDescription;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }
    
}
