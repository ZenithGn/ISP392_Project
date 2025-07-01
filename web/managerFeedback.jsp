<%-- 
    Document   : managerFeedback
    Created on : Jul 1, 2025, 10:13:01 AM
    Author     : Khanh
--%>

<%@page import="java.util.List"%>
<%@page import="project.model.dto.FeedBackDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    List<FeedBackDTO> feedbackList = (List<FeedBackDTO>) request.getAttribute("FEEDBACK_LIST");
%>
<html>
<head>
    <title>Danh sách đánh giá</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            padding: 10px;
            border: 1px solid #ccc;
            text-align: left;
        }
        th {
            background-color: #007BFF;
            color: white;
        }
    </style>
</head>
<body>
    <h2>Danh sách đánh giá từ khách hàng</h2>

    <% if (feedbackList != null && !feedbackList.isEmpty()) { %>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Mã Yêu Cầu</th>
                <th>Khách Hàng</th>
                <th>Dịch Vụ</th>
                <th>Đánh Giá</th>
                <th>Nhận Xét</th>
            </tr>
        </thead>
        <tbody>
            <% for (FeedBackDTO fb : feedbackList) { %>
            <tr>
                <td><%= fb.getFeedbackId() %></td>
                <td><%= fb.getRequestId() %></td>
                <td><%= fb.getCustomerName() %></td>
                <td><%= fb.getServiceName() %></td>
                <td><%= fb.getRating() %> ★</td>
                <td><%= fb.getComment() %></td>
            </tr>
            <% } %>
        </tbody>
    </table>
    <% } else { %>
        <p>Không có đánh giá nào được tìm thấy.</p>
    <% } %>
</body>
</html>

