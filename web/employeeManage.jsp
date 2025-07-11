<%-- 
    Document   : employeeManager
    Created on : Jun 27, 2025, 8:06:29 AM
    Author     : Khanh
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
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản Lý Nhân Viên - MRS</title>
        <link rel="stylesheet" href="css/employeeMangage.css" />
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
            %>

            <!-- Statistics Row -->
            <%
                List<EmployeeDTO> allEmployees = (List<EmployeeDTO>) request.getAttribute("employees");
                int totalEmployees = allEmployees != null ? allEmployees.size() : 0;
                int activeEmployees = 0;
                int technicianCount = 0;
                int specialistCount = 0;

                if (allEmployees != null) {
                    for (EmployeeDTO emp : allEmployees) {
                        if (emp.getRole() != null) {
                            if (emp.getRole().toLowerCase().contains("technician")) {
                                technicianCount++;
                            } else if (emp.getRole().toLowerCase().contains("specialist")) {
                                specialistCount++;
                            }
                        }
                        if (emp.getAccountId() > 0) {
                            activeEmployees++;
                        }
                    }
                }
            %>
            <div class="stats-row">
                <div class="stat-card">
                    <div class="stat-number"><%= totalEmployees%></div>
                    <div class="stat-label">Tổng Nhân Viên</div>
                </div>
                <div class="stat-card">
                    <div class="stat-number"><%= activeEmployees%></div>
                    <div class="stat-label">Có Tài Khoản</div>
                </div>
                <div class="stat-card">
                    <div class="stat-number"><%= technicianCount%></div>
                    <div class="stat-label">Kỹ Thuật Viên</div>
                </div>
                <div class="stat-card">
                    <div class="stat-number"><%= specialistCount%></div>
                    <div class="stat-label">Chuyên Gia</div>
                </div>
            </div>

            <!-- Controls -->
            <div class="controls">
                <div class="search-box">
                    <form action="EmployeeManageController" method="GET" style="display: flex; flex: 1; gap: 10px;">
                        <input type="text" name="searchTerm" class="search-input" 
                               placeholder="🔍 Tìm kiếm theo tên, vai trò, email..." 
                               value="<%= request.getParameter("searchTerm") != null ? request.getParameter("searchTerm") : ""%>">
                        <button type="submit" name="action" value="search" class="btn btn-primary">Tìm Kiếm</button>
                        <button type="submit" name="action" value="getAllEmployees" class="btn btn-secondary">Tất Cả</button>
                    </form>
                </div>
                <div>
                    <a href="addEmployee.jsp" class="btn btn-success">➕ Thêm Nhân Viên</a>
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
                        <form action="EmployeeManageController" method="GET" style="display: inline;">
                            <button type="submit" name="action" value="getAllEmployees" class="btn btn-primary">
                                🔄 Tải Lại Danh Sách
                            </button>
                        </form>
                        <a href="addEmployee.jsp" class="btn btn-success" style="margin-left: 10px;">
                            ➕ Thêm Nhân Viên Mới
                        </a>
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
                            <td><strong>#<%= employee.getEmployeeId()%></strong></td>
                            <td>
                                <div>
                                    <strong><%= employee.getName() != null ? employee.getName() : "N/A"%></strong>
                                    <% if (employee.getFirstName() != null && employee.getLastName() != null) {%>
                                    <br><small style="color: #718096;">
                                        <%= employee.getFirstName()%> <%= employee.getLastName()%>
                                    </small>
                                    <% } %>
                                </div>
                            </td>
                            <td>
                                <% if (employee.getEmail() != null && !employee.getEmail().isEmpty()) {%>
                                <a href="mailto:<%= employee.getEmail()%>" style="color: #667eea;">
                                    <%= employee.getEmail()%>
                                </a>
                                <% } else { %>
                                <span style="color: #a0aec0;">Chưa có email</span>
                                <% } %>
                            </td>
                            <td>
                                <% if (employee.getPhone() != null && !employee.getPhone().isEmpty()) {%>
                                <a href="tel:<%= employee.getPhone()%>" style="color: #48bb78;">
                                    📞 <%= employee.getPhone()%>
                                </a>
                                <% } else { %>
                                <span style="color: #a0aec0;">Chưa có SĐT</span>
                                <% } %>
                            </td>
                            <td>
                                <% if (employee.getRole() != null) {%>
                                <span class="role-badge role-<%= employee.getRole().toLowerCase().replaceAll(" ", "-")%>">
                                    <%= employee.getRole()%>
                                </span>
                                <% } else { %>
                                <span style="color: #a0aec0;">Chưa phân vai trò</span>
                                <% } %>
                            </td>
                            <td>
                                <% if (employee.getWorkingHours() != null && !employee.getWorkingHours().isEmpty()) {%>
                                <small>🕒 <%= employee.getWorkingHours()%></small>
                                <% } else { %>
                                <span style="color: #a0aec0;">Chưa xác định</span>
                                <% } %>
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
                                <span style="color: #22543d;">✅ <%= employee.getAccountUsername()%></span>
                                <% } else { %>
                                <span style="color: #742a2a;">❌ Chưa có tài khoản</span>
                                <% }%>
                            </td>
                            <td>
                                <div class="action-buttons">
                                    <form action="EmployeeManageController" method="GET" style="display: inline;">
                                        <input type="hidden" name="action" value="viewEmployee">
                                        <input type="hidden" name="employeeId" value="<%= employee.getEmployeeId()%>">
                                        <button type="submit" class="btn btn-primary btn-sm">
                                            👁️ Xem
                                        </button>
                                    </form>
                                    <form action="EmployeeManageController" method="GET" style="display: inline;">
                                        <input type="hidden" name="action" value="editEmployee">
                                        <input type="hidden" name="employeeId" value="<%= employee.getEmployeeId()%>">
                                        <button type="submit" class="btn btn-warning btn-sm">
                                            ✏️ Sửa
                                        </button>
                                    </form>
                                    <% if ("owner".equals(user.getRole())) {%>
                                    <form action="EmployeeManageController" method="POST" style="display: inline;" 
                                          onsubmit="return confirm('⚠️ Bạn có chắc chắn muốn xóa nhân viên <%= employee.getName()%>?\\n\\nHành động này không thể hoàn tác!')">
                                        <input type="hidden" name="action" value="deleteEmployee">
                                        <input type="hidden" name="employeeId" value="<%= employee.getEmployeeId()%>">
                                        <button type="submit" class="btn btn-danger btn-sm">
                                            🗑️ Xóa
                                        </button>
                                    </form>
                                    <% } %>
                                </div>
                            </td>
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

            <!-- Navigation -->
            <div class="back-navigation">
                <a href="homepage.jsp" class="btn btn-secondary">
                    ← Quay Về Trang Chủ
                </a>
                <form action="EmployeeManageController" method="GET" style="display: inline; margin-left: 15px;">
                    <button type="submit" name="action" value="getAllEmployees" class="btn btn-primary">
                        🔄 Tải Lại Danh Sách
                    </button>
                </form>
            </div>
        </div>

        <script>
            // Auto-submit search form on Enter key
            document.querySelector('.search-input').addEventListener('keypress', function (e) {
                if (e.key === 'Enter') {
                    e.preventDefault();
                    this.form.submit();
                }
            });

            // Add loading state to buttons
            document.querySelectorAll('button[type="submit"]').forEach(button => {
                button.addEventListener('click', function () {
                    if (this.form.checkValidity()) {
                        this.innerHTML = '⏳ Đang xử lý...';
                        this.disabled = true;
                        setTimeout(() => {
                            this.form.submit();
                        }, 100);
                    }
                });
            });

            // Highlight search terms
            const searchTerm = '<%= request.getParameter("searchTerm") != null ? request.getParameter("searchTerm") : ""%>';
            if (searchTerm) {
                const regex = new RegExp(`(${searchTerm})`, 'gi');
                document.querySelectorAll('td').forEach(cell => {
                    if (cell.textContent.toLowerCase().includes(searchTerm.toLowerCase())) {
                        cell.innerHTML = cell.innerHTML.replace(regex, '<mark style="background-color: #fef08a;">$1</mark>');
                    }
                });
            }

            // Add tooltips for truncated content
            document.querySelectorAll('td').forEach(cell => {
                if (cell.scrollWidth > cell.clientWidth) {
                    cell.title = cell.textContent;
                }
            });
        </script>
    </body>
</html>