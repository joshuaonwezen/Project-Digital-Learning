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

        //search user on the search page
//        if (action.equals("searchUser")) {
//            Session session = HibernateUtil.getSessionFactory().openSession();
//            String searchQuery = request.getParameter("searchQuery");
//            String[] query = searchQuery.split(" ");
//
//            List<User> usersFound = new ArrayList<User>();
//            for (int i = 0; i < query.length; i++) {
//                String hql = "FROM User WHERE username LIKE '%" + query[i] + "%' OR firstname LIKE '%" + query[i]
//                        + "%' OR lastname LIKE '%" + query[i] + "%' OR emailAddress LIKE '%" + query[i] + "%' "
//                        + "GROUP BY userId";
//                
//                List<User> result = session.createQuery(hql).list();
//                if (result != null) {
//                    usersFound.addAll(result);
//                    System.out.println("size:" + result.size());
//                }
//            }
//
//            // set our results on the request and redirect back
//            request.setAttribute("users", usersFound);
//            request.setAttribute("usersSize", usersFound.size());
//            request.setAttribute("usersSizeResults", usersFound.size());
//
//            redirect(request, response, "/search.jsp");
//            session.close();
//        }
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
        hql.append("from User ");
        if (params.length > 0) {
            hql.append("where ");
            for (int i = 0; i < params.length; i++) {
                if (i > 0) {
                    hql.append(andOrfilter);
                }
                hql.append(" (username like '%").append(params[i]);
                hql.append("%' OR firstname like '%").append(params[i]);
                hql.append("%' OR lastname like '%").append(params[i]);
                hql.append("%' OR emailAddress like '%").append(params[i]);
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
