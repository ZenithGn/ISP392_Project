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
import project.model.dto.RequestDTO;

/**
 *
 * @author Khanh
 */
@WebServlet(name = "RequestWaitingController", urlPatterns = {"/RequestWaitingController"})
public class RequestWaitingController extends HttpServlet {

    private static final String ERROR = "requestWaiting.jsp";
    private static final String SUCCESS = "requestWaiting.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR;

        try {
            HttpSession session = request.getSession();
            AccountDTO loginUser = (AccountDTO) session.getAttribute("LOGIN_USER");

            // Kiểm tra người dùng hợp lệ và là customer
            if (loginUser == null || !"customer".equals(loginUser.getRole())) {
                request.setAttribute("ERROR", "Bạn không có quyền truy cập!");
                request.getRequestDispatcher(url).forward(request, response);
                return;
            }

            // Kiểm tra session flag chỉ hiển thị sau khi tạo yêu cầu
            Boolean justCreated = (Boolean) session.getAttribute("JUST_CREATED_REQUEST");

            if (justCreated != null && justCreated) {
                // Lấy danh sách request đang chờ xử lý của người dùng
                RequestDAO dao = new RequestDAO();
                List<RequestDTO> waitingRequests = dao.getPendingRequestsByCustomer(loginUser.getUserId());

                request.setAttribute("waitingRequests", waitingRequests);

                // Xóa flag để không hiển thị lại nếu F5
                session.removeAttribute("JUST_CREATED_REQUEST");

                url = SUCCESS;
            } else {
                request.setAttribute("waitingRequests", null);
                url = SUCCESS;
            }

        } catch (Exception e) {
            log("Error at CustomerRequestWaitingController: " + e.toString());
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
