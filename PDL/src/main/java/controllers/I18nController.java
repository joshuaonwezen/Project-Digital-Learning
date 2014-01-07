/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Course;
import models.Internationalization;
import models.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.HibernateUtil;

/**
 *
 * @author Martijn
 */
public class I18nController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        
        setMetadataOnRequest(request);
        
        if (uri.contains("i18n_nl")){
            setPropertiesOnRequest(request, "nl_NL");
            redirect(request, response, "./i18n_nl.jsp");
        }
        else if (uri.contains("i18n_en")){
            setPropertiesOnRequest(request, "en_US");
            redirect(request, response, "./i18n_en.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        String action = uri.substring(uri.lastIndexOf("/") + 1);
        
        if (action.equals("updateDutch")) {
            updateFile(request, "nl_NL");
            updateMetadata(request);
            
            setPropertiesOnRequest(request, "nl_NL");
            setMetadataOnRequest(request);
            redirect(request, response, "/i18n_nl.jsp");
        }
        else if (action.equals("updateEnglish")) {
            
            updateFile(request, "en_US");
            updateMetadata(request);
            
            setPropertiesOnRequest(request, "en_US");
            setMetadataOnRequest(request);
            redirect(request, response, "/i18n_en.jsp");
        }
        else if (action.equals("applyTranslations")){
            // check if the password was correct
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            
            //get the user that is currently logged in
            int userId = Integer.parseInt(request.getSession().getAttribute("loggedInUserId").toString());
            User user = (User) session.load(User.class, userId);
            
            String passwordFromRequest = request.getParameter("password");
            if (User.md5(passwordFromRequest).equals(user.getPassword())){
                //password is correct: proceed
                Internationalization i18n = new Internationalization();
                i18n.setI18nId(1);
                i18n.setAppliedBy(user);
                i18n.setLastAppliedToSystem(new Date());
                i18n.setNeedsUpdate(false);
            
                session.saveOrUpdate(i18n);
                tx.commit();
                
                //run the restart script
                String s;
               Process p = Runtime.getRuntime().exec("service tomcat restart"); // command to restart webserver  
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((s = stdInput.readLine()) != null) {
                    System.out.println(s);
                }
                System.out.println("DONE");
            }
            else{
                //incorrect password, redirect back
                request.setAttribute("verificationFailed", true);
                
                setPropertiesOnRequest(request, "nl_NL");
                redirect(request, response, "/i18n_nl.jsp");
            }
            session.close();
            setMetadataOnRequest(request);
        }
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String address)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
    
    private void setPropertiesOnRequest(HttpServletRequest request, String language) throws IOException{
        ServletContext context = getServletContext();
        InputStream inStream1 = new FileInputStream("/var/lib/tomcat7/webapps/PDL/WEB-INF/classes/index_" + language + ".properties");
        Properties pr = new Properties();
        pr.load(inStream1);
        request.setAttribute("i18nProperties", pr);
    }
    
    private void updateFile(HttpServletRequest request, String language) throws IOException{
        ServletContext context = getServletContext();
        InputStream inStream1 = context.getResourceAsStream("/WEB-INF/classes/index_" + language + ".properties");            
        Properties oldPr1 = new Properties();            
        oldPr1.load(inStream1);            
        Properties newPr1 = new Properties();                  
        Enumeration en1 = oldPr1.keys();            
        while (en1.hasMoreElements()) {
            String key1 = (String) en1.nextElement();
            String value1 = request.getParameter(key1);
            System.out.println("KEY:" + key1 +" VALUE:" + value1);
            newPr1.setProperty(key1, value1);
        }
        File file = new File("/var/lib/tomcat7/webapps/PDL/WEB-INF/classes/index_" + language + ".properties");
        newPr1.store(new FileOutputStream(file), null);
    }
    
    private void updateMetadata(HttpServletRequest request){
            //update the metadata
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            
            //get the user that is currently logged in
            int userId = Integer.parseInt(request.getSession().getAttribute("loggedInUserId").toString());
            User user = (User) session.load(User.class, userId);
            
            Internationalization i18n = (Internationalization) session.load(Internationalization.class, 1);
            i18n.setUpdatedBy(user);
            i18n.setLastUpdated(new Date());
            i18n.setNeedsUpdate(true);
            
            session.saveOrUpdate(i18n);
            tx.commit();
            session.close();
    }
    
    /**
     * Information about when the updates where last applied to the server etc.
     * @param request 
     */
    private void setMetadataOnRequest(HttpServletRequest request){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
            
            
            Criteria criteria = session.createCriteria(Internationalization.class);
            List<Internationalization> i18ns = criteria.list();
            
            Internationalization i18n;
            //if it is empty we need to create a new object here
            if (i18ns.isEmpty()){
                i18n = new Internationalization();
                i18n.setNeedsUpdate(false);
                session.save(i18n);
                tx.commit();
            }
        
        //set data on request
        i18n = (Internationalization) session.load(Internationalization.class, 1);
        
        request.setAttribute("lastAppliedBy", i18n.getAppliedBy());
        request.setAttribute("lastAppliedOn", i18n.getLastAppliedToSystem());
        request.setAttribute("lastUpdatedBy", i18n.getUpdatedBy());
        request.setAttribute("lastUpdatedOn", i18n.getLastUpdated());
        System.out.println("NEDSUPDATE: " + i18n.isNeedsUpdate());
        request.setAttribute("needsUpdate", i18n.isNeedsUpdate());
        
        session.close();
    }
}
