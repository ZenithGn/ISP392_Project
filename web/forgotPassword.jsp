<%-- 
    Document   : forgotPassword
    Created on : Jun 23, 2025, 8:05:25 PM
    Author     : Khanh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
       <title>Forgot Password</title>
    <link rel="stylesheet" href="css/forgotPassword.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600&display=swap" rel="stylesheet">
</head>
<body>
    <div class="container">
        <form action="send-reset-link" method="post" class="form-box">
            <h2>Forgot Your Password?</h2>
            <p>Enter your email and we'll send you a link to reset your password.</p>

            <% String error = (String) request.getAttribute("error"); %>
            <% if (error != null) { %>
                <div class="error"><%= error %></div>
            <% } %>

            <% String message = (String) request.getAttribute("message"); %>
            <% if (message != null) { %>
                <div class="success"><%= message %></div>
            <% } %>

            <input type="email" name="email" placeholder="Enter your email" required>

            <button type="submit" id="reset">Send Reset Link</button>

            <div class="back">
                <a href="login.jsp">← Back to Login</a>
            </div>
            
            <script>
        // Hiện/ẩn dịch vụ khác
        document.getElementById('reset').addEventListener('change', function () {
            const box = document.getElementById('reset');
            box.classList.toggle('hidden', !this.checked);
        });
    </script>
        </form>
    </div>
            
</body>
</html>