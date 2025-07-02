package project.controllers;

import project.model.dao.EmployeeDAO;
import project.model.dto.EmployeeDTO;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "UpdateEmployeeController", urlPatterns = {"/UpdateEmployeeController"})
public class UpdateEmployeeController extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int employeeId = Integer.parseInt(request.getParameter("employeeId"));
            String name = request.getParameter("employeeName");
            String email = request.getParameter("employeeEmail");
            String phone = request.getParameter("employeePhone");
            String role = request.getParameter("employeeRole");
            String workingHours = request.getParameter("employeeWorking");

            EmployeeDAO dao = new EmployeeDAO();
            EmployeeDTO existing = dao.getEmployeeById(employeeId);
            if (existing == null) {
                request.setAttribute("ERROR", "Không tìm thấy nhân viên.");
            } else {
                existing.setName(name);
                existing.setEmail(email);
                existing.setPhone(phone);
                existing.setRole(role);
                existing.setWorkingHours(workingHours);

                boolean success = dao.updateEmployee(existing);
                if (success) {
                    request.setAttribute("SUCCESS", "Cập nhật nhân viên thành công!");
                } else {
                    request.setAttribute("ERROR", "Không thể cập nhật nhân viên.");
                }
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