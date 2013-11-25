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
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.HibernateUtil;
import validators.GenericValidator;
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
            } else {
                isUpdate = false;
            }

            //edit a skill
            if (isUpdate) {
                //extract skillId
                int id = Integer.parseInt(queryString.substring(queryString.indexOf("=") + 1));
                System.out.println("we are updating: " + id);
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
        } //deleten van een skill
        else if (action.equals("delete")) {
            long skillId = Long.parseLong(request.getParameter("id"));
            Transaction tx = session.beginTransaction();
            Skill skill = (Skill) session.load(Skill.class, skillId);
            session.delete(skill);

            tx.commit();
            response.sendRedirect("../profile?id=");
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

            //step 1b: check if skill exists (skill id must not be empty)
            List result = null;
            if (!GenericValidator.isEmpty("skillId")) {
                Session session = HibernateUtil.getSessionFactory().openSession();
                String hql = "from Skill where skillId = ?";
                result = session.createQuery(hql)
                        .setString(0, request.getParameter("skillId"))
                        .list();
                session.close();

                //on creating skill
                if (!isUpdate && result != null && !result.isEmpty()) {
                    errors.add("Skill Id already exists");
                } //on updating skill
                else {
                    //check if skill id is changed (and still unique) while editing
                    if (!result.isEmpty()) {
                        Skill skill = (Skill) result.get(0);
                        if (request.getParameter("skillId").equals(skill.getSkillId())) {
                            if (Integer.parseInt(request.getParameter("skillId")) != skill.getSkillId()) {
                                errors.add("Skill Id already exists");
                            }
                        }
                    }
                }
            }
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
                //step 3: there are no errors. We can start to create or update a skill
                Skill skill;
                Session session = HibernateUtil.getSessionFactory().openSession();
                Transaction tx = session.beginTransaction();

                //get the skillId if we are updating a skill
                if (isUpdate) {
                    Long skillId = Long.parseLong(request.getParameter("skillId"));
                    skill = (Skill) session.load(Skill.class, skillId);
                } else {
                    skill = new Skill();
                }
                skill.setName(request.getParameter("name"));
                skill.setLevel(request.getParameter("level"));
                skill.setDescription(request.getParameter("description"));

                User user = new User();
                int userId = Integer.parseInt(request.getSession().getAttribute("loggedInUserId").toString());
                user.setUserId(userId);
                skill.setUser(user);

                session.saveOrUpdate(skill);
                tx.commit();
                session.close();

                //request handling
                if (isUpdate) {
                    request.setAttribute("skillUpdated", true);
                } else {
                    request.setAttribute("skillCreated", true);
                }

                request.setAttribute("skillId", skill.getSkillId());
                request.setAttribute("name", skill.getName());
                request.setAttribute("level", skill.getLevel());
                request.setAttribute("description", skill.getDescription());

                //we are now editing
                request.setAttribute("isUpdate", true);

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
