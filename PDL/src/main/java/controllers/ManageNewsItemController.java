package controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.NewsItem;
import models.NewsItemForm;
import models.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.HibernateUtil;
import validators.NewsItemValidator;

/**
 *
 * @author wesley
 *
 */
public class ManageNewsItemController extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //get the action
        String uri = request.getRequestURI();
        String action = uri.substring(uri.lastIndexOf("/") + 1);

        //NOTICE: in this context edit can be both creating a new newsItem or updating a newsItem
        if (action.equals("edit")) {
            //extract newsItemId (if available)
            boolean isUpdate;
            String queryString = request.getQueryString();
            //we are updating a newsItem if the newsItemId existst
            if (queryString.substring(queryString.indexOf("=")).length() > 1) {
                isUpdate = true;
            } else {
                isUpdate = false;
            }

            //edit a newsItem
            if (isUpdate) {
                //extract newsItemId
                int newsItemId = Integer.parseInt(queryString.substring(queryString.indexOf("=") + 1));
                System.out.println("we are updating: " + newsItemId);
                //get newsItem from database and set in the request
                Session session = HibernateUtil.getSessionFactory().openSession();
                Transaction tx = session.beginTransaction();
                NewsItem managedNewsItem = (NewsItem) session.load(NewsItem.class, newsItemId);

                request.setAttribute("newsId", managedNewsItem.getNewsId());
                request.setAttribute("title", managedNewsItem.getTitle());
                request.setAttribute("description", managedNewsItem.getDescription());
                request.setAttribute("date", managedNewsItem.getUpdatedFormatted());
                request.setAttribute("editedBy", managedNewsItem.getEditedBy());

                session.close();
                request.setAttribute("isUpdate", true);
            } //create a newsItem
            else {
                request.setAttribute("isUpdate", false);
            }
            //set the users on the request so we can select them
            setUsersOnRequest(request);

            redirect(request, response, "/edit_news_item.jsp");
        } //deleten van een newsItem
        else if (action.equals("delete")) {
            //extract newsItemId
            String queryString = request.getQueryString();
            int newsItemId = Integer.parseInt(queryString.substring(queryString.indexOf("=") + 1));

            //do the delete operation
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            NewsItem managedNewsItem = (NewsItem) session.load(NewsItem.class, newsItemId);
            session.delete(managedNewsItem);
            tx.commit();
            session.close();

            response.sendRedirect("../management");
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //get the action
        String uri = request.getRequestURI();
        String action = uri.substring(uri.lastIndexOf("/") + 1);

        if (action.equals("edit") || action.equals("new")) {
            System.out.println("action equals: " + action);
            boolean isUpdate = (action.equals("edit") ? true : false);

            //step 1: do a form validation
            NewsItemForm newsItemForm = new NewsItemForm();
            newsItemForm.setTitle(request.getParameter("title"));
            newsItemForm.setDescription(request.getParameter("description"));
            newsItemForm.setUpdated(request.getParameter("date"));
            newsItemForm.setEditedBy(getSelectedOption(request, "editedByValues"));

            NewsItemValidator validator = new NewsItemValidator();
            List<String> errors = validator.validate(newsItemForm);

            //step 2: redirect newsItem back if there are any errors
            if (!errors.isEmpty()) {
                //newsItemId needs only to be set when we are editing
                if (isUpdate) {
                    request.setAttribute("newsId", request.getParameter("newsId"));
                    //don't forget to set that we are still updating
                    request.setAttribute("isUpdate", true);
                }
                request.setAttribute("title", request.getParameter("title"));
                request.setAttribute("description", request.getParameter("description"));
                request.setAttribute("date", request.getParameter("date"));
                setUsersOnRequest(request);
                request.setAttribute("editedBy", request.getParameter("editedBy"));

                //vergeet de errors niet op de request te zetten
                request.setAttribute("errorsSize", errors.size());
                request.setAttribute("errors", errors);

                redirect(request, response, "/edit_news_item.jsp");
            } else {
                //step 3: there are no errors. We can start to create or update a newsItem
                NewsItem newsItem;
                User editedBy;
                Session session = HibernateUtil.getSessionFactory().openSession();
                Transaction tx = session.beginTransaction();

                editedBy = (User) session.load(User.class, Integer.parseInt(getSelectedOption(request, "editedByValues")));

                //get the newsItemId if we are updating a newsItem
                if (isUpdate) {
                    int newsItemId = Integer.parseInt(request.getParameter("newsId"));
                    newsItem = (NewsItem) session.load(NewsItem.class, newsItemId);
                } else {
                    newsItem = new NewsItem();
                }

                /**
                 * GOD KNOWS WHY but if you don't use this user.getFirstname(),
                 * hibernate will through an
                 * org.hibernate.LazyInitializationException: could not
                 * initialize proxy - no Session
                 */
                editedBy.getFirstname();

                newsItem.setTitle(request.getParameter("title"));
                newsItem.setDescription(request.getParameter("description"));
                //parse the date from the request to a java.util.Date
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                try {
                    date = sdf.parse(request.getParameter("date"));
                } 
                catch (ParseException e) {
                    e.printStackTrace();
                }
                newsItem.setUpdated(date);
                newsItem.setEditedBy(editedBy);

                session.saveOrUpdate(newsItem);
                tx.commit();
                session.close();

                //request handling
                if (isUpdate) {
                    request.setAttribute("newsItemUpdated", true);
                } else {
                    request.setAttribute("newsItemCreated", true);
                }

                request.setAttribute("newsId", newsItem.getNewsId());
                request.setAttribute("title", newsItem.getTitle());
                request.setAttribute("description", newsItem.getDescription());
                request.setAttribute("date", newsItem.getUpdatedFormatted());
                request.setAttribute("editedBy", newsItem.getEditedBy());

                setUsersOnRequest(request);

                //we are now editing
                request.setAttribute("isUpdate", true);

                redirect(request, response, "/edit_news_item.jsp");
            }
        }
    }

    private String getSelectedOption(HttpServletRequest request, String selectElement) {
        String[] selectValues = request.getParameterValues(selectElement);
        String selected = "";
        for (String item : selectValues) {
            selected = item;
        }
        return selected;
    }

    private void setUsersOnRequest(HttpServletRequest request) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Criteria criteria = session.createCriteria(User.class);
        List<User> users = criteria.list();
        
        //filter on admins and managers
        List<User> usersWithRights = new ArrayList<User>();
        
        for (User user : users){
            if (user.isIsAdmin() || user.isIsManager() || user.isIsTeacher()){
                usersWithRights.add(user);
            }
        }
        
        session.close();
        
        request.setAttribute("users", usersWithRights);
        request.setAttribute("usersSize", usersWithRights.size());
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String address)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
}