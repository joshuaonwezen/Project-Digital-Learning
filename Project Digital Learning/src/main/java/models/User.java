package models;

import java.io.Serializable;
import java.security.MessageDigest;
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
    private String fullName;
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
        fullName = firstname + " " + lastname;
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
    
    public String getFullName(){
        return fullName;
    }
    
    /**
     * Maakt een MD5-hash van een String
     * @param password      De password String die moet worden geconvert naar een MD5 hash
     * @return              MD5-hash in String formaat
     */
    public static String md5(String password){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(password.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } 
        catch (java.security.NoSuchAlgorithmException e) {
            //todo
        }
        return null;
    }
}