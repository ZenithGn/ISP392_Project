/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author KhoaLe
 */
public class MainController extends HttpServlet {

    private static final String WELCOME = "homepage.jsp";
    private static final String LOGIN = "Login";
    private static final String LOGIN_CONTROLLER = "LoginController";
    private static final String LOGOUT = "Logout";
    private static final String LOGOUT_CONTROLLER = "LogoutController";
    private static final String CREATE = "Create";
    private static final String CREATE_CONTROLLER = "CreateController";
    private static final String CUSTOMER_REQUEST = "CustomerRequest";
    private static final String CUSTOMER_REQUEST_CONTROLLER = "CreateRequestController";
    private static final String MANAGER_REQUEST = "ManagerRequest";
    private static final String MANAGER_REQUEST_CONTROLLER = "ManagerRequestController";
    private static final String SERVICE_CREATE = "createService";
    private static final String SERVICE_CREATE_CONTROLLER = "CreateServiceController";
    private static final String SERVICE_DELETE = "deleteService";
    private static final String SERVICE_DELETE_CONTROLLER = "DeleteServiceController";
    private static final String SERVICE_UPDATE = "updateService";
    private static final String SERVICE_UPDATE_CONTROLLER = "UpdateServiceController";
    private static final String ADD_EMPLOYEE = "AddEmployee";
    private static final String ADD_EMPLOYEE_CONTROLLER = "AddEmployeeController";
    private static final String UPDATE_EMPLOYEE = "updateEmployee";
    private static final String UPDATE_EMPLOYEE_CONTROLLER = "UpdateEmployeeController";
    private static final String DELETE_EMPLOYEE = "Delete";
    private static final String DELETE_EMPLOYEE_CONTROLLER = "DeleteEmployeeController";
    private static final String SEARCH_EMPLOYEE = "searchEmployee";
    private static final String SEARCH_EMPLOYEE_CONTROLLER = "SearchEmployeeController";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = WELCOME;
        try {
            String action = request.getParameter("action");
            if (action == null) {
                url = WELCOME;
            } else if (LOGIN.equals(action)) {
                url = LOGIN_CONTROLLER;
            } else if (LOGOUT.equals(action)) {
                url = LOGOUT_CONTROLLER;
            } else if (CREATE.equals(action)) {
                url = CREATE_CONTROLLER;
            } else if (CUSTOMER_REQUEST.equals(action)) {
                url = CUSTOMER_REQUEST_CONTROLLER;
            } else if (MANAGER_REQUEST.equals(action)) {
                url = MANAGER_REQUEST_CONTROLLER;
            } else if (SERVICE_CREATE.equals(action)) {
                url = SERVICE_CREATE_CONTROLLER;
            } else if (SERVICE_DELETE.equals(action)) {
                url = SERVICE_DELETE_CONTROLLER;
            } else if (SERVICE_UPDATE.equals(action)) {
                url = SERVICE_UPDATE_CONTROLLER;
            } else if (ADD_EMPLOYEE.equals(action)) {
                url = ADD_EMPLOYEE_CONTROLLER;
            } else if (UPDATE_EMPLOYEE.equals(action)) {
                url = UPDATE_EMPLOYEE_CONTROLLER;
            } else if (DELETE_EMPLOYEE.equals(action)) {
                url = DELETE_EMPLOYEE_CONTROLLER;
            } else if (SEARCH_EMPLOYEE.equals(action)) {
                url = SEARCH_EMPLOYEE_CONTROLLER;
            }
        } catch (Exception e) {
            log("Error at MainController: " + e.toString());
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
