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
import models.User;
import models.Work;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.HibernateUtil;
import validators.GenericValidator;

/**
 *
 * @author Joshua
 */
@WebServlet(name = "ActivityFeedController", urlPatterns = {"/ActivityFeedController"})
public class ActivityFeedController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
      if (request.getParameter("id") != null) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        String dispatchUrl = null;
        int id = Integer.parseInt(request.getParameter("id"));
        
       
        List<Activity> tempActivity = new LinkedList();
        Query queryActivity = session.createQuery("from Activity where user_userId = " + id);
        tempActivity = queryActivity.list();
        request.setAttribute("activityList", tempActivity);

            dispatchUrl = "/homepage.jsp";

            if (dispatchUrl != null) {
                RequestDispatcher rd
                        = request.getRequestDispatcher(dispatchUrl);
                rd.forward(request, response);
            }
        }
    }
}