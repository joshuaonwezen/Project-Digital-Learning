package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Course;
import models.CourseForm;
import models.Skill;
import models.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.HibernateUtil;
import validators.CourseValidator;

/**
 *
 * @author Shahin Mokhtar
 */
public class ManageSearchController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Hij komt er in");

        //get the action
        String uri = request.getRequestURI();
        String action = uri.substring(uri.lastIndexOf("/") + 1);
        
        //search user on the search page
        if (action.equals("searchUser")){
            System.out.println("Komt in search user");
            Session session = HibernateUtil.getSessionFactory().openSession();
            String searchQuery = request.getParameter("searchQuery");
            String hql = "FROM User WHERE username LIKE '%" + searchQuery + "%' OR firstname LIKE '%" + searchQuery + "%' OR lastname LIKE '%" + searchQuery + "%' OR emailAddress LIKE '%" + searchQuery + "%'";
            List<User> result = session.createQuery(hql).list();
            
            //set our results on the request and redirect back
            request.setAttribute("users", result);
            request.setAttribute("usersSize", result.size());
            request.setAttribute("usersSizeResults", result.size());
            
            redirect(request, response, "/search.jsp");
            session.close();
            System.out.println("Eind actie");
            System.out.println("usersSizeResults" + result.size());
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //get the action
        String uri = request.getRequestURI();
        String action = uri.substring(uri.lastIndexOf("/") + 1);
        
        System.out.println("Hij komt er in post");
    }


    private void redirect(HttpServletRequest request, HttpServletResponse response, String address)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
}
