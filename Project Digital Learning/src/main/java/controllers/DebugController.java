package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Activity;
import models.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.Datamining;

/**
 *
 * @author wesley
 */
public class DebugController extends HttpServlet {

    private static final Logger logger = Logger.getLogger(Datamining.class.getName());

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //get the action
        String uri = request.getRequestURI();
        String action = uri.substring(uri.lastIndexOf("/") + 1);

        System.out.println("GET action: " + action);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //get the action
        String uri = request.getRequestURI();
        String action = uri.substring(uri.lastIndexOf("/") + 1);

        System.out.println("POST action: " + action);

        if (action.equals("createAdminUser")) {
            //maak een admin user aan
            //now create an user from the read line
            Session session = services.HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();

            User user = new User();
            user.setUsername("admin");
            user.setFirstname("Jan");
            user.setLastname("Klaassen");
            user.setEmailAddress("j.klaassen@example.demo");
            user.setPosition("IT Management");
            user.setIsAdmin(true);
            //password=admin
            user.setPassword("21232f297a57a5a743894a0e4a801fc3");

            session.save(user);
            tx.commit();
            session.close();

            logger.log(Level.INFO, "Done creating admin user");
        }
        else if (action.equals("mineUserData")){
            int amount = Integer.parseInt(request.getParameter("amount"));
            
            Datamining dm = new Datamining();
            
            dm.mineUsers(amount);
        }
        else if (action.equals("mineCourseData")){
            int amount = Integer.parseInt(request.getParameter("amount"));
            
            Datamining dm = new Datamining();
            
            dm.mineCourses(amount);
        }
        else if (action.equals("createActivity")){
            Session session = services.HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();

            Activity activity = new Activity();
            activity.setSender("Virtual Guardian Angel");
            activity.setMessage("hallo dit is een testmessage, ik hoop dat dit leesbaar is");
            activity.setActivityOpened(true);
            activity.setActivityId(1);
           
            User user = new User();
            user.setUserId(1);
            activity.setUser(user);
            
            session.save(activity);
            tx.commit();
            session.close();
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
