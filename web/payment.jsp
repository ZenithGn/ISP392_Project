<%-- 
    Document   : payment
    Created on : Jun 20, 2025, 1:37:48 PM
    Author     : Khanh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    <head>
    <title>Thanh toán dịch vụ cứu hộ MRS</title>
    <link rel="stylesheet" href="css/payment.css">
</head>
<body>
<div class="payment-container">
    <h2>Thanh toán dịch vụ cứu hộ MRS</h2>

    <form action="processPayment" method="post">
        <label for="requestId">Request Id:</label>
        <input type="text" id="requestId" name="requestId" required>
        
        <label for="username">Họ và tên:</label>
        <input type="text" id="username" name="username" required>

        <label for="phone">Số điện thoại:</label>
        <input type="tel" id="phone" name="phone" required>

        <label for="service">Dịch vụ:</label>
        <input type="text" id="service" name="service" value="Cứu hộ khẩn cấp" readonly>

        <label for="price">Giá tiền:</label>
        <input type="text" id="price" name="price" value="300,000 VND" readonly>

        <label for="total">Tổng tiền cần thanh toán:</label>
        <input type="text" id="total" name="total" value="300,000 VND" readonly>

        <div class="payment-method">
            <h3>Phương thức thanh toán</h3>
            <p>Chuyển khoản đến tài khoản sau:</p>
            <ul>
                <li><strong>Ngân hàng:</strong> Vietcombank</li>
                <li><strong>Số tài khoản:</strong> 0123456789</li>
                <li><strong>Chủ tài khoản:</strong> Cty TNHH MRS</li>
                <li><strong>Nội dung chuyển khoản:</strong> [HọTên] - MRS</li>
            </ul>
            <p>Hoặc quét mã QR bên dưới để thanh toán nhanh:</p>
            <img src="images/payment1.png" alt="Mã QR thanh toán" class="qr-code">
        </div>
        <a href="rateService.jsp">
        <button type="submit">Tôi đã thanh toán</button>
        </a>
    </form>
</div>
</body>
</html>
