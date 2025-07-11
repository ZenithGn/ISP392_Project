<%-- 
    Document   : rateService
    Created on : Jun 20, 2025, 3:02:07 PM
    Author     : Khanh
--%>

<%@page import="project.model.dto.RequestDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Đánh giá dịch vụ MRS</title>
    <link rel="stylesheet" href="css/rate.css">
</head>
<body>
<div class="rate-container">
    <h2>Đánh giá dịch vụ cứu hộ MRS</h2>
    <form action="MainController" method="POST">
<%
    RequestDTO newRequest = (RequestDTO) session.getAttribute("newRequest");
    String requestId = newRequest != null ? newRequest.getRequestId() : "";
    String customerId = newRequest != null ? newRequest.getCustomerId() : "";
    String serviceType = newRequest != null ? newRequest.getServiceType() : "N/A";
%>
<div class="rate-container">
    <h2>Đánh giá dịch vụ cứu hộ MRS</h2>
    <form action="MainController" method="POST">
        <input type="hidden" name="action" value="SubmitRate" />

        <label for="requestId">Mã yêu cầu:</label>
        <input type="text" id="requestId" name="requestId" value="<%= requestId %>" readonly>

        <label for="user">ID của bạn:</label>
        <input type="text" id="user" name="user" value="<%= customerId %>" readonly>

        <label for="service">Dịch vụ đã sử dụng:</label>
        <input type="text" id="service" name="service" value="<%= serviceType %>" readonly>

        <label>Chọn số sao:</label>
        <div class="star-rating">
    <input type="radio" id="star5" name="stars" value="5" required><label for="star5">★</label>
    <input type="radio" id="star4" name="stars" value="4"><label for="star4">★</label>
    <input type="radio" id="star3" name="stars" value="3"><label for="star3">★</label>
    <input type="radio" id="star2" name="stars" value="2"><label for="star2">★</label>
    <input type="radio" id="star1" name="stars" value="1"><label for="star1">★</label>
</div>
        <label for="comment">Nhận xét của bạn:</label>
        <textarea id="comment" name="comment" rows="4" placeholder="Viết nhận xét..."></textarea>

          <button type="submit">Gửi đánh giá</button>
        
        <div style="margin-top: 20px;">
    <a href="homepage.jsp">← Quay lại trang chủ</a>
</div>
    </form>
</div>
</body>
</html>