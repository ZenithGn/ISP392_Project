<%-- 
    Document   : requestCreated
    Created on : Jul 6, 2025, 11:38:41 AM
    Author     : Khanh
--%>

<%@page import="project.model.dto.RequestDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
   RequestDTO requestCreated = (RequestDTO) session.getAttribute("newRequest");
    
%>

<html>
<head>
    <title>Đơn hàng đã được tạo</title>
</head>
<body>
<h2>✅ Đơn hàng của bạn đã được tạo thành công!</h2>

<% if (requestCreated != null) { %>
    <table border="1">
        <tr><th>Mã Yêu Cầu</th><td><%= requestCreated.getRequestId() %></td></tr>
        <tr><th>Vị trí</th><td><%= requestCreated.getLocation() %></td></tr>
        <tr><th>Trạng thái</th><td><%= requestCreated.getStatus() %></td></tr>
        <tr><th>Độ khẩn</th><td><%= requestCreated.getUrgency() %></td></tr>
        <tr><th>Tổng tiền</th><td><%= requestCreated.getTotalPrice() %> đ</td></tr>
    </table>
<% } else { %>
    <p style="color:red;">Không tìm thấy đơn hàng!</p>
<% } %>

<div style="margin-top: 20px;">
    <a href="homepage.jsp">← Quay lại trang chủ</a>
</div>
</body>
</html>