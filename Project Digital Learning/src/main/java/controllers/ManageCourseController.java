package controllers;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Course;
import models.CourseForm;
import models.User;
import models.UserForm;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.HibernateUtil;
import validators.CourseValidator;
//import services.HibernateUtil;
import validators.UserValidator;

/**
 *
 * @author martijn
 * @todo de user die is ingelogd moet ook default geselecteerd staan in edit_course onder owner (wanneer je een nieuwe course aanmaakt)
 * 
 */
public class ManageCourseController extends HttpServlet {

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

        if (action.equals("edit")){
            boolean isUpdate=false;
            
            //extract courseId
            String queryString = request.getQueryString();
            //als er een id is meegegeven gaan we wijzigen, anders een nieuwe course aanmaken
            if (queryString.substring(queryString.indexOf("=")).length() > 1){
                isUpdate = true;
            }
            //wijzigen van een course
            if (isUpdate){
                int courseId = Integer.parseInt(queryString.substring(queryString.indexOf("=")+1));
            
                //haal de course op uit de database om deze vervolgens in de request te zetten
                Session session = HibernateUtil.getSessionFactory().openSession();
                Transaction tx = session.beginTransaction();
                Course managedCourse = (Course)session.load(Course.class, courseId);
                
                //set ze nu in de request
                request.setAttribute("courseId", managedCourse.getCourseId());
                request.setAttribute("name", managedCourse.getName());
                request.setAttribute("description", managedCourse.getDescription());
                request.setAttribute("level", managedCourse.getLevel().getLevel());
                request.setAttribute("ownerId", managedCourse.getOwner().getUserId());
                session.close();
                
                //geef aan dat we gaan updaten
                request.setAttribute("update", true);
            }
            else{
                //geef aan dat we een nieuwe course gaan aanmaken
                request.setAttribute("update", false);
            }
            //set the users op de request zodat we deze kunnen selecteren als eigenaar
            //van een course
            setUsersOnRequest(request);
            
            redirect(request, response, "/edit_course.jsp");
        }
        //deleten van een course
        else if (action.equals("delete")){
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            //extract courseId
            String queryString = request.getQueryString();
            int courseId = Integer.parseInt(queryString.substring(queryString.indexOf("=")+1));
            Course managedCourse = (Course)session.load(Course.class, courseId);
            session.delete(managedCourse);
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
        
        System.out.println("POST action: " + action);
        
        //we moeten eerst een formvalidate doen als we een course gaan bewerken of toevoegen
        if (action.equals("new") || action.equals("edit")){
            CourseForm courseForm = new CourseForm();
            courseForm.setName(request.getParameter("name"));
            courseForm.setDescription(request.getParameter("description"));
            courseForm.setLevel(getSelectedOption(request, "levelValues"));
            courseForm.setOwner(getSelectedOption(request, "ownerValues"));
            
            CourseValidator validator = new CourseValidator();
            List<String> errors = validator.validate(courseForm);
            
            //wanneer we geen errors hebben kunnen we een course gaan bewerken of toevoegen
            if (errors.isEmpty()) {
                System.out.println("there are no errors");
                
                //zoek de owner op
                Session session = HibernateUtil.getSessionFactory().openSession();
                User user = (User)session.load(User.class, Integer.parseInt(getSelectedOption(request, "ownerValues")));
                session.close();
                
                //aanmaken van een course
                if (action.equals("new")) {
                    System.out.println("action equals new");
                    session = HibernateUtil.getSessionFactory().openSession();
                    Transaction tx = session.beginTransaction();

                    Course course = new Course();
                    course.setName(request.getParameter("name"));
                    course.setDescription(request.getParameter("description"));
                    course.setLevel(Course.Level.valueOf(getSelectedOption(request, "levelValues")));
                    course.setOwner(user);
                    
                    session.save(course);
                    tx.commit();
                    session.close();
                } 
                //wijzigen van een gebruiker
                else if (action.equals("edit")) {
                    System.out.println("action equals edit");
                    int courseId;

                    session = HibernateUtil.getSessionFactory().openSession();
                    Transaction tx = session.beginTransaction();

                    courseId = Integer.parseInt(request.getParameter("courseId"));
                    Course managedCourse = (Course) session.load(Course.class, courseId);

                    managedCourse.setName(request.getParameter("name"));
                    managedCourse.setDescription(request.getParameter("description"));
                    managedCourse.setLevel(Course.Level.valueOf(getSelectedOption(request, "levelValues")));
                    managedCourse.setOwner(user);
                    
                    session.saveOrUpdate(managedCourse);
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
                
                redirect(request, response, "/edit_course.jsp");
            }
        }
    }
    
    private String getSelectedOption(HttpServletRequest request, String selectElement){
        String[] selectValues = request.getParameterValues(selectElement);
        String selected="";
        for (String item : selectValues){
            selected=item;
        }
        System.out.println("RETURNING: " + selected);
        return selected;
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