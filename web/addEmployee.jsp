<%-- 
    Document   : addEmployee
    Created on : Jun 26, 2025, 3:30:00 PM
    Author     : KhoaLe
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <title>Thêm Nhân Viên Mới - MRS</title>
        <link rel="stylesheet" href="css/addEmployee.css" />
        <style>
            .password-container {
                position: relative;
            }
            .toggle-password {
                position: absolute;
                right: 10px;
                top: 50%;
                transform: translateY(-50%);
                cursor: pointer;
                background: none;
                border: none;
                font-size: 16px;
            }
            .form-input[type="password"] {
                padding-right: 40px;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <!-- Header -->
            <div class="header">
                <h1>🧑‍💼 Thêm Nhân Viên Mới</h1>
                <p>Nhập thông tin đầy đủ để thêm nhân viên mới vào hệ thống. Tài khoản đăng nhập sẽ được tự động tạo.</p>
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

            <!-- Form Container: ONLY POST TO MAINCONTROLLER -->
            <div class="form-container">
                <form action="MainController" method="POST" id="addEmployeeForm">
                    <input type="hidden" name="action" value="AddEmployee">

                    <!-- Basic Information Section -->
                    <div class="form-section">
                        <h3>📋 Thông Tin Cơ Bản</h3>
                        <div class="form-grid">
                            <div class="form-group">
                                <label for="name">Tên Hiển Thị <span class="required">*</span></label>
                                <input type="text" id="name" name="name" class="form-input" 
                                       placeholder="Nguyễn Văn A" required>
                                <div class="help-text">Tên sẽ hiển thị trong hệ thống</div>
                            </div>

                            <div class="form-group">
                                <label for="role">Vai Trò <span class="required">*</span></label>
                                <select id="role" name="role" class="form-input" required>
                                    <option value="">-- Chọn vai trò --</option>
                                    <option value="Technician">Kỹ Thuật Viên</option>
                                    <option value="Specialist">Chuyên Gia</option>
                                    <option value="Admin">Quản Trị Viên</option>
                                    <option value="Support">Hỗ Trợ</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="firstName">Họ</label>
                                <input type="text" id="firstName" name="firstName" class="form-input" 
                                       placeholder="Nguyễn">
                            </div>

                            <div class="form-group">
                                <label for="lastName">Tên</label>
                                <input type="text" id="lastName" name="lastName" class="form-input" 
                                       placeholder="Văn A">
                            </div>
                        </div>
                    </div>

                    <!-- Contact Information Section -->
                    <div class="form-section">
                        <h3>📞 Thông Tin Liên Hệ</h3>
                        <div class="form-grid">
                            <div class="form-group">
                                <label for="phone">Số Điện Thoại</label>
                                <input type="tel" id="phone" name="phone" class="form-input" 
                                       placeholder="0901234567">
                                <div class="help-text">Định dạng: 0901234567</div>
                            </div>

                            <div class="form-group">
                                <label for="email">Email <span class="required">*</span></label>
                                <input type="email" id="email" name="email" class="form-input" 
                                       placeholder="example@company.com" required>
                                <div class="help-text">Email sẽ được dùng làm tên đăng nhập</div>
                            </div>
                        </div>
                    </div>

                    <!-- Account Information Section -->
                    <div class="form-section">
                        <h3>🔐 Thông Tin Tài Khoản</h3>
                        <div class="form-grid">
                            <div class="form-group">
                                <label for="password">Mật Khẩu <span class="required">*</span></label>
                                <div class="password-container">
                                    <input type="password" id="password" name="password" class="form-input" 
                                           placeholder="Nhập mật khẩu" required minlength="6">
                                    <button type="button" class="toggle-password" onclick="togglePassword()">👁️</button>
                                </div>
                                <div class="help-text">Mật khẩu tối thiểu 6 ký tự. Tài khoản sẽ được tự động tạo.</div>
                            </div>
                            
                            <div class="form-group">
                                <div style="background: #e8f4fd; padding: 15px; border-radius: 8px; border-left: 4px solid #2196F3;">
                                    <strong>🔵 Lưu ý về tài khoản:</strong><br>
                                    • Tài khoản đăng nhập sẽ được tự động tạo<br>
                                    • Email sẽ là tên đăng nhập<br>
                                    • Vai trò tài khoản: Nhân viên
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Work Information Section -->
                    <div class="form-section">
                        <h3>⏰ Thông Tin Công Việc</h3>
                        <div class="form-grid">
                            <div class="form-group">
                                <label for="workingHours">Giờ Làm Việc</label>
                                <select id="workingHours" name="workingHours" class="form-input">
                                    <option value="">-- Chọn ca làm việc --</option>
                                    <option value="8:00-17:00">Ca Hành Chính (8:00 - 17:00)</option>
                                    <option value="9:00-18:00">Ca Sáng (9:00 - 18:00)</option>
                                    <option value="13:00-22:00">Ca Chiều (13:00 - 22:00)</option>
                                    <option value="6:00-15:00">Ca Sớm (6:00 - 15:00)</option>
                                    <option value="Part-time Morning">Bán thời gian - Sáng</option>
                                    <option value="Part-time Afternoon">Bán thời gian - Chiều</option>
                                    <option value="Flexible">Linh Hoạt</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="managerId">Quản Lý Trực Tiếp</label>
                                <input type="number" id="managerId" name="managerId" class="form-input" 
                                       placeholder="Nhập ID quản lý" min="1">
                                <div class="help-text">ID của quản lý trực tiếp (nếu có)</div>
                            </div>

                            <div class="form-group">
                                <label for="ownerId">Chủ Sở Hữu</label>
                                <input type="number" id="ownerId" name="ownerId" class="form-input" 
                                       placeholder="Nhập ID chủ sở hữu" min="1">
                                <div class="help-text">ID của chủ sở hữu phụ trách (nếu có)</div>
                            </div>
                        </div>
                    </div>

                    <!-- Buttons -->
                    <div class="button-group">
                        <button type="submit" class="btn btn-success">
                            ✅ Thêm Nhân Viên
                        </button>
                        <a href="MainController?action=searchEmployee&searchTerm=" class="btn btn-secondary">
                            ❌ Hủy Bỏ
                        </a>
                        <button type="reset" class="btn btn-secondary">
                            🔄 Làm Mới
                        </button>
                    </div>
                </form>
            </div>
        </div>

        <script>
            function togglePassword() {
                const passwordField = document.getElementById('password');
                const toggleButton = document.querySelector('.toggle-password');
                
                if (passwordField.type === 'password') {
                    passwordField.type = 'text';
                    toggleButton.textContent = '🙈';
                } else {
                    passwordField.type = 'password';
                    toggleButton.textContent = '👁️';
                }
            }

            // Form validation
            document.getElementById('addEmployeeForm').addEventListener('submit', function(e) {
                const name = document.getElementById('name').value.trim();
                const role = document.getElementById('role').value;
                const email = document.getElementById('email').value.trim();
                const password = document.getElementById('password').value;

                if (!name) {
                    alert('Vui lòng nhập tên hiển thị');
                    e.preventDefault();
                    return;
                }

                if (!role) {
                    alert('Vui lòng chọn vai trò');
                    e.preventDefault();
                    return;
                }

                if (!email) {
                    alert('Vui lòng nhập email');
                    e.preventDefault();
                    return;
                }

                if (!password || password.length < 6) {
                    alert('Mật khẩu phải có ít nhất 6 ký tự');
                    e.preventDefault();
                    return;
                }

                // Email validation
                const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                if (!emailRegex.test(email)) {
                    alert('Vui lòng nhập email hợp lệ');
                    e.preventDefault();
                    return;
                }
            });
        </script>
    </body>
</html>