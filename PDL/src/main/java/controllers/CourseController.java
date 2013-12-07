package controllers;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Course;
import models.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.HibernateUtil;

/**
 *
 * @author wesley
 */
public class CourseController extends HttpServlet {

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
     
                    
            int courseId = Integer.parseInt(queryString.substring(queryString.indexOf("=")+1));
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Course course = (Course)session.load(Course.class, courseId);
        
        //go to the selected course
        if (action.equals("course")){            
            request.setAttribute("courseName", course.getName());
            request.setAttribute("courseOwner", course.getOwner());
            request.setAttribute("courseDescription", course.getDescription());
            request.setAttribute("courseId", course.getCourseId());
            redirect(request, response, "/selected_coursepage.jsp");
        }
            
        if (action.equals("virtualclassroom")){     
            request.setAttribute("courseName", course.getName());
            request.setAttribute("courseOwner", course.getOwner());
            request.setAttribute("courseDescription", course.getDescription());
            request.setAttribute("courseId", course.getCourseId());
            request.setAttribute("courseKey", course.getCourseKey());
            redirect(request, response, "/virtualclassroom.jsp");
        }   
        session.close();
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
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
}
