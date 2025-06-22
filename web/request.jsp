<%-- 
    Document   : request
    Created on : Jun 13, 2025, 9:37:49 AM
    Author     : Khanh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
       <link rel="stylesheet" href="css/request.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
    <div class="request-form">
        <h2><i class="fas fa-motorcycle"></i> Yêu cầu cứu hộ xe máy</h2>

        <form action="requestWaiting.jsp" method="post">
            <div class="services-section">
                <p><strong>Chọn dịch vụ bạn cần:</strong></p>
                <label><input type="checkbox" name="service" value="tire patching"> Vá xe tại chỗ</label>
                <label><input type="checkbox" name="service" value="Tire replacement"> Thay lốp</label>
                <label><input type="checkbox" name="service" value="Towing service"> Cứu hộ kéo xe</label>
                <label><input type="checkbox" name="service" value="Battery jump-start"> Nạp bình ắc quy</label>
                <label><input type="checkbox" name="service" value="Spark plug / filter / oil replacement"> Thay bugi / lọc gió / dầu nhớt</label>
                <label><input type="checkbox" name="service" value="Other" id="otherCheckbox"> Dịch vụ khác</label>

                <div id="other-service-box" class="hidden">
                    <textarea name="otherService" placeholder="Vui lòng ghi rõ dịch vụ khác..."></textarea>
                </div>
            </div>

            <div class="form-group">
                <label for="location">Vị trí (nếu có GPS có thể bỏ qua):</label>
                <textarea name="location" placeholder="Nhập vị trí bạn đang gặp sự cố..."></textarea>
            </div>

            <div class="form-group">
                <label for="note">Ghi chú thêm:</label>
                <textarea name="note" placeholder="Ghi chú thêm cho nhân viên cứu hộ..."></textarea>
            </div>

            <div class="terms-box">
                <input type="checkbox" id="terms" name="terms" required>
                <label for="terms">Tôi đồng ý với <a href="#">điều khoản dịch vụ</a></label>
            </div>

            <button type="submit"><i class="fas fa-paper-plane"></i> Gửi yêu cầu</button>
        </form>
    </div>

    <script>
        // Hiện/ẩn dịch vụ khác
        document.getElementById('otherCheckbox').addEventListener('change', function () {
            const box = document.getElementById('other-service-box');
            box.classList.toggle('hidden', !this.checked);
        });
    </script>
</body>
</html>