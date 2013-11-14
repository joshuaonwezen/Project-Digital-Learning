package controllers;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import models.User;
import models.Project;
import models.Skill;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.HibernateUtil;
//import services.HibernateUtil;

public class SkillWijzigController extends HttpServlet {

    private static String titelNieuw = "New skill";
    private static String titelWijzig = "Edit skill";

    /* HTTP GET request */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        /* start session */
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        /* User information */
        List<User> tempSkill = new LinkedList();
        // Zet de session in een variabele
        Criteria criteria = session.createCriteria(User.class);
        tempSkill = criteria.list();
        request.setAttribute("skillList", tempSkill);

        if (request.getParameter("id") != null) {
            // If ID is present, retrieve information.
            long skillId = Long.parseLong(request.getParameter("id"));
            request.setAttribute("id", skillId);

            Skill managedSkill = (Skill) session.load(Skill.class, skillId);

            request.setAttribute("name", managedSkill.getName());
            request.setAttribute("level", managedSkill.getLevel());

            doorsturen(request, response, titelWijzig); //Stuurt door naar de Wijzig gebruiker pagina.
        } else {
            doorsturen(request, response, titelNieuw); //Stuurt door naar de Nieuwe gebruiker pagina.
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String dispatchUrl = null;

        boolean isSkillUpdate = request.getParameter("id") != null;

        if (isSkillUpdate) {
            //nieuwe code:
            long skillId = Long.parseLong(request.getParameter("id"));

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            Transaction tx = session.beginTransaction();
            Skill managedSkill = (Skill) session.load(Skill.class, skillId);

            managedSkill.setName(request.getParameter("name"));
            managedSkill.setLevel(request.getParameter("level"));

            User user = new User();
            user.setUserId(Integer.parseInt(request.getParameter("user")));
            managedSkill.setUser(user);

            session.update(managedSkill);

            tx.commit();

        } else {
            Skill skill = new Skill();

            skill.setName(request.getParameter("name"));
            skill.setLevel(request.getParameter("level"));

            User user = new User();
            user.setUserId(Integer.parseInt(request.getParameter("user")));
            skill.setUser(user);

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();

            Transaction tx = session.beginTransaction();

            session.save(skill);

            tx.commit();

        }

        response.sendRedirect("../profile");
        if (dispatchUrl != null) {
            RequestDispatcher rd
                    = request.getRequestDispatcher(dispatchUrl);
            rd.forward(request, response);
        }
    }

    private void doorsturen(HttpServletRequest request, HttpServletResponse response, String titel)
            throws ServletException, IOException {
        // Set de pagina titel op het request
        request.setAttribute("paginaTitel", titel);

        // Stuur het resultaat van gebruiker_wijzigen.jsp terug naar de client
        String address = "/skill_wijzigen.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }

    /**
     * Maakt een Project object aan de hand van de parameters uit het http request.
     */
    private Skill getProductFromRequest(HttpServletRequest request) {
        Skill p = new Skill();

        if (request.getParameter("id") != null && !request.getParameter("id").isEmpty()) {
            p.setSkillNumber(Long.parseLong(request.getParameter("id")));
        }
        if (request.getParameter("name") != null) {
            p.setName(request.getParameter("name"));
        }
        if (request.getParameter("level") != null) {
            p.setLevel(request.getParameter("level"));
        }

        return p;
    }

}
