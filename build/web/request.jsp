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
        <link rel="stylesheet" type="text/css" href="css/request.css">
    </head>
    <body>
        <div class="request-form">
            <h2>Request information</h2>

            <!-- FORM BẮT ĐẦU -->
            <form action="requestWaiting.jsp" method="post">
                <div class="form-group">
                    <label for="service">Service</label>
                    <select id="service" name="service" required>
                        <option value="">Select service</option>
                        <option value="tire patching">Vá xe tại chỗ</option>
                        <option value="Tire replacement">Thay lốp</option>
                        <option value="Towing service">Cứu hộ kéo xe</option>
                        <option value="Battery jump-start">Nạp bình ắc quy</option>
                        <option value="Spark plug / filter / oil replacement">Thay bugi / lọc gió / dầu nhớt</option>
                        <option value="Other">Dịch vụ khác</option>
                        <div class="form-group" id="other-service-box">
                            <label for="otherService">Nhập dịch vụ khác</label>
                            <textarea id="otherService" name="otherService" placeholder="Vui lòng ghi rõ dịch vụ..."></textarea>
                        </div>
                    </select>
                </div>

                <div class="form-group">
                    <label for="location">Enter location (if GPS enable please ignore)</label>
                    <textarea id="location" name="location" placeholder="Your location..."></textarea>
                </div>

                <div class="form-group">
                    <label for="note">Request note</label>
                    <textarea id="note" name="note" placeholder="Your note..."></textarea>
                </div>

                <div class="terms-box">
                    <input type="checkbox" id="terms" name="terms" required>
                    <label for="terms">Tôi đồng ý với điều khoản</label>
                </div>

                <button type="submit">Send request information</button>
            </form>
            <!-- FORM KẾT THÚC -->
        </div>
    </body>
</html>