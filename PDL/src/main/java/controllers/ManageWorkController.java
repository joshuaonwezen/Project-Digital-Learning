package controllers;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Work;
import models.WorkForm;
import models.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.HibernateUtil;
import validators.GenericValidator;
import validators.WorkValidator;

/**
 *
 * @author Shahin Mokhtar
 */
public class ManageWorkController extends HttpServlet {

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
            //extract workId (if available)
            boolean isUpdate;
            String queryString = request.getQueryString();
            //we are updating a work if the workId existst
            if (queryString.substring(queryString.indexOf("=")).length() > 1) {
                isUpdate = true;
            } else {
                isUpdate = false;
            }

            //edit a work
            if (isUpdate) {
                //extract workId
                int id = Integer.parseInt(queryString.substring(queryString.indexOf("=") + 1));
                System.out.println("we are updating: " + id);
                //get work from database and set in the request
                long workId = Long.parseLong(request.getParameter("id"));
                request.setAttribute("workId", workId);
                Work work = (Work) session.load(Work.class, workId);

                // Place in request
                request.setAttribute("fromMonth", work.getFromMonth());
                request.setAttribute("tillMonth", work.getTillMonth());
                request.setAttribute("fromYear", work.getFromYear());
                request.setAttribute("tillYear", work.getTillYear());
                request.setAttribute("name", work.getName());
                request.setAttribute("profession", work.getProfession());
                request.setAttribute("description", work.getDescription());
                session.close();

                request.setAttribute("isUpdate", true);
            } //create a work
            else {
                request.setAttribute("isUpdate", false);
            }

            redirect(request, response, "/edit_work.jsp");
        } //deleten van een work
        else if (action.equals("delete")) {
            long workId = Long.parseLong(request.getParameter("id"));
            Transaction tx = session.beginTransaction();
            Work work = (Work) session.load(Work.class, workId);
            session.delete(work);

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
            WorkForm workForm = new WorkForm();
            workForm.setFromMonth(request.getParameter("fromMonth"));
            workForm.setTillMonth(request.getParameter("tillMonth"));
            workForm.setFromYear(request.getParameter("fromYear"));
            workForm.setTillYear(request.getParameter("tillYear"));
            workForm.setName(request.getParameter("name"));
            workForm.setProfession(request.getParameter("profession"));
            workForm.setDescription(request.getParameter("description"));

            WorkValidator validator = new WorkValidator();
            List<String> errors = validator.validate(workForm);

            //step 1b: check if work exists (work id must not be empty)
            List result = null;
            if (!GenericValidator.isEmpty("workId")) {
                Session session = HibernateUtil.getSessionFactory().openSession();
                String hql = "from Work where workId = ?";
                result = session.createQuery(hql)
                        .setString(0, request.getParameter("workId"))
                        .list();
                session.close();

                //on creating work
                if (!isUpdate && result != null && !result.isEmpty()) {
                    errors.add("Work Id already exists");
                } //on updating work
                else {
                    //check if work id is changed (and still unique) while editing
                    if (!result.isEmpty()) {
                        Work work = (Work) result.get(0);
                        if (request.getParameter("workId").equals(work.getWorkId())) {
                            if (Integer.parseInt(request.getParameter("workId")) != work.getWorkId()) {
                                errors.add("Work Id already exists");
                            }
                        }
                    }
                }
            }
            //step 2: redirect user back if there are any errors
            if (!errors.isEmpty()) {
                //workId needs only to be set when we are editing
                if (isUpdate) {
                    request.setAttribute("workId", request.getParameter("workId"));
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

                redirect(request, response, "/edit_work.jsp");
            } else {
                //step 3: there are no errors. We can start to create or update a work
                Work work;
                Session session = HibernateUtil.getSessionFactory().openSession();
                Transaction tx = session.beginTransaction();

                //get the workId if we are updating a work
                if (isUpdate) {
                    Long workId = Long.parseLong(request.getParameter("workId"));
                    work = (Work) session.load(Work.class, workId);
                } else {
                    work = new Work();
                }
                work.setFromMonth(Integer.parseInt(request.getParameter("fromMonth")));
                work.setTillMonth(Integer.parseInt(request.getParameter("tillMonth")));
                work.setFromYear(Integer.parseInt(request.getParameter("fromYear")));
                work.setTillYear(Integer.parseInt(request.getParameter("tillYear")));
                work.setName(request.getParameter("name"));
                work.setProfession(request.getParameter("profession"));
                work.setDescription(request.getParameter("description"));

                User user = new User();
                int userId = Integer.parseInt(request.getSession().getAttribute("loggedInUserId").toString());
                user.setUserId(userId);
                work.setUser(user);

                session.saveOrUpdate(work);
                tx.commit();
                session.close();

                //request handling
                if (isUpdate) {
                    request.setAttribute("workUpdated", true);
                } else {
                    request.setAttribute("workCreated", true);
                }

                request.setAttribute("workId", work.getWorkId());
                request.setAttribute("fromMonth", work.getFromMonth());
                request.setAttribute("tillMonth", work.getTillMonth());
                request.setAttribute("fromYear", work.getFromYear());
                request.setAttribute("tillYear", work.getTillYear());
                request.setAttribute("name", work.getName());
                request.setAttribute("profession", work.getProfession());
                request.setAttribute("description", work.getDescription());

                //we are now editing
                request.setAttribute("isUpdate", true);

                redirect(request, response, "/edit_work.jsp");
            }
        }
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String address)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
}
