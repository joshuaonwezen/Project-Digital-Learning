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

public class CourseDAO  {

    MyDBConnection connection = new MyDBConnection();

    public CourseDAO() {
    // initialization 
    }

    public List<Course> findAll() {
        List<Course> list = new LinkedList<Course>();
        try {
            connection.startConnection();

            String query = "SELECT * FROM Course";

            PreparedStatement pstmt = connection.getConnection().prepareStatement(query);
            ResultSet rs = connection.performSelect(pstmt);

            while (rs.next()) {
                Course tempCourseData = new Course();
                tempCourseData.setCourseId(rs.getInt("courseId"));
                tempCourseData.setName(rs.getString("name"));
                tempCourseData.setIsVisible(((rs.getByte("isVisible") == 1)? true:false));
                
                //add the skills that are linked to the course
               SkillDAO skillDAO = new SkillDAO();
               tempCourseData.setSkills(skillDAO.findAllByCourse(tempCourseData));
                
                list.add(tempCourseData);
            }

            connection.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }
}