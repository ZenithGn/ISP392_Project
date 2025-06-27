<%-- 
    Document   : requestWaiting
    Created on : Jun 14, 2025, 7:54:54 PM
    Author     : Khanh
--%>

<%@page import="java.util.List"%>
<%@page import="project.model.dto.RequestDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    List<RequestDTO> waitingRequests = (List<RequestDTO>) request.getAttribute("waitingRequests");
%>

<html>
<head>
    <title>Yêu Cầu Đang Chờ</title>
</head>
<body>
    <h2>Danh Sách Yêu Cầu Đang Chờ</h2>

    <% if (waitingRequests == null || waitingRequests.isEmpty()) { %>
        <p>Không có yêu cầu nào đang chờ xử lý.</p>
    <% } else { %>
        <table border="1" cellpadding="10">
            <tr>
                <th>Mã Yêu Cầu</th>
                <th>Vị Trí</th>
                <th>Độ Khẩn</th>
                <th>Ngày Tạo</th>
                <th>Trạng Thái</th>
            </tr>
            <% for (RequestDTO req : waitingRequests) { %>
                <tr>
                    <td><%= req.getRequestId() %></td>
                    <td><%= req.getLocation() %></td>
                    <td><%= req.getUrgency() %></td>
                    <td><%= req.getCreatedAt() %></td>
                    <td><%= req.getStatus() %></td>
                </tr>
            <% } %>
        </table>
    <% } %>

    <a href="homepage.jsp">← Quay lại trang chủ</a>
</body>
</html>