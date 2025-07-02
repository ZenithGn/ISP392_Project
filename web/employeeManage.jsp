<%-- 
    Document   : employeeManage
    Created on : Jun 23, 2025, 3:08:28 PM
    Author     : lehan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="project.model.dto.EmployeeDTO" %>
<%@ page import="project.model.dto.AccountDTO" %>
<%
    // Check user authentication and role
    AccountDTO user = (AccountDTO) session.getAttribute("LOGIN_USER");
    if (user == null) {
        user = (AccountDTO) session.getAttribute("account");
    }

    if (user == null || (!"manager".equals(user.getRole()) && !"owner".equals(user.getRole()))) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Get search term
    String searchTerm = request.getParameter("searchTerm");
    if (searchTerm == null) {
        searchTerm = "";
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản Lý Nhân Viên - MRS</title>
        <link rel="stylesheet" href="css/employeeManage.css" />
    </head>
    <body>
        <div class="container">
            <!-- Header -->
            <div class="header">
                <h1>Quản Lý Nhân Viên</h1>
                <p>Quản lý thông tin và hoạt động của tất cả nhân viên trong hệ thống</p>
            </div>

            <!-- Display Messages -->
            <%
                String successMessage = (String) request.getAttribute("SUCCESS");
                if (successMessage != null) {
            %>
            <div class="message success">
                ✅ <%= successMessage%>
            </div>
            <%
                }
                String errorMessage = (String) request.getAttribute("ERROR");
                if (errorMessage != null) {
            %>
            <div class="message error">
                ❌ <%= errorMessage%>
            </div>
            <%
                }
                String infoMessage = (String) request.getAttribute("INFO");
                if (infoMessage != null) {
            %>
            <div class="message info">
                ℹ️ <%= infoMessage%>
            </div>
            <%
                }
            %>

            <!-- Controls -->
            <div class="controls">
                <div class="search-box">
                    <form action="MainController" method="GET" style="display: flex; flex: 1; gap: 10px;">
                        <input type="text" name="searchTerm" class="search-input" 
                               placeholder="🔍 Tìm kiếm theo tên, vai trò, email..." 
                               value="<%= searchTerm%>">
                        <input type="hidden" name="action" value="searchEmployee">
                        <button type="submit" class="btn btn-primary">Tìm Kiếm</button>
                    </form>
                </div>
                <div>
                    <a href="addEmployee.jsp" class="btn btn-success">➕ Thêm Nhân Viên</a>
                </div>
                <div class="back-navigation">
                    <a href="homepage.jsp" class="btn btn-secondary">
                        ← Quay Về Trang Chủ
                    </a>
                </div>
            </div>

            <!-- Employee Table -->
            <%
                List<EmployeeDTO> employees = (List<EmployeeDTO>) request.getAttribute("employees");
                if (employees == null || employees.isEmpty()) {
            %>
            <div class="table-container">
                <div class="no-data">
                    <div style="font-size: 4rem; margin-bottom: 20px;">👤</div>
                    <h3>Không tìm thấy nhân viên nào</h3>
                    <p>Hãy thử tìm kiếm với từ khóa khác hoặc thêm nhân viên mới</p>
                    <div style="margin-top: 20px;">
                    </div>
                </div>
            </div>
            <%
            } else {
            %>
            <div class="table-container">
                <table class="table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Họ Tên</th>
                            <th>Email</th>
                            <th>Số Điện Thoại</th>
                            <th>Vai Trò</th>
                            <th>Giờ Làm Việc</th>
                            <th>Quản Lý</th>
                            <th>Chủ Sở Hữu</th>
                            <th>Tài Khoản</th>
                            <th>Hành Động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            for (EmployeeDTO employee : employees) {
                        %>
                        <tr>
                    <form action="MainController" method="POST">
                        <td><strong>#<%= employee.getEmployeeId()%></strong></td>
                        <td>
                            <div>
                                <strong>
                                    <input type="text" name="employeeName" value="<%= employee.getName() != null ? employee.getName() : ""%>" required="" style="border: 1px solid #ddd; padding: 4px; border-radius: 4px; width: 100%;">
                                </strong>
                                <% if (employee.getFirstName() != null && employee.getLastName() != null) {%>
                                <br><small style="color: #718096;">
                                    <%= employee.getFirstName()%> <%= employee.getLastName()%>
                                </small>
                                <% }%>
                            </div>
                        </td>
                        <td>
                            <input type="email" name="employeeEmail" value="<%= employee.getEmail() != null ? employee.getEmail() : ""%>" style="border: 1px solid #ddd; padding: 4px; border-radius: 4px; width: 100%;">
                        </td>
                        <td>
                            <input type="text" name="employeePhone" value="<%= employee.getPhone() != null ? employee.getPhone() : ""%>" style="border: 1px solid #ddd; padding: 4px; border-radius: 4px; width: 100%;">
                        </td>
                        <td>
                            <select name="employeeRole" style="border: 1px solid #ddd; padding: 4px; border-radius: 4px; width: 100%;" required>
                                <option value="">-- Chọn vai trò --</option>
                                <option value="Technician" <%= "Technician".equals(employee.getRole()) ? "selected" : ""%>>Kỹ Thuật Viên</option>
                                <option value="Specialist" <%= "Specialist".equals(employee.getRole()) ? "selected" : ""%>>Chuyên Gia</option>
                            </select>
                        </td>
                        <td>
                            <select name="employeeWorking" style="border: 1px solid #ddd; padding: 4px; border-radius: 4px; width: 100%;">
                                <option value="">-- Chọn ca làm việc --</option>
                                <option value="8:00-17:00" <%= "8:00-17:00".equals(employee.getWorkingHours()) ? "selected" : ""%>>Ca Hành Chính (8:00 - 17:00)</option>
                                <option value="9:00-18:00" <%= "9:00-18:00".equals(employee.getWorkingHours()) ? "selected" : ""%>>Ca Sáng (9:00 - 18:00)</option>
                                <option value="13:00-22:00" <%= "13:00-22:00".equals(employee.getWorkingHours()) ? "selected" : ""%>>Ca Chiều (13:00 - 22:00)</option>
                                <option value="6:00-15:00" <%= "6:00-15:00".equals(employee.getWorkingHours()) ? "selected" : ""%>>Ca Sớm (6:00 - 15:00)</option>
                                <option value="Part-time Morning" <%= "Part-time Morning".equals(employee.getWorkingHours()) ? "selected" : ""%>>Bán thời gian - Sáng</option>
                                <option value="Part-time Afternoon" <%= "Part-time Afternoon".equals(employee.getWorkingHours()) ? "selected" : ""%>>Bán thời gian - Chiều</option>
                                <option value="Flexible" <%= "Flexible".equals(employee.getWorkingHours()) ? "selected" : ""%>>Linh Hoạt</option>
                            </select>
                        </td>
                        <td>
                            <% if (employee.getManagerName() != null) {%>
                            <span style="color: #553c9a;"><%= employee.getManagerName()%></span>
                            <% } else { %>
                            <span style="color: #a0aec0;">Không có</span>
                            <% } %>
                        </td>
                        <td>
                            <% if (employee.getOwnerName() != null) {%>
                            <span style="color: #c05621;"><%= employee.getOwnerName()%></span>
                            <% } else { %>
                            <span style="color: #a0aec0;">Không có</span>
                            <% } %>
                        </td>
                        <td>
                            <% if (employee.getAccountUsername() != null) {%>
                            <span style="color: #22543d;"><%= employee.getAccountUsername()%></span>
                            <% } else { %>
                            <span style="color: #742a2a;">❌ Chưa có tài khoản</span>
                            <% }%>
                        </td>
                        <td>
                            <input type="hidden" name="employeeId" value="<%= employee.getEmployeeId()%>">
                            <button type="submit" name="action" value="updateEmployee" class="btn btn-warning btn-sm" style="margin-right: 5px;">
                                ✏️ Sửa
                            </button>
                        </td>
                    </form>
                    <form action="MainController" method="POST" onsubmit="return confirm('Bạn có chắc chắn muốn xóa nhân viên này?');" style="margin:0;">
                        <td>
                            <input type="hidden" name="employeeId" value="<%= employee.getEmployeeId()%>">
                            <button type="submit" name="action" value="Delete" class="btn btn-danger btn-sm">
                                🗑️ Xóa
                            </button>
                        </td>
                    </form>
                    </tr>
                    <%
                        }
                    %>
                    </tbody>
                </table>
            </div>
            <%
                }
            %>
        </div>
    </body>
</html>