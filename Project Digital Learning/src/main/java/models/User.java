package models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue
    private String userName;
    private String firstName;
    private String lastName;
    private String password;
    private String userRole;
    
    
    public User() {
    }



    public User(String userName, String firstName, String lastName, String password, String userRole) {
        this.userName=userName;
        this.firstName=firstName;
        this.lastName=lastName;
        this.password=password;
        this.userRole=userRole;
    }

    /* Getters en setters voor de verschillende attributen van het Model */


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
       
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

}
