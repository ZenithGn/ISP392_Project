<%-- 
    Document   : homepage
    Created on : Jun 15, 2025, 10:37:12 PM
    Author     : Dang Quang Phu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="project.model.dto.AccountDTO" %>
<%
    // Get user from session
    AccountDTO user = (AccountDTO) session.getAttribute("LOGIN_USER");
    if (user == null) {
        user = (AccountDTO) session.getAttribute("account");
    }

    String userRole = "";
    String userName = "Guest";
    boolean isLoggedIn = false;

    if (user != null) {
        userRole = user.getRole() != null ? user.getRole().toLowerCase() : "";
        userName = user.getUserName() != null ? user.getUserName() : "User";
        isLoggedIn = true;
    }
%>
<!DOCTYPE html>
<html lang="vi">

    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>MRS - Dịch vụ cứu hộ xe máy</title>
        <link rel="stylesheet" href="css/homepage.css" />
        <style>
            .user-info {
                display: flex;
                align-items: center;
                gap: 10px;
            }
            .role-badge {
                padding: 4px 8px;
                border-radius: 12px;
                font-size: 12px;
                font-weight: bold;
                text-transform: uppercase;
            }
            .role-owner { background-color: #ff6b6b; color: white; }
            .role-manager { background-color: #4ecdc4; color: white; }
            .role-employee { background-color: #45b7d1; color: white; }
            .role-customer { background-color: #96ceb4; color: white; }
            .logout-btn {
                background-color: #ff4757;
                color: white;
                border: none;
                padding: 8px 15px;
                border-radius: 4px;
                cursor: pointer;
                text-decoration: none;
                font-size: 14px;
            }
            .logout-btn:hover {
                background-color: #ff3742;
            }
            .role-specific-content {
                margin: 20px 0;
                padding: 20px;
                border-radius: 8px;
                background-color: #f8f9fa;
            }
            .admin-panel {
                background-color: #e3f2fd;
                border-left: 4px solid #2196f3;
            }
            .manager-panel {
                background-color: #f3e5f5;
                border-left: 4px solid #9c27b0;
            }
            .employee-panel {
                background-color: #e8f5e8;
                border-left: 4px solid #4caf50;
            }
            .customer-panel {
                background-color: #fff3e0;
                border-left: 4px solid #ff9800;
            }
            .dashboard-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
                gap: 15px;
                margin-top: 15px;
            }
            .dashboard-card {
                background: white;
                padding: 15px;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                text-align: center;
            }
            .dashboard-card h4 {
                margin: 0 0 10px 0;
                color: #333;
            }
            .dashboard-card a {
                text-decoration: none;
                color: #007bff;
                font-weight: bold;
            }
            .dashboard-card a:hover {
                color: #0056b3;
            }
        </style>
    </head>
    <body>
        <div class="homepage">
            <!-- Navbar -->
            <header class="navbar">
        <div class="logo">
            <img src="images/logo1.jpg" class="logo-img" />
        </div>
        <nav class="nav-links">
            <% if (isLoggedIn) { %>
            <div class="user-info">
                <span>Xin chào, <%= userName %>!</span>
                <span class="role-badge role-<%= userRole %>"><%= userRole %></span>
                <a href="LogoutController" class="logout-btn">Đăng xuất</a>
            </div>
            <% } else { %>
            <a href="login.jsp">Login</a>
            <% } %>
            <a href="menu.jsp">Service</a>
            <a href="aboutUs.jsp">About Us</a>
            <a href="#" class="active">Home</a>
        </nav>
    </header>

            <!-- Role-specific Dashboard -->
            <% if (isLoggedIn) { %>
            <section class="role-specific-content">
                <% if ("owner".equals(userRole)) { %>
                <div class="admin-panel">
                    <h2>🏢 Owner Dashboard</h2>
                    <p>Chào mừng chủ sở hữu! Bạn có quyền truy cập toàn bộ hệ thống.</p>
                    <div class="dashboard-grid">
                        <div class="dashboard-card">
                            <h4>👥 Quản lý nhân viên</h4>
                            <p>Xem và quản lý tất cả nhân viên</p>
                            <a href="owner/employees">Xem chi tiết</a>
                        </div>
                        <div class="dashboard-card">
                            <h4>📊 Báo cáo doanh thu</h4>
                            <p>Xem báo cáo tài chính tổng thể</p>
                            <a href="owner/reports">Xem báo cáo</a>
                        </div>
                        <div class="dashboard-card">
                            <h4>⚙️ Cài đặt hệ thống</h4>
                            <p>Cấu hình hệ thống và dịch vụ</p>
                            <a href="owner/settings">Cài đặt</a>
                        </div>
                        <div class="dashboard-card">
                            <h4>🏪 Quản lý chi nhánh</h4>
                            <p>Quản lý các chi nhánh và khu vực</p>
                            <a href="owner/branches">Quản lý</a>
                        </div>
                    </div>
                </div>
                <% } else if ("manager".equals(userRole)) { %>
                <div class="manager-panel">
                    <h2>👔 Manager Dashboard</h2>
                    <p>Chào mừng quản lý! Quản lý các hoạt động hàng ngày của chi nhánh.</p>
                    <div class="dashboard-grid">
                        <div class="dashboard-card">
                            <h4>📋 Quản lý đơn hàng</h4>
                            <p>Theo dõi và xử lý các yêu cầu cứu hộ</p>
                            <a href="managerOrderMenu.jsp">Xem đơn hàng</a>
                        </div>
                        <div class="dashboard-card">
                            <h4>👷 Quản lý nhân viên</h4>
                            <p>Phân công và theo dõi nhân viên</p>
                            <a href="employeeManage.jsp">Quản lý nhân viên</a>
                        </div>
                        <div class="dashboard-card">
                            <h4>📈 Báo cáo chi nhánh</h4>
                            <p>Xem hiệu suất chi nhánh</p>
                            <a href="manager/branch-reports">Xem báo cáo</a>
                        </div>
                        <div class="dashboard-card">
                            <h4>📞 Hỗ trợ khách hàng</h4>
                            <p>Xử lý khiếu nại và phản hồi</p>
                            <a href="managerFeedback.jsp">Hỗ trợ</a>
                        </div>
                    </div>
                </div>
                <% } else if ("employee".equals(userRole)) { %>
                <div class="employee-panel">
                    <h2>🔧 Employee Dashboard</h2>
                    <p>Chào mừng nhân viên! Xem các nhiệm vụ được giao và cập nhật trạng thái.</p>
                    <div class="dashboard-grid">
                        <div class="dashboard-card">
                            <h4>📝 Nhiệm vụ của tôi</h4>
                            <p>Xem các yêu cầu cứu hộ được giao</p>
                            <a href="employeeTask.jsp">Xem nhiệm vụ</a>
                        </div>
                        <div class="dashboard-card">
                            <h4>📍 Cập nhật vị trí</h4>
                            <p>Cập nhật vị trí hiện tại của bạn</p>
                            <a href="employee/location">Cập nhật vị trí</a>
                        </div>
                        <div class="dashboard-card">
                            <h4>⏰ Chấm công</h4>
                            <p>Ghi nhận giờ làm việc</p>
                            <a href="employee/timesheet">Chấm công</a>
                        </div>
                        <div class="dashboard-card">
                            <h4>📊 Hiệu suất của tôi</h4>
                            <p>Xem thống kê công việc</p>
                            <a href="employee/performance">Xem hiệu suất</a>
                        </div>
                    </div>
                </div>
                <% } else if ("customer".equals(userRole)) { %>
                <div class="customer-panel">
                    <h2>🛵 Customer Dashboard</h2>
                    <p>Chào mừng khách hàng! Đặt dịch vụ cứu hộ và theo dõi trạng thái.</p>
                    <div class="dashboard-grid">
                        <div class="dashboard-card">
                            <h4>🆘 Yêu cầu cứu hộ</h4>
                            <p>Đặt dịch vụ cứu hộ khẩn cấp</p>
                            <a href="customerRequest.jsp">Yêu cầu ngay</a>
                        </div>
                        <div class="dashboard-card">
                            <h4>📋 Đơn hàng của tôi</h4>
                            <p>Theo dõi trạng thái các yêu cầu</p>
                            <a href="requestCreated.jsp">Xem đơn hàng</a>
                        </div>
                        <div class="dashboard-card">
                            <h4>💳 Thanh toán</h4>
                            <p>Quản lý phương thức thanh toán</p>
                            <a href="payment.jsp">Thanh toán</a>
                        </div>
                        <div class="dashboard-card">
                            <h4>⭐ Đánh giá dịch vụ</h4>
                            <p>Đánh giá và phản hồi dịch vụ</p>
                            <a href="rateService.jsp">Đánh giá</a>
                        </div>
                    </div>
                </div>
                <% } %>
            </section>
            <% } else { %>
            <!-- Hero Section for non-logged in users -->
            <section class="hero">
                <h1>Bạn gặp vấn đề về xe máy?</h1>
                <p>Liên hệ ngay với đội ngũ nhân viên của chúng tôi.</p>
                <a href="login.jsp">
                    <button class="btn-join">Join Us</button>
                </a>
                <div class="hero-image">
                    <img src="images/dich-vu-cuu-ho-xe-may.png" alt="Dịch vụ cứu hộ xe máy" />
                </div>
            </section>
            <% } %>

            <!-- Services Section -->
            <section class="services">
                <h2>Các dịch vụ của chúng tôi</h2>
                <div class="service-grid">
                    <div class="service-card">
                        <img src="images/keo-xe2.jpg" alt="Kéo xe" />
                        <p class="service-title">Kéo xe</p>
                        <% if ("customer".equals(userRole)) { %>
                        <p class="service-desc">Dịch vụ kéo xe chuyên nghiệp 24/7</p>
                        <a href="customer/book-service?type=keo-xe">Đặt dịch vụ</a>
                        <% } else { %>
                        <p class="service-desc">Dịch vụ này đang cập nhật vui lòng chọn dịch vụ khác</p>
                        <% } %>
                    </div>
                    <div class="service-card">
                        <img src="images/bom-vo.jpg" alt="Bơm và vá lốp xe" />
                        <p class="service-title">Bơm và vá lốp xe</p>
                        <% if ("customer".equals(userRole)) { %>
                        <p class="service-desc">Sửa chữa lốp xe tại chỗ nhanh chóng</p>
                        <a href="customer/book-service?type=bom-lop">Đặt dịch vụ</a>
                        <% } else { %>
                        <p class="service-desc">Dịch vụ này đang cập nhật vui lòng chọn dịch vụ khác</p>
                        <% } %>
                    </div>
                    <div class="service-card">
                        <img src="images/do-xang.jpg" alt="Đổ xăng" />
                        <p class="service-title">Đổ xăng</p>
                        <% if ("customer".equals(userRole)) { %>
                        <p class="service-desc">Giao xăng tận nơi khi bạn cần</p>
                        <a href="customer/book-service?type=do-xang">Đặt dịch vụ</a>
                        <% } else { %>
                        <p class="service-desc">Dịch vụ này đang cập nhật vui lòng chọn dịch vụ khác</p>
                        <% } %>
                    </div>
                </div>
            </section>

            <!-- Extra Utilities -->
            <section class="utilities">
                <h3>Ngoài ra, chúng tôi còn có nhiều tiện ích:</h3>
                <ul>
                    <li>Tìm cứu hộ gần nhất (dựa trên định vị GPS)</li>
                    <li>Hợp tác với các gara để nhanh chóng hỗ trợ</li>
                    <li>Đội ngũ kỹ thuật trực tiếp đánh giá sự cố và sửa chữa tại chỗ</li>
                    <li>Thời gian phản hồi nhanh chóng</li>
                </ul>
                <div class="buttons">
                    <% if (isLoggedIn) { %>
                    <% if ("customer".equals(userRole)) { %>
                    <a href="customer/emergency"><button>Khẩn cấp</button></a>
                    <a href="customer/find-nearby"><button class="secondary">Tìm gần nhất</button></a>
                    <% } else { %>
                    <button>Dashboard</button>
                    <button class="secondary">Báo cáo</button>
                    <% } %>
                    <% } else { %>
                    <button>Button</button>
                    <button class="secondary">Secondary button</button>
                    <% }%>
                </div>
                <img src="images/map-pin.png" alt="Map" class="map-image" />
            </section>

            <!-- Partnership -->
            <section class="partnership">
                <h3>Hợp tác với</h3>
                <div class="partner-card">
                    <img src="images/honda-head.jpg" alt="Honda Head" />
                    <p class="partner-name">Honda Head</p>
                    <p class="partner-desc">Honda là thương hiệu xe của Nhật Bản uy tín hàng đầu Việt Nam... chất lượng tốt nhất.</p>
                </div>
            </section>

            <!-- Feedback -->
            <section class="feedback">
                <h3>Phản hồi từ khách hàng</h3>
                <div class="feedback-grid">
                    <div class="feedback-card">
                        <p><em>"Dịch vụ hết nước chấm"</em></p>
                        <p class="name">Không phải Phong</p>
                    </div>
                    <div class="feedback-card">
                        <p><em>"Phản ứng cực kì nhanh"</em></p>
                        <p class="name">Bảo Long</p>
                    </div>
                    <div class="feedback-card">
                        <p><em>"Cho em xin info anh đeo kính"</em></p>
                        <p class="name">Minh Trí</p>
                    </div>
                </div>
            </section>

            <!-- Footer -->
            <footer class="footer">
                <div class="footer-content">
                    <div>
                        <h4>Liên hệ chúng tôi</h4>
                        <p>Hotline: 19001234567</p>
                        <p>Zalo: Khoa</p>
                        <p>Địa chỉ: Trường đại học FPT</p>
                        <p>Email: KhoaNT123456@gmail.com</p>
                    </div>
                    <div>
                        <p>Topic</p>
                        <p>Page</p>
                        <p>Page</p>
                        <p>Page</p>
                    </div>
                </div>
            </footer>

        </div>
    </body>
</html>