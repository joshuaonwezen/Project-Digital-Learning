package vga;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Activity;

public class ActivityDAO {

    MyDBConnection connection = new MyDBConnection();

    public ActivityDAO() {
        // initialization 
    }

    public long insert(Activity activity) {
        try {
            connection.startConnection();

            String query = "INSERT INTO Activity (message, sent, title, user_userId)"
                    + " VALUES (?,?,?,?) ";

            PreparedStatement pstmt = connection.getConnection().prepareStatement(query);
            pstmt.setString(1, activity.getMessage());
            pstmt.setTimestamp(2, new Timestamp(activity.getSent().getTime()));
            pstmt.setString(3, activity.getTitle());
            pstmt.setInt(4, activity.getUser().getUserId());

            //pstmt.executeUpdate();
            connection.performUpdate(pstmt);

            connection.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(ActivityDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return -1;
    }
}
