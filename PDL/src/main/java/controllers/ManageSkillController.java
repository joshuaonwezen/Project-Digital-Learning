package controllers;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Skill;
import models.SkillForm;
import models.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.HibernateUtil;
import validators.SkillValidator;

/**
 *
 * @author Shahin Mokhtar
 */
public class ManageSkillController extends HttpServlet {

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

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        //get the action
        String uri = request.getRequestURI();
        String action = uri.substring(uri.lastIndexOf("/") + 1);

        //NOTICE: in this context edit can be both creating a new user or updating a user
        if (action.equals("edit")) {
            //extract skillId (if available)
            boolean isUpdate;
            String queryString = request.getQueryString();
            //we are updating a skill if the skillId existst
            if (queryString.substring(queryString.indexOf("=")).length() > 1) {
                isUpdate = true;
            } 
            else {
                isUpdate = false;
            }

            //edit a skill
            if (isUpdate) {
                //extract skillId
                int id = Integer.parseInt(queryString.substring(queryString.indexOf("=") + 1));
                //get skill from database and set in the request
                long skillId = Long.parseLong(request.getParameter("id"));
                request.setAttribute("skillId", skillId);
                Skill skill = (Skill) session.load(Skill.class, skillId);

                // Place in request
                request.setAttribute("name", skill.getName());
                request.setAttribute("level", skill.getLevel());
                request.setAttribute("description", skill.getDescription());
                session.close();

                request.setAttribute("isUpdate", true);
            } //create a skill
            else {
                request.setAttribute("isUpdate", false);
            }

            redirect(request, response, "/edit_skill.jsp");
        } //deleten (unlink) skill from user
        else if (action.equals("delete")) {
            //extract the id of the skill from the url
            String queryString = request.getQueryString();
            long skillId = Long.parseLong(queryString.substring(queryString.indexOf("=") + 1));
            
            //now remove the skill that is linked to the user
            int userId = Integer.parseInt(request.getSession().getAttribute("loggedInUserId").toString());
            Transaction tx = session.beginTransaction();
            User managedUser = (User) session.load(User.class, userId);
            List<Skill> skills = managedUser.getSkills();
            
            int temp=0;
            //check to see which item we need to remove from the list
            for (int i=0;i<skills.size();i++){
                if (skills.get(i).getSkillId() == skillId){
                    temp = i;
                    break;
                }
            }
            skills.remove(temp);
            managedUser.setSkills(skills);
            session.update(managedUser);
            tx.commit();
            
            response.sendRedirect("../profile?id=" + userId);
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
            SkillForm skillForm = new SkillForm();
            skillForm.setName(request.getParameter("name"));
            skillForm.setLevel(request.getParameter("level"));
            skillForm.setDescription(request.getParameter("description"));

            SkillValidator validator = new SkillValidator();
            List<String> errors = validator.validate(skillForm);
            
            //step 2: redirect user back if there are any errors
            if (!errors.isEmpty()) {
                //skillId needs only to be set when we are editing
                if (isUpdate) {
                    request.setAttribute("skillId", request.getParameter("skillId"));
                    //don't forget to set that we are still updating
                    request.setAttribute("isUpdate", true);
                }
                request.setAttribute("name", request.getParameter("name"));
                request.setAttribute("level", request.getParameter("level"));
                request.setAttribute("description", request.getParameter("description"));
                //placing errors in request
                request.setAttribute("errorsSize", errors.size());
                request.setAttribute("errors", errors);

                redirect(request, response, "/edit_skill.jsp");
            } else {
                //step 3: there are no errors. We can start to (create and) link skills
                Session session = HibernateUtil.getSessionFactory().openSession();
                Transaction tx = session.beginTransaction();
                
                int userId = Integer.parseInt(request.getSession().getAttribute("loggedInUserId").toString());
                User managedUser = (User) session.load(User.class, userId);
                List<Skill> skillsLinkedToUser = managedUser.getSkills();
                
                //step 3a: add skill to the database if it does not yet exist
                Criteria criteria = session.createCriteria(Skill.class);
                List<Skill> skillsFromDatabase = criteria.list();
                
                //we know for sure that we need to create a skill if their are no skills in the db
                if (skillsFromDatabase.isEmpty()) {
                    System.out.println("skill needs to be created because db is empty");
                    Skill newSkill = new Skill();
                    newSkill.setName(request.getParameter("name"));
                    session.save(newSkill);
                    tx.commit();
                    //now link it to the user
                    skillsLinkedToUser.add(newSkill);
                } 
                else {
                    for (int i = 0; i < skillsFromDatabase.size(); i++) {
                        if (skillsFromDatabase.get(i).getName().equals(request.getParameter("name"))) {
                            System.out.println("we hava match, now linking");
                            //we have a match so we don't have to create it, only link it
                            skillsLinkedToUser.add(skillsFromDatabase.get(i));
                            break;
                        } 
                        else if (i == skillsFromDatabase.size() - 1 && !request.getParameter("name").equals(skillsFromDatabase.get(i).getName())) {
                        //we don't have a match if we are at the end, 1. create it and 2. link it
                            System.out.println("skill needs to be created");
                            Skill newSkill = new Skill();
                            newSkill.setName(request.getParameter("name"));
                            session.save(newSkill);
                            tx.commit();
                            //now link it to the user
                            skillsLinkedToUser.add(newSkill);
                        }
                    }
                }
                //we need to get the user from the database so we can add skills to him
                tx = session.beginTransaction();
                managedUser = (User) session.load(User.class, userId);
  
                managedUser.setSkills(skillsLinkedToUser);
                
                session.saveOrUpdate(managedUser);
                tx.commit();
                session.close();


                redirect(request, response, "/edit_skill.jsp");
            }
        }
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String address)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
}
