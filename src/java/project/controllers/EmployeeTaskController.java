/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import project.model.dao.RequestDAO;
import project.model.dto.AccountDTO;
import project.model.dto.EmployeeDTO;
import project.model.dto.RequestDTO;
import project.model.dto.RequestDetailDTO;

/**
 *
 * @author Khanh
 */
@WebServlet(name = "EmployeeTaskController", urlPatterns = {"/EmployeeTaskController"})
public class EmployeeTaskController extends HttpServlet {

    private static final String ERROR = "login.jsp";
    private static final String SUCCESS = "employeeTask.jsp";
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
       String url = ERROR;

        try {
            HttpSession session = request.getSession();
            AccountDTO loginUser = (AccountDTO) session.getAttribute("LOGIN_USER");
            
            

            if (loginUser == null || !"employee".equals(loginUser.getRole())) {
                request.setAttribute("ERROR", "Bạn không có quyền truy cập!");
                request.getRequestDispatcher(ERROR).forward(request, response);
                return;
            }

            EmployeeDTO emp = (EmployeeDTO) session.getAttribute("LOGIN_EMPLOYEE");

            if (emp == null) {
                request.setAttribute("ERROR", "Không tìm thấy thông tin nhân viên.");
                request.getRequestDispatcher(ERROR).forward(request, response);
                return;
            }

            int employeeId = emp.getEmployeeId();

            RequestDAO dao = new RequestDAO();
            List<RequestDetailDTO> taskList = dao.getTasksByEmployeeId(employeeId);

            request.setAttribute("tasks", taskList);
            
          

            url = SUCCESS;

        } catch (Exception e) {
            log("Error at EmployeeTaskController: " + e.toString());
            request.setAttribute("ERROR", "Có lỗi xảy ra: " + e.getMessage());
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
