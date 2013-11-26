package controllers;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Education;
import models.EducationForm;
import models.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.HibernateUtil;
import validators.GenericValidator;
import validators.EducationValidator;

/**
 *
 * @author Shahin Mokhtar
 */
public class ManageEducationController extends HttpServlet {

    
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
            //extract educationId (if available)
            boolean isUpdate;
            String queryString = request.getQueryString();
            //we are updating a education if the educationId existst
            if (queryString.substring(queryString.indexOf("=")).length() > 1) {
                isUpdate = true;
            } else {
                isUpdate = false;
            }

            //edit a education
            if (isUpdate) {
                //extract educationId
                int id = Integer.parseInt(queryString.substring(queryString.indexOf("=") + 1));
                System.out.println("we are updating: " + id);
                //get education from database and set in the request
                long educationId = Long.parseLong(request.getParameter("id"));
                request.setAttribute("educationId", educationId);
                Education education = (Education) session.load(Education.class, educationId);

                // Place in request
                request.setAttribute("fromMonth", education.getFromMonth());
                request.setAttribute("tillMonth", education.getTillMonth());
                request.setAttribute("fromYear", education.getFromYear());
                request.setAttribute("tillYear", education.getTillYear());
                request.setAttribute("name", education.getName());
                request.setAttribute("profession", education.getProfession());
                request.setAttribute("description", education.getDescription());
                session.close();

                request.setAttribute("isUpdate", true);
            } //create a project
            else {
                request.setAttribute("isUpdate", false);
            }

            redirect(request, response, "/edit_education.jsp");
        } //deleten van een education
        else if (action.equals("delete")) {
            long educationId = Long.parseLong(request.getParameter("id"));
            Transaction tx = session.beginTransaction();
            Education education = (Education) session.load(Education.class, educationId);
            session.delete(education);

            tx.commit();
            response.sendRedirect("../profile?id=");
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
            EducationForm educationForm = new EducationForm();
            educationForm.setFromMonth(request.getParameter("fromMonth"));
            educationForm.setTillMonth(request.getParameter("tillMonth"));
            educationForm.setFromYear(request.getParameter("fromYear"));
            educationForm.setTillYear(request.getParameter("tillYear"));
            educationForm.setName(request.getParameter("name"));
            educationForm.setProfession(request.getParameter("profession"));
            educationForm.setDescription(request.getParameter("description"));

            EducationValidator validator = new EducationValidator();
            List<String> errors = validator.validate(educationForm);

            //step 1b: check if education exists (education id must not be empty)
            List result = null;
            if (!GenericValidator.isEmpty("educationId")) {
                Session session = HibernateUtil.getSessionFactory().openSession();
                String hql = "from Education where educationId = ?";
                result = session.createQuery(hql)
                        .setString(0, request.getParameter("educationId"))
                        .list();
                session.close();

                //on creating education
                if (!isUpdate && result != null && !result.isEmpty()) {
                    errors.add("Education Id already exists");
                } //on updating education
                else {
                    //check if education id is changed (and still unique) while editing
                    if (!result.isEmpty()) {
                        Education education = (Education) result.get(0);
                        if (request.getParameter("educationId").equals(education.getEducationId())) {
                            if (Integer.parseInt(request.getParameter("educationId")) != education.getEducationId()) {
                                errors.add("Education Id already exists");
                            }
                        }
                    }
                }
            }
            //step 2: redirect user back if there are any errors
            if (!errors.isEmpty()) {
                //educationId needs only to be set when we are editing
                if (isUpdate) {
                    request.setAttribute("educationId", request.getParameter("educationId"));
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

                redirect(request, response, "/edit_education.jsp");
            } else {
                //step 3: there are no errors. We can start to create or update a education
                Education education;
                Session session = HibernateUtil.getSessionFactory().openSession();
                Transaction tx = session.beginTransaction();

                //get the educationId if we are updating a education
                if (isUpdate) {
                    Long educationId = Long.parseLong(request.getParameter("educationId"));
                    education = (Education) session.load(Education.class, educationId);
                } else {
                    education = new Education();
                }
                education.setFromMonth(Integer.parseInt(request.getParameter("fromMonth")));
                education.setTillMonth(Integer.parseInt(request.getParameter("tillMonth")));
                education.setFromYear(Integer.parseInt(request.getParameter("fromYear")));
                education.setTillYear(Integer.parseInt(request.getParameter("tillYear")));
                education.setName(request.getParameter("name"));
                education.setProfession(request.getParameter("profession"));
                education.setDescription(request.getParameter("description"));

                User user = new User();
                int userId = Integer.parseInt(request.getSession().getAttribute("loggedInUserId").toString());
                user.setUserId(userId);
                education.setUser(user);

                session.saveOrUpdate(education);
                tx.commit();
                session.close();

                //request handling
                if (isUpdate) {
                    request.setAttribute("educationUpdated", true);
                } else {
                    request.setAttribute("educationCreated", true);
                }

                request.setAttribute("educationId", education.getEducationId());
                request.setAttribute("fromMonth", education.getFromMonth());
                request.setAttribute("tillMonth", education.getTillMonth());
                request.setAttribute("fromYear", education.getFromYear());
                request.setAttribute("tillYear", education.getTillYear());
                request.setAttribute("name", education.getName());
                request.setAttribute("profession", education.getProfession());
                request.setAttribute("description", education.getDescription());

                //we are now editing
                request.setAttribute("isUpdate", true);

                redirect(request, response, "/edit_education.jsp");
            }
        }
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String address)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
}
