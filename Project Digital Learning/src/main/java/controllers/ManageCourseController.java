/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Course;
import models.CourseForm;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.HibernateUtil;
import validators.CourseValidator;


/**
 *
 * @author martijn
 */
public class ManageCourseController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
    }

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
                
        String uri = request.getRequestURI();
        String action = uri.substring(uri.lastIndexOf("/") + 1);
        
        System.out.println("GET action: " + action);
                
        if (action.equals("courses")){
            setUsersOnRequest(request);
            redirect(request, response, "courses.jsp");
        }
        
        else if (action.equals("edit")){
            boolean isUpdate=false;
                        
            String queryString = request.getQueryString();
            
            if (queryString.substring(queryString.indexOf("=")).length() > 1){
                isUpdate = true;
            }
            
            if (isUpdate){
                int courseId = Integer.parseInt(queryString.substring(queryString.indexOf("=")+1));
            
                Session session = HibernateUtil.getSessionFactory().openSession();
                Transaction tx = session.beginTransaction();
                Course managedCourse = (Course)session.load(Course.class, courseId);
                                
                request.setAttribute("courseId", managedCourse.getCourseId());
                request.setAttribute("courseName", managedCourse.getCourseName());
                request.setAttribute("courseDiscription", managedCourse.getCourseDiscription());
                request.setAttribute("courseLevel", managedCourse.getCourseLevel());
                request.setAttribute("courseSkills", managedCourse.getCourseSkills());
                session.close();
                                
                request.setAttribute("update", true);
            }
            else{
                request.setAttribute("update", false);
            }
            redirect(request, response, "/edit_course.jsp");
        }
        
        else if (action.equals("delete")){
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            String queryString = request.getQueryString();
            int courseId = Integer.parseInt(queryString.substring(queryString.indexOf("=")+1));
            Course managedCourse = (Course)session.load(Course.class, courseId);
            session.delete(managedCourse);
            tx.commit();
            session.close();
            
            redirect(request, response, "../courses");
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

        String uri = request.getRequestURI();
        String action = uri.substring(uri.lastIndexOf("/") + 1);
        
        System.out.println("POST action: " + action);
        
        if (action.equals("courses")){
            setUsersOnRequest(request);
            redirect(request, response, "courses.jsp");
        }
        else if (action.equals("new") || action.equals("edit")){
            CourseForm courseForm = new CourseForm();
            courseForm.setCourseName(request.getParameter("coursename"));
            courseForm.setCourseDiscription(request.getParameter("coursediscription"));
            courseForm.setCourseLevel(request.getParameter("courselevel"));
            courseForm.setCourseSkills(request.getParameter("courseskills"));
            
            
            CourseValidator validators = new CourseValidator();
            List<String> errors = validators.validate(courseForm);
            
            if (errors.isEmpty()) {
                System.out.println("there are no errors");
                if (action.equals("new")) {
                    System.out.println("action equals new");
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    Transaction tx = session.beginTransaction();

                    Course course = new Course();
                    course.setCourseName(request.getParameter("coursename"));
                    course.setCourseDiscription(request.getParameter("coursediscription"));
                    course.setCourseLevel(request.getParameter("courselevel"));
                    course.setCourseSkills(request.getParameter("courseskills"));
                    

                    session.save(course);
                    tx.commit();
                    session.close();
                } 
                else if (action.equals("edit")) {
                    System.out.println("action equals edit");
                    int courseId;

                    Session session = HibernateUtil.getSessionFactory().openSession();
                    Transaction tx = session.beginTransaction();

                    courseId = Integer.parseInt(request.getParameter("courseId"));
                    Course managedCourse = (Course) session.load(Course.class, courseId);

                    managedCourse.setCourseName(request.getParameter("coursename"));
                    managedCourse.setCourseDiscription(request.getParameter("coursediscription"));
                    managedCourse.setCourseLevel(request.getParameter("courselevel"));
                    managedCourse.setCourseSkills(request.getParameter("courseskills"));
                    

                    session.saveOrUpdate(managedCourse);
                    tx.commit();
                    session.close();
                }
            }
            
            else{
                System.out.println("there are errors");
                
                
                request.setAttribute("errorsSize", errors.size());
                request.setAttribute("errors", errors);
                
                redirect(request, response, "/edit_course.jsp");
            }
        }
    }
    
    private void setUsersOnRequest(HttpServletRequest request){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Criteria criteria = session.createCriteria(Course.class);
        List<Course> courses = criteria.list();
        session.close();
        
        request.setAttribute("courses", courses);
        request.setAttribute("coursesSize", courses.size());
    }
        
    private void redirect(HttpServletRequest request, HttpServletResponse response, String address) 
            throws ServletException, IOException{
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
}