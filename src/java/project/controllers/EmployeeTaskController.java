/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.controllers;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import project.model.dao.EmployeeDAO;
import project.model.dao.RequestDAO;
import project.model.dto.AccountDTO;
import project.model.dto.EmployeeDTO;
import project.model.dto.RequestDTO;
import project.model.dto.RequestDetailDTO;
import javax.servlet.annotation.MultipartConfig;


/**
 *
 * @author Khanh
 */
@MultipartConfig
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

            String action = request.getParameter("action");
            String requestId = request.getParameter("requestId");

            RequestDAO dao = new RequestDAO();
            EmployeeDAO daoEmp = new EmployeeDAO();
            int employeeId = emp.getEmployeeId();

            if ("accept".equals(action)) {
                boolean updated = dao.updateRequestStatus(requestId, "In Progress");
                if (updated) {
                    request.setAttribute("SUCCESS", "Bạn đã chấp nhận nhiệm vụ: " + requestId);
                } else {
                    request.setAttribute("ERROR", "Không thể cập nhật trạng thái nhiệm vụ.");
                }

            } else if ("reject".equals(action)) {
                boolean updated = dao.updateRequestStatus(requestId, "rejected");
                boolean unassigned = daoEmp.unassignEmployee(requestId); // Cần thêm hàm này
                if (updated && unassigned) {
                    request.setAttribute("SUCCESS", "Bạn đã từ chối nhiệm vụ: " + requestId);
                } else {
                    request.setAttribute("ERROR", "Không thể từ chối nhiệm vụ.");
                }

           } else if ("complete".equals(action)) {
    
    String note = request.getParameter("employeeNote");

    Part imagePart = request.getPart("imageFile");
    String fileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
    String uploadPath = getServletContext().getRealPath("/") + "uploads";
    File uploadDir = new File(uploadPath);
    if (!uploadDir.exists()) uploadDir.mkdirs();

    String filePath = uploadPath + File.separator + fileName;
    imagePart.write(filePath); // Lưu ảnh

    boolean noteSaved = dao.updateRequestDetailNoteAndImage(requestId, employeeId, note, "uploads/" + fileName);
    boolean statusUpdated = dao.updateRequestStatus(requestId, "completed");

    if (noteSaved && statusUpdated) {
        request.setAttribute("SUCCESS", "Đã hoàn thành nhiệm vụ và gửi thông tin cho quản lý.");
    } else {
        request.setAttribute("ERROR", "Không thể cập nhật ghi chú hoặc trạng thái.");
    }
}

            // Load lại danh sách nhiệm vụ
            List<RequestDetailDTO> taskList = dao.getTasksByEmployeeId(employeeId);
            request.setAttribute("tasks", taskList);
            url = SUCCESS;

        } catch (Exception e) {
            log("❌ Error at EmployeeTaskController: " + e.toString());
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
