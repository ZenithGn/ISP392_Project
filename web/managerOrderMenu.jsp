<%-- 
    Document   : managerOrderMenu
    Created on : Jul 3, 2025, 3:50:50 PM
    Author     : Khanh
--%>

<%@page import="project.model.dto.AccountDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%
    AccountDTO user = (AccountDTO) session.getAttribute("LOGIN_USER");
    if (user == null || !"manager".equals(user.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý đơn hàng</title>
        <link rel="stylesheet" href="css/manageOrderMenu.css" />
    </head>
    <body>
        <h2>📋 Chọn chức năng quản lý đơn hàng</h2>
        <div class="card-container">
            <div class="card">
                <h3>📩 Yêu cầu mới</h3>
                <p>Xem và phân công các yêu cầu đang chờ xử lý</p>
                <a href="managerRequest.jsp">Quản lý yêu cầu</a>
            </div>
            <div class="card">
                <h3>✅ Theo dõi nhiệm vụ</h3>
                <p>Theo dõi tiến độ, kết quả và ghi chú từ nhân viên</p>
                <a href="managerAllTasks.jsp">Xem tất cả nhiệm vụ</a>
            </div>


        </div>

        <div class="card-container" style="text-align: center; margin-top: 100px;">
            <a href="homepage.jsp" class="btn-back">← Quay lại trang chủ</a>
        </div>
    </body>
</html>
