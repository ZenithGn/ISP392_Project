package project.controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import project.model.dao.AccountDAO;

import java.io.IOException;
import project.model.dao.CustomerDAO;

@WebServlet(name = "ResetPasswordController", urlPatterns = {"/ResetPasswordController"})
public class ResetPasswordController extends HttpServlet {

    private static final String ERROR = "resetPassword.jsp";
    private static final String SUCCESS = "login.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = ERROR;
        try {
            String newPassword = request.getParameter("newPassword");
            String confirmNewPassword = request.getParameter("confirmNewPassword");

            if (!newPassword.equals(confirmNewPassword)) {
                request.setAttribute("MESSAGE", "Passwords do not match.");
            } else {
                HttpSession session = request.getSession(false);
                if (session != null) {
                    String email = (String) session.getAttribute("email");
                    System.out.println("Resetting password for email: " + email);
                    System.out.println("New password: " + newPassword);
                    if (email != null && !email.isEmpty()) {
                        CustomerDAO dao = new CustomerDAO();
                        boolean updated = dao.updatePassword(email, newPassword);
                        if (updated) {
                            session.invalidate();
                            url = SUCCESS;
                        } else {
                            request.setAttribute("MESSAGE", "Password update failed.");
                        }
                    }
                } else {
                    request.setAttribute("MESSAGE", "Session expired.");
                }
            }
        } catch (Exception e) {
            log("Error at ResetPasswordController: " + e.toString());
            request.setAttribute("MESSAGE", "Error resetting password.");
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
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
        processRequest(request, response);
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
        processRequest(request, response);
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
