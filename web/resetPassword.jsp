<%-- 
    Document   : resetPassword
    Created on : Jul 3, 2025, 11:18:46 PM
    Author     : TUAN
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Reset Password</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <h2>Reset Your Password</h2>
        <form action="MainController" method="POST">
            <input type="hidden" name="action" value="ResetPassword" />
            <div class="mb-3">
                <label for="newPassword" class="form-label">New Password</label>
                <input type="password" class="form-control" id="newPassword" name="newPassword" required>
            </div>
            <div class="mb-3">
                <label for="confirmNewPassword" class="form-label">Confirm New Password</label>
                <input type="password" class="form-control" id="confirmNewPassword" name="confirmNewPassword" required>
            </div>
            <button type="submit" class="btn btn-success">Update Password</button>
        </form>
        <% String msg = (String) request.getAttribute("MESSAGE");
           if (msg != null && !msg.isEmpty()) { %>
            <div class="alert alert-info mt-3"><%= msg %></div>
        <% } %>
    </div>
</body>
</html>

