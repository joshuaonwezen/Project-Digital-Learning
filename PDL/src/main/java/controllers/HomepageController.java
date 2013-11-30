/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Activity;
import models.NewsItem;
import models.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.HibernateUtil;
import validators.GenericValidator;

/**
 *
 * @author Joshua
 */
@WebServlet(name = "HomepageController", urlPatterns = {"/HomepageController"})
public class HomepageController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
        
        redirect(request, response, "/homepage.jsp");

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
        
 

                                  
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String address) 
            throws ServletException, IOException{
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
}