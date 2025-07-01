<%-- 
    Document   : Menu
    Created on : Jun 15, 2025, 10:28:55 AM
    Author     : Khanh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dịch vụ cứu hộ xe máy</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="css/menu.css">
</head>
<body>
    <div class="menu-container">
        <!-- Nút back -->
        <a href="homepage.jsp">
            <button class="back-button"><i class="fa fa-arrow-left"></i>Back</button>
        </a>

        <!-- Phần dưới cùng: bảng + hotline -->
        <div class="bottom-section">
            <table class="service-table">
                <thead>
                    <tr>
                        <th>Dịch vụ</th>
                        <th>Mô tả</th>
                        <th>Giá tham khảo (VNĐ)</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>Vá xe tại chỗ</td>
                        <td>Vá lốp xe máy tận nơi</td>
                        <td>30.000 - 50.000</td>
                    </tr>
                    <tr>
                        <td>Thay lốp</td>
                        <td>Thay lốp xe mới, tùy loại xe</td>
                        <td>150.000 - 350.000</td>
                    </tr>
                    <tr>
                        <td>Cứu hộ kéo xe</td>
                        <td>Kéo xe về garage gần nhất</td>
                        <td>300.000 - 700.000</td>
                    </tr>
                    <tr>
                        <td>Nạp bình ắc quy</td>
                        <td>Hỗ trợ khởi động xe khi hết bình</td>
                        <td>100.000 - 200.000</td>
                    </tr>
                    <tr>
                        <td>Thay bugi / lọc gió / dầu nhớt</td>
                        <td>Bảo trì các bộ phận cơ bản của xe</td>
                        <td>50.000 - 250.000</td>
                    </tr>
                    <tr>
                        <td>Dịch vụ khác</td>
                        <td>Liên hệ để được tư vấn cụ thể</td>
                        <td>Liên hệ</td>
                    </tr>
                </tbody>
            </table>

            <div class="hotline">
                📞 Hotline hỗ trợ: <strong>1900 9999</strong>
            </div>
        </div>
    </div>
</body>
</html>