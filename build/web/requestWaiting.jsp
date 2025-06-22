<%-- 
    Document   : requestWaiting
    Created on : Jun 14, 2025, 7:54:54 PM
    Author     : Khanh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
         <link rel="stylesheet" type="text/css" href="css/requestWaiting.css">
   <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
    <div class="status-box">
        <i class="fas fa-clock status-icon"></i>
        <h2>Yêu cầu của bạn đang được xử lý</h2>
        <p class="status-text"><strong>Trạng thái:</strong> <span class="status waiting">ĐANG CHỜ XỬ LÝ</span></p>
        <p>Cảm ơn bạn đã gửi yêu cầu cứu hộ. Nhân viên sẽ liên hệ với bạn trong thời gian sớm nhất.</p>
        
        <%
    String role = (String) session.getAttribute("userRole");
    String backHome = "homepage.jsp"; 

    if ("customer".equals(role)) {
        backHome = "customer/home.jsp";
    } else if ("owner".equals(role)) {
        backHome = "owner/home.jsp";
    } else if ("manager".equals(role)) {
        backHome = "manager/home.jsp";
    } else if ("employee".equals(role)) {
        backHome = "employee/home.jsp";
    }
%>

<a href="<%= backHome %>" class="back-button">
    <i class="fas fa-arrow-left"></i> Quay về trang chủ
</a>
        
    </div>
</body>
</html>