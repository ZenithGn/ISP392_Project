/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import project.model.dao.AccountDAO;
import project.model.dto.AccountDTO;

/**
 *
 * @author KhoaLe
 */
@WebServlet(name = "CreateController", urlPatterns = {"/CreateController"})
public class CreateController extends HttpServlet {

    private static final String ERROR = "register.jsp";
    private static final String SUCCESS = "login.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String url = ERROR;
        
        try {
            // Get parameters - using both possible parameter names for email
            String userName = request.getParameter("userName");
            String email = request.getParameter("emailAddress");
            if (email == null || email.trim().isEmpty()) {
                email = request.getParameter("emailAdress"); // fallback for typo
            }
            String phone = request.getParameter("phone");
            String password = request.getParameter("password");
            String confirm = request.getParameter("confirm");

            // Debug logging
            System.out.println("DEBUG - Received parameters:");
            System.out.println("userName: " + userName);
            System.out.println("email: " + email);
            System.out.println("phone: " + phone);
            System.out.println("password: " + (password != null ? "***" : "null"));
            System.out.println("confirm: " + (confirm != null ? "***" : "null"));

            // Basic validation
            if (userName == null || userName.trim().isEmpty()) {
                request.setAttribute("error", "Username is required.");
                request.getRequestDispatcher(url).forward(request, response);
                return;
            }
            
            if (email == null || email.trim().isEmpty()) {
                request.setAttribute("error", "Email address is required.");
                request.getRequestDispatcher(url).forward(request, response);
                return;
            }
            
            if (phone == null || phone.trim().isEmpty()) {
                request.setAttribute("error", "Phone number is required.");
                request.getRequestDispatcher(url).forward(request, response);
                return;
            }
            
            if (password == null || password.trim().isEmpty()) {
                request.setAttribute("error", "Password is required.");
                request.getRequestDispatcher(url).forward(request, response);
                return;
            }

            if (confirm == null || !password.equals(confirm)) {
                request.setAttribute("error", "Passwords do not match.");
                request.getRequestDispatcher(url).forward(request, response);
                return;
            }

            System.out.println("DEBUG - Basic validation passed");

            // Database operations
            AccountDAO dao = new AccountDAO();
            
            // Check duplicate username
            System.out.println("DEBUG - Checking duplicate username");
            boolean isDuplicate = dao.checkDuplicate(userName);
            if (isDuplicate) {
                request.setAttribute("error", "Username already exists.");
                request.getRequestDispatcher(url).forward(request, response);
                return;
            }
            
            System.out.println("DEBUG - Username is unique");

            // Generate account ID
            System.out.println("DEBUG - Generating account ID");
            String accId = dao.generateAccountId();
            System.out.println("DEBUG - Generated account ID: " + accId);
            
            if (accId == null) {
                request.setAttribute("error", "Failed to generate account ID.");
                request.getRequestDispatcher(url).forward(request, response);
                return;
            }

            // Create account
            System.out.println("DEBUG - Creating account");
            AccountDTO acc = new AccountDTO(accId, userName, password, true, "customer");
            boolean accountCreated = dao.createAccount(acc);
            System.out.println("DEBUG - Account created: " + accountCreated);
            
            if (!accountCreated) {
                request.setAttribute("error", "Failed to create account.");
                request.getRequestDispatcher(url).forward(request, response);
                return;
            }

            // Generate customer ID
            System.out.println("DEBUG - Generating customer ID");
            String custId = dao.generateCustomerId();
            System.out.println("DEBUG - Generated customer ID: " + custId);
            
            if (custId == null) {
                request.setAttribute("error", "Failed to generate customer ID.");
                request.getRequestDispatcher(url).forward(request, response);
                return;
            }

            // Create customer record
            System.out.println("DEBUG - Creating customer record");
            dao.createCustomer(custId, userName, email, phone, accId);
            System.out.println("DEBUG - Customer record created successfully");

            // Success
            request.setAttribute("message", "Registration successful. Please login.");
            url = SUCCESS;
            System.out.println("DEBUG - Registration completed successfully");
            
        } catch (Exception e) {
            System.err.println("ERROR in CreateController: " + e.getMessage());
            e.printStackTrace();
            log("Error at CreateController: " + e.toString());
            request.setAttribute("error", "Database error: " + e.getMessage());
        }
        
        request.getRequestDispatcher(url).forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Controller for creating new user accounts";
    }
}