<%-- 
    Document   : manageManager
    Created on : Jul 15, 2025, 4:57:19 PM
    Author     : Khanh
--%>

<%@page import="java.util.List"%>
<%@page import="project.model.dto.AccountDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <%
    List<AccountDTO> managerList = (List<AccountDTO>) request.getAttribute("MANAGER_LIST");
    String error = (String) request.getAttribute("ERROR");
    String message = (String) request.getAttribute("MESSAGE");
    String warning = (String) request.getAttribute("WARNING");

    AccountDTO user = (AccountDTO) session.getAttribute("LOGIN_USER");
    if (user == null) {
        user = (AccountDTO) session.getAttribute("account");
    }

    if (user == null || (!"owner".equals(user.getRole()))) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<head>
    <meta charset="UTF-8">
    <title>Quản lý tài khoản Quản lý</title>
    <link rel="stylesheet" href="css/manageManager.css">
    <style>
        .hidden { display: none; }
        table, th, td { border: 1px solid black; border-collapse: collapse; padding: 6px; }
        .message { color: green; }
        .error { color: red; }
    </style>
    <script>
        function toggleCreateForm() {
            var form = document.getElementById("createForm");
            form.classList.toggle("hidden");
        }
    </script>
</head>
<body>
    <h1>Quản lý tài khoản Quản lý</h1>

    <% if (message != null) { %>
        <p class="message"><%= message %></p>
    <% } %>
    <% if (error != null) { %>
        <p class="error"><%= error %></p>
    <% } %>
    <% if (warning != null) { %>
        <p class="error"><%= warning %></p>
    <% } %>

    <!-- Nút thao tác -->
    <form action="MainController" method="post" style="margin-bottom: 10px; display: inline;">
        <input type="hidden" name="action" value="ManageManager">
        <button type="submit">Tải lại danh sách</button>
    </form>
    <button onclick="toggleCreateForm()">Tạo mới tài khoản</button>

    <!-- Form Tạo mới Manager -->
   <div id="createForm" class="hidden" style="margin-top: 10px;">
    <form action="MainController" method="post" style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 10px; max-width: 600px;">
        <input type="hidden" name="action" value="ManageManagerAction">
         <input type="hidden" name="subAction" value="create">
        <div>Username: <input type="text" name="username" required></div>
        <div>Password: <input type="password" name="password" required></div>
        <div>Nickname: <input type="text" name="nickname" required></div>
        <div>Phone: <input type="text" name="phone" required></div>
        <div>Email: <input type="email" name="email" required></div>
        <div style="grid-column: span 2;">
            <button type="submit">Tạo</button>
        </div>
    </form>
</div>

    <!-- Danh sách Manager -->
    <h3>Danh sách quản lý</h3>
    <table>
        <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Đăng ký?</th>
            <th>Mật khẩu mới</th>
            <th>Nickname</th>
            <th>Phone</th>
            <th>Email</th>
            <th>Hành động</th>
        </tr>
        <% if (managerList != null) {
            for (AccountDTO acc : managerList) { %>
        <tr>
            <form action="MainController" method="post">
                <input type="hidden" name="action" value="ManageManagerAction">
                <input type="hidden" name="subAction" value="update">
                <input type="hidden" name="accountId" value="<%= acc.getId() %>">
                <td><%= acc.getId() %></td>
                <td><input type="text" name="username" value="<%= acc.getUserName() %>" required></td>
                <td>
                    <select name="isregistered">
                        <option value="1" <%= acc.getIsRegistered() ? "selected" : "" %>>Có</option>
                        <option value="0" <%= !acc.getIsRegistered() ? "selected" : "" %>>Chưa</option>
                    </select>
                </td>
                <td><input type="password" name="password" placeholder="Để trống nếu không đổi"></td>
                <td><input type="text" name="nickname" value="<%= acc.getNickName() == null ? "" : acc.getNickName() %>"></td>
<td><input type="text" name="phone" value="<%= acc.getPhone() == null ? "" : acc.getPhone() %>"></td>
<td><input type="email" name="email" value="<%= acc.getEmail() == null ? "" : acc.getEmail() %>"></td>
                <td>
                    <button type="submit">Cập nhật</button>
            </form>
            <form action="MainController" method="post"
                  onsubmit="return confirm('Bạn có chắc muốn xoá tài khoản này không?');" style="display:inline;">
                <input type="hidden" name="action" value="ManageManagerAction">
                <input type="hidden" name="subAction" value="delete">
                <input type="hidden" name="accountId" value="<%= acc.getId() %>">
                <button type="submit" style="background-color:red;color:white;">Xoá</button>
            </form>
            </td>
        </tr>
        <% } } %>
    </table>

    <br><a href="homepage.jsp">← Quay lại trang chủ</a>
</body>
</html>