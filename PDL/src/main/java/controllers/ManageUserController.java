package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Chat;
import models.Course;
import models.CourseSuggestion;
import models.File;
import models.Internationalization;
import models.User;
import models.UserForm;
import org.hibernate.Session;
import org.hibernate.Criteria;
import org.hibernate.Transaction;
import services.HibernateUtil;
import validators.GenericValidator;
import validators.UserValidator;

/**
 *
 * @author wesley
 *
 * @todo password strength
 */
public class ManageUserController extends HttpServlet {

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

        //NOTICE: in this context edit can be both creating a new user or updating a user
        if (action.equals("edit")){
            //extract userId (if available)
            boolean isUpdate;
            String queryString = request.getQueryString();
            //we are updating a user if the userId existst
            if (queryString.substring(queryString.indexOf("=")).length() > 1){
                isUpdate = true;
            }
            else{
                isUpdate = false;
            }
            
            //edit a user
            if (isUpdate) {
                //extract userId
                int userId = Integer.parseInt(queryString.substring(queryString.indexOf("=")+1));
                System.out.println("we are updating: " + userId);
                //get user from database and set in the request
                Session session = HibernateUtil.getSessionFactory().openSession();
                Transaction tx = session.beginTransaction();
                User managedUser = (User)session.load(User.class, userId);
                
                request.setAttribute("userId", managedUser.getUserId());
                request.setAttribute("username", managedUser.getUsername());
                request.setAttribute("firstname", managedUser.getFirstname());
                request.setAttribute("lastname", managedUser.getLastname());
                request.setAttribute("emailAddress", managedUser.getEmailAddress());
                request.setAttribute("position", managedUser.getPosition());
                request.setAttribute("isAdmin", managedUser.isIsAdmin());
                request.setAttribute("isManager", managedUser.isIsManager());
                request.setAttribute("isTeacher", managedUser.isIsTeacher());
                request.setAttribute("password", managedUser.getPassword());
                request.setAttribute("gender", managedUser.getGender());
                request.setAttribute("profileUrl", managedUser.getProfileUrl());
                
                session.close();
                request.setAttribute("isUpdate", true);
            } 
            //create a user
            else {
                request.setAttribute("isUpdate", false);
            }

            redirect(request, response, "/edit_user.jsp");
        } 
        //deleten van een user
        //@todo could be fancier
        else if (action.equals("delete")) {
            //extract userId
            String queryString = request.getQueryString();
            int userId = Integer.parseInt(queryString.substring(queryString.indexOf("=") + 1));

            System.out.println("deleting user: " + userId);
            
            //do the delete operation
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            User managedUser = (User) session.load(User.class, userId);
            User administrator = (User) session.load(User.class, 1);
            
            // delete all activity linked to the user
            String hql = "delete from Activity where user_userId= :userId";
            session.createQuery(hql).setInteger("userId", managedUser.getUserId()).executeUpdate();
            
            Criteria criteria;
            
            // delete the associated course suggestions
            criteria = session.createCriteria(CourseSuggestion.class);
            List<CourseSuggestion> courseSuggestions = criteria.list();
            
            for (int i=0;i<courseSuggestions.size();i++){
                if (courseSuggestions.get(i).getUser().getUserId() == managedUser.getUserId()){
                    session.delete(courseSuggestions.get(i));
                }
            }
            
            // update all associated documents
            criteria = session.createCriteria(File.class);
            List<File> documents = criteria.list();
            
            for (int i=0;i<documents.size();i++){
                if (documents.get(i).getOwner().getUserId() == managedUser.getUserId()){
                    documents.get(i).setOwner(administrator);
                    session.update(documents.get(i));
                }
            }
            
            // delete from jointable where user is enrolled
            criteria = session.createCriteria(Course.class);
            List<Course> courses = criteria.list();
            //@todo expensive operation
            for (int c=0;c<courses.size();c++){
                List<User> enrolledUsers = courses.get(c).getEnrolledUsers();
                
                for (int u=0;u<enrolledUsers.size();u++){
                    if (enrolledUsers.get(u).getUserId() == managedUser.getUserId()){
                        enrolledUsers.remove(enrolledUsers.get(u));
                        session.update(courses.get(c));
                    }
                }
            }
            
            // update all courses where the user is owner
            for (int i=0;i<courses.size();i++){
                if (courses.get(i).getOwner().getUserId() == managedUser.getUserId()){
                    courses.get(i).setOwner(administrator);
                    session.update(courses.get(i));
                }
            }
            
            // delete chats where the user is involved or where he is owner
            criteria = session.createCriteria(Chat.class);
            List<Chat> chats = criteria.list();
            
            for (int c=0;c<chats.size();c++){
                System.out.println("TEST");
                List<User> chatUsers = chats.get(c).getUsers();
                if (chatUsers != null){
                
                for (int cu=0;cu<chatUsers.size();cu++){ // delete all occurences of the user
                    if (chatUsers.get(cu).getUserId() == managedUser.getUserId()){
                        chats.get(c).getUsers().remove(cu);
                        session.update(chats.get(c));
                    }
                }
                
                if (chats.get(c).getCreated().getUserId() == managedUser.getUserId()){
                    chats.get(c).setUsers(null); // delete all OTHER USERS that are linked to the chat of the owner
                    session.update(chats.get(c));
                    
                    session.delete(chats.get(c)); // delete the chat itself
                }
                }
             }
            
            //update i18n
            Internationalization i18n = (Internationalization) session.load(Internationalization.class, 1);
            if (i18n.getAppliedBy() != null && i18n.getAppliedBy().getUserId() == managedUser.getUserId()){
                i18n.setAppliedBy(null);
            }
            if (i18n.getUpdatedBy() != null && i18n.getUpdatedBy().getUserId() == managedUser.getUserId()){
                i18n.setUpdatedBy(null);
            }
            session.save(i18n);
            
            // delete education
            hql = "delete from Education where user_userId= :userId";
            session.createQuery(hql).setInteger("userId", managedUser.getUserId()).executeUpdate();
            
            // delete project
            hql = "delete from Project where user_userId= :userId";
            session.createQuery(hql).setInteger("userId", managedUser.getUserId()).executeUpdate();
            
            // delete work
            hql = "delete from Work where user_userId= :userId";
            session.createQuery(hql).setInteger("userId", managedUser.getUserId()).executeUpdate();
            
            // delete education
            hql = "delete from NewsItem where editedBy_userId= :userId";
            session.createQuery(hql).setInteger("userId", managedUser.getUserId()).executeUpdate();
            
            
             session.delete(managedUser);
             tx.commit();
             session.close();
 
             response.sendRedirect("../management");
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

        if (action.equals("edit") || action.equals("new")) {
            System.out.println("action equals: " + action);
            boolean isUpdate = (action.equals("edit") ? true : false);

            //step 1: do a form validation
            UserForm userForm = new UserForm();
            userForm.setUsername(request.getParameter("username"));
            userForm.setFirstname(request.getParameter("firstname"));
            userForm.setLastname(request.getParameter("lastname"));
            userForm.setEmailAddress(request.getParameter("emailAddress"));
            userForm.setPosition(request.getParameter("position"));
            userForm.setPassword(request.getParameter("password"));
            UserValidator validator = new UserValidator();
            List<String> errors = validator.validate(userForm);

            //step 1b: check if username exists (username must not be empty)
            List result = null;
            if (!GenericValidator.isEmpty("username")) {
                Session session = HibernateUtil.getSessionFactory().openSession();
                String hql = "from User where username = ?";
                result = session.createQuery(hql)
                        .setString(0, request.getParameter("username"))
                        .list();
                session.close();

                //on creating user
                if (!isUpdate && result != null && !result.isEmpty()) {
                    errors.add("Username already exists");
                } 
                //on updating user
                else {
                    //check if username is changed (and still unique) while editing
                    if (!result.isEmpty()) {
                        User user = (User) result.get(0);
                        if (request.getParameter("username").equals(user.getUsername())) {
                            if (Integer.parseInt(request.getParameter("userId")) != user.getUserId()) {
                                errors.add("Username already exists");
                            }
                        }
                    }
                }
            }
            //step 2: redirect user back if there are any errors
            if (!errors.isEmpty()) {
                //userId needs only to be set when we are editing
                if (isUpdate) {
                    request.setAttribute("userId", request.getParameter("userId"));
                    //don't forget to set that we are still updating
                    request.setAttribute("isUpdate", true);
                }
                request.setAttribute("username", request.getParameter("username"));
                request.setAttribute("firstname", request.getParameter("firstname"));
                request.setAttribute("lastname", request.getParameter("lastname"));
                request.setAttribute("emailAddress", request.getParameter("emailAddress"));
                request.setAttribute("position", request.getParameter("position"));
                request.setAttribute("isAdmin", request.getParameter("isAdmin"));
                request.setAttribute("isManager", request.getParameter("isManager"));
                request.setAttribute("isTeacher", request.getParameter("isTeacher"));
                request.setAttribute("password", request.getParameter("password"));
                request.setAttribute("gender", request.getParameter("gender"));
                request.setAttribute("profileUrl", request.getParameter("profileUrl"));

                //vergeet de errors niet op de request te zetten
                request.setAttribute("errorsSize", errors.size());
                request.setAttribute("errors", errors);

                redirect(request, response, "/edit_user.jsp");
            } 
            else {
                //step 3: there are no errors. We can start to create or update a user
                User user;
                Session session = HibernateUtil.getSessionFactory().openSession();
                Transaction tx = session.beginTransaction();

                //get the userId if we are updating a user
                if (isUpdate) {
                    int userId = Integer.parseInt(request.getParameter("userId"));
                    user = (User) session.load(User.class, userId);
                } else {
                    user = new User();
                }
                user.setUsername(request.getParameter("username"));
                user.setFirstname(request.getParameter("firstname"));
                user.setLastname(request.getParameter("lastname"));
                user.setEmailAddress(request.getParameter("emailAddress"));
                user.setPosition(request.getParameter("position"));
                user.setGender(request.getParameter("gender"));
                user.setProfileUrl(request.getParameter("profileUrl"));
                user.setIsAdmin((request.getParameter("isAdmin") != null ? true : false));
                user.setIsManager((request.getParameter("isManager") != null ? true : false));
                user.setIsTeacher((request.getParameter("isTeacher") != null ? true : false));

                if (isUpdate) {
                    //check if password is changed
                    if (!user.getPassword().equals(request.getParameter("password"))) {
                        user.setPassword(User.md5(request.getParameter("password")));
                    }
                } 
                else {
                    user.setPassword(User.md5(request.getParameter("password")));
                }

                session.saveOrUpdate(user);
                tx.commit();
                session.close();

                //request handling
                if (isUpdate) {
                    request.setAttribute("userUpdated", true);
                } 
                else {
                    request.setAttribute("userCreated", true);
                }
                
                request.setAttribute("userId", user.getUserId());
                request.setAttribute("username", user.getUsername());
                request.setAttribute("firstname", user.getFirstname());
                request.setAttribute("lastname", user.getLastname());
                request.setAttribute("emailAddress", user.getEmailAddress());
                request.setAttribute("position", user.getPosition());
                request.setAttribute("isAdmin", user.isIsAdmin());
                request.setAttribute("isManager", user.isIsManager());
                request.setAttribute("isTeacher", user.isIsTeacher());
                request.setAttribute("password", user.getPassword());
                request.setAttribute("gender", user.getGender());
                request.setAttribute("profileUrl", user.getProfileUrl());
                
                //we are now editing
                request.setAttribute("isUpdate", true);
                
                redirect(request, response, "/edit_user.jsp");
            }
        }
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String address)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
}
