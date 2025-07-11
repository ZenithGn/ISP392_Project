/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import project.model.dao.RequestDAO;
import project.model.dto.AccountDTO;
import project.model.dto.RequestDetailDTO;

/**
 *
 * @author Khanh
 */
@WebServlet(name = "ManagerAllTaskController", urlPatterns = {"/ManagerAllTaskController"})
public class ManagerAllTaskController extends HttpServlet {

    private static final String ERROR = "managerAllTasks.jsp";
    private static final String SUCCESS = "managerAllTasks.jsp";

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

            // Xử lý các hành động nếu cần mở rộng (ví dụ: lọc trạng thái, tìm kiếm, v.v.)
            String action = request.getParameter("action");

            RequestDAO dao = new RequestDAO();
            List<RequestDetailDTO> assignedTasks = dao.getAllAssignedTasks(); // lấy các task trạng thái != pending

            if (assignedTasks == null || assignedTasks.isEmpty()) {
                request.setAttribute("WARNING", "Không có yêu cầu nào đang xử lý hoặc đã hoàn tất.");
            }

            request.setAttribute("assignedTasks", assignedTasks);
            url = SUCCESS;

        } catch (Exception e) {
            log("Error at ManagerAllTaskController: " + e.toString());
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
