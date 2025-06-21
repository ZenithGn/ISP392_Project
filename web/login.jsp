<%-- 
    Document   : Login
    Created on : Jun 12, 2025, 9:42:32 AM
    Author     : Khanh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Trang đăng nhập</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="css/login.css">
         <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    </head>
    <body>
        <!-- Login Form -->
        <div class="login">
            <a href="homepage.jsp">
            <button class="back-button"><i class="fa fa-arrow-left"></i>Back</button>
        </a>
            <h1>Dịch vụ cứu hộ xe máy</h1>

            <!-- Display error message if any -->
            <% String error = (String) request.getAttribute("error"); %>
            <% if (error != null) { %>
                <div style="color: red; margin-bottom: 10px;">
                    <%= error %>
                </div>
            <% } %>

            <form action="MainController" method="POST">
                <input type="text" name="phone" required placeholder="Phone"/> <br>
                <input type="password" name="password" required placeholder="Password"/><br>

                <p>Chưa có tài khoản?  <a href="register.jsp">Đăng ký</a></p>
                <br><input type="submit" name="action" value="Login"/>
            </form>
        </div>
    </body>
</html>