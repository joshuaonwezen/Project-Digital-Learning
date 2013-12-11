package controllers;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.criterion.Restrictions;
import models.Course;
import models.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.HibernateUtil;

/**
 *
 * @author wesley
 */
public class ManageDocumentController extends HttpServlet {

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

        if (action.equals("documents")) {
            String queryString = request.getQueryString();
            int courseId = Integer.parseInt(queryString.substring(queryString.indexOf("=") + 1));

            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Course course = (Course) session.load(Course.class, courseId);
            
            request.getSession().setAttribute("course", course);
            
            setFilesOnRequest(request);
            redirect(request, response, "/documents.jsp");
            session.close();
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

        if (action.equals("uploadFiles")) {
            System.out.println("file upload");

            File file;
            int maxFileSize = 5000 * 1024;
            int maxMemSize = 5000 * 1024;
            String filePath = request.getServletContext().getInitParameter("file-upload"); //gets the path from web.xml

            // Verify the content type
            String contentType = request.getContentType();
            if ((contentType.indexOf("multipart/form-data") >= 0)) {
                DiskFileItemFactory factory = new DiskFileItemFactory();
                // maximum size that will be stored in memory
                factory.setSizeThreshold(maxMemSize);
                // Location to save data that is larger than maxMemSize.
                //factory.setRepository(new File("c:\\temp"));

                // Create a new file upload handler
                ServletFileUpload upload = new ServletFileUpload(factory);
                // maximum file size to be uploaded.
                upload.setSizeMax(maxFileSize);
                try {
                    // Parse the request to get file items.
                    List fileItems = upload.parseRequest(request);
                    // Process the uploaded file items
                    Iterator i = fileItems.iterator();

                    while (i.hasNext()) {
                        FileItem fi = (FileItem) i.next();
                        if (!fi.isFormField()) {
                            // Get the uploaded file parameters
                            String fieldName = fi.getFieldName();
                            String fileName = fi.getName();
                            boolean isInMemory = fi.isInMemory();
                            long sizeInBytes = fi.getSize();
                            // Write the file
                            if (fileName.lastIndexOf("\\") >= 0) {
                                file = new File(filePath
                                        + fileName.substring(fileName.lastIndexOf("\\")));
                            } else {
                                file = new File(filePath
                                        + fileName.substring(fileName.lastIndexOf("\\") + 1));
                            }
                            fi.write(file);
                            System.out.println("Uploaded Filename: " + filePath
                                    + fileName + "<br>");

                            //create a custom file object which contains the owner etc.
                            models.File customFile = new models.File();

                            //find the owner
                            Session session = HibernateUtil.getSessionFactory().openSession();
                            Transaction tx = session.beginTransaction();
                            int loggedInUserId = Integer.parseInt(request.getSession().getAttribute("loggedInUserId").toString());
                            User owner = (User) session.load(User.class, loggedInUserId);

                            //find the course
                            Course course = (Course)request.getSession().getAttribute("course");

                            customFile.setCourse(course);
                            customFile.setOwner(owner);
                            customFile.setVisible(true);
                            customFile.setName(fileName);
                            customFile.setLastEdited(new Date());
                            customFile.setFileSize(fi.getSize());
                            session.save(customFile);
                            tx.commit();
                            session.close();
                        }
                    }

                } catch (Exception ex) {
                    System.out.println(ex);
                }
            } else {
                System.out.println("FORM CONTENT TYPE ERROR");
            }
        } 
        else if (action.equals("deleteFile")) {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            int fileId = Integer.parseInt(request.getParameter("fileToDelete"));
            models.File file = (models.File) session.load(models.File.class, fileId);
            session.delete(file);
            tx.commit();
            session.close();
        }
        else if (action.equals("changeFileVisibility")){
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            int fileId = Integer.parseInt(request.getParameter("fileToChangeVisibility"));
            models.File file = (models.File) session.load(models.File.class, fileId);
            
            if (file.isVisible()){
                file.setVisible(false);
            }
            else{
                file.setVisible(true);
            }
            file.setLastEdited(new Date());
            session.save(file);
            tx.commit();
            session.close();
        }
        
        setFilesOnRequest(request);
            redirect(request, response, "/documents.jsp");
       }

    private void setFilesOnRequest(HttpServletRequest request) {
        Course course = (Course)request.getSession().getAttribute("course");
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
        StringBuilder query = new StringBuilder("from File ");
        query.append(" where course_courseId = " + course.getCourseId() + "");
        Query result = session.createQuery(query.toString());
        List<models.File> files = result.list();
        tx.commit();
        session.close();

        request.setAttribute("files", files);
        request.setAttribute("filesSize", files.size());
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String address)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
}
