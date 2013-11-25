package controllers;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Activity;
import models.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.HibernateUtil;
import validators.GenericValidator;
//import services.HibernateUtil;
/**
 *
 * @author wesley
 */
public class LoginController extends HttpServlet {

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
        if (request.getParameter("id") != null) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        String dispatchUrl = null;
        int id = Integer.parseInt(request.getParameter("id"));
        
       
        List<Activity> tempActivity = new LinkedList();
        Query queryActivity = session.createQuery("from Activity where user_userId = " + id);
        tempActivity = queryActivity.list();
        request.setAttribute("activityList", tempActivity);
        System.out.println("GET action: " + action);
    }
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
        
        if (action.equals("login") && !GenericValidator.isEmpty(request.getParameter("username"))){
            Session session = HibernateUtil.getSessionFactory().openSession();
            String hql = "from User  where username = ?";
            List result = session.createQuery(hql)
            .setString(0, request.getParameter("username"))
            .list();
            session.close();
            
            //als de list leeg is dan is de username zoiezo fout
            List<String> errors = new ArrayList<String>();
            if (result.isEmpty()){
                errors.add("Username or password is incorrect. ");
                request.setAttribute("errors", errors);
                redirect(request, response, "/index.jsp");
            }
            //anders kunnen we nu de password gaan vergelijken
            else{
                User user = (User)result.get(0);
                
                //geef foutmelding als password leeg is
                if (request.getParameter("password").trim().isEmpty()){
                    errors.add("Username or password is incorrect. ");
                    request.setAttribute("errors", errors);
                    redirect(request, response, "/index.jsp");
                }
                else{
                    //password gaan vergelijken
                    String md5 = User.md5(request.getParameter("password"));
                    
                    if (!user.getPassword().equals(md5)){
                        errors.add("Username or password is incorrect. ");
                        request.setAttribute("errors", errors);
                        redirect(request, response, "/index.jsp");
                    }
                    //login is goed
                    else{
                        request.getSession().setAttribute("loggedInUsername", user.getUsername());
                        request.getSession().setAttribute("loggedInUserId", user.getUserId());
                        request.getSession().setAttribute("loggedInIsAdmin", user.isIsAdmin());
                        request.getSession().setAttribute("loggedInFirstname", user.getFirstname());
                        request.getSession().setAttribute("loggedInLastname", user.getLastname());

           
                                int id = user.getUserId();
                                Session activitySession = HibernateUtil.getSessionFactory().openSession();
                                Transaction tx = activitySession.beginTransaction();

                                List<Activity> tempActivity = new LinkedList();
                                Query queryActivity = activitySession.createQuery("from Activity where user_userId = " + id);
                                tempActivity = queryActivity.list();
                                request.setAttribute("activityList", tempActivity);
                                activitySession.close();

                                  redirect(request, response, "/homepage.jsp");
                              }
                    
                }
            }
        }
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String address) 
            throws ServletException, IOException{
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
}