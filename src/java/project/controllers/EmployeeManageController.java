package project.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import project.model.dao.EmployeeDAO;
import project.model.dto.AccountDTO;
import project.model.dto.EmployeeDTO;

/**
 * Controller for Employee Management operations Handles CRUD operations for
 * employees Follows the principle: Controller only calls functions, no business
 * logic
 */
@WebServlet(name = "EmployeeManageController", urlPatterns = {"/EmployeeManageController"})
public class EmployeeManageController extends HttpServlet {

    private static final String ERROR = "ERROR";
    private static final String SUCCESS = "SUCCESS";
    private static final String INFO = "INFO";
    private static final String EMPLOYEE_MANAGE_PAGE = "employeeManage.jsp";
    private static final String LOGIN_PAGE = "login.jsp";
    private static final String EMPLOYEE_DETAIL_PAGE = "employeeDetail.jsp";
    private static final String EMPLOYEE_EDIT_PAGE = "editEmployee.jsp";
    private static final String HOMEPAGE = "homepage.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String url = EMPLOYEE_MANAGE_PAGE;

        try {
            // Authentication check
            if (!isAuthenticated(request)) {
                url = LOGIN_PAGE;
            } else if (!isAuthorized(request)) {
                setError(request, "Bạn không có quyền truy cập chức năng này!");
                url = HOMEPAGE;
            } else {
                url = handleAction(request);
            }
        } catch (Exception e) {
            handleException(request, e);
        }

        forwardToPage(request, response, url);
    }

    /**
     * Check if user is authenticated
     */
    private boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession();
        AccountDTO loginUser = getLoginUser(session);
        return loginUser != null;
    }

    /**
     * Check if user is authorized (manager or owner)
     */
    private boolean isAuthorized(HttpServletRequest request) {
        HttpSession session = request.getSession();
        AccountDTO loginUser = getLoginUser(session);
        if (loginUser == null) {
            return false;
        }

        String role = loginUser.getRole();
        return "manager".equals(role) || "owner".equals(role);
    }

    /**
     * Get login user from session
     */
    private AccountDTO getLoginUser(HttpSession session) {
        AccountDTO loginUser = (AccountDTO) session.getAttribute("LOGIN_USER");
        if (loginUser == null) {
            loginUser = (AccountDTO) session.getAttribute("account");
        }
        return loginUser;
    }

    /**
     * Handle different actions
     */
    private String handleAction(HttpServletRequest request) throws SQLException {
        String action = getAction(request);
        EmployeeDAO dao = new EmployeeDAO();

        switch (action) {
            case "getAllEmployees":
                return getAllEmployees(request, dao);
            case "search":
                return searchEmployees(request, dao);
            case "viewEmployee":
                return viewEmployee(request, dao);
            case "editEmployee":
                return editEmployee(request, dao);
            case "deleteEmployee":
                return deleteEmployee(request, dao);
            case "addEmployee":
                return addEmployee(request, dao);
            case "updateEmployee":
                return updateEmployee(request, dao);
            default:
                return getAllEmployees(request, dao);
        }
    }

    /**
     * Get action parameter with default value
     */
    private String getAction(HttpServletRequest request) {
        String action = request.getParameter("action");
        return (action == null) ? "getAllEmployees" : action;
    }

    /**
     * Get all employees
     */
    private String getAllEmployees(HttpServletRequest request, EmployeeDAO dao) throws SQLException {
        List<EmployeeDTO> employees = dao.getAllEmployees();
        request.setAttribute("employees", employees);

        if (employees.isEmpty()) {
            setInfo(request, "Hiện tại chưa có nhân viên nào trong hệ thống.");
        } else {
            setSuccess(request, "Đã tải " + employees.size() + " nhân viên thành công!");
        }

        return EMPLOYEE_MANAGE_PAGE;
    }

    /**
     * Search employees by search term
     */
    private String searchEmployees(HttpServletRequest request, EmployeeDAO dao) throws SQLException {
        String searchTerm = getSearchTerm(request);

        if (isEmptyString(searchTerm)) {
            return getAllEmployees(request, dao);
        }

        List<EmployeeDTO> employees = dao.searchEmployees(searchTerm.trim());
        request.setAttribute("employees", employees);

        if (employees.isEmpty()) {
            setInfo(request, "Không tìm thấy nhân viên nào với từ khóa: \"" + searchTerm + "\"");
        } else {
            setSuccess(request, "Tìm thấy " + employees.size() + " nhân viên với từ khóa: \"" + searchTerm + "\"");
        }

        return EMPLOYEE_MANAGE_PAGE;
    }

    /**
     * View employee details
     */
    private String viewEmployee(HttpServletRequest request, EmployeeDAO dao) throws SQLException {
        String employeeIdStr = request.getParameter("employeeId");

        if (isEmptyString(employeeIdStr)) {
            setError(request, "ID nhân viên không hợp lệ!");
            return getAllEmployees(request, dao);
        }

        try {
            int employeeId = parseEmployeeId(employeeIdStr);
            EmployeeDTO employee = dao.getEmployeeById(employeeId);

            if (employee != null) {
                request.setAttribute("employee", employee);
                return EMPLOYEE_DETAIL_PAGE;
            } else {
                setError(request, "Không tìm thấy nhân viên với ID: " + employeeId);
                return getAllEmployees(request, dao);
            }
        } catch (NumberFormatException e) {
            setError(request, "ID nhân viên phải là số!");
            return getAllEmployees(request, dao);
        }
    }

    /**
     * Edit employee - load employee data for editing
     */
    private String editEmployee(HttpServletRequest request, EmployeeDAO dao) throws SQLException {
        String employeeIdStr = request.getParameter("employeeId");

        if (isEmptyString(employeeIdStr)) {
            setError(request, "ID nhân viên không hợp lệ!");
            return getAllEmployees(request, dao);
        }

        try {
            int employeeId = parseEmployeeId(employeeIdStr);
            EmployeeDTO employee = dao.getEmployeeById(employeeId);

            if (employee != null) {
                request.setAttribute("employee", employee);
                return EMPLOYEE_EDIT_PAGE;
            } else {
                setError(request, "Không tìm thấy nhân viên với ID: " + employeeId);
                return getAllEmployees(request, dao);
            }
        } catch (NumberFormatException e) {
            setError(request, "ID nhân viên phải là số!");
            return getAllEmployees(request, dao);
        }
    }

    /**
     * Delete employee
     */
    private String deleteEmployee(HttpServletRequest request, EmployeeDAO dao) throws SQLException {
        String employeeIdStr = request.getParameter("employeeId");

        if (isEmptyString(employeeIdStr)) {
            setError(request, "ID nhân viên không hợp lệ!");
            return getAllEmployees(request, dao);
        }

        try {
            int employeeId = parseEmployeeId(employeeIdStr);
            boolean success = dao.deleteEmployee(employeeId);

            if (success) {
                setSuccess(request, "Xóa nhân viên thành công!");
            } else {
                setError(request, "Xóa nhân viên thất bại!");
            }
        } catch (NumberFormatException e) {
            setError(request, "ID nhân viên phải là số!");
        }

        return getAllEmployees(request, dao);
    }

    /**
     * Add new employee
     */
    private String addEmployee(HttpServletRequest request, EmployeeDAO dao) throws SQLException {
        EmployeeDTO employee = buildEmployeeFromRequest(request);

        if (employee == null) {
            setError(request, "Thông tin nhân viên không hợp lệ!");
            return getAllEmployees(request, dao);
        }

        boolean success = dao.addEmployee(employee);

        if (success) {
            setSuccess(request, "Thêm nhân viên mới thành công!");
        } else {
            setError(request, "Thêm nhân viên thất bại!");
        }

        return getAllEmployees(request, dao);
    }

    /**
     * Update existing employee
     */
    private String updateEmployee(HttpServletRequest request, EmployeeDAO dao) throws SQLException {
        EmployeeDTO employee = buildEmployeeFromRequest(request);

        if (employee == null) {
            setError(request, "Thông tin nhân viên không hợp lệ!");
            return getAllEmployees(request, dao);
        }

        boolean success = dao.updateEmployee(employee);

        if (success) {
            setSuccess(request, "Cập nhật thông tin nhân viên thành công!");
        } else {
            setError(request, "Cập nhật thông tin nhân viên thất bại!");
        }

        return getAllEmployees(request, dao);
    }

    // Utility methods
    private String getSearchTerm(HttpServletRequest request) {
        return request.getParameter("searchTerm");
    }

    private boolean isEmptyString(String str) {
        return str == null || str.trim().isEmpty();
    }

    private int parseEmployeeId(String employeeIdStr) throws NumberFormatException {
        return Integer.parseInt(employeeIdStr);
    }

    private EmployeeDTO buildEmployeeFromRequest(HttpServletRequest request) {
        try {
            EmployeeDTO employee = new EmployeeDTO();

            // Get employee ID for update operations
            String employeeIdStr = request.getParameter("employeeId");
            if (!isEmptyString(employeeIdStr)) {
                employee.setEmployeeId(parseEmployeeId(employeeIdStr));
            }

            employee.setName(request.getParameter("name"));
            employee.setFirstName(request.getParameter("firstName"));
            employee.setLastName(request.getParameter("lastName"));
            employee.setPhone(request.getParameter("phone"));
            employee.setEmail(request.getParameter("email"));
            employee.setRole(request.getParameter("role"));
            employee.setWorkingHours(request.getParameter("workingHours"));

            // Handle foreign key IDs
            employee.setOwnerId(parseIntParameter(request, "ownerId"));
            employee.setManagerId(parseIntParameter(request, "managerId"));
            employee.setAccountId(parseIntParameter(request, "accountId"));

            return employee;
        } catch (Exception e) {
            return null;
        }
    }

    private int parseIntParameter(HttpServletRequest request, String paramName) {
        String paramValue = request.getParameter(paramName);
        if (isEmptyString(paramValue)) {
            return 0;
        }
        try {
            return Integer.parseInt(paramValue);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void setError(HttpServletRequest request, String message) {
        request.setAttribute(ERROR, message);
    }

    private void setSuccess(HttpServletRequest request, String message) {
        request.setAttribute(SUCCESS, message);
    }

    private void setInfo(HttpServletRequest request, String message) {
        request.setAttribute(INFO, message);
    }

    private void handleException(HttpServletRequest request, Exception e) {
        log("Error at EmployeeManageController: " + e.toString());
        setError(request, "Đã xảy ra lỗi hệ thống: " + e.getMessage());
    }

    private void forwardToPage(HttpServletRequest request, HttpServletResponse response, String url)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
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
}
