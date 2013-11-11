package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.User;
import models.UserForm;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.HibernateUtil;
import validators.UserValidator;

/**
 *
 * @author wesley
 */
public class ManageUserController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       //nop
    }

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
        
        //overzicht tonen met alle users
        if (action.equals("users")){
            setUsersOnRequest(request);
            redirect(request, response, "users.jsp");
        }
        //wijzigen of aanmaken van een user
        else if (action.equals("edit")){
            boolean isUpdate=false;
            
            //extract userId
            String queryString = request.getQueryString();
            //als er een id is meegegeven gaan we wijzigen, anders een nieuwe user aanmaken
            if (queryString.substring(queryString.indexOf("=")).length() > 1){
                isUpdate = true;
            }
            //wijzigen van een gebruiker
            if (isUpdate){
                int userId = Integer.parseInt(queryString.substring(queryString.indexOf("=")+1));
            
                //haal de user op uit de database om deze vervolgens in de request te zetten
                Session session = HibernateUtil.getSessionFactory().openSession();
                Transaction tx = session.beginTransaction();
                User managedUser = (User)session.load(User.class, userId);
                
                //set ze nu in de request
                request.setAttribute("userId", managedUser.getUserId());
                request.setAttribute("username", managedUser.getUsername());
                request.setAttribute("firstName", managedUser.getFirstname());
                request.setAttribute("lastName", managedUser.getLastname());
                request.setAttribute("emailAddress", managedUser.getEmailAddress());
                request.setAttribute("position", managedUser.getPosition());
                request.setAttribute("isAdmin", managedUser.isIsAdmin());
                session.close();
                
                //geef aan dat we gaan updaten
                request.setAttribute("update", true);
            }
            else{
                //geef aan dat we een nieuwe user gaan aanmaken
                request.setAttribute("update", false);
            }
            redirect(request, response, "/edit_user.jsp");
        }
        //deleten van een user
        else if (action.equals("delete")){
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            //extract userId
            String queryString = request.getQueryString();
            int userId = Integer.parseInt(queryString.substring(queryString.indexOf("=")+1));
            User managedUser = (User)session.load(User.class, userId);
            session.delete(managedUser);
            tx.commit();
            session.close();
            
            redirect(request, response, "../users");
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
        
        //overzicht tonen met alle users
        if (action.equals("users")){
            setUsersOnRequest(request);
            redirect(request, response, "users.jsp");
        }
        //we moeten eerst een formvalidate doen als we een user gaan bewerken of toevoegen
        else if (action.equals("new") || action.equals("edit")){
            UserForm userForm = new UserForm();
            userForm.setUsername(request.getParameter("username"));
            userForm.setFirstname(request.getParameter("firstname"));
            userForm.setLastname(request.getParameter("lastname"));
            userForm.setEmailAddress(request.getParameter("emailAddress"));
            userForm.setPosition(request.getParameter("position"));
            
            UserValidator validator = new UserValidator();
            List<String> errors = validator.validate(userForm);
            
            //wanneer we geen errors hebben kunnen we een user gaan bewerken of toevoegen
            if (errors.isEmpty()) {
                System.out.println("there are no errors");
                //aanmaken van een gebruiker
                if (action.equals("new")) {
                    System.out.println("action equals new");
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    Transaction tx = session.beginTransaction();

                    User user = new User();
                    user.setUsername(request.getParameter("username"));
                    user.setFirstname(request.getParameter("firstname"));
                    user.setLastname(request.getParameter("lastname"));
                    user.setEmailAddress(request.getParameter("emailAddress"));
                    user.setPosition(request.getParameter("position"));
                    user.setIsAdmin((request.getParameter("isAdmin") != null ? true : false));

                    session.save(user);
                    tx.commit();
                    session.close();
                } 
                //wijzigen van een gebruiker
                else if (action.equals("edit")) {
                    System.out.println("action equals edit");
                    int userId;

                    Session session = HibernateUtil.getSessionFactory().openSession();
                    Transaction tx = session.beginTransaction();

                    userId = Integer.parseInt(request.getParameter("userId"));
                    User managedUser = (User) session.load(User.class, userId);

                    managedUser.setUsername(request.getParameter("username"));
                    managedUser.setFirstname(request.getParameter("firstname"));
                    managedUser.setLastname(request.getParameter("lastname"));
                    managedUser.setEmailAddress(request.getParameter("emailAddress"));
                    managedUser.setPosition(request.getParameter("position"));
                    managedUser.setIsAdmin((request.getParameter("isAdmin") != null ? true : false));

                    session.saveOrUpdate(managedUser);
                    tx.commit();
                    session.close();
                }
            }
            //anders sturen we de gebruiker terug naar het form met de errors
            //LETOP: we vullen de velden niet opnieuw in voor de gebruiker, de velden worden
            //namelijk al default gevalideerd aan de client side: dit is dus een extra measure
            else{
                System.out.println("there are errors");
                
                //vergeet de errors niet op de request te zetten
                request.setAttribute("errorsSize", errors.size());
                request.setAttribute("errors", errors);
                
                redirect(request, response, "/edit_user.jsp");
            }
        }
    }
    
    private void setUsersOnRequest(HttpServletRequest request){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Criteria criteria = session.createCriteria(User.class);
        List<User> users = criteria.list();
        session.close();
        
        request.setAttribute("users", users);
        request.setAttribute("usersSize", users.size());
    }
        
    private void redirect(HttpServletRequest request, HttpServletResponse response, String address) 
            throws ServletException, IOException{
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
}
