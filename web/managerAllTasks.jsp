<%-- 
    Document   : managerAllTasks
    Created on : Jul 3, 2025, 1:23:44 PM
    Author     : Khanh
--%>

<%@page import="java.util.List"%>
<%@page import="project.model.dto.RequestDetailDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Danh sách các yêu cầu đang xử lý</title>
        <link rel="stylesheet" href="css/managerAllTask.css">
    
</head>
<body>
    <h2>📋 Danh sách các yêu cầu đang/đã xử lý</h2>

    <%
        List<RequestDetailDTO> tasks = (List<RequestDetailDTO>) request.getAttribute("assignedTasks");
        if (tasks == null || tasks.isEmpty()) {
    %>
        <p style="text-align:center;">Không có yêu cầu nào được xử lý.</p>
    <%
        } else {
    %>
    <table>
        <tr>
            <th>Mã Yêu Cầu</th>
            <th>Khách Hàng</th>
            <th>Nhân Viên</th>
            <th>Dịch Vụ</th>
            <th>Vị Trí</th>
            <th>Độ Khẩn</th>
            <th>Trạng Thái</th>
            <th>Ghi Chú Nhân viên</th>
            <th>Hình Ảnh</th>
        </tr>
        <%
            for (RequestDetailDTO task : tasks) {
        %>
        <tr>
            <td><%= task.getRequestId() %></td>
            <td><%= task.getCustomerName() %></td>
            <td><%= task.getEmployeeName() %></td>
            <td><%= task.getServiceName() %></td>
            <td><%= task.getLocation() %></td>
            <td><%= task.getUrgency() %></td>
            <td class="status <%= task.getStatus().replace(" ", "").toLowerCase() %>">
                <%= task.getStatus().toUpperCase() %>
            </td>
             
            <td><%= task.getEmployeeNote()!= null ? task.getNotes() : "—" %></td>
            <td>
                <% if (task.getImagePath() != null) { %>
                    <a href="<%= task.getImagePath() %>" target="_blank">Xem ảnh</a>
                <% } else { %>
                    —
                <% } %>
            </td>
        </tr>
        <%
            }
        %>
    </table>
    <% } %>

    <div style="margin-top:20px; text-align:center;">
    <form action="ManagerAllTaskController" method="GET" style="display:inline;">
        <button type="submit" style="padding: 8px 16px; background-color: #007BFF; color: white; border: none; border-radius: 4px;">
            🔄 Tải lại danh sách
        </button>
    </form>
    <a href="managerOrderMenu.jsp" class="button-link">
    ← Quay lại trang yêu cầu
</a>

</div>
</body>
</html>