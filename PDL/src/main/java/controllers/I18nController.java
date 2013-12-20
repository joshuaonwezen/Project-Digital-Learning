/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Martijn
 */
public class I18nController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ServletContext context = getServletContext();
        InputStream inStream1 = context.getResourceAsStream("/WEB-INF/classes/index_nl_NL.properties");
        InputStream inStream2 = context.getResourceAsStream("/WEB-INF/classes/index_en_US.properties");
        Properties pr = new Properties();
        pr.load(inStream1);
        pr.load(inStream2);
        request.setAttribute("i18nPropertiesNL", pr);
        request.setAttribute("i18nPropertiesEN", pr);
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
            redirect(request, response, "/i18n");
        }
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String address)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
}
