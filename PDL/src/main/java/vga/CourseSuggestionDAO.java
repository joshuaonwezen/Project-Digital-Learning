package vga;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Activity;
import models.CourseSuggestion;
import models.Skill;

public class CourseSuggestionDAO {

    MyDBConnection connection = new MyDBConnection();

    public CourseSuggestionDAO() {
        // initialization 
    }

    public long insert(CourseSuggestion courseSuggestion) {
        try {
            connection.startConnection();

            String query = "INSERT INTO CourseSuggestion (course_courseId, user_userId)"
                    + " VALUES (?,?) ";

            PreparedStatement pstmt = connection.getConnection().prepareStatement(query);
            pstmt.setInt(1, courseSuggestion.getCourse().getCourseId());
            pstmt.setInt(2, courseSuggestion.getUser().getUserId());

            //pstmt.executeUpdate();
            connection.performUpdate(pstmt);

            connection.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(CourseSuggestionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return -1;
    }
    
    public boolean existInDb(CourseSuggestion courseSuggestion) {
        boolean exists = false;
        try {
            connection.startConnection();

            String query = ""
                    + "SELECT * "
                    + "FROM CourseSuggestion "
                    + "WHERE course_courseId = " + courseSuggestion.getCourse().getCourseId() + " "
                    + "AND user_userId = " + courseSuggestion.getUser().getUserId();

            PreparedStatement pstmt = connection.getConnection().prepareStatement(query);
            ResultSet rs = connection.performSelect(pstmt);

            
            if (rs.next()){
             System.out.println("EXISTS"); 
             exists = true;
            }

            connection.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(SkillDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return exists;
    }
}
