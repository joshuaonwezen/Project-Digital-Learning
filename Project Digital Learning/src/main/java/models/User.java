package models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author wesley
 */
@Entity
public class User implements Serializable{
    
    @Id
    @GeneratedValue
    private int userId;
    private String username, firstname, lastname, emailAddress, position, password;
    private boolean isAdmin;
    
    public User(){
        
    }
    
    public User(String username, String firstname, String lastname, String emailAddress, String position, String password, boolean isAdmin){
        this.username=username;
        this.firstname=firstname;
        this.lastname=lastname;
        this.emailAddress=emailAddress;
        this.position=position;
        this.password=password;
        this.isAdmin=isAdmin;
    }
    
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
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

    public boolean isIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}