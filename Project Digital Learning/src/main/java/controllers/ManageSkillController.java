package controllers;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Project;
import models.Skill;
import models.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.HibernateUtil;

/**
 *
 * @author Shahin
 */
public class ManageSkillController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        /* User information */
        List<User> tempSkill = new LinkedList();
        // Put session into variable criteria
        Criteria criteria = session.createCriteria(User.class);
        tempSkill = criteria.list();
        request.setAttribute("skillList", tempSkill);

        /* Get action */
        String uri = request.getRequestURI();
        String action = uri.substring(uri.lastIndexOf("/") + 1);

        if (action.equals("edit")) {
            boolean isUpdate = false;

            /* extract Id */
            String queryString = request.getQueryString();
            // If id = true, edit, else new
            if (queryString.substring(queryString.indexOf("=")).length() > 1) {
                isUpdate = true;
            }

            // if isUpdate = true, edit
            if (isUpdate) {

                long skillId = Long.parseLong(request.getParameter("id"));
                request.setAttribute("id", skillId);
                Skill skill = (Skill) session.load(Skill.class, skillId);

                // Place in request
                request.setAttribute("name", skill.getName());
                request.setAttribute("level", skill.getLevel());
                session.close();

                // Update
                request.setAttribute("update", true);
            } else {
                // New
                request.setAttribute("update", false);
            }
            redirect(request, response, "/edit_skill.jsp");
        } // Delete
        else if (action.equals("delete")) {

            long skillId = Long.parseLong(request.getParameter("id"));
            Transaction tx = session.beginTransaction();
            Skill skill = (Skill) session.load(Skill.class, skillId);
            session.delete(skill);

            tx.commit();

            response.sendRedirect("../profile");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /* Get action */
        String uri = request.getRequestURI();
        String action = uri.substring(uri.lastIndexOf("/") + 1);

        // New
        if (action.equals("new")) {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();

            Skill skill = new Skill();
            skill.setName(request.getParameter("name"));
            skill.setLevel(request.getParameter("level"));

            User user = new User();
            user.setUserId(Integer.parseInt(request.getParameter("user")));
            skill.setUser(user);

            session.save(skill);
            tx.commit();
            session.close();
        } // Edit 
        else if (action.equals("edit")) {

            long skillId = Long.parseLong(request.getParameter("id"));
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();

            Skill skill = (Skill) session.load(Skill.class, skillId);
            skill.setName(request.getParameter("name"));
            skill.setLevel(request.getParameter("level"));

            User user = new User();
            user.setUserId(Integer.parseInt(request.getParameter("user")));
            skill.setUser(user);

            session.saveOrUpdate(skill);
            tx.commit();
            session.close();
        }
        response.sendRedirect("../profile");
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String address)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
}
