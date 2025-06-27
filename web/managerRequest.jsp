<%-- 
    Document   : managerRequest
    Created on : Jun 27, 2025, 8:07:28 AM
    Author     : Khanh
--%>

<%@page import="project.model.dto.EmployeeDTO"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="project.model.dto.RequestDTO" %>

<html>
    <head>
    <meta charset="UTF-8">
    <title>Quản Lý Yêu Cầu</title>
    <link rel="stylesheet" href="css/managerRequest.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
</head>
<body>
<div class="container">
    <h2><i class="fas fa-clipboard-list"></i> Quản Lý Yêu Cầu Khách Hàng</h2>

    <% String successMessage = (String) request.getAttribute("SUCCESS"); %>
    <% if (successMessage != null) { %>
        <div class="message success"><i class="fas fa-check-circle"></i> <%= successMessage %></div>
    <% } %>

    <% String errorMessage = (String) request.getAttribute("ERROR"); %>
    <% if (errorMessage != null) { %>
        <div class="message error"><i class="fas fa-exclamation-circle"></i> <%= errorMessage %></div>
    <% } %>

    <%
        List<RequestDTO> requests = (List<RequestDTO>) request.getAttribute("requests");
        List<EmployeeDTO> employees = (List<EmployeeDTO>) request.getAttribute("employees");
    %>

    <% if (requests == null || requests.isEmpty()) { %>
        <div class="no-requests">
            <p><i class="fas fa-info-circle"></i> Không có yêu cầu nào đang chờ xử lý.</p>
            <form action="ManagerRequestController" method="GET">
                <button type="submit" name="action" value="ManagerRequest" class="load-btn">
                    <i class="fas fa-rotate-right"></i> Tải lại danh sách
                </button>
            </form>
            <a href="javascript:history.back()" class="back-btn">
                ← Quay lại
            </a>
        </div>
    <% } else { %>

    <div class="table-wrapper">
        <table>
            <thead>
                <tr>
                    <th>Mã Yêu Cầu</th>
                    <th>Khách Hàng</th>
                    <th>Liên Hệ</th>
                    <th>Trạng Thái</th>
                    <th>Vị Trí</th>
                    <th>Tình Trạng</th>
                    <th>Ngày Tạo</th>
                    <th>Phân Công</th>
                    <th>Hành Động</th>
                </tr>
            </thead>
            <tbody>
                <% for (RequestDTO req : requests) { %>
                <tr>
                    <td><%= req.getRequestId() %></td>
                    <td>
                        <strong><%= req.getCustomerName() != null ? req.getCustomerName() : "N/A" %></strong><br>
                        <small>ID: <%= req.getCustomerId() %></small>
                    </td>
                    <td>
                        <%= req.getCustomerPhone() != null ? req.getCustomerPhone() : "N/A" %><br>
                        <%= req.getCustomerEmail() != null ? req.getCustomerEmail() : "N/A" %>
                    </td>
                    <td>
                        <span class="status <%= req.getStatus().toLowerCase() %>">
                            <%= req.getStatus().toUpperCase() %>
                        </span>
                    </td>
                    <td><%= req.getLocation() %></td>
                    <td><%= req.getUrgency() %></td>
                    <td><%= req.getCreatedAt() %></td>
                    <td>
                        <select name="employeeId" form="form_<%= req.getRequestId() %>">
                            <% for (EmployeeDTO emp : employees) { %>
                                <option value="<%= emp.getEmployeeId() %>"><%= emp.getName() %></option>
                            <% } %>
                        </select>
                    </td>
                    <td>
                        <% if ("pending".equals(req.getStatus())) { %>
                        <form id="form_<%= req.getRequestId() %>" action="ManagerRequestController" method="POST">
                            <input type="hidden" name="action" value="accept">
                            <input type="hidden" name="requestId" value="<%= req.getRequestId() %>">
                            <button type="submit" class="btn btn-accept"
                                onclick="return confirm('Bạn có chắc chắn muốn chấp nhận yêu cầu này?')">
                                <i class="fas fa-check"></i> Chấp Nhận
                            </button>
                        </form>
                        <form action="ManagerRequestController" method="POST">
                            <input type="hidden" name="action" value="reject">
                            <input type="hidden" name="requestId" value="<%= req.getRequestId() %>">
                            <button type="submit" class="btn btn-reject"
                                    onclick="return confirm('Bạn có chắc chắn muốn từ chối yêu cầu này?')">
                                <i class="fas fa-times"></i> Từ Chối
                            </button>
                        </form>
                        <% } else { %>
                            <span class="status done">Đã xử lý</span>
                        <% } %>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>

    <div class="footer-action">
        <form action="ManagerRequestController" method="GET">
            <button type="submit" class="load-btn"><i class="fas fa-sync-alt"></i> Tải lại danh sách</button>
        </form>
        <a href="homepage.jsp" class="back-btn">← Quay lại</a>
    </div>

    <% } %>
</div>
</body>
</html>