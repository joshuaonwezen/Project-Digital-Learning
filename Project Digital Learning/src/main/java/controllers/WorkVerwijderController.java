package controllers;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import models.Work;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.HibernateUtil;
//import services.HibernateUtil;

public class WorkVerwijderController extends HttpServlet {
    /* HTTP GET request */

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if (request.getParameter("id") != null) {
            // Haal het 'id' parameter op uit het request
            long id = Long.parseLong(request.getParameter("id"));
            
            
            long workId = Long.parseLong(request.getParameter("id"));
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            Transaction tx = session.beginTransaction();
            Work managedWork = (Work) session.load(Work.class, workId);
            session.delete(managedWork);

            tx.commit();

            // Stuur een redirect terug naar de client. De client zal dan
            // meteen een nieuwe GET request doen naar ../gebruikers
            response.sendRedirect("../profile");
        }
    }
}
