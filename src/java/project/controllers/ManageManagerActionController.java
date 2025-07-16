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
import project.model.dao.AccountDAO;
import project.model.dto.AccountDTO;

/**
 *
 * @author Khanh
 */
@WebServlet(name = "ManageManagerActionController", urlPatterns = {"/ManageManagerActionController"})
public class ManageManagerActionController extends HttpServlet {
 private static final String ERROR = "manageManager.jsp";
    private static final String SUCCESS = "manageManager.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String url = ERROR;

        try {
            HttpSession session = request.getSession();
            AccountDTO loginUser = (AccountDTO) session.getAttribute("LOGIN_USER");

            if (loginUser == null || !"owner".equals(loginUser.getRole())) {
                request.setAttribute("ERROR", "Bạn không có quyền thực hiện chức năng này!");
                request.getRequestDispatcher(url).forward(request, response);
                return;
            }

            String subAction = request.getParameter("subAction");
            AccountDAO dao = new AccountDAO();

            switch (subAction) {
case "create": {
    
    
    try {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");

        if (userName == null || password == null || nickname == null || phone == null || email == null ||
            userName.trim().isEmpty() || password.trim().isEmpty() || nickname.trim().isEmpty() ||
            phone.trim().isEmpty() || email.trim().isEmpty()) {

            request.setAttribute("ERROR", "Vui lòng điền đầy đủ thông tin.");
            return;
        }

        if (dao.checkDuplicate(userName)) {
            request.setAttribute("ERROR", "Tên đăng nhập đã tồn tại.");
            return;
        }

        AccountDTO acc = new AccountDTO();
        acc.setUserName(userName);
        acc.setPassword(password);
        acc.setIsRegistered(false);
        acc.setRole("manager");

        int accountId = dao.createAccount(acc);
        if (accountId <= 0) {
            request.setAttribute("ERROR", "Tạo tài khoản thất bại.");
            return;
        }

        if (!dao.createManagerInfo(nickname, phone, email, accountId)) {
            request.setAttribute("ERROR", "Tạo hồ sơ Manager thất bại.");
            return;
        }

        request.setAttribute("MESSAGE", "Tạo tài khoản quản lý thành công.");
    } catch (Exception e) {
        e.printStackTrace();
        request.setAttribute("ERROR", "Lỗi hệ thống: " + e.getMessage());
    }
    return;
}
               case "update": {
    int accountId = Integer.parseInt(request.getParameter("accountId"));
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    int isRegistered = Integer.parseInt(request.getParameter("isregistered")); // checkbox/select
    String nickname = request.getParameter("nickname");
    String phone = request.getParameter("phone");
    String email = request.getParameter("email");

    // Tạo đối tượng AccountDTO
    AccountDTO acc = new AccountDTO();
    acc.setId(String.valueOf(accountId)); // ID dạng String
    acc.setUserName(username);
    acc.setPassword(password);
    acc.setIsRegistered(isRegistered == 1); // convert sang boolean

    // Gọi DAO cập nhật cả Account và Manager
    boolean success = dao.updateManager(acc, nickname, phone, email);

    if (success) {
        request.setAttribute("MESSAGE", "Cập nhật tài khoản thành công.");
    } else {
        request.setAttribute("ERROR", "Cập nhật tài khoản thất bại.");
    }
    break;
}
                case "delete": {
                    String accountId = request.getParameter("accountId");
                    boolean success = dao.deleteManager(accountId);
                    if (success) {
                        request.setAttribute("MESSAGE", "Xoá tài khoản thành công.");
                    } else {
                        request.setAttribute("ERROR", "Xoá tài khoản thất bại.");
                    }
                    break;
                }
                default:
                    request.setAttribute("ERROR", "Hành động không hợp lệ.");
            }

            // Load lại danh sách manager sau khi thao tác
            List<AccountDTO> managerList = dao.getAllManagers();
            if (managerList == null || managerList.isEmpty()) {
                request.setAttribute("WARNING", "Không có tài khoản quản lý nào.");
            }
            request.setAttribute("MANAGER_LIST", managerList);
            url = SUCCESS;

        } catch (Exception e) {
            log("Error at ManageManagerActionController: " + e.toString());
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
