package models;

/**
 *
 * @author wesley
 */
public class UserForm {
    private String userId, username, firstname, lastname, emailAddress, position, isAdmin, isTeacher, isManager, password;
    
    public UserForm(){
        
    }
    
    public UserForm(String userId, String username, String firstname, String lastname, 
            String emailAddress, String position, String isAdmin, String isTeacher, String isManager, String password){
        this.userId=userId;
        this.username=username;
        this.firstname=firstname;
        this.lastname=lastname;
        this.emailAddress=emailAddress;
        this.position=position;
        this.isAdmin=isAdmin;
        this.isTeacher=isTeacher;
        this.isManager=isManager;
        this.password=password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getIsTeacher() {
        return isTeacher;
    }

    public void setIsTeacher(String isTeacher) {
        this.isTeacher = isTeacher;
    }

    public String getIsManager() {
        return isManager;
    }

    public void setIsManager(String isManager) {
        this.isManager = isManager;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}