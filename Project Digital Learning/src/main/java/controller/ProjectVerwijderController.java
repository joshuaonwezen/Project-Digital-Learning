package controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import models.Project;
import org.hibernate.Session;
import org.hibernate.Transaction;
//import service.HibernateUtil;
import service.HibernateUtil;
//import services.HibernateUtil;

public class ProjectVerwijderController extends HttpServlet {
    /* HTTP GET request */

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if (request.getParameter("id") != null) {
            // Haal het 'id' parameter op uit het request
            long id = Long.parseLong(request.getParameter("id"));
            
            
            long projectId = Long.parseLong(request.getParameter("id"));
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            Transaction tx = session.beginTransaction();
            Project managedProject = (Project) session.load(Project.class, projectId);
            session.delete(managedProject);

            tx.commit();

            // Stuur een redirect terug naar de client. De client zal dan
            // meteen een nieuwe GET request doen naar ../gebruikers
            response.sendRedirect("../profile");
        }
    }
}
