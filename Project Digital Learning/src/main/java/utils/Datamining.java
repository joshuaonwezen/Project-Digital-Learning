package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Course;
import models.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.HibernateUtil;

/**
 *
 * @author wesley 
 * CSV reader by: http://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/ 
 * Test data by: http://www.mockaroo.com
 * 
 * Mine some example data in the database
 */
public class Datamining {

    private final Logger logger = Logger.getLogger(Datamining.class.getName());

    /**
     * Create random users
     *
     * @param amount amount of example users to create
     */
    public void mineUsers(int amount) {
        
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("user_data.csv");
        BufferedReader br = null;
        String line = "";
        String delimiter = ",";
        int i = 0;

        if (amount > 100) {
            logger.log(Level.INFO, "Entered amount too large, 100 is max");
        }
        try {
            br = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = br.readLine()) != null && i < amount) {
                // use comma as separator
                String[] user_line = line.split(delimiter);

                //now create an user from the read line
                Session session = services.HibernateUtil.getSessionFactory().openSession();
                Transaction tx = session.beginTransaction();

                User user = new User();
                user.setUsername(user_line[4]);
                user.setFirstname(user_line[1]);
                user.setLastname(user_line[2]);
                user.setEmailAddress(user_line[3]);
                user.setPosition(user_line[5]);
                user.setIsAdmin((Math.random() < 0.5) ? false : true);
                //password=admin
                user.setPassword("21232f297a57a5a743894a0e4a801fc3");

                session.save(user);
                tx.commit();
                session.close();

                i++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        logger.log(Level.INFO, "Done mining users");
    }
    
    /**
     * Create random courses
     *
     * @param amount amount of example courses to create
     */
    public void mineCourses(int amount) {
        
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("course_data.csv");
        BufferedReader br = null;
        String line = "";
        String delimiter = ",";
        int i = 0;
        //beschikbare levels
        final String[] COURSE_LEVELS = new String[]{"Beginner", "Intermediate", "Advanced"};
        Random r = new Random();
        //beschikbare users
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Criteria criteria = session.createCriteria(User.class);
        List<User> users = criteria.list();
        session.close();

        if (amount > 100) {
            logger.log(Level.INFO, "Entered amount too large, 100 is max");
        }
        try {
            br = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = br.readLine()) != null && i < amount) {
                // use comma as separator
                String[] course_line = line.split(delimiter);

                //now create an user from the read line
                session = services.HibernateUtil.getSessionFactory().openSession();
                tx = session.beginTransaction();

                Course course = new Course();
                course.setName(course_line[1]);
                course.setDescription(course_line[2]);
                course.setLevel(Course.Level.valueOf(COURSE_LEVELS[r.nextInt(3)]));
                course.setOwner(users.get(r.nextInt(users.size())));

                session.save(course);
                tx.commit();
                session.close();

                i++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        logger.log(Level.INFO, "Done mining courses");
    }
}
