<%-- 
    Document   : rateService
    Created on : Jun 20, 2025, 3:02:07 PM
    Author     : Khanh
--%>

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
    <form action="submitRate" method="post">
         <input type="hidden" name="action" value="rate" />
        <label for="requestId">Mã yêu cầu:</label>
        <input type="text" id="requestId" name="requestId" required>

        <label for="user">Id của bạn:</label>
        <input type="text" id="user" name="user" required>

        <label for="service">Dịch vụ đã sử dụng:</label>
        <input type="text" id="service" name="service" value="Cứu hộ khẩn cấp" readonly>

        <label>Chọn số sao:</label>
        <div class="star-rating">
    <input type="radio" id="star1" name="stars" value="1"><label for="star1">★</label>
    <input type="radio" id="star2" name="stars" value="2"><label for="star2">★</label>
    <input type="radio" id="star3" name="stars" value="3"><label for="star3">★</label>
    <input type="radio" id="star4" name="stars" value="4"><label for="star4">★</label>
    <input type="radio" id="star5" name="stars" value="5"><label for="star5">★</label>
</div>

        <label for="comment">Nhận xét của bạn:</label>
        <textarea id="comment" name="comment" rows="4" placeholder="Viết nhận xét..."></textarea>

        <button type="submit">Gửi đánh giá</button>
    </form>
</div>
</body>
</html>