package vga;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Course;
import models.Skill;
import models.User;

public class SkillDAO  {

    MyDBConnection connection = new MyDBConnection();

    public SkillDAO() {
    // initialization 
    }

    /**
     * Find all skills that have checkByVga = 1
     * @return 
     */
    public List<Skill> findAll() {
        List<Skill> list = new LinkedList<Skill>();
        try {
            connection.startConnection();

            String query = "SELECT * FROM Skill WHERE checkByVGA = 1";

            PreparedStatement pstmt = connection.getConnection().prepareStatement(query);
            ResultSet rs = connection.performSelect(pstmt);

            while (rs.next()) {
                Skill tempSkillData = new Skill();
                tempSkillData.setSkillId(rs.getInt("skillId"));
                tempSkillData.setName(rs.getString("name"));
                
                list.add(tempSkillData);
            }

            connection.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(SkillDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }
    
    /**
     * Find all skills that are linked to a user
     * @param        user to look for
     * @return 
     */
    public List<Skill> findAllByUser(User user) {
        List<Skill> list = new LinkedList<Skill>();
        try {
            connection.startConnection();

            String query = ""
                    + "SELECT * "
                    + "FROM Skill s, User_Skill u "
                    + "WHERE s.skillId = u.skills_skillId "
                    + "AND u.User_userId = " + user.getUserId();

            PreparedStatement pstmt = connection.getConnection().prepareStatement(query);
            ResultSet rs = connection.performSelect(pstmt);

            while (rs.next()) {
                Skill tempSkillData = new Skill();
                tempSkillData.setSkillId(rs.getInt("skillId"));
                tempSkillData.setName(rs.getString("name"));
                
                list.add(tempSkillData);
            }

            connection.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(SkillDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }
    
    /**
     * Find all skills that are linked to a course
     * @param        course to look for
     * @return 
     */
    public List<Skill> findAllByCourse(Course course) {
        List<Skill> list = new LinkedList<Skill>();
        try {
            connection.startConnection();

            String query = ""
                    + "SELECT * "
                    + "FROM Skill s, Course_Skill c "
                    + "WHERE s.skillId = c.skills_skillId "
                    + "AND c.Course_courseId = " + course.getCourseId();

            PreparedStatement pstmt = connection.getConnection().prepareStatement(query);
            ResultSet rs = connection.performSelect(pstmt);

            while (rs.next()) {
                Skill tempSkillData = new Skill();
                tempSkillData.setSkillId(rs.getInt("skillId"));
                tempSkillData.setName(rs.getString("name"));
                
                list.add(tempSkillData);
            }

            connection.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(SkillDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }
}