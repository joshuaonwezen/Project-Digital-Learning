package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Activity;
import models.Course;
import models.Skill;
import models.User;
import models.UserVGAStatus;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Query;
import services.HibernateUtil;

/**
 *
 * @author F4LLCON
 */
public class VGAController extends HttpServlet {

   

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
        
        if (action.equals("vga")){
            
            setSkillsOnRequest(request);
            redirect(request, response, "/vga.jsp");
            
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
        
        /**
         * There will be searched if a provided skill is acquired by the users. If not:
         * a. check if there is a course available with that skill, then suggest it to the user. DONE
         * b. check if there is another user that does own this skill, if yes let him know someone needs his help. DONE
         */
        if (action.equals("doSweep")){
            System.out.println("=====================");
            //1a. get all users
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Criteria criteria = session.createCriteria(User.class);
        List<User> users = criteria.list();
        //1b. get all the courses
        criteria = session.createCriteria(Course.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);//prevent duplicate courses
        List<Course> courses = criteria.list();
        
        //1c. get the skill from the request where we need to search for
        List<String> skillsFromRequest = Arrays.asList(request.getParameter("tagSkills").split(","));
        
        Query query = session.createQuery("from Skill where name = :name ");
        query.setParameter("name", skillsFromRequest.get(0));
        List list = query.list();
        Skill skill = (Skill)list.get(0);
        System.out.println("skill we are looking for: " + skill.getName());
        
        //1d. create a list which hols our ouput from below
        List<UserVGAStatus> userVGAStatuses = new ArrayList();
        
        //2a. check for every user:
        for (int i = 0; i < users.size(); i++) {
            System.out.println("================checking user: " + users.get(i).getUsername());
            //2b. get the skills from the user
            List<Skill> userSkills = users.get(i).getSkills();
            //2c. check if the user has the skill we are looking for
            boolean found = false;
            for(int j = 0; j < userSkills.size(); j++) {
                System.out.println("userSkills.get" + userSkills.get(j).getName());
                if(userSkills.get(j).equals(skill)) {
                    System.out.println("user " + users.get(i).getUsername() + " owns skill");
                    found = true;
                    userVGAStatuses.add(new UserVGAStatus(users.get(i), "Skill Owner", "No action required"));
                    break;
                }
            }
            //this for loop from 2c. will ofcourse only run when we have at least one skill, so make an extra check
            if (userSkills.isEmpty()){
                found = false;
            }
                if (!found){
                    System.out.println("user " + users.get(i).getUsername() + " doesnt own the skill - checking for course");
                    //3a. check the courses that provide this skill
                    List<Course> coursesWithSkill = new ArrayList(); // this list holds our courses that provide the skill
                    for (int a=0;a<courses.size();a++){
                        System.out.println("CHECKING COURSE: " + courses.get(a).getName());
                        if (courses.get(a).isIsVisible()){//only iterate over courses that are visible
                        List<Skill> temp = courses.get(a).getSkills(); // this list holds all the skills from the course
                        for (Skill skillI: temp){
                            if (skillI.equals(skill)){
                                //we found it
                                coursesWithSkill.add(courses.get(a));
                                break;
                            }
                        }
                    }
                    }
                    //3b. check if their is a course with the provided skill
                    if (!coursesWithSkill.isEmpty()){
                        System.out.println("we found a course to suggest!!");
                        //suggest it in the activity feed
                        String coursesSummed = "";
                        for (Course c : coursesWithSkill){ //sum all the courses
                            coursesSummed+= c.getName() + ", ";
                        }
                        coursesSummed = coursesSummed.substring(0, coursesSummed.length()-2);//remove the last comma of the sum up
                    
                        tx = session.beginTransaction();
                        //now create an activity feed for this user
                        Activity activityCourseFound = new Activity();
                        activityCourseFound.setTitle("Course Suggestion");
                        activityCourseFound.setMessage("The course(s) " + coursesSummed + " could probably contribute to the missing skill: " + skill.getName());
                        activityCourseFound.setSent(new Date());
                        activityCourseFound.setUser(users.get(i));
                        session.save(activityCourseFound);
                        tx.commit();
                        userVGAStatuses.add(new UserVGAStatus(users.get(i), "Missing Skill", "Course(s) " + coursesSummed + " suggested"));
                    }
                    else{
                        System.out.println("we found no course to sggest");
                       //4a. find other users that do own this skill
                    List<User> usersWithSkill = new ArrayList();
                    for(int t = 0; t < users.size(); t++) {
                        List<Skill> userSkillWithSkill = users.get(t).getSkills();
                        for(int u = 0; u < userSkillWithSkill.size(); u++) {
                            if(userSkillWithSkill.get(u).equals(skill)) {
                                //4b. add the user to a list of users who own the skill
                                usersWithSkill.add(users.get(t));
                            }
                        }
                    } 
                    //4c. if we found a user who does own the skill we can create some activity
                    if(!usersWithSkill.isEmpty()) {
                        //pick a random user who can help the user that doesnt own the skill
                        Random randomNr = new Random();
                        User helper = usersWithSkill.get(randomNr.nextInt(usersWithSkill.size()));
                       System.out.println("user " + users.get(i).getUsername() + " misses the skill, helper: " + helper.getUsername());
                        
                        tx = session.beginTransaction();
                        //let the user that misses the skill know this
                        Activity activityMissSkill = new Activity();
                        activityMissSkill.setTitle("Missing Skill");
                        activityMissSkill.setMessage(helper.getFirstname() + " " + helper.getLastname() + " could help you acquire the Skill " + skill.getName() + ".");
                        activityMissSkill.setSent(new Date());
                        activityMissSkill.setUser(users.get(i));
                        
                        userVGAStatuses.add(new UserVGAStatus(users.get(i), "Missing Skill", helper.getFirstname() + " " + helper.getLastname() + " recommended to help"));
                        
                        //let the user that owns the skill know that someone needs his help
                        Activity activityOwnsSkill = new Activity();
                        activityOwnsSkill.setTitle("Someone needs your help");
                        activityOwnsSkill.setMessage(users.get(i).getFirstname() + " " + users.get(i).getLastname() + " could need your help acquiring the Skill " + skill.getName() + ".");
                        activityOwnsSkill.setSent(new Date());
                        activityOwnsSkill.setUser(helper);
                        
                        //save to the database
                        session.save(activityMissSkill);
                        session.save(activityOwnsSkill);
                        tx.commit();
                    }
                    else{
                        userVGAStatuses.add(new UserVGAStatus(null, "New Course Required", "There are no existing courses or users with this skill."));
                        break;//at this point we know for sure that a new course needs to be created
                    }
                }
                    
            }
        }
        session.close();
        
        //set output on the request
        request.setAttribute("userVGAStatusesSize", userVGAStatuses.size());
        request.setAttribute("userVGAStatuses", userVGAStatuses);
        
        setSkillsOnRequest(request);//set skills on the request where we can choose from in the sweep
        redirect(request, response, "/vga.jsp");
        
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
    
    private void redirect(HttpServletRequest request, HttpServletResponse response, String address)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }

}
