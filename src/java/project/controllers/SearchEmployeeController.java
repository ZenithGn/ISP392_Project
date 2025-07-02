package project.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import project.model.dao.EmployeeDAO;
import project.model.dto.EmployeeDTO;

@WebServlet(name = "SearchEmployeeController", urlPatterns = {"/SearchEmployeeController"})
public class SearchEmployeeController extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchTerm = request.getParameter("searchTerm");
        try {
            EmployeeDAO dao = new EmployeeDAO();
            List<EmployeeDTO> employees = dao.searchEmployees(searchTerm != null ? searchTerm : "");
            request.setAttribute("employees", employees);
        } catch (Exception ex) {
            request.setAttribute("ERROR", "Lỗi hệ thống: " + ex.getMessage());
        }
        request.getRequestDispatcher("employeeManage.jsp").forward(request, response);
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