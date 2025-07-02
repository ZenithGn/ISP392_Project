package project.controllers;

import project.model.dao.EmployeeDAO;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "DeleteEmployeeController", urlPatterns = {"/DeleteEmployeeController"})
public class DeleteEmployeeController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String employeeIdStr = request.getParameter("employeeId"); // FIXED: Changed from "employee" to "employeeId"
        try {
            int employeeId = Integer.parseInt(employeeIdStr);
            EmployeeDAO dao = new EmployeeDAO();
            boolean success = dao.deleteEmployee(employeeId);
            if (success) {
                request.setAttribute("SUCCESS", "Xóa nhân viên thành công!");
            } else {
                request.setAttribute("ERROR", "Không thể xóa nhân viên.");
            }
        } catch (Exception ex) {
            request.setAttribute("ERROR", "Lỗi hệ thống: " + ex.getMessage());
        }
        // FIXED: Forward to correct controller
        request.getRequestDispatcher("MainController?action=searchEmployee&searchTerm=").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
