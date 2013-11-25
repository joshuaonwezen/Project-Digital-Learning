package controllers;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Skill;
import models.User;
import models.Work;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.HibernateUtil;

/**
 *
 * @author Shahin Mokhtar
 */
public class ManageWorkController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        /* User information */
        List<User> tempWork = new LinkedList();
        // Put session into variable criteria
        Criteria criteria = session.createCriteria(User.class);
        tempWork = criteria.list();
        request.setAttribute("workList", tempWork);

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

                long workId = Long.parseLong(request.getParameter("id"));
                request.setAttribute("id", workId);
                Work work = (Work) session.load(Work.class, workId);

                // Place in request
                request.setAttribute("fromMonth", work.getFromMonth());
                request.setAttribute("tillMonth", work.getTillMonth());
                request.setAttribute("fromYear", work.getFromYear());
                request.setAttribute("tillYear", work.getTillYear());
                request.setAttribute("name", work.getName());
                request.setAttribute("profession", work.getProfession());
                request.setAttribute("description", work.getDescription());
                session.close();

                // Update
                request.setAttribute("update", true);
            } else {
                // New
                request.setAttribute("update", false);
            }
            redirect(request, response, "/edit_work.jsp");
        } // Delete
        else if (action.equals("delete")) {

            long workId = Long.parseLong(request.getParameter("id"));
            Transaction tx = session.beginTransaction();
            Work work = (Work) session.load(Work.class, workId);
            session.delete(work);

            tx.commit();

            response.sendRedirect("../profile?id=" + request.getParameter("user"));
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

            Work work = new Work();
            work.setFromMonth(Integer.parseInt(request.getParameter("fromMonth")));
            work.setTillMonth(Integer.parseInt(request.getParameter("tillMonth")));
            work.setFromYear(Integer.parseInt(request.getParameter("fromYear")));
            work.setTillYear(Integer.parseInt(request.getParameter("tillYear")));
            work.setName(request.getParameter("name"));
            work.setProfession(request.getParameter("profession"));
            work.setDescription(request.getParameter("description"));

            User user = new User();
            user.setUserId(Integer.parseInt(request.getParameter("user")));
            work.setUser(user);

            session.save(work);
            tx.commit();
            session.close();
        } // Edit 
        else if (action.equals("edit")) {

            long workId = Long.parseLong(request.getParameter("id"));
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();

            Work work = (Work) session.load(Work.class, workId);
            work.setFromMonth(Integer.parseInt(request.getParameter("fromMonth")));
            work.setTillMonth(Integer.parseInt(request.getParameter("tillMonth")));
            work.setFromYear(Integer.parseInt(request.getParameter("fromYear")));
            work.setTillYear(Integer.parseInt(request.getParameter("tillYear")));
            work.setName(request.getParameter("name"));
            work.setProfession(request.getParameter("profession"));
            work.setDescription(request.getParameter("description"));

            User user = new User();
            user.setUserId(Integer.parseInt(request.getParameter("user")));
            work.setUser(user);

            session.saveOrUpdate(work);
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
