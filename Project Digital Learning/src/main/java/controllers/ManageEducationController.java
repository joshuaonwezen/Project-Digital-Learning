package controllers;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Education;
import models.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.HibernateUtil;

/**
 *
 * @author Shahin
 */
public class ManageEducationController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();


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

                long educationId = Long.parseLong(request.getParameter("id"));
                request.setAttribute("id", educationId);
                Education education = (Education) session.load(Education.class, educationId);

                // Place in request
                request.setAttribute("fromYear", education.getFromYear());
                request.setAttribute("tillYear", education.getTillYear());
                request.setAttribute("name", education.getName());
                request.setAttribute("profession", education.getProfession());
                request.setAttribute("description", education.getDescription());
                session.close();

                // Update
                request.setAttribute("update", true);
            } else {
                // New
                request.setAttribute("update", false);
            }
            redirect(request, response, "/edit_project.jsp");
        } // Delete
        else if (action.equals("delete")) {
            long educationId = Long.parseLong(request.getParameter("id"));
            Transaction tx = session.beginTransaction();
            Education education = (Education) session.load(Education.class, educationId);
            session.delete(education);

            tx.commit();
            response.sendRedirect("../profile?id=");
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

            Education education = new Education();
            education.setFromYear(Integer.parseInt(request.getParameter("fromYear")));
            education.setTillYear(Integer.parseInt(request.getParameter("tillYear")));
            education.setName(request.getParameter("name"));
            education.setProfession(request.getParameter("profession"));
            education.setDescription(request.getParameter("description"));

            User user = new User();
            user.setUserId(Integer.parseInt(request.getParameter("user")));
            education.setUser(user);

            session.save(education);
            tx.commit();
            session.close();
        } // Edit 
        else if (action.equals("edit")) {

            long educationId = Long.parseLong(request.getParameter("id"));
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();

            Education education = (Education) session.load(Education.class, educationId);
            education.setFromYear(Integer.parseInt(request.getParameter("fromYear")));
            education.setTillYear(Integer.parseInt(request.getParameter("tillYear")));
            education.setName(request.getParameter("name"));
            education.setProfession(request.getParameter("profession"));
            education.setDescription(request.getParameter("description"));

            User user = new User();
            user.setUserId(Integer.parseInt(request.getParameter("user")));
            education.setUser(user);

            session.saveOrUpdate(education);
            tx.commit();
            session.close();
        }
        response.sendRedirect("../profile?id=" + request.getParameter("user"));
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String address)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
}
