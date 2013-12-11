/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Activity;
import models.Skill;
import models.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.HibernateUtil;

/**
 *
 * @author F4LLCON
 */
public class VgaController extends HttpServlet {

   

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
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Criteria criteria = session.createCriteria(User.class);
        List<User> users = criteria.list();
        
        Criteria criteriaSkill = session.createCriteria(Skill.class);
        List<Skill> skills = criteriaSkill.list();
        
        Skill skill = skills.get(0);
        
        for (int i = 0; i < users.size(); i++) {
            // stap 1 a skills ophalen
            List<Skill> UserSkills = users.get(i).getSkills();
            // stap 1 b
            for(int j = 0; j < UserSkills.size(); j++) {
                if(UserSkills.get(j).equals(skill)) {
                    break;
                } else {
                    // stap 2 zoeken naar andere users die de skill hebben
                    List<User> UsersWithSkill = new ArrayList();
                    for(int t = 0; t < users.size(); t++) {
                        List<Skill> UserSkillWithSkill = users.get(t).getSkills();
                        for(int u = 0; u < UserSkillWithSkill.size(); u++) {
                            if(UserSkillWithSkill.get(u).equals(skill)) {
                                UsersWithSkill.add(users.get(t));
                            }
                        }
                    } 
                    if(!UsersWithSkill.isEmpty()) {
                        // pak een random user die moet gaan helpen
                        Random randomNr = new Random();
                        User helper = UsersWithSkill.get(randomNr.nextInt(UsersWithSkill.size()));
                        
                        //plaats nu in de activity feed van de helper dat de user hulp nodig heeft
                        Activity activityHelper = new Activity();
                        activityHelper.setTitle("Someone can help you");
                        activityHelper.setMessage(users.get(i).getFirstname() + " " + users.get(i).getLastname() + " could help you acquire the Skill" + skill.getName());
                        activityHelper.setSent(new Date());
                        activityHelper.setUser(helper);
                        
                        //plaats in de activity feed van de user dat er iemand is die hem kan helpen de skill te verwerven
                        Activity activityReport = new Activity();
                        activityReport.setTitle("Someone needs your help");
                        activityReport.setMessage(helper.getFirstname() + " " + helper.getLastname() + " Could you help this person acquire a skill, please? " + skill.getName());
                        activityReport.setSent(new Date());
                        activityReport.setUser(users.get(i));
                        
                        // plaats activity in de database
                        session.save(activityHelper);
                        session.save(activityReport);
                    } else {
                        // course moet worden aangemaakt
                    }
                }
            }
        }
        
        session.close();
        
    }
    
    private void redirect(HttpServletRequest request, HttpServletResponse response, String address)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }

}
