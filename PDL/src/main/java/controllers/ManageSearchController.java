package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.User;
import org.hibernate.Session;
import services.HibernateUtil;

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

        if (action.equals("searchUser")) {
            Session session = HibernateUtil.getSessionFactory().openSession();
            String searchQuery = request.getParameter("searchQuery");
            String[] params = searchQuery.split(" ");

            // Found users
            List<User> usersFound = new ArrayList<User>();

            // Exact match
            String hqlMatch = this.getSearchHqlQuery(params, "AND");
            List<User> exactResult = session.createQuery(hqlMatch).list();
            if (exactResult != null && !exactResult.isEmpty()) {
                usersFound.addAll(exactResult);
            } // Multiple search
            else {
                String hqlLike = this.getSearchHqlQuery(params, "OR");
                List<User> likeResult = session.createQuery(hqlLike).list();
                if (likeResult != null && !likeResult.isEmpty()) {
                    usersFound.addAll(likeResult);
                }
            }

            System.out.println("size:" + usersFound.size());
            // set our results on the request and redirect back
            request.setAttribute("users", usersFound);
            request.setAttribute("usersSize", usersFound.size());
            request.setAttribute("usersSizeResults", usersFound.size());

            redirect(request, response, "/search.jsp");
            session.close();
        }
    }

    private String getSearchHqlQuery(String[] params, String andOrfilter) {
        StringBuilder hql = new StringBuilder();
        hql.append("SELECT distinct u FROM User u ");
        hql.append("LEFT JOIN u.skills s ");
        if (params.length > 0) {
            hql.append("where ");
            for (int i = 0; i < params.length; i++) {
                if (i > 0) {
                    hql.append(andOrfilter);
                }
                hql.append(" (u.username like '%").append(params[i]);
                hql.append("%' OR u.firstname like '%").append(params[i]);
                hql.append("%' OR u.lastname like '%").append(params[i]);
                hql.append("%' OR u.emailAddress like '%").append(params[i]);
                hql.append("%' OR s.name like '%").append(params[i]);
                for (int t = 0; t < params.length; t++) {
                    hql.append("%' AND s.name like '%").append(params[i]);
                }
                hql.append("%') ");
            }
        }
        return hql.toString();
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String address)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
}
