/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.BufferedReader;
import java.io.File;
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

        setMetadataOnRequest(request);
        setPropertiesOnRequest(request);
        redirect(request, response, "./i18n.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        String action = uri.substring(uri.lastIndexOf("/") + 1);
        
        if (action.equals("update")) {
            ServletContext context = getServletContext();
            InputStream inStream = context.getResourceAsStream("/WEB-INF/classes/index_nl_NL.properties");
            InputStream inStream2 = context.getResourceAsStream("/WEB-INF/classes/index_en_US.properties");
            Properties oldPr1 = new Properties();
            Properties oldPr2 = new Properties();
            oldPr1.load(inStream);
            oldPr2.load(inStream2);
            Properties newPr1 = new Properties();      
            Properties newPr2 = new Properties(); 
            Enumeration en = oldPr1.keys();
            Enumeration en2 = oldPr2.keys();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                String value = request.getParameter(key);
                newPr1.setProperty(key, value);
            }
            File file = new File("C:\\Users\\Martijn\\Documents\\GitHub\\Project-Digital-Learning\\PDL\\src\\main\\webapp\\WEB-INF\\classes\\index_nl_NL.properties");
            newPr1.store(new FileOutputStream(file), null);
            while (en2.hasMoreElements()) {
                String key = (String) en2.nextElement();
                String value = request.getParameter(key);
                newPr2.setProperty(key, value);
            }
            File file2 = new File("C:\\Users\\Martijn\\Documents\\GitHub\\Project-Digital-Learning\\PDL\\src\\main\\webapp\\WEB-INF\\classes\\index_en_US.properties");
            File file3 = new File("C:\\Users\\Martijn\\Documents\\GitHub\\Project-Digital-Learning\\PDL\\src\\main\\webapp\\WEB-INF\\classes\\index.properties");
            newPr2.store(new FileOutputStream(file2), null);
            newPr2.store(new FileOutputStream(file3), null);
            
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
                
            setMetadataOnRequest(request);
            redirect(request, response, "/i18n");
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
            }
            else{
                //incorrect password, redirect back
                request.setAttribute("verificationFailed", true);
                
                setPropertiesOnRequest(request);
                redirect(request, response, "/i18n.jsp");
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
    
    private void setPropertiesOnRequest(HttpServletRequest request) throws IOException{
        ServletContext context = getServletContext();
        InputStream inStream1 = context.getResourceAsStream("/WEB-INF/classes/index_nl_NL.properties");
        InputStream inStream2 = context.getResourceAsStream("/WEB-INF/classes/index_en_US.properties");
        Properties pr = new Properties();
        pr.load(inStream1);
        pr.load(inStream2);
        request.setAttribute("i18nPropertiesNL", pr);
        request.setAttribute("i18nPropertiesEN", pr);
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
