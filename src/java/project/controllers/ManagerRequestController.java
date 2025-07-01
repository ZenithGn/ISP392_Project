package project.controllers;

import project.model.dao.RequestDAO;
import project.model.dto.RequestDTO;
import project.model.dto.AccountDTO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import project.model.dao.EmployeeDAO;
import project.model.dto.EmployeeDTO;

@WebServlet(name = "ManagerRequestController", urlPatterns = {"/ManagerRequestController"})
public class ManagerRequestController extends HttpServlet {

    private static final String ERROR = "managerRequest.jsp";
    private static final String SUCCESS = "managerRequest.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR;
        try {
            HttpSession session = request.getSession();
            AccountDTO loginUser = (AccountDTO) session.getAttribute("LOGIN_USER");
            
            if (loginUser == null || !"manager".equals(loginUser.getRole())) {
                request.setAttribute("ERROR", "Bạn không có quyền truy cập!");
                request.getRequestDispatcher(url).forward(request, response);
                return;
            }

            String action = request.getParameter("action");
            RequestDAO dao = new RequestDAO();

            if ("accept".equals(action)) {
    String requestId = request.getParameter("requestId");
    String employeeIdStr = request.getParameter("employeeId");

    if (requestId != null && employeeIdStr != null) {
        int employeeId = Integer.parseInt(employeeIdStr);

        // Gán cho bảng Request hoặc RequestDetail tuỳ thiết kế
        boolean assigned = dao.assignEmployeeToRequest(requestId, employeeId);

        if (assigned) {
            dao.updateRequestStatus(requestId, "In Progress");
            request.setAttribute("SUCCESS", "Đã gán nhân viên và chuyển trạng thái!");
        } else {
            request.setAttribute("ERROR", "Không thể gán nhân viên.");
        }
    } else {
        request.setAttribute("ERROR", "Thiếu thông tin gán nhân viên.");
    }


            } else if ("reject".equals(action)) {
                String requestId = request.getParameter("requestId");
                if (requestId != null && !requestId.trim().isEmpty()) {
                    boolean check = dao.updateRequestStatus(requestId, "rejected");
                    if (check) {
                        request.setAttribute("SUCCESS", "Đã từ chối yêu cầu: " + requestId);
                    } else {
                        request.setAttribute("ERROR", "Không thể từ chối yêu cầu!");
                    }
                }
            }

            // Always load pending requests and set the correct attribute name
            List<RequestDTO> pendingRequests = dao.getAllPendingRequests();
            request.setAttribute("LIST_REQUESTS", pendingRequests);
            request.setAttribute("requests", pendingRequests); // JSP expects "requests"
            
            EmployeeDAO empDAO = new EmployeeDAO();
            List<EmployeeDTO> employees = empDAO.getAllEmployees();
            request.setAttribute("employees", employees);
            url = SUCCESS;

        } catch (Exception e) {
            log("Error at ManagerRequestController: " + e.toString());
            request.setAttribute("ERROR", "Có lỗi xảy ra: " + e.getMessage());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
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