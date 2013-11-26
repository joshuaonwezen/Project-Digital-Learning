package controllers;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Project;
import models.ProjectForm;
import models.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.HibernateUtil;
import validators.GenericValidator;
import validators.ProjectValidator;

/**
 *
 * @author Shahin Mokhtar
 */
public class ManageProjectController extends HttpServlet {

    
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

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        //get the action
        String uri = request.getRequestURI();
        String action = uri.substring(uri.lastIndexOf("/") + 1);

        //NOTICE: in this context edit can be both creating a new user or updating a user
        if (action.equals("edit")) {
            //extract projectId (if available)
            boolean isUpdate;
            String queryString = request.getQueryString();
            //we are updating a project if the projectId existst
            if (queryString.substring(queryString.indexOf("=")).length() > 1) {
                isUpdate = true;
            } else {
                isUpdate = false;
            }

            //edit a project
            if (isUpdate) {
                //extract projectId
                int id = Integer.parseInt(queryString.substring(queryString.indexOf("=") + 1));
                System.out.println("we are updating: " + id);
                //get project from database and set in the request
                long projectId = Long.parseLong(request.getParameter("id"));
                request.setAttribute("projectId", projectId);
                Project project = (Project) session.load(Project.class, projectId);

                // Place in request
                request.setAttribute("fromMonth", project.getFromMonth());
                request.setAttribute("tillMonth", project.getTillMonth());
                request.setAttribute("fromYear", project.getFromYear());
                request.setAttribute("tillYear", project.getTillYear());
                request.setAttribute("name", project.getName());
                request.setAttribute("profession", project.getProfession());
                request.setAttribute("description", project.getDescription());
                session.close();

                request.setAttribute("isUpdate", true);
            } //create a project
            else {
                request.setAttribute("isUpdate", false);
            }

            redirect(request, response, "/edit_project.jsp");
        } //deleten van een project
        else if (action.equals("delete")) {
            long projectId = Long.parseLong(request.getParameter("id"));
            Transaction tx = session.beginTransaction();
            Project project = (Project) session.load(Project.class, projectId);
            session.delete(project);

            tx.commit();
            
            int userId = Integer.parseInt(request.getSession().getAttribute("loggedInUserId").toString());
            response.sendRedirect("../profile?id=" + userId);
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

        if (action.equals("edit") || action.equals("new")) {
            System.out.println("action equals: " + action);
            boolean isUpdate = (action.equals("edit") ? true : false);

            //step 1: do a form validation
            ProjectForm projectForm = new ProjectForm();
            projectForm.setFromMonth(request.getParameter("fromMonth"));
            projectForm.setTillMonth(request.getParameter("tillMonth"));
            projectForm.setFromYear(request.getParameter("fromYear"));
            projectForm.setTillYear(request.getParameter("tillYear"));
            projectForm.setName(request.getParameter("name"));
            projectForm.setProfession(request.getParameter("profession"));
            projectForm.setDescription(request.getParameter("description"));

            ProjectValidator validator = new ProjectValidator();
            List<String> errors = validator.validate(projectForm);

            //step 1b: check if project exists (project id must not be empty)
            List result = null;
            if (!GenericValidator.isEmpty("projectId")) {
                Session session = HibernateUtil.getSessionFactory().openSession();
                String hql = "from Project where projectId = ?";
                result = session.createQuery(hql)
                        .setString(0, request.getParameter("projectId"))
                        .list();
                session.close();

                //on creating project
                if (!isUpdate && result != null && !result.isEmpty()) {
                    errors.add("Project Id already exists");
                } //on updating project
                else {
                    //check if project id is changed (and still unique) while editing
                    if (!result.isEmpty()) {
                        Project project = (Project) result.get(0);
                        if (request.getParameter("projectId").equals(project.getProjectId())) {
                            if (Integer.parseInt(request.getParameter("projectId")) != project.getProjectId()) {
                                errors.add("Project Id already exists");
                            }
                        }
                    }
                }
            }
            //step 2: redirect user back if there are any errors
            if (!errors.isEmpty()) {
                //projectId needs only to be set when we are editing
                if (isUpdate) {
                    request.setAttribute("projectId", request.getParameter("projectId"));
                    //don't forget to set that we are still updating
                    request.setAttribute("isUpdate", true);
                }
                request.setAttribute("fromMonth", request.getParameter("fromMonth"));
                request.setAttribute("tillMonth", request.getParameter("tillMonth"));
                request.setAttribute("fromYear", request.getParameter("fromYear"));
                request.setAttribute("tillYear", request.getParameter("tillYear"));
                request.setAttribute("name", request.getParameter("name"));
                request.setAttribute("profession", request.getParameter("profession"));
                request.setAttribute("description", request.getParameter("description"));

                //placing errors in request
                request.setAttribute("errorsSize", errors.size());
                request.setAttribute("errors", errors);

                redirect(request, response, "/edit_project.jsp");
            } else {
                //step 3: there are no errors. We can start to create or update a project
                Project project;
                Session session = HibernateUtil.getSessionFactory().openSession();
                Transaction tx = session.beginTransaction();

                //get the projectId if we are updating a project
                if (isUpdate) {
                    System.out.println("projectid: " + request.getParameter("projectId"));
                    System.out.println("test: " + request.getParameter("projectId"));
                    Long projectId = Long.parseLong(request.getParameter("projectId"));
                    project = (Project) session.load(Project.class, projectId);
                } else {
                    project = new Project();
                }
                project.setFromMonth(Integer.parseInt(request.getParameter("fromMonth")));
                project.setTillMonth(Integer.parseInt(request.getParameter("tillMonth")));
                project.setFromYear(Integer.parseInt(request.getParameter("fromYear")));
                project.setTillYear(Integer.parseInt(request.getParameter("tillYear")));
                project.setName(request.getParameter("name"));
                project.setProfession(request.getParameter("profession"));
                project.setDescription(request.getParameter("description"));

                User user = new User();
                int userId = Integer.parseInt(request.getSession().getAttribute("loggedInUserId").toString());
                user.setUserId(userId);
                project.setUser(user);

                session.saveOrUpdate(project);
                tx.commit();
                session.close();

                //request handling
                if (isUpdate) {
                    request.setAttribute("projectUpdated", true);
                } else {
                    request.setAttribute("projectCreated", true);
                }

                request.setAttribute("projectId", project.getProjectId());
                request.setAttribute("fromMonth", project.getFromMonth());
                request.setAttribute("tillMonth", project.getTillMonth());
                request.setAttribute("fromYear", project.getFromYear());
                request.setAttribute("tillYear", project.getTillYear());
                request.setAttribute("name", project.getName());
                request.setAttribute("profession", project.getProfession());
                request.setAttribute("description", project.getDescription());

                //we are now editing
                request.setAttribute("isUpdate", true);

                redirect(request, response, "/edit_project.jsp");
            }
        }
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String address)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
}
