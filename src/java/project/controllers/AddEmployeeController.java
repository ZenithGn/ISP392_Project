package project.controllers;

import project.model.dao.EmployeeDAO;
import project.model.dao.AccountDAO;
import project.model.dto.EmployeeDTO;
import project.model.dto.AccountDTO;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "AddEmployeeController", urlPatterns = {"/AddEmployeeController"})
public class AddEmployeeController extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Get form parameters
            String name = request.getParameter("name");
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            String role = request.getParameter("role");
            String workingHours = request.getParameter("workingHours");
            String password = request.getParameter("password");
            int ownerId = parseIntOrZero(request.getParameter("ownerId"));
            int managerId = parseIntOrZero(request.getParameter("managerId"));

            // Validate required fields
            if (name == null || name.trim().isEmpty()) {
                throw new Exception("Tên hiển thị là bắt buộc");
            }
            if (role == null || role.trim().isEmpty()) {
                throw new Exception("Vai trò là bắt buộc");
            }
            if (email == null || email.trim().isEmpty()) {
                throw new Exception("Email là bắt buộc");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new Exception("Mật khẩu là bắt buộc");
            }

            // Create account first
            AccountDAO accountDAO = new AccountDAO();
            
            // Check if email already exists as username
            if (accountDAO.checkDuplicate(email)) {
                throw new Exception("Email này đã được sử dụng để tạo tài khoản");
            }

            // Create account with email as username
            AccountDTO account = new AccountDTO();
            account.setUserName(email);
            account.setPassword(password);
            account.setIsRegistered(true);
            account.setRole("employee"); // Set role as employee

            // Generate account and get the ID
            int generatedAccountId = accountDAO.createAccount(account);
            
            if (generatedAccountId <= 0) {
                throw new Exception("Không thể tạo tài khoản cho nhân viên");
            }

            // Create employee with the generated account ID
            EmployeeDTO employee = new EmployeeDTO(name, firstName, lastName, phone, email, role, workingHours);
            employee.setOwnerId(ownerId);
            employee.setManagerId(managerId);
            employee.setAccountId(generatedAccountId); // Auto-assign the generated account ID

            EmployeeDAO employeeDAO = new EmployeeDAO();
            boolean success = employeeDAO.addEmployee(employee);
            
            if (success) {
                request.setAttribute("SUCCESS", "Thêm nhân viên mới thành công! Tài khoản đăng nhập: " + email);
            } else {
                // If employee creation fails, we should ideally rollback the account creation
                // For now, just show error
                request.setAttribute("ERROR", "Không thể thêm nhân viên mới. Tài khoản đã được tạo nhưng thông tin nhân viên chưa được lưu.");
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("ERROR", "Lỗi hệ thống: " + ex.getMessage());
        }
        
        request.getRequestDispatcher("addEmployee.jsp").forward(request, response);
    }

    private int parseIntOrZero(String s) {
        try {
            return (s != null && !s.trim().isEmpty()) ? Integer.parseInt(s) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
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