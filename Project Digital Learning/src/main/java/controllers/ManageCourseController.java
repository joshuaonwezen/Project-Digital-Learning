package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Course;
import models.CourseForm;
import models.Skill;
import models.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import services.HibernateUtil;
import validators.CourseValidator;

/**
 *
 * @author wesley
 * @todo de user die is ingelogd moet ook default geselecteerd staan in edit_course onder owner (wanneer je een nieuwe course aanmaakt)
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
        
        //NOTICE: in this context edit can be both creating a new course or updating a course
        if (action.equals("edit")){
            //extract courseId (if available)
            boolean isUpdate;
            String queryString = request.getQueryString();
            //we are updating a course if the courseId existss
            if (queryString.substring(queryString.indexOf("=")).length() > 1){
                isUpdate = true;
            }
            else{
                isUpdate = false;
            }
            
            //edit a course
            if (isUpdate) {
                //extract courseId
                int courseId = Integer.parseInt(queryString.substring(queryString.indexOf("=")+1));
                System.out.println("we are updating: " + courseId);
                //get course from database and set in the request
                Session session = HibernateUtil.getSessionFactory().openSession();
                Transaction tx = session.beginTransaction();
                Course managedCourse = (Course)session.load(Course.class, courseId);
                
                request.setAttribute("courseId", managedCourse.getCourseId());
                request.setAttribute("name", managedCourse.getName());
                request.setAttribute("description", managedCourse.getDescription());
                request.setAttribute("owner", managedCourse.getOwner());
                request.setAttribute("level", managedCourse.getLevel());
                request.setAttribute("skills", managedCourse.getSkills());
                request.setAttribute("isVisible", managedCourse.isIsVisible());
                request.setAttribute("courseSkills", managedCourse.getSkillsJSONFormat());
                
                session.close();
                request.setAttribute("isUpdate", true);
            } 
            //create a course
            else {
                request.setAttribute("isUpdate", false);
            }
            //set the users on the request so we can select them
            //van een course
            setUsersOnRequest(request);
            redirect(request, response, "/edit_course.jsp");
        } 
        //deleten van een course
        else if (action.equals("delete")) {
            //extract courseId
            String queryString = request.getQueryString();
            int courseId = Integer.parseInt(queryString.substring(queryString.indexOf("=") + 1));

            //do the delete operation
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Course managedCourse = (Course) session.load(Course.class, courseId);
            session.delete(managedCourse);
            tx.commit();
            session.close();

            response.sendRedirect("../management");
        }
        //course overview
        else if (action.equals("courses")) {
            //set all courses on request
            setCoursesOnRequest(request);                        
            redirect(request, response, "/courses.jsp");
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
        
        
        System.out.println("skills: " + request.getParameter("skills"));
        
        if (action.equals("edit") || action.equals("new")) {
            System.out.println("action equals: " + action);
            boolean isUpdate = (action.equals("edit") ? true : false);

            //step 1: do a form validation
            CourseForm courseForm = new CourseForm();
            courseForm.setCourseId(request.getParameter("courseId"));
            courseForm.setName(request.getParameter("name"));
            courseForm.setDescription(request.getParameter("description"));
            courseForm.setLevel(getSelectedOption(request, "levelValues"));
            courseForm.setOwner(getSelectedOption(request, "ownerValues"));
            courseForm.setSkills(request.getParameter("skills"));
            
            CourseValidator validator = new CourseValidator();
            List<String> errors = validator.validate(courseForm);

            //step 2: redirect user back if there are any errors
            if (!errors.isEmpty()) {
                //courseId needs only to be set when we are editing
                if (isUpdate) {
                    request.setAttribute("courseId", request.getParameter("courseId"));
                    //don't forget to set that we are still updating
                    request.setAttribute("isUpdate", true);
                }
                request.setAttribute("name", request.getParameter("name"));
                request.setAttribute("description", request.getParameter("description"));
                setUsersOnRequest(request);
                request.setAttribute("owner", request.getParameter("owner"));
                request.setAttribute("level", request.getParameter("level"));
                request.setAttribute("skills", request.getParameter("skills"));
                request.setAttribute("isVisible", request.getParameter("isVisible"));
                
                //vergeet de errors niet op de request te zetten
                request.setAttribute("errorsSize", errors.size());
                request.setAttribute("errors", errors);

                redirect(request, response, "/edit_course.jsp");
            } 
            else {
                System.out.println("there are no errors");
                //step 3: there are no errors. We can start to create or update a course
                //zoek de owner op
                Course course;
                User user;
                Session session = HibernateUtil.getSessionFactory().openSession();
                Transaction tx = session.beginTransaction();
                
                user = (User)session.load(User.class, Integer.parseInt(getSelectedOption(request, "ownerValues")));

                //get the courseId if we are updating a course
                if (isUpdate) {
                    int courseId = Integer.parseInt(request.getParameter("courseId"));
                    course = (Course) session.load(Course.class, courseId);
                } 
                else {
                    course = new Course();
                }
                course.setName(request.getParameter("name"));
                course.setLevel(Course.Level.valueOf(getSelectedOption(request, "levelValues")));
                course.setOwner(user);
                course.setDescription(request.getParameter("description"));
                course.setIsVisible((request.getParameter("isVisible") != null ? true : false));
                /**
                 * Skills
                 */
                List<Skill> skills = new ArrayList<Skill>(); //this our the skills that must be linked to the course

                Object obj = JSONValue.parse(request.getParameter("skills"));
                JSONArray array = (JSONArray) obj;

                //convert the skills from json string and punt them in a map (which has an id + value)
                for (int i = 0; i < array.size(); i++) {
                    String line = array.get(i).toString();
                    String id = line.substring(line.indexOf("id\":") + 5, line.indexOf("\",\""));
                    String text = line.substring(line.indexOf("text\":") + 7, line.length() - 2);

                    //if we can parse the id to a long, then it is an existing skill (it has an id) 
                    try {
                        long skillId = Long.parseLong(id);
                        Skill managedSkill = (Skill) session.load(Skill.class, skillId);
                        skills.add(managedSkill);
                        session.close();
                    } //otherwise we need to create it
                    catch (NumberFormatException e) {
                        skills.add(new Skill(text));
                    }
                }
                course.setSkills(skills);

                session.saveOrUpdate(course);
                tx.commit();
                session.close();
                
                //request handling
                if (isUpdate) {
                    request.setAttribute("courseUpdated", true);
                } 
                else {
                    request.setAttribute("courseCreated", true);
                }
                
                request.setAttribute("courseId", course.getCourseId());
                request.setAttribute("name", course.getName());
                request.setAttribute("level", course.getLevel());
                request.setAttribute("owner", course.getOwner());
                request.setAttribute("description", course.getDescription());
                request.setAttribute("isVisible", course.isIsVisible());
                System.out.println("Sjson: " + course.getSkillsJSONFormat());
                request.setAttribute("courseSkills", course.getSkillsJSONFormat());
                
                //@TODO
                //setUsersOnRequest(request);
                //setSkillsOnRequest(request);
                
                //we are now editing
                request.setAttribute("isUpdate", true);
                
                redirect(request, response, "/edit_course.jsp");
            }
        }
    }
    private void setSkillsOnRequest(HttpServletRequest request){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Criteria criteria = session.createCriteria(Skill.class);
        List<Skill> skills = criteria.list();
        session.close();
        
        request.setAttribute("skills", skills);
        request.setAttribute("skillsSize", skills.size());
    }
    
    private void setCoursesOnRequest(HttpServletRequest request){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Criteria criteria = session.createCriteria(Course.class);
        List<Course> courses = criteria.list();
        session.close();
        
        request.setAttribute("courses", courses);
        request.setAttribute("coursesSize", courses.size());
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
    
    private String getSelectedOption(HttpServletRequest request, String selectElement){
        String[] selectValues = request.getParameterValues(selectElement);
        String selected="";
        for (String item : selectValues){
            selected=item;
        }
        return selected;
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String address)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
        System.out.println("redirected");
    }
}
