package controller;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import models.Project;
import models.Skill;
import models.User;
import models.Work;
import org.hibernate.Criteria;
import org.hibernate.Session;
import service.HibernateUtil;
//import services.HibernateUtil;

public class GebruikerController extends HttpServlet {

    /* HTTP GET request */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String dispatchUrl = null;
        
        /* create session */
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        /* User information */
        List<User> tempGebruiker = new LinkedList();
        // Zet de session in een variabele        
        Criteria criteria = session.createCriteria(User.class);
        tempGebruiker = criteria.list();
        // Zet de lijst met gebruikers en het totaal aantal gebruikers op het request
        request.setAttribute("userList", tempGebruiker);
        request.setAttribute("aantalGebruikers", tempGebruiker.size());
        
        
        /* Work experience */
        List<Work> tempWork = new LinkedList();
        // Zet de session in een variable
        Criteria criteriaWork = session.createCriteria(Work.class);
        tempWork = criteriaWork.list();
        // Zet de lijst met gebruikers en het totaal aantal gebruikers op het request
        request.setAttribute("workList", tempWork);
        request.setAttribute("aantalWork", tempWork.size());
        
        /* Project experience */
        List<Project> tempProject = new LinkedList();
        // Zet de session in een variable
        Criteria criteriaProject = session.createCriteria(Project.class);
        tempProject = criteriaProject.list();
        // Zet de lijst met gebruikers en het totaal aantal gebruikers op het request
        request.setAttribute("projectList", tempProject);
        request.setAttribute("aantalProjects", tempProject.size());
        
        /* Skill  */
        List<Skill> tempSkill = new LinkedList();
        // Zet de session in een variable
        Criteria criteriaSkill = session.createCriteria(Skill.class);
        tempSkill = criteriaSkill.list();
        // Zet de lijst met gebruikers en het totaal aantal gebruikers op het request
        request.setAttribute("skillList", tempSkill);
        request.setAttribute("aantalSkills", tempSkill.size());

        
        /* stuur door naar */
        dispatchUrl = "/profile.jsp";

        if (dispatchUrl != null) {
            RequestDispatcher rd
                    = request.getRequestDispatcher(dispatchUrl);
            rd.forward(request, response);
        }

    }
}
