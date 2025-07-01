/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import project.model.dao.RequestDAO;
import project.model.dto.AccountDTO;
import project.model.dto.FeedBackDTO;
import project.model.dto.RequestDetailDTO;

/**
 *
 * @author Khanh
 */
@WebServlet(name = "SubmitRateController", urlPatterns = {"/SubmitRateController"})
public class SubmitRateController extends HttpServlet {

   

    private static final String ERROR = "rateService.jsp";
    private static final String SUCCESS = "thankyou.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR;

        try {
            HttpSession session = request.getSession();
            AccountDTO loginUser = (AccountDTO) session.getAttribute("LOGIN_USER");

            // Kiểm tra đăng nhập và role là customer
            if (loginUser == null || !"customer".equals(loginUser.getRole())) {
                request.setAttribute("ERROR", "Bạn cần đăng nhập với tài khoản khách hàng!");
                request.getRequestDispatcher(url).forward(request, response);
                return;
            }

            String action = request.getParameter("action"); // nếu muốn dùng action
            if ("rate".equals(action)) {

                String requestId = request.getParameter("requestId");
                String comment = request.getParameter("comment");
                int stars = Integer.parseInt(request.getParameter("stars"));

                RequestDAO reqDao = new RequestDAO();
                RequestDetailDTO detail = reqDao.getRequestDetailInfo(requestId);

                if (detail == null) {
                    request.setAttribute("ERROR", "Không tìm thấy thông tin yêu cầu!");
                } else {
                    FeedBackDTO feedback = new FeedBackDTO(
                        requestId, stars, comment,
                        detail.getServiceId(), detail.getCustomerId()
                    );

                    RequestDAO dao = new RequestDAO();
                    boolean inserted = dao.insertFeedback(feedback);

                    if (inserted) {
                        request.setAttribute("SUCCESS", "Cảm ơn bạn đã đánh giá dịch vụ!");
                        url = SUCCESS;
                    } else {
                        request.setAttribute("ERROR", "Không thể lưu đánh giá.");
                    }
                }
            } else {
                request.setAttribute("ERROR", "Hành động không hợp lệ.");
            }

        } catch (Exception e) {
            log("Error at SubmitRateController: " + e.toString());
            request.setAttribute("ERROR", "Lỗi hệ thống: " + e.getMessage());
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
