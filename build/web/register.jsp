<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register - Garage Service</title>
        <link rel="stylesheet" type="text/css" href="css/register.css">
    </head>
    <body>
        <div class="image-box"></div>
        <div class="container">
            
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
                <table>
                    <tr>
                        <td>User Name:</td>
                        <td><input type="text" name="userName" required minlength="2" maxlength="20"/></td>
                    </tr>
                    <tr>
                        <td>Email Address:</td>
                        <td><input type="email" name="emailAddress" required maxlength="250"/></td>
                    </tr>
                    <tr>
                        <td>Phone:</td>
                        <td><input type="text" name="phone" required/></td>
                    </tr>
                    <tr>
                        <td>Password:</td>
                        <td><input type="password" name="password" required minlength="8" maxlength="30"/></td>
                    </tr>
                    <tr>
                        <td>Confirm Password:</td>
                        <td><input type="password" name="confirm" required/></td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <p>Đã có tài khoản? <a href="login.jsp">Login</a></p>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <input type="submit" name="action" value="Create"/>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </body>
</html>