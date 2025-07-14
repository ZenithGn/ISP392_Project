<%-- 
    Document   : verifyOtp
    Created on : Jul 3, 2025, 11:05:46 PM
    Author     : TUAN
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Verify OTP</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <h2>Verify OTP</h2>
        <form action="MainController" method="POST">
            <input type="hidden" name="action" value="VerifyOtp" />
            <div class="mb-3">
                <label for="otp" class="form-label">Enter OTP</label>
                <input type="text" class="form-control" id="otp" name="otp" required>
            </div>
            <button type="submit" class="btn btn-primary">Verify</button>
        </form>
        <% String msg = (String) request.getAttribute("MESSAGE");
           if (msg != null && !msg.isEmpty()) { %>
            <div class="alert alert-info mt-3"><%= msg %></div>
        <% } %>
    </div>
</body>
</html>
