<%-- 
    Document   : Menu
    Created on : Jun 15, 2025, 10:28:55 AM
    Author     : Khanh
--%>

<%@page import="project.model.dto.AccountDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    // Get user from session
    AccountDTO user = (AccountDTO) session.getAttribute("LOGIN_USER");
    if (user == null) {
        user = (AccountDTO) session.getAttribute("account");
    }

    String userRole = "";
    String userName = "Guest";
    boolean isLoggedIn = false;

    if (user != null) {
        userRole = user.getRole() != null ? user.getRole().toLowerCase() : "";
        userName = user.getUserName() != null ? user.getUserName() : "User";
        isLoggedIn = true;
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dịch vụ cứu hộ xe máy</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="css/menu.css">
        <link rel="stylesheet" href="css/homepage.css" />
    </head>
    <body>
        <input type="checkbox" id="sidebar-active" style="display: none;">
           

            <header class="navbar">
                 <div class="left-group">
                <label for="sidebar-active" class="menu-toggle">☰</label>
                <div class="logo">
                    <img src="images/logo1.jpg" class="logo-img" />
                </div>
                  </div>
                <nav class="nav-links">
                    <% if (isLoggedIn) {%>
                    <div class="user-info">
                        <span>Xin chào, <%= userName%>!</span>
                        <span class="role-badge role-<%= userRole%>"><%= userRole%></span>
                        <a href="LogoutController" class="logout-btn">Đăng xuất</a>
                    </div>
                    <% } else { %>
                    <a href="login.jsp">Login</a>
                    <% } %>
                    <a href="menu.jsp">Service</a>
                    <a href="aboutUs.jsp">About Us</a>
                    <a href="#" class="active">Home</a>
                </nav>
            </header>
            <!-- Sidebar -->
            <div class="sidebar">                
             
                <div class="links-container">
                
                    <ul>
                        <a href="homepage.jsp" class="active">Home</a>
                        <a href="menu.jsp">Service</a>
                        <a href="aboutUs.jsp">About Us</a>

                    </ul>
                </div>
            </div>
        <div class="menu-container">
            <!-- Nút back -->
            <a href="homepage.jsp">
                <button class="back-button"><i class="fa fa-arrow-left"></i>Back</button>
            </a>

            <!-- Phần dưới cùng: bảng + hotline -->
            <div class="bottom-section">
                <table class="service-table">
                    <thead>
                        <tr>
                            <th>Dịch vụ</th>
                            <th>Mô tả</th>
                            <th>Giá tham khảo (VNĐ)</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>Vá xe tại chỗ</td>
                            <td>Vá lốp xe máy tận nơi</td>
                            <td>30.000 - 50.000</td>
                        </tr>
                        <tr>
                            <td>Thay lốp</td>
                            <td>Thay lốp xe mới, tùy loại xe</td>
                            <td>150.000 - 350.000</td>
                        </tr>
                        <tr>
                            <td>Cứu hộ kéo xe</td>
                            <td>Kéo xe về garage gần nhất</td>
                            <td>300.000 - 700.000</td>
                        </tr>
                        <tr>
                            <td>Nạp bình ắc quy</td>
                            <td>Hỗ trợ khởi động xe khi hết bình</td>
                            <td>100.000 - 200.000</td>
                        </tr>
                        <tr>
                            <td>Thay bugi / lọc gió / dầu nhớt</td>
                            <td>Bảo trì các bộ phận cơ bản của xe</td>
                            <td>50.000 - 250.000</td>
                        </tr>
                        <tr>
                            <td>Dịch vụ khác</td>
                            <td>Liên hệ để được tư vấn cụ thể</td>
                            <td>Liên hệ</td>
                        </tr>
                    </tbody>
                </table>

                <div class="hotline">
                    📞 Hotline hỗ trợ: <strong>1900 9999</strong>
                </div>
            </div>
        </div>
    </body>
</html>