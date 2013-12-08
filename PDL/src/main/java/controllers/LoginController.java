package controllers;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Activity;
import models.Chat;
import models.Course;
import models.NewsItem;
import models.Skill;
import models.User;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.HibernateUtil;
import validators.GenericValidator;
//import services.HibernateUtil;

/**
 *
 * @author wesley
 */
public class LoginController extends HttpServlet {

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
        String queryString = request.getQueryString();

        System.out.println("GET action: " + action);

        
        /**
         * Show all the messages that a User is currently linked to
         */
        if (action.equals("messages")) {
            //set the chats on the request that are linked to the user
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(Chat.class);
            List<Chat> chats = criteria.list();

            //now filter on the user that is logged in
            int loggedInUserId = Integer.parseInt(request.getSession().getAttribute("loggedInUserId").toString());
            List<Chat> userChats = new ArrayList<Chat>();

            //iterate over the users that are linked to the chat
            for (int i = 0; i < chats.size(); i++) {
                List<User> tempUsers = chats.get(i).getUsers();
                for (int j = 0; j < tempUsers.size(); j++) {
                    if (tempUsers.get(j).getUserId() == loggedInUserId) {
                        userChats.add(chats.get(i));
                        break;
                    }
                }
            }

            session.close();

            //set on request
            request.setAttribute("userChats", userChats);//chats that the user is enrolled to
            request.setAttribute("userChatsSize", userChats.size());
            setUsersOnRequest(request); //users for selecting in a new chat
            redirect(request, response, "/messages.jsp");
        } 
        else if (action.equals("message")) {
            //find the chatroom and set it on the request
            int chatId = Integer.parseInt(queryString.substring(queryString.indexOf("=")+1));
            
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Chat managedChat = (Chat) session.load(Chat.class, chatId);
            managedChat.getChatId();//dont remove this line because of hibernate lazy exception
            
            request.setAttribute("chat", managedChat);
            session.close();
            redirect(request, response, "/message.jsp");
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

        System.out.println("POST action: " + action);

        if (action.equals("login") && !GenericValidator.isEmpty(request.getParameter("username"))) {
            Session session = HibernateUtil.getSessionFactory().openSession();
            String hql = "from User  where username = ?";
            List result = session.createQuery(hql)
                    .setString(0, request.getParameter("username"))
                    .list();
            session.close();

            //als de list leeg is dan is de username zoiezo fout
            List<String> errors = new ArrayList<String>();
            if (result.isEmpty()) {
                errors.add("Username or password is incorrect. ");
                request.setAttribute("errors", errors);
                redirect(request, response, "/index.jsp");
            } //anders kunnen we nu de password gaan vergelijken
            else {
                User user = (User) result.get(0);

                //geef foutmelding als password leeg is
                if (request.getParameter("password").trim().isEmpty()) {
                    errors.add("Username or password is incorrect. ");
                    request.setAttribute("errors", errors);
                    redirect(request, response, "/index.jsp");
                } else {
                    //password gaan vergelijken
                    String md5 = User.md5(request.getParameter("password"));

                    if (!user.getPassword().equals(md5)) {
                        errors.add("Username or password is incorrect. ");
                        request.setAttribute("errors", errors);
                        redirect(request, response, "/index.jsp");
                    } //login is goed
                    else {
                        request.getSession().setAttribute("loggedInUsername", user.getUsername());
                        request.getSession().setAttribute("loggedInUserId", user.getUserId());
                        request.getSession().setAttribute("loggedInIsAdmin", user.isIsAdmin());
                        request.getSession().setAttribute("loggedInFirstname", user.getFirstname());
                        request.getSession().setAttribute("loggedInLastname", user.getLastname());

                        int id = user.getUserId();
                        Session activitySession = HibernateUtil.getSessionFactory().openSession();
                        Transaction tx = activitySession.beginTransaction();

                        List<Activity> tempActivity = new LinkedList();
                        Query queryActivity = activitySession.createQuery("from Activity where user_userId = " + id + "order by sent desc");
                        tempActivity = queryActivity.list();
                        request.setAttribute("activityList", tempActivity);
                        activitySession.close();

                        Session newsitemSession = HibernateUtil.getSessionFactory().openSession();
                        List<NewsItem> tempNewsItem = new LinkedList();
                        Query queryNewsItem = newsitemSession.createQuery("from NewsItem order by updated desc");
                        tempNewsItem = queryNewsItem.list();
                        request.setAttribute("newsitemList", tempNewsItem);
                        newsitemSession.close();
                        redirect(request, response, "/homepage.jsp");
                    }

                }
            }
        } //create a new message (chat)
        else if (action.equals("message")) {
            //1. get users from the database
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(User.class);
            List<User> usersFromDatabase = criteria.list();

            //2. the list with users from the request
            List<String> usersFromRequest = Arrays.asList(request.getParameter("tagUsers").split(","));
            List<User> users = new ArrayList<User>(); // list with the users that match in step 3

            //3. we have to match the users from the array to real users in the database
            boolean loggedInUserFound=false;
            String loggedInUsername = request.getSession().getAttribute("loggedInUsername").toString();
            for (int i = 0; i < usersFromRequest.size(); i++) {
                for (int j = 0; j < usersFromDatabase.size(); j++) {
                    //the first if adds the user that is logged in to the chat
                    if (!loggedInUserFound && usersFromDatabase.get(j).getUsername().equals(loggedInUsername)){
                        users.add(usersFromDatabase.get(j));
                        loggedInUserFound = true;
                    }
                    //this if checks for other users from the request
                    if (usersFromRequest.get(i).equals(usersFromDatabase.get(j).getUsername())) {
                        users.add(usersFromDatabase.get(j));
                        System.out.println("user match");
                        break;
                    }
                }
            }

            //4. create a new Chat and add the users to the chat
            Chat chat = new Chat();
            chat.setSubject(request.getParameter("subject"));
            chat.setUsers(users);

            //4b. save to the db
            session.save(chat);
            tx.commit();
            
            request.setAttribute("chat", chat);
            
            session.close();
            
            redirect(request, response, "/message.jsp");
        }
    }

    /**
     * Set's all the users on the request except for the user that is logged in
     * @param request 
     */
    private void setUsersOnRequest(HttpServletRequest request) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Criteria criteria = session.createCriteria(User.class);
        List<User> users = criteria.list();
        session.close();
        
        //now find the user that is logged in
        String loggedInUsername = request.getSession().getAttribute("loggedInUsername").toString();
        int userIndex=-1;
        for (int i=0;i<users.size();i++){
            if (users.get(i).getUsername().equals(loggedInUsername)){
                userIndex = i;
                break;
            }
        }
        users.remove(userIndex);

        request.setAttribute("users", users);
        request.setAttribute("usersSize", users.size());
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String address)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
}
