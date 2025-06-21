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
     <div class="request-form">
            <h2>Request information</h2>

            <!-- FORM BẮT ĐẦU -->
            <form action="requestWaiting.jsp" method="post">
                <div class="form-group">
                    <table class="service-table">
    <tr>
        <td colspan="2"><label for="service"><strong>Service</strong></label></td>
    </tr>
    <tr>
        <td><input type="checkbox" name="service" value="tire patching"></td>
        <td>Vá xe tại chỗ</td>
    </tr>
    <tr>
        <td><input type="checkbox" name="service" value="Tire replacement"></td>
        <td>Thay lốp</td>
    </tr>
    <tr>
        <td><input type="checkbox" name="service" value="Towing service"></td>
        <td>Cứu hộ kéo xe</td>
    </tr>
    <tr>
        <td><input type="checkbox" name="service" value="Battery jump-start"></td>
        <td>Nạp bình ắc quy</td>
    </tr>
    <tr>
        <td><input type="checkbox" name="service" value="Spark plug / filter / oil replacement"></td>
        <td>Thay bugi / lọc gió / dầu nhớt</td>
    </tr>
    <tr>
        <td><input type="checkbox" name="service" value="Other" id="otherCheckbox"></td>
        <td>Dịch vụ khác</td>
    </tr>
    <tr id="other-service-box" >
        <td colspan="8">
            <label for="otherService">Nhập dịch vụ khác:</label><br>
            <textarea id="otherService" name="otherService" placeholder="Vui lòng ghi rõ dịch vụ..." rows="3" cols="41"></textarea>
        </td>
    </tr>
</table>


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