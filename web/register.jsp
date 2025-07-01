<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register - Garage Service</title>
        <link rel="stylesheet" type="text/css" href="css/register.css">
    </head>
    <body>
        
        <div class="container">
             <h1>Đăng ký</h1>
            <!-- Display error message -->
            <% 
                String error = (String) request.getAttribute("error");
                if (error != null) {
            %>
                <div style="color: red; margin-bottom: 15px; padding: 10px; border: 1px solid red; background-color: #ffe6e6;">
                    Error: <%= error %>
                </div>
            <% } %>
            
            <!-- Display success message -->
            <% 
                String message = (String) request.getAttribute("message");
                if (message != null) {
            %>
                <div style="color: green; margin-bottom: 15px; padding: 10px; border: 1px solid green; background-color: #e6ffe6;">
                    <%= message %>
                </div>
            <% } %>

           <form action="MainController" method="POST">
    
    <input type="text" name="userName" required minlength="2" maxlength="20" placeholder="Nhập tên người dùng"/>
    <br>
    <input type="text" name="emailAddress" required maxlength="250" minlength="2" placeholder="Nhập email"/>
    <br>
    <input type="text" name="phone" required placeholder="Nhập số điện thoại"/>
    <br>
    <input type="password" name="password" required minlength="8" maxlength="30" placeholder="Nhập mật khẩu"/>
    <br>
    <input type="password" name="confirm" required placeholder="Nhập lại mật khẩu"/>

    <p>Đã có tài khoản? <a href="login.jsp">Login</a></p>
    <br><input type="submit" name="action" value="Create"/>
</form>

        </div>
    </body>
</html>