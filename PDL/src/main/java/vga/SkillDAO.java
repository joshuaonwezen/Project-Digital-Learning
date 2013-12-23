package vga;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Skill;
import models.User;

public class SkillDAO  {

    MyDBConnection connection = new MyDBConnection();

    public SkillDAO() {
    // initialization 
    }

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
}