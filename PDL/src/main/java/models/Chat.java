package models;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

/**
 *
 * @author wesley
 */
@Entity
public class Chat {
    
    @Id
    @GeneratedValue
    private int chatId;
    private String subject;
    @ManyToMany
    private List<User> users;
    @ManyToOne
    private User created;
    
    public Chat(){
        
    }
    public Chat(int chatId, String subject, List<User> users, User created){
        this.chatId = chatId;
        this.subject = subject;
        this.users = users;
        this.created = created;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    /**
     * Returns the users in the following format: User1,User2,User3
     */
    public String getUsersSeperatedByComma(){
        String usersSeperatedByComma="";
        for (int i=0;i<users.size();i++){
            usersSeperatedByComma += users.get(i).getUsername();
            if (i!=users.size()-1){
                usersSeperatedByComma += ",";
            }
        }
        System.out.println("returning: " + usersSeperatedByComma);
        return usersSeperatedByComma;
    }
    
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User getCreated() {
        return created;
    }

    public void setCreated(User created) {
        this.created = created;
    }
}