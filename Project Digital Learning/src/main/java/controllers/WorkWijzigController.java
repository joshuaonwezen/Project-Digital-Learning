package controllers;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import models.Work;
import models.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.HibernateUtil;
//import services.HibernateUtil;

public class WorkWijzigController extends HttpServlet {

    private static String titelNieuw = "New work experience";
    private static String titelWijzig = "Edit work experience";

    /* HTTP GET request */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        /* start session */
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        /* User information */
        List<User> tempWork = new LinkedList();
        // Zet de session in een variabele
        Criteria criteria = session.createCriteria(User.class);
        tempWork = criteria.list();
        request.setAttribute("workList", tempWork);

        if (request.getParameter("id") != null) {
            // If ID is present, retrieve information.
            long workId = Long.parseLong(request.getParameter("id"));
            request.setAttribute("id", workId);
            
            Work managedWork = (Work) session.load(Work.class, workId);

            request.setAttribute("fromYear", managedWork.getFromYear());
            request.setAttribute("tillYear", managedWork.getTillYear());
            request.setAttribute("name", managedWork.getName());
            request.setAttribute("profession", managedWork.getProfession());
            request.setAttribute("description", managedWork.getDescription());

            doorsturen(request, response, titelWijzig); //Stuurt door naar de Wijzig gebruiker pagina.
        } else {
            doorsturen(request, response, titelNieuw); //Stuurt door naar de Nieuwe gebruiker pagina.
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String dispatchUrl = null;

        boolean isWorkUpdate = request.getParameter("id") != null;

        if (isWorkUpdate) {
            //nieuwe code:
            long workId = Long.parseLong(request.getParameter("id"));

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            Transaction tx = session.beginTransaction();
            Work managedWork = (Work) session.load(Work.class, workId);

            managedWork.setFromYear(Integer.parseInt(request.getParameter("fromYear")));
            managedWork.setTillYear(Integer.parseInt(request.getParameter("tillYear")));
            managedWork.setName(request.getParameter("name"));
            managedWork.setProfession(request.getParameter("profession"));
            managedWork.setDescription(request.getParameter("description"));

            User user = new User();
            user.setUserId(Integer.parseInt(request.getParameter("user")));
            managedWork.setUser(user);

            session.update(managedWork);

            tx.commit();

        } else {
            Work work = new Work();

            work.setFromYear(Integer.parseInt(request.getParameter("fromYear")));
            work.setTillYear(Integer.parseInt(request.getParameter("tillYear")));
            work.setName(request.getParameter("name"));
            work.setProfession(request.getParameter("profession"));
            work.setDescription(request.getParameter("description"));

            User user = new User();
            user.setUserId(Integer.parseInt(request.getParameter("user")));
            work.setUser(user);

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();

            Transaction tx = session.beginTransaction();

            session.save(work);

            tx.commit();

        }

//            sessie.setAttribute("gebruikers", gebruikers);
//
//            sessie.setAttribute("aantalGebruikers", gebruikers.size());
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
        String address = "/work_wijzigen.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }

    /**
     * Maakt een Work object aan de hand van de parameters uit het http request.
     */
    private Work getProductFromRequest(HttpServletRequest request) {
        Work p = new Work();

        if (request.getParameter("id") != null && !request.getParameter("id").isEmpty()) {
            p.setWorkNumber(Long.parseLong(request.getParameter("id")));
        }
        if (request.getParameter("fromYear") != null) {
            p.setFromYear(Integer.parseInt(request.getParameter("fromYear")));
        }
        if (request.getParameter("tillYear") != null) {
            p.setTillYear(Integer.parseInt(request.getParameter("tillYear")));
        }
        if (request.getParameter("name") != null) {
            p.setName(request.getParameter("name"));
        }
        if (request.getParameter("profession") != null) {
            p.setProfession(request.getParameter("profession"));
        }
        if (request.getParameter("description") != null) {
            p.setDescription(request.getParameter("description"));
        }
        
        return p;
    }

}
