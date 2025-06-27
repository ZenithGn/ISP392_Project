/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import project.model.dao.AccountDAO;
import project.model.dto.AccountDTO;
import project.recap.VerifyRecaptcha;

/**
 *
 * @author KhoaLe
 */
@WebServlet(name = "LoginController", urlPatterns = {"/LoginController"})
public class LoginController extends HttpServlet {

    private static final String ERROR = "login.jsp";
    private static final String SUCCESS = "homepage.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String url = ERROR;
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String gRecaptcha = request.getParameter("g-recaptcha-response");

        try {
            if (gRecaptcha == null || gRecaptcha.isEmpty()) {
            request.setAttribute("error", "Vui lòng xác minh CAPTCHA.");
            request.getRequestDispatcher(ERROR).forward(request, response);
            return;
            }

            boolean isCaptchaValid = VerifyRecaptcha.verify(gRecaptcha);
            if (!isCaptchaValid) {
            request.setAttribute("error", "CAPTCHA không hợp lệ hoặc hết hạn.");
            request.getRequestDispatcher(ERROR).forward(request, response);
            return;
            }
            
            AccountDAO dao = new AccountDAO();
            AccountDTO account = dao.checkLoginByPhone(phone, password);

            if (account != null) {
                HttpSession session = request.getSession();
                // Fix: Change attribute name to match what AuthenticationFilter expects
                session.setAttribute("LOGIN_USER", account);
                session.setAttribute("account", account); // Keep this for backward compatibility

                switch (account.getRole()) {
                    case "owner":
                    case "manager":
                    case "customer":
                    case "employee":
                        url = SUCCESS;
                        break;
                    default:
                        request.setAttribute("error", "Invalid role");
                        request.getRequestDispatcher(ERROR).forward(request, response);
                        return;
                }
                response.sendRedirect(url);
            } else {
                request.setAttribute("error", "Invalid phone or password");
                request.getRequestDispatcher(url).forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "System error occurred");
            request.getRequestDispatcher(ERROR).forward(request, response);
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
