<%-- 
    Document   : employeeTask
    Created on : Jun 27, 2025, 8:06:06 AM
    Author     : Khanh
--%>

<%@page import="project.model.dto.RequestDTO"%>
<%@page import="java.util.List"%>
<%@page import="project.model.dto.RequestDetailDTO"%>
<%@page import="project.model.dto.AccountDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Check user authentication and role
    AccountDTO user = (AccountDTO) session.getAttribute("LOGIN_USER");
    if (user == null) {
        user = (AccountDTO) session.getAttribute("account");
    }

    if (user == null || (!"employee".equals(user.getRole()))) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<%
    AccountDTO loginUser = (AccountDTO) session.getAttribute("LOGIN_USER");
    
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Nhiệm Vụ Của Nhân Viên</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f6f9;
            padding: 20px;
        }

        h2 {
            text-align: center;
            margin-bottom: 30px;
        }

        .message {
            text-align: center;
            margin-bottom: 15px;
        }

        .success {
            color: green;
        }

        .error {
            color: red;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background-color: white;
        }

        th, td {
            padding: 12px;
            border: 1px solid #ccc;
            text-align: center;
        }

        th {
            background-color: #007BFF;
            color: white;
        }

        .btn {
            padding: 6px 12px;
            border: none;
            background-color: #28a745;
            color: white;
            cursor: pointer;
        }

        .btn:hover {
            background-color: #218838;
        }

        .btn-reload {
            background-color: #007bff;
            margin-right: 10px;
        }

        .btn-reload:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>

    <h2>Danh Sách Nhiệm Vụ Của Bạn</h2>

    <!-- Thông báo -->
    <div class="message">
        <%
            String success = (String) request.getAttribute("SUCCESS");
            String error = (String) request.getAttribute("ERROR");
            if (success != null) {
        %>
            <div class="success"><%= success %></div>
        <%
            }
            if (error != null) {
        %>
            <div class="error"><%= error %></div>
        <%
            }
        %>
    </div>

    <%
        List<RequestDTO> requests = (List<RequestDTO>) request.getAttribute("requests");

                
       List<RequestDetailDTO> tasks = (List<RequestDetailDTO>) request.getAttribute("tasks");
        if (tasks == null || tasks.isEmpty()) {
    %>
        <p style="text-align:center;">Không có nhiệm vụ nào được giao.</p>
        
    <%
        } else {
    %>

    <table>
        <thead>
            <tr>
                <th>Mã Yêu Cầu</th>
                <th>Tên khách hàng</th>
                <th>DỊch vụ</th>
                <th>Vị Trí</th>
                <th>Tình Trạng</th>
                <th>Độ Khẩn</th>
                <th>Notes</th>
                <th>Thao Tác</th>
            </tr>
        </thead>
        <tbody>
            <%
                
                for (RequestDetailDTO task : tasks) {
                    
            %>
            <tr>
               <td><%= task.getRequestId() %></td>
<td><%= task.getCustomerName() %></td>
<td><%= task.getServiceName() %></td>
<td><%= task.getLocation() %></td>
<td><%= task.getStatus() %></td>
<td><%= task.getUrgency() %></td>
<td><%= task.getNotes() %></td>
                
                <td>
                    <% if ("In Progress".equalsIgnoreCase(task.getStatus())) { %>
                        <form action="EmployeeTaskController" method="POST" style="display:inline;">
                            <input type="hidden" name="action" value="complete" />
                            <input type="hidden" name="requestId" value="<%= task.getRequestId() %>" />
                            <button type="submit" class="btn">Hoàn Thành</button>
                        </form>
                    <% } else { %>
                        <span style="color: gray;">Đã xử lý</span>
                    <% } %>
                </td>
            </tr>
            <%
                
                }


            %>
        </tbody>
    </table>

    <% } %>

    <div style="text-align: center; margin-top: 30px;">
        <form action="EmployeeTaskController" method="GET" style="display:inline;">
            <button type="submit" class="btn btn-reload">Tải lại danh sách</button>
        </form>
        <a href="homepage.jsp" style="margin-left: 15px;">← Quay lại</a>
    </div>

</body>
</html>
