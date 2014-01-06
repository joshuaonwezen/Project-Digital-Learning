package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
                request.setAttribute("courseSkills", managedCourse.getSkillsSeperatedByComma());
                request.setAttribute("isVisible", managedCourse.isIsVisible());
                request.setAttribute("courseKey", managedCourse.getCourseKey());
                
                session.close();
                request.setAttribute("isUpdate", true);
            } 
            //create a course
            else {
                request.setAttribute("isUpdate", false);
            }
            //set the users and skills on the request so we can select them
            setSkillsOnRequest(request);
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
            
            for (int i=0;i<managedCourse.getEnrolledUsers().size();i++){ // delete all associations of users that are enrolled
                managedCourse.getEnrolledUsers().remove(managedCourse.getEnrolledUsers().get(i));
            }
            for (int i=0;i<managedCourse.getSkills().size();i++){ // delete all associated skills
                managedCourse.getSkills().remove(managedCourse.getSkills().get(i));
            }
            managedCourse.setOwner(null); // set the owner to null
            
            session.update(managedCourse); // update the course
            session.delete(managedCourse); // and delete it
            tx.commit();
            session.close();

            response.sendRedirect("../management");
        }
        //course overview
        else if (action.equals("courses")) {
            //set all courses on request
            setCoursesOnRequest(request);    
            //and the courses that a user is enrolled to
            int userId = Integer.parseInt(request.getSession().getAttribute("loggedInUserId").toString());
            setUserEnrolledCoursesOnRequest(request, userId);
            redirect(request, response, "/courses.jsp");
        }
        //search course on the course page
        else if (action.equals("searchCourse")){
            Session session = HibernateUtil.getSessionFactory().openSession();
            String searchQuery = request.getParameter("searchQuery");
            String hql = "FROM Course WHERE name LIKE '%" + searchQuery + "%' OR description LIKE '%" + searchQuery + "%'";
            List<Course> result = session.createQuery(hql).list();
            
            //set our results on the request and redirect back
            request.setAttribute("courses", result);
            request.setAttribute("coursesSize", result.size());
            request.setAttribute("coursesSizeResults", result.size());
            int userId = Integer.parseInt(request.getSession().getAttribute("loggedInUserId").toString());
            setUserEnrolledCoursesOnRequest(request, userId);
            
            redirect(request, response, "/courses.jsp");
            session.close();
        }
        else if (action.equals("enroll")){
            //extract the courseId
            int courseId = Integer.parseInt(request.getParameter("courseId"));
            //enroll the user in the course
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Course managedCourse = (Course) session.load(Course.class, courseId);
            
            List<User> enrolledUsers = managedCourse.getEnrolledUsers();
            //add the logged in user to the course
            int userId = Integer.parseInt(request.getSession().getAttribute("loggedInUserId").toString());
            User managedUser = (User) session.load(User.class, userId);
            //now add the user to the course and update the course
            enrolledUsers.add(managedUser);
            session.update(managedCourse);
            tx.commit();
            
            //redirect the user back and let it now he was enrolled
            request.setAttribute("enrolledIn", managedCourse.getName());
            setCoursesOnRequest(request);
            setUserEnrolledCoursesOnRequest(request, userId);
            redirect(request, response, "/courses.jsp");
            }
                else if (action.equals("withdraw")){
            //extract the courseId
            int courseId = Integer.parseInt(request.getParameter("courseId"));
            int userId = Integer.parseInt(request.getSession().getAttribute("loggedInUserId").toString());
            
            //remove the course from the user
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Course managedCourse = (Course) session.load(Course.class, courseId);
            User managedUser = (User) session.load(User.class, userId);
            
            managedCourse.getEnrolledUsers().remove(managedUser);
            session.update(managedCourse);
            tx.commit();
            session.close();
            
            
            //redirect the user back and let it now he was removed
            request.setAttribute("withdrawedFrom", managedCourse.getName());
            setCoursesOnRequest(request);
            setUserEnrolledCoursesOnRequest(request, userId);
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
            courseForm.setSkills(request.getParameter("tagSkills"));
            
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
                request.setAttribute("tagSkills", request.getParameter("tagSkills"));
                request.setAttribute("isVisible", request.getParameter("isVisible"));
                setSkillsOnRequest(request);
                
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
                /**
                 * GOD KNOWS WHY but if you don't use this user.getFirstname(), hibernate will through an 
                 * org.hibernate.LazyInitializationException: could not initialize proxy - no Session
                 */
                user.getFirstname();
                
                course.setName(request.getParameter("name"));
                course.setCourseKey();
                course.setLevel(Course.Level.valueOf(getSelectedOption(request, "levelValues")));
                course.setOwner(user);
                course.setDescription(request.getParameter("description"));
                course.setIsVisible((request.getParameter("isVisible") != null ? true : false));
                /**
                 * Skills
                 */
                List<Skill> courseSkills = new ArrayList<Skill>(); //this our the skills that must be linked to the course
                List<String> skillsFromRequest = Arrays.asList(request.getParameter("tagSkills").split(","));

                Criteria criteria = session.createCriteria(Skill.class);
                List<Skill> skillsFromDatabase = criteria.list();
                
                /**
                 * here we are matching skills from the request against skills from the database
                 * if we have a match: add it to our courseSkill list, else create a new skill and add it to the courseSkill list
                 */
                if (skillsFromDatabase.isEmpty()){
                    //we know for sure that a skill needs to be added to the database if the database contains no skills
                    for (int i=0;i<skillsFromRequest.size();i++){
                        Skill newSkill = new Skill();
                        newSkill.setName(skillsFromRequest.get(i));
                        session.save(newSkill);
                        courseSkills.add(newSkill);
                    }
                }
                for (int i=0; i<skillsFromRequest.size(); i++){
                    for (int j=0; j<skillsFromDatabase.size();j++){
                        //when we enter this if we found a match from the database and use this to add it to our course
                        if (skillsFromRequest.get(i).equals(skillsFromDatabase.get(j).getName())){
                            courseSkills.add(skillsFromDatabase.get(j));
                            break;
                        }
                        //when we enter the else if it means that we did not found the skill from our database
                        else if (j==skillsFromDatabase.size()-1 && !skillsFromRequest.get(i).equals(skillsFromDatabase.get(j).getName())){
                            System.out.println("saving new skill");
                            Skill newSkill = new Skill();
                            newSkill.setName(skillsFromRequest.get(i));
                            session.save(newSkill);
                            courseSkills.add(newSkill);
                        }
                    }
                }
                course.setSkills(courseSkills);

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
                request.setAttribute("courseSkills", course.getSkillsSeperatedByComma());
                
                setUsersOnRequest(request);
                setSkillsOnRequest(request);
                
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
    
    /**
     * @todo This is a very expensive operation as it needs to go along every course and user linked to that course
     */
    private void setUserEnrolledCoursesOnRequest(HttpServletRequest request, int userId){
        //1: get all courses
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Criteria criteria = session.createCriteria(Course.class);
        List<Course> courses = criteria.list();
        
        List<Integer> enrolledCourses = new ArrayList<Integer>();
        //2: get the courses that a user is enrolled to
        for (Course course : courses){
            for (User user : course.getEnrolledUsers()){
                if (user.getUserId() == userId){
                    //user is enrolled in this course
                    enrolledCourses.add(course.getCourseId());
                    break;
                }
            }
        }
        
        session.close();
        System.out.println("there are: " + enrolledCourses.size() + "enrolledcourses");
        
        //3: set the course id's of the courses that a user is enrolled to in the request
        request.setAttribute("userEnrolledCourses", enrolledCourses);
        request.setAttribute("userEnrolledCoursesSize", enrolledCourses.size());
    } 
    
    private void setCoursesOnRequest(HttpServletRequest request){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Criteria criteria = session.createCriteria(Course.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);//prevent duplicate courses
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
        
        //filter on admins, teachers and managers
        List<User> usersWithRights = new ArrayList<User>();
        
        for (User user : users){
            if (user.isIsAdmin() || user.isIsManager() || user.isIsTeacher()){
                usersWithRights.add(user);
            }
        }
        
        session.close();
        
        request.setAttribute("users", usersWithRights);
        request.setAttribute("usersSize", usersWithRights.size());
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
    }
}
