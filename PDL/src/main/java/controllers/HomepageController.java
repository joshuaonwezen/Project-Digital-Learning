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
public class HomepageController extends HttpServlet {

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

            setUserChatsOnRequest(request);
            setUsersOnRequest(request); //users for selecting in a new chat
            redirect(request, response, "/messages.jsp");
        } 
        else if (action.equals("message")) {
            //find the chatroom and set it on the request
            int chatId = Integer.parseInt(queryString.substring(queryString.indexOf("=") + 1));

            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Chat managedChat = (Chat) session.load(Chat.class, chatId);
            managedChat.getChatId();//dont remove this line because of hibernate lazy exception

            request.setAttribute("chat", managedChat);
            session.close();
            redirect(request, response, "/message.jsp");
        } 
        else if (action.equals("homepage")) {
            int userId = Integer.parseInt(request.getSession().getAttribute("loggedInUserId").toString());
            Session activitySession = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = activitySession.beginTransaction();

            List<Activity> tempActivity = new LinkedList();
            Query queryActivity = activitySession.createQuery("from Activity where user_userId = " + userId + "order by sent desc");
            tempActivity = queryActivity.list();
            request.setAttribute("activityList", tempActivity);
            activitySession.close();

            Session newsitemSession = HibernateUtil.getSessionFactory().openSession();
            List<NewsItem> tempNewsItem = new LinkedList();
            Query queryNewsItem = newsitemSession.createQuery("from NewsItem order by updated desc");
            tempNewsItem = queryNewsItem.list();
            request.setAttribute("newsitemList", tempNewsItem);
            newsitemSession.close();

            setUserChatsOnRequest(request);//chats so we can see if there are unread notifications
            redirect(request, response, "/homepage.jsp");
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
                        request.getSession().setAttribute("loggedInIsManager", user.isIsManager());
                        request.getSession().setAttribute("loggedInIsTeacher", user.isIsTeacher());
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
                        
                        setUserChatsOnRequest(request);
                        redirect(request, response, "/homepage.jsp");
                    }

                }
            }
        } //create a new message (chat)
        else if (action.equals("createMessage") || action.equals("manageMessage")) {
            boolean isUpdate = false;
            if (action.equals("manageMessage")) {
                isUpdate = true;
            }
            //1. get users from the database
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(User.class);
            List<User> usersFromDatabase = criteria.list();

            //2. the list with users from the request
            String pmTagUsers = "tagUsers";
            if (isUpdate) {
                pmTagUsers = "tagManageUsers";
            }
            List<String> usersFromRequest = Arrays.asList(request.getParameter(pmTagUsers).split(","));
            List<User> users = new ArrayList<User>(); // list with the users that match in step 3

            //3. we have to match the users from the array to real users in the database
            //3a. alway add the user that created the cat
            int loggedInUserId = Integer.parseInt(request.getSession().getAttribute("loggedInUserId").toString());
            User loggedInUser = (User) session.load(User.class, loggedInUserId);
            users.add(loggedInUser);

            //3b. match for other users
            for (int i = 0; i < usersFromRequest.size(); i++) {
                for (int j = 0; j < usersFromDatabase.size(); j++) {
                    //match the users from the request against the db and add them
                    if (usersFromRequest.get(i).equals(usersFromDatabase.get(j).getUsername())) {
                        users.add(usersFromDatabase.get(j));
                        System.out.println("user match");
                        break;
                    }
                }
            }

            //4. create a new or edit a Chat and add the users to the chat
            Chat chat;
            if (!isUpdate) {
                chat = new Chat();
                chat.setSubject(request.getParameter("subject"));
                chat.setUsers(users);
                User createdBy = (User) session.load(User.class, loggedInUserId);

                for (User u : users) {
                    System.out.println("added: " + u.getUsername());
                }

                chat.setCreated(createdBy);
            } //updating a chat
            else {
                System.out.println("chatId " + request.getParameter("chatToManage"));
                int chatId = Integer.parseInt(request.getParameter("chatToManage"));
                chat = (Chat) session.load(Chat.class, chatId);
                chat.setSubject(request.getParameter("subjectUpdate"));
                chat.setUsers(users);
            }

            //4b. save to the db
            session.saveOrUpdate(chat);
            tx.commit();

            //redirect to the message if we created a new one
            if (!isUpdate) {
                request.setAttribute("chat", chat);
                redirect(request, response, "/message.jsp");
            } //otherwise show all the messages again
            else {
                response.sendRedirect("messages");
            }

            session.close();
        } //delete a message 
        else if (action.equals("deleteMessage")) {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            int chatId = Integer.parseInt(request.getParameter("chatToDelete"));

            System.out.println("request: " + request.getParameter("chatToDelete"));
            Chat chatToDelete = (Chat) session.load(Chat.class, chatId);

            session.delete(chatToDelete);
            tx.commit();
            session.close();

            response.sendRedirect("messages");
        } //remove a user from a message
        else if (action.equals("removeUserFromMessage")) {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            int chatId = Integer.parseInt(request.getParameter("chatToRemoveUser"));
            Chat chat = (Chat) session.load(Chat.class, chatId);

            int userId = Integer.parseInt(request.getSession().getAttribute("loggedInUserId").toString());

            //iterate over the userlist to found the user which has to be removed from the list
            int userRemoveIndex = -1;
            for (int i = 0; i < chat.getUsers().size(); i++) {
                if (chat.getUsers().get(i).getUserId() == userId) {
                    userRemoveIndex = i;
                    break;
                }
            }
            List<User> updatedUsers = new ArrayList<User>();
            updatedUsers = chat.getUsers();
            updatedUsers.remove(userRemoveIndex);

            chat.setUsers(updatedUsers);
            //and save to db
            session.save(chat);
            tx.commit();
            session.close();

            response.sendRedirect("messages");
        }

    }

    /**
     * Set's all the users on the request except for the user that is logged in
     *
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
        int userIndex = -1;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(loggedInUsername)) {
                userIndex = i;
                break;
            }
        }
        users.remove(userIndex);

        request.setAttribute("users", users);
        request.setAttribute("usersSize", users.size());
    }

    private void setUserChatsOnRequest(HttpServletRequest request) {
        //set the chats on the request that are linked to the user
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Criteria criteria = session.createCriteria(Chat.class);
         criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);//prevent duplicates
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
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String address)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
}
