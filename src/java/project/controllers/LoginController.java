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
import project.model.dao.EmployeeDAO;
import project.model.dto.AccountDTO;
import project.model.dto.EmployeeDTO;

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

        try {
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
                        AccountDAO customerDAO = new AccountDAO();
            String customerId = customerDAO.getCustomerIdByAccountId(account.getId()); // account.getId() là account_id
            session.setAttribute("CUSTOMER_ID", customerId);
            url = SUCCESS;
            break;
                    case "employee":
    EmployeeDAO empDAO = new EmployeeDAO();
    
    // Ép kiểu từ String -> int
    int accountId = Integer.parseInt(account.getId());

    EmployeeDTO employee = empDAO.getEmployeeByAccountId(accountId);
    if (employee != null) {
        session.setAttribute("LOGIN_EMPLOYEE", employee);
        url = SUCCESS;
    } else {
        request.setAttribute("error", "Không tìm thấy thông tin nhân viên.");
        request.getRequestDispatcher(ERROR).forward(request, response);
        return;
    }
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
