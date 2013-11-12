package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.User;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import service.HibernateUtil;
//import services.HibernateUtil;
/**
 *
 * @author wesley
 */
public class LoginController extends HttpServlet {

    
    private static final boolean DEBUG = true; //wanneer true dan hoef je niet iedere keer opnieuw in te loggen
    
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
        
        System.out.println("GET action: " + action);
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
        
        System.out.println("POST action: " + action);
        
        if (DEBUG){
            //maak een admin user aan (als we die nog niet hebben) ter debug
            Session session = HibernateUtil.getSessionFactory().openSession();
            String hql = "from User  where username = ?";
            List result = session.createQuery(hql)
            .setString(0, request.getParameter("administrator"))
            .list();
            session.close();
            
            //er bestaat nog geen administrator: maak aan
            if (result.isEmpty()){
                    session = HibernateUtil.getSessionFactory().openSession();
                    Transaction tx = session.beginTransaction();

                    User user = new User();
                    user.setUsername("admin");
                    user.setFirstname("Jan");
                    user.setLastname("Klaassen");
                    user.setEmailAddress("j.klaassen@demo.nl");
                    user.setPosition("IT Manager");
                    //password = admin
                    user.setPassword("21232f297a57a5a743894a0e4a801fc3");
                    user.setIsAdmin(true);

                    session.save(user);
                    tx.commit();
                    session.close();
                    
                    request.setAttribute("userId", user.getUserId());
                    request.setAttribute("username", user.getUsername());
                    request.setAttribute("isAdmin", user.isIsAdmin());
                    
                    redirect(request, response, "/homepage.jsp");
            }
        }
        else if (action.equals("login") && !request.getParameter("username").trim().isEmpty()){
            Session session = HibernateUtil.getSessionFactory().openSession();
            String hql = "from User  where username = ?";
            List result = session.createQuery(hql)
            .setString(0, request.getParameter("username"))
            .list();
            session.close();
            
            //als de list leeg is dan is de username zoiezo fout
            List<String> errors = new ArrayList<String>();
            if (result.isEmpty()){
                errors.add("Username or password incorrect");
                request.setAttribute("errors", errors);
                redirect(request, response, "/index.jsp");
            }
            //anders kunnen we nu de password gaan vergelijken
            else{
                User user = (User)result.get(0);
                
                //geef foutmelding als password leeg is
                if (request.getParameter("password").trim().isEmpty()){
                    errors.add("Username or password incorrect");
                    request.setAttribute("errors", errors);
                    redirect(request, response, "/index.jsp");
                }
                else{
                    //password gaan vergelijken
                    String md5 = md5(request.getParameter("password"));
                    
                    if (!user.getPassword().equals(md5)){
                        errors.add("Username or password incorrect");
                        request.setAttribute("errors", errors);
                        redirect(request, response, "/index.jsp");
                    }
                    //login is goed
                    else{
                        request.setAttribute("userId", user.getUserId());
                        request.setAttribute("username", user.getUsername());
                        request.setAttribute("isAdmin", user.isIsAdmin());
                        
                        redirect(request, response, "/homepage.jsp");
                    }
                }
            }
        }
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String address) 
            throws ServletException, IOException{
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
    /**
     * Maakt een MD5-hash van een String
     * @param password      De password String die moet worden geconvert naar een MD5 hash
     * @return              MD5-hash in String formaat
     */
    public static String md5(String password){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(password.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } 
        catch (java.security.NoSuchAlgorithmException e) {
            //todo
        }
        return null;
    }
}