/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import service.HibernateUtil;

/**
 *
 * @author Joshua
 */
@WebServlet(name = "ControllerServlet", urlPatterns = {"/ControllerServlet","/VirtualClassRoom","/CreateAccount","/StartStream","/LoginPage","/CreateAccountPage","/Login","/CreateAccount"})
public class ControllerServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
  



    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        

        String uri = request.getRequestURI();
        
        int lastIndex = uri.lastIndexOf("/");
        String action = uri.substring(lastIndex + 1);
        String dispatchUrl = "index.jsp";
        
        if (action.equals("VirtualClassRoom")) {
            dispatchUrl = "VirtualClassRoom.jsp";}
        if (action.equals("CreateAccountPage")) {
            dispatchUrl = "CreateAccountPage.jsp";}
        if (action.equals("CreateAccount")) {
            dispatchUrl = "index.jsp";}
        if (action.equals("LoginPage")) {
            dispatchUrl = "LoginPage.jsp";}
        if (action.equals("Login")) {
            dispatchUrl = "index.jsp";}
        if (action.equals("StartStream")) {
            //String vlcstreamstart = "vlc screen:// \"#transcode{vcodec=WMV2,vb=800,acodec=wma2,ab=128,channels=2,samplerate=44100}:duplicate{dst=http{mux=asf,dst=:1234/},dst=display}\"";
            //Runtime.getRuntime().exec("C:\\Program Files (x86)\\VideoLAN\\VLC");
            //System.exec("rsh remotemachine programname");
            Runtime.getRuntime().exec("\"C:\\Program Files (x86)\\VideoLAN\\VLC\\vlc.exe\" vlc screen:// --sout=#transcode{vcodec=WMV2,vb=800,acodec=wma2,ab=128,channels=2,samplerate=44100}:duplicate{dst=http{mux=asf,dst=:1234/}");
            dispatchUrl = "VirtualClassRoom.jsp";}
        
       if (dispatchUrl != null) {
            RequestDispatcher rd =
                    request.getRequestDispatcher(dispatchUrl);
            rd.forward(request, response);
        }
    }
  @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
          if (request.getParameter("userName") != null) {
            
            
        List<User> gebruikersLijst = new LinkedList();

	// Zet de session in een variabele
	Session session = HibernateUtil.getSessionFactory().getCurrentSession();

	Transaction tx = session.beginTransaction();

	Criteria criteria = session.createCriteria(User.class);
	gebruikersLijst = criteria.list();
            
            //Als er een id is meegegeven, worden de gegevens van de gebruiker opgehaald.
            String userName = String.valueOf(request.getParameter("userName"));
            request.setAttribute("userName", userName); // TODO: why?
            // Haal een sessie object op uit het request
            HttpSession sessie = request.getSession();
            //List<User> gebruikers = (LinkedList) sessie.getAttribute("gebruikers"); //Haalt de lijst met gebruikers op en slaat deze op in een LinkedList
            
            for (int i = 0; i < gebruikersLijst.size(); i++) {
                User tempGebruiker = (User) gebruikersLijst.get(i);

              //Als de gebruiker overeenkomt met het gegeven id, worden de gegevens ingevuld in het formulier.
              if (tempGebruiker.getUserName() == userName) {
                  
                    request.setAttribute("userName", tempGebruiker.getUserName());
                    request.setAttribute("firstName", tempGebruiker.getFirstName());
                    request.setAttribute("lastName", tempGebruiker.getLastName());
                    request.setAttribute("password", tempGebruiker.getPassword());
                    request.setAttribute("userRole", tempGebruiker.getUserRole());
                }
            }
          }
               
        
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);

         // Haal een sessie object op uit het request
        HttpSession sessie = request.getSession();
        
        // Als the parameter 'id' niet null is, dan hebben we te maken met
        // een user die wordt geupdate

        // Haal de lijst met gebruikers op uit de sessie
        List<User> gebruikers = (List) sessie.getAttribute("username");
        
        // Controleer of de lijst met gebruikers niet null is, zo ja, 
        // creër een lege lijst en zet deze op de sessie
        if (gebruikers == null) {
            gebruikers = new LinkedList<User>();
        }
        
        // Zet de form parameters om in een User object
        User formUser = getUserFromRequest(request);
        
           
           
        sessie.setAttribute("gebruikers", gebruikers);

        // Stuur een redirect terug naar de client. De client zal dan
        // meteen een nieuwe GET request doen naar ../gebruikers
        response.sendRedirect("../gebruikers");
    }
    

    private User getUserFromRequest(HttpServletRequest request) {
        User user = new User();
        
        if (request.getParameter("userName") != null && !request.getParameter("id").isEmpty()) {
            user.setUserName(request.getParameter("userName"));
        }
        if (request.getParameter("firstName") != null) {
            user.setFirstName(request.getParameter("firstName"));
        }
        if (request.getParameter("lastName") != null) {
            user.setLastName(request.getParameter("lastName"));
        }
        if (request.getParameter("password") != null) {
            user.setPassword(request.getParameter("password"));
        }
        if (request.getParameter("userRole") != null) {
            user.setPassword(request.getParameter("userRole"));
        }
        
        return user;
    }
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}