package controllers;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import models.Education;
import models.Project;
import models.Skill;
import models.User;
import models.Work;
import org.hibernate.*;
import services.HibernateUtil;

/**
 *
 * @author Shahin Mokhtar
 */
public class ProfileHolder extends HttpServlet {

    /* HTTP GET request */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        
        if (request.getParameter("id") != null) {
            /* create session */
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            String dispatchUrl = null;

            int id = Integer.parseInt(request.getParameter("id"));

            User tempGebruiker = (User) session.load(User.class, id);
            // Zet de lijst met gebruikers en het totaal aantal gebruikers op het request
            request.setAttribute("firstname", tempGebruiker.getFirstname());
            request.setAttribute("lastname", tempGebruiker.getLastname());
            request.setAttribute("emailAddress", tempGebruiker.getEmailAddress());
            request.setAttribute("position", tempGebruiker.getPosition());
            request.setAttribute("userId", tempGebruiker.getUserId());

            /* Work experience */
            List<Work> tempWork = new LinkedList();
            // Zet de session in een variable
            Query queryWork = session.createQuery("from Work where user_userId = " + id);
            tempWork = queryWork.list();
            // Zet de lijst met work en het totaal aantal work op het request
            request.setAttribute("workList", tempWork);
            request.setAttribute("aantalWork", tempWork.size());

            /* Project experience */
            List<Project> tempProject = new LinkedList();
            // Zet de session in een variable
            Query queryProject = session.createQuery("from Project where user_userId = " + id);
            tempProject = queryProject.list();
            // Zet de lijst met project en het totaal aantal project op het request
            request.setAttribute("projectList", tempProject);
            request.setAttribute("aantalProjects", tempProject.size());

            /* Skill  */
            List<Skill> tempSkill = new LinkedList();
            // Zet de session in een variable
            Query querySkill = session.createQuery("from Skill where user_userId = " + id);
            tempSkill = querySkill.list();
            // Zet de lijst met skill en het totaal aantal skill op het request
            request.setAttribute("skillList", tempSkill);
            request.setAttribute("aantalSkills", tempSkill.size());
            
            /* Education  */
            List<Education> tempEducation = new LinkedList();
            // Zet de session in een variable
            Query queryEducation = session.createQuery("from Education where user_userId = " + id);
            tempEducation = queryEducation.list();
            // Zet de lijst met skill en het totaal aantal skill op het request
            request.setAttribute("educationList", tempEducation);
            request.setAttribute("aantalEducation", tempEducation.size());

            /* stuur door naar */
            dispatchUrl = "/profile.jsp";

            if (dispatchUrl != null) {
                RequestDispatcher rd
                        = request.getRequestDispatcher(dispatchUrl);
                rd.forward(request, response);
            }
        }
        else
        {
            response.setStatus(404);
        }

    }
}
