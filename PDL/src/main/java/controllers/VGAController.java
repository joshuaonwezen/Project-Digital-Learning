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
import models.CourseSuggestion;
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
            setSkillsToCheckByVGAOnRequest(request);
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
        
        //1d. create a list which holds our ouput from below
        List<UserVGAStatus> userVGAStatuses = new ArrayList();
        
        System.out.println();
        System.out.println("1. skill to look for: " + skill.getName());
            
            //2b. create a list with users that own this skill and that don't own the skill
            List<User> usersWithSkill = new ArrayList();
            List<User> usersWithoutSkill = new ArrayList();
            for(int t = 0; t < users.size(); t++) {
                List<Skill> userSkills = users.get(t).getSkills();
                boolean foundSkill = false;
                for(int u = 0; u < userSkills.size(); u++) {
                    //2c. user owns the skill: add it to the found list
                    if(userSkills.get(u).getSkillId() == skill.getSkillId()) {
                        userVGAStatuses.add(new UserVGAStatus(users.get(t), "Skill Owner", "No action required"));
                        usersWithSkill.add(users.get(t)); // user owns the skill
                        foundSkill = true;
                    }
                }
                //2d. user doesn't own the skill: add it to the not found list
                if (!foundSkill){
                    usersWithoutSkill.add(users.get(t));
                }
            }
            
            List<Course> coursesWithSkill = new ArrayList(); // this list holds our courses that provide the skill
            //3a. create a list with courses that provide this skill
            if (!usersWithoutSkill.isEmpty()){ // check only if their are users that needs to be checked
                    for (int a=0;a<courses.size();a++){
                        System.out.print("2-" + a + ". checking course: " + courses.get(a).getName());
                        if (courses.get(a).isIsVisible()){//only iterate over courses that are visible
                            List<Skill> temp = courses.get(a).getSkills(); // this list holds all the skills from the course
                            for (Skill skillI: temp){
                                if (skillI.getSkillId() == skill.getSkillId()){
                                    //we found it
                                    System.out.println(" - SUGGESTABLE");
                                    coursesWithSkill.add(courses.get(a));
                                    break;
                                } // end if 
                            } // end for
                        } // end if
                    } // end for
            } // end if
            System.out.println();
            
            //4a. check for every user that doesnt own the skill
            for (int u=0; u < usersWithoutSkill.size(); u++){
                System.out.println("3-" + u + ". user to look for: " + usersWithoutSkill.get(u).getUsername());
                
                //4b. check if their is a course which provides the skill
                if (!coursesWithSkill.isEmpty()){
                    //4c. now sum the courses and suggest it to the user
                    String coursesSummed = "";
                    for (Course c : coursesWithSkill){
                        coursesSummed+= c.getName() + ", ";
                        
                        
                          //new course suggestion
                        CourseSuggestion coursesuggestion = new CourseSuggestion(c,usersWithoutSkill.get(u));
                        insertCourseSuggestion(coursesuggestion);
                        
                    }
                    coursesSummed = coursesSummed.substring(0, coursesSummed.length()-2);//remove the last comma of the sum up
                    
                    System.out.println("3b. Suggesting course");
                    userVGAStatuses.add(new UserVGAStatus(usersWithoutSkill.get(u), "Missing Skill", "Course(s) " + coursesSummed + " suggested"));
                    //create an activity feed for this user (suggest it)
                    String title = "Course Suggestion";
                    String message = "The course(s) " + coursesSummed + " could probably contribute to the missing skill: " + skill.getName();
                    insertActivity(title, message, new Date(), usersWithoutSkill.get(u));
                }
                //5a. there are no courses which provide the skill: check if their are other users who can help
                else{
                    if (!usersWithSkill.isEmpty()){
                        //pick a random user who can help the user that doesnt own the skill
                        Random randomNr = new Random();
                        User helper = usersWithSkill.get(randomNr.nextInt(usersWithSkill.size()));
                        System.out.println("4. user " + usersWithoutSkill.get(u).getUsername() + " misses the skill, helper: " + helper.getUsername());

                        userVGAStatuses.add(new UserVGAStatus(usersWithoutSkill.get(u), "Missing Skill", helper.getFirstname() + " " + helper.getLastname() + " recommended to help"));
                        
                        //suggest the helper to the user that misses the skill
                        String title = "Missing Skill";
                        String message = helper.getFirstname() + " " + helper.getLastname() + " could help you acquire the Skill " + skill.getName() + ".";
                        insertActivity(title, message, new Date(), usersWithoutSkill.get(u));
                        
                        //let the user that owns the skill know that someone needs his help
                        title = "Someone needs your help";
                        message = usersWithoutSkill.get(u).getFirstname() + " " + usersWithoutSkill.get(u).getLastname() + " could need your help acquiring the Skill " + skill.getName() + ".";
                        insertActivity(title, message, new Date(), helper);
                    }
                    //at this else-point we know for sure that a new course needs to be created
                    else{
                        userVGAStatuses.add(new UserVGAStatus(null, "New Course Required", "There are no existing courses or users with this skill."));
                        System.out.println("END: a new course should be created.");
                        break;
                    }
                }
        }
        session.close();
        
        //set output on the request
        request.setAttribute("userVGAStatusesSize", userVGAStatuses.size());
        request.setAttribute("userVGAStatuses", userVGAStatuses);
        
        setSkillsOnRequest(request);//set skills on the request where we can choose from in the sweep
        setSkillsToCheckByVGAOnRequest(request);
        redirect(request, response, "/vga.jsp");
        
        }
        else if (action.equals("updatePeriodic")){
            //match the skills from the request agains the database and set the flag for checkByVGA = 1
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            
            Criteria criteria = session.createCriteria(Skill.class);
            List<Skill> skills = criteria.list(); // all the skills
            System.out.println("TEST: " + request.getParameter("tagSkillsPeriodic"));
            List<String> skillsFromRequest = Arrays.asList(request.getParameter("tagSkillsPeriodic").split(","));
            
            for (int i=0;i<skills.size();i++){
                boolean found = false;
                for (String temp : skillsFromRequest){
                    if (skills.get(i).getName().equals(temp)){ // we found it: set check = 1
                        skills.get(i).setCheckByVGA(true);
                        found = true;
                    }
                }
                if (!found){
                    skills.get(i).setCheckByVGA(false); // no need to check for (any more)
                }
            }
            //update the skills
            for (Skill skill : skills){
                session.update(skill);
            }
            tx.commit();
            session.close();
            
            //redirect
            request.setAttribute("editedPSkills", true);
            setSkillsToCheckByVGAOnRequest(request);
            setSkillsOnRequest(request);//set skills on the request where we can choose from in the sweep
            redirect(request, response, "/vga.jsp");
        }
    }
    
          private void insertCourseSuggestion(CourseSuggestion coursesuggestion){         
        //insert it in the db
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
        session.saveOrUpdate(coursesuggestion);      
        tx.commit();
        session.close();
       
            
        System.out.println("COURSE SUGGESTED");
    }
        
    private void insertActivity(String title, String message, Date sent, User user){
        //create new activity item
        Activity a = new Activity(title, message, sent, user);
        
        //insert it in the db
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        session.saveOrUpdate(a);
        tx.commit();
        session.close();
        System.out.println("ACTIVITY SAVED");
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
    
    private void setSkillsToCheckByVGAOnRequest(HttpServletRequest request){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Criteria criteria = session.createCriteria(Skill.class);
        List<Skill> skillsTemp = criteria.list();
        session.close();
        
        //filter on checkByVGA
        List<String> skills = new ArrayList<String>();
        for (Skill skill : skillsTemp){
            if (skill.isCheckByVGA()){
                skills.add(skill.getName());
            }
        }
        
        request.setAttribute("skillsToCheckByVGA", skills);
        request.setAttribute("skillsToCheckByVGASize", skills.size());
    }
    
    private void redirect(HttpServletRequest request, HttpServletResponse response, String address)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }

}
