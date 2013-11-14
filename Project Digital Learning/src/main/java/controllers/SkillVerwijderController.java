package controllers;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import models.Skill;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.HibernateUtil;
//import services.HibernateUtil;

public class SkillVerwijderController extends HttpServlet {
    /* HTTP GET request */

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if (request.getParameter("id") != null) {
            // Haal het 'id' parameter op uit het request
            long id = Long.parseLong(request.getParameter("id"));
            
            
            long skillId = Long.parseLong(request.getParameter("id"));
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            Transaction tx = session.beginTransaction();
            Skill managedSkill = (Skill) session.load(Skill.class, skillId);
            session.delete(managedSkill);

            tx.commit();

            // Stuur een redirect terug naar de client. De client zal dan
            // meteen een nieuwe GET request doen naar ../gebruikers
            response.sendRedirect("../profile");
        }
    }
}
