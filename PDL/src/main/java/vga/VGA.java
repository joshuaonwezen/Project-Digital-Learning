package vga;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import models.Activity;
import models.Course;
import models.CourseSuggestion;
import models.Skill;
import models.User;
import models.UserVGAStatus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import services.HibernateUtil;

/**
 *
 * @author wesley
 * 
 * Scheduler for performing the VGA task
 */
public class VGA implements Job{

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {

        //1a. get all users
        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.findAll();
        
        //1b. get all the courses
        CourseDAO courseDAO = new CourseDAO();
        List<Course> courses = courseDAO.findAll();
        
        //1c. get the skill from the database where we need to search for
        SkillDAO skillDAO = new SkillDAO();
        List<Skill> skills = skillDAO.findAll();
        
        //1d. create a list which holds our ouput from below
        List<UserVGAStatus> userVGAStatuses = new ArrayList();
        
        //2a. check for every skill
        for (int s=0;s<skills.size();s++){
            System.out.println();
            System.out.println("1. skill to look for: " + skills.get(s).getName());
            
            //2b. create a list with users that own this skill and that don't own the skill
            List<User> usersWithSkill = new ArrayList();
            List<User> usersWithoutSkill = new ArrayList();
            for(int t = 0; t < users.size(); t++) {
                List<Skill> userSkills = users.get(t).getSkills();
                boolean foundSkill = false;
                for(int u = 0; u < userSkills.size(); u++) {
                    //2c. user owns the skill: add it to the found list
                    if(userSkills.get(u).getSkillId() == skills.get(s).getSkillId()) {
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
                                if (skillI.getSkillId() == skills.get(s).getSkillId()){
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
            
            //3b. filter the users that doesnt own the skill but are enrolled to a course which offers it
            if (!coursesWithSkill.isEmpty()){
                for (int t=0;t<usersWithoutSkill.size();t++){
                    for (int c=0;c<coursesWithSkill.size();c++){
                        List<User> usersEnrolledToCourse = coursesWithSkill.get(c).getEnrolledUsers(); // users that are enrolled to this course
                        System.out.println("WITHSKILL: " + usersEnrolledToCourse.size());
                        boolean found = false;
                        for (User tempUser : usersEnrolledToCourse){
                            if (tempUser.getUserId() == usersWithoutSkill.get(t).getUserId()){
                                System.out.println("FONUD!!!");
                                found = true;
                            }
                        }
                        if (found){ // if we found a user that is enrolled to a course with the skill: delete it from the users we need to check for
                            usersWithoutSkill.remove(usersWithoutSkill.get(t));
                            System.out.println("REMOVING: " + usersWithoutSkill.get(t));
                        }
                    }
                }
            }
            
            
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
                    //create an activity feed for this user (suggest it)
                    String title = "Course Suggestion";
                    String message = "The course(s) " + coursesSummed + " could probably contribute to the missing skill: " + skills.get(s).getName();
                    insertActivity(title, message, new Date(), usersWithoutSkill.get(u));
                }
                //5a. there are no courses which provide the skill: check if their are other users who can help
                else{
                    if (!usersWithSkill.isEmpty()){
                        //pick a random user who can help the user that doesnt own the skill
                        Random randomNr = new Random();
                        User helper = usersWithSkill.get(randomNr.nextInt(usersWithSkill.size()));
                        System.out.println("4. user " + usersWithoutSkill.get(u).getUsername() + " misses the skill, helper: " + helper.getUsername());

                        //suggest the helper to the user that misses the skill
                        String title = "Missing Skill";
                        String message = helper.getFirstname() + " " + helper.getLastname() + " could help you acquire the Skill " + skills.get(s).getName() + ".";
                        insertActivity(title, message, new Date(), usersWithoutSkill.get(u));
                        
                        //let the user that owns the skill know that someone needs his help
                        title = "Someone needs your help";
                        message = usersWithoutSkill.get(u).getFirstname() + " " + usersWithoutSkill.get(u).getLastname() + " could need your help acquiring the Skill " + skills.get(s).getName() + ".";
                        insertActivity(title, message, new Date(), helper);
                    }
                    //at this else-point we know for sure that a new course needs to be created
                    else{
                        System.out.println("END: a new course should be created.");
                        break;
                    }
                }
            }
        }
    }
    
    private void insertCourseSuggestion(CourseSuggestion coursesuggestion){         
        //insert it in the db
        CourseSuggestionDAO courseSuggestionDAO = new CourseSuggestionDAO();
        
        if (!courseSuggestionDAO.existInDb(coursesuggestion)){ // prevent duplicates
            courseSuggestionDAO.insert(coursesuggestion);
        }
        
        System.out.println("COURSE SUGGESTED");
    }
    
    private void insertActivity(String title, String message, Date sent, User user){
        //create new activity item
        Activity a = new Activity(title, message, sent, user);
        
        //insert it in the db
        ActivityDAO activityDAO = new ActivityDAO();
        activityDAO.insert(a);
        System.out.println("ACTIVITY SAVED");
    }
}