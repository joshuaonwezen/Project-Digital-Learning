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
import models.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.HibernateUtil;

/**
 *
 * @author Shahin
 */
public class ManageProjectController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        /* User information */
        List<User> tempProject = new LinkedList();
        // Put session into variable criteria
        Criteria criteria = session.createCriteria(User.class);
        tempProject = criteria.list();
        request.setAttribute("projectList", tempProject);

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

                long projectId = Long.parseLong(request.getParameter("id"));
                request.setAttribute("id", projectId);
                Project project = (Project) session.load(Project.class, projectId);

                // Place in request
                request.setAttribute("fromYear", project.getFromYear());
                request.setAttribute("tillYear", project.getTillYear());
                request.setAttribute("name", project.getName());
                request.setAttribute("profession", project.getProfession());
                request.setAttribute("description", project.getDescription());
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

            long projectId = Long.parseLong(request.getParameter("id"));
            Transaction tx = session.beginTransaction();
            Project project = (Project) session.load(Project.class, projectId);
            session.delete(project);

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

            Project project = new Project();
            project.setFromYear(Integer.parseInt(request.getParameter("fromYear")));
            project.setTillYear(Integer.parseInt(request.getParameter("tillYear")));
            project.setName(request.getParameter("name"));
            project.setProfession(request.getParameter("profession"));
            project.setDescription(request.getParameter("description"));

            User user = new User();
            user.setUserId(Integer.parseInt(request.getParameter("user")));
            project.setUser(user);

            session.save(project);
            tx.commit();
            session.close();
        } // Edit 
        else if (action.equals("edit")) {

            long projectId = Long.parseLong(request.getParameter("id"));
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();

            Project project = (Project) session.load(Project.class, projectId);
            project.setFromYear(Integer.parseInt(request.getParameter("fromYear")));
            project.setTillYear(Integer.parseInt(request.getParameter("tillYear")));
            project.setName(request.getParameter("name"));
            project.setProfession(request.getParameter("profession"));
            project.setDescription(request.getParameter("description"));

            User user = new User();
            user.setUserId(Integer.parseInt(request.getParameter("user")));
            project.setUser(user);

            session.saveOrUpdate(project);
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
