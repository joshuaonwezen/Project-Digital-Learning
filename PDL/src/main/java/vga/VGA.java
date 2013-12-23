package vga;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import models.Activity;
import models.Course;
import models.Skill;
import models.User;
import models.UserVGAStatus;
import org.hibernate.Criteria;
import org.hibernate.Query;
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
        
        System.out.println("skill we are looking for: " + skills.get(0).getName());
        
        //1d. create a list which holds our ouput from below
        List<UserVGAStatus> userVGAStatuses = new ArrayList();
        
        //2a. check for every user:
        for (int i = 0; i < users.size(); i++) {
            System.out.println("================checking user: " + users.get(i).getUsername());
            //2b. get the skills from the user
            List<Skill> userSkills;
            
            
            
            
            //2c. check if the user has the skill we are looking for
            boolean found = false;
            for(int j = 0; j < userSkills.size(); j++) {
                System.out.println("userSkills.get" + userSkills.get(j).getName());
                if(userSkills.get(j).getSkillId() == skills.get(0).getSkillId()) {
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
                            if (skillI.getSkillId() == skills.get(0).getSkillId()){
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
                    
                        System.out.println("SHOULD SET ACTIVITI FEED");
                        userVGAStatuses.add(new UserVGAStatus(users.get(i), "Missing Skill", "Course(s) " + coursesSummed + " suggested"));
                    }
                    else{
                        System.out.println("we found no course to sggest");
                       //4a. find other users that do own this skill
                    List<User> usersWithSkill = new ArrayList();
                    for(int t = 0; t < users.size(); t++) {
                        List<Skill> userSkillWithSkill = users.get(t).getSkills();
                        for(int u = 0; u < userSkillWithSkill.size(); u++) {
                            if(userSkillWithSkill.get(u).getSkillId() == skills.get(0).getSkillId()) {
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
                        
                        System.out.println("SHOULD SET 2 ACTIVITY FEED");
                        
                        userVGAStatuses.add(new UserVGAStatus(users.get(i), "Missing Skill", helper.getFirstname() + " " + helper.getLastname() + " recommended to help"));
                        
                        //let the user that owns the skill know that someone needs his help
                        System.out.println("SHOULD SET 3 ACTIVIYR FEED");
                    }
                    else{
                        userVGAStatuses.add(new UserVGAStatus(null, "New Course Required", "There are no existing courses or users with this skill."));
                        break;//at this point we know for sure that a new course needs to be created
                    }
                }
                    
            }
        }
    }
}