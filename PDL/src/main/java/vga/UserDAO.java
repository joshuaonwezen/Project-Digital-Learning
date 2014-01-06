package vga;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Course;
import models.Skill;
import models.User;

public class UserDAO  {

    MyDBConnection connection = new MyDBConnection();

    public UserDAO() {
    // initialization 
    }

    public List<User> findAll() {
        List<User> list = new LinkedList<User>();
        try {
            connection.startConnection();

            String query = "SELECT * FROM User";

            PreparedStatement pstmt = connection.getConnection().prepareStatement(query);
            ResultSet rs = connection.performSelect(pstmt);

            while (rs.next()) {
                User tempUserData = new User();
                tempUserData.setUserId(rs.getInt("userId"));
                tempUserData.setFirstname(rs.getString("firstname"));
                tempUserData.setLastname(rs.getString("lastname"));
                tempUserData.setUsername(rs.getString("username"));
                
                //add skills that are linked to a user
                SkillDAO skillDAO = new SkillDAO();
                tempUserData.setSkills(skillDAO.findAllByUser(tempUserData));
                
                list.add(tempUserData);
            }

            connection.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }
    
    public List<User> findAllByCourse(Course course) {
        List<User> list = new LinkedList<User>();
        try {
            connection.startConnection();

            String query = ""
                    + "SELECT * "
                    + "FROM User u, Course_User cu "
                    + "WHERE u.userId=cu.enrolledUsers_userId "
                    + "AND cu.Course_courseId=" + course.getCourseId();

            PreparedStatement pstmt = connection.getConnection().prepareStatement(query);
            ResultSet rs = connection.performSelect(pstmt);

            while (rs.next()) {
                User tempUserData = new User();
                tempUserData.setUserId(rs.getInt("userId"));
                tempUserData.setFirstname(rs.getString("firstname"));
                tempUserData.setLastname(rs.getString("lastname"));
                tempUserData.setUsername(rs.getString("username"));
                
                list.add(tempUserData);
            }

            connection.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }
    
    
    
    
    
    
    
    
}