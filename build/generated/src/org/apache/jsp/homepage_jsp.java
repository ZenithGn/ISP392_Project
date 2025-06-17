package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import project.model.dto.AccountDTO;

public final class homepage_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");

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

      out.write("\r\n");
      out.write("<!DOCTYPE html>\r\n");
      out.write("<html lang=\"vi\">\r\n");
      out.write("    <head>\r\n");
      out.write("        <meta charset=\"UTF-8\" />\r\n");
      out.write("        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\r\n");
      out.write("        <title>MRS - Dịch vụ cứu hộ xe máy</title>\r\n");
      out.write("        <link rel=\"stylesheet\" href=\"css/homepage.css\" />\r\n");
      out.write("        <style>\r\n");
      out.write("            .user-info {\r\n");
      out.write("                display: flex;\r\n");
      out.write("                align-items: center;\r\n");
      out.write("                gap: 10px;\r\n");
      out.write("            }\r\n");
      out.write("            .role-badge {\r\n");
      out.write("                padding: 4px 8px;\r\n");
      out.write("                border-radius: 12px;\r\n");
      out.write("                font-size: 12px;\r\n");
      out.write("                font-weight: bold;\r\n");
      out.write("                text-transform: uppercase;\r\n");
      out.write("            }\r\n");
      out.write("            .role-owner { background-color: #ff6b6b; color: white; }\r\n");
      out.write("            .role-manager { background-color: #4ecdc4; color: white; }\r\n");
      out.write("            .role-employee { background-color: #45b7d1; color: white; }\r\n");
      out.write("            .role-customer { background-color: #96ceb4; color: white; }\r\n");
      out.write("            .logout-btn {\r\n");
      out.write("                background-color: #ff4757;\r\n");
      out.write("                color: white;\r\n");
      out.write("                border: none;\r\n");
      out.write("                padding: 8px 15px;\r\n");
      out.write("                border-radius: 4px;\r\n");
      out.write("                cursor: pointer;\r\n");
      out.write("                text-decoration: none;\r\n");
      out.write("                font-size: 14px;\r\n");
      out.write("            }\r\n");
      out.write("            .logout-btn:hover {\r\n");
      out.write("                background-color: #ff3742;\r\n");
      out.write("            }\r\n");
      out.write("            .role-specific-content {\r\n");
      out.write("                margin: 20px 0;\r\n");
      out.write("                padding: 20px;\r\n");
      out.write("                border-radius: 8px;\r\n");
      out.write("                background-color: #f8f9fa;\r\n");
      out.write("            }\r\n");
      out.write("            .admin-panel {\r\n");
      out.write("                background-color: #e3f2fd;\r\n");
      out.write("                border-left: 4px solid #2196f3;\r\n");
      out.write("            }\r\n");
      out.write("            .manager-panel {\r\n");
      out.write("                background-color: #f3e5f5;\r\n");
      out.write("                border-left: 4px solid #9c27b0;\r\n");
      out.write("            }\r\n");
      out.write("            .employee-panel {\r\n");
      out.write("                background-color: #e8f5e8;\r\n");
      out.write("                border-left: 4px solid #4caf50;\r\n");
      out.write("            }\r\n");
      out.write("            .customer-panel {\r\n");
      out.write("                background-color: #fff3e0;\r\n");
      out.write("                border-left: 4px solid #ff9800;\r\n");
      out.write("            }\r\n");
      out.write("            .dashboard-grid {\r\n");
      out.write("                display: grid;\r\n");
      out.write("                grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));\r\n");
      out.write("                gap: 15px;\r\n");
      out.write("                margin-top: 15px;\r\n");
      out.write("            }\r\n");
      out.write("            .dashboard-card {\r\n");
      out.write("                background: white;\r\n");
      out.write("                padding: 15px;\r\n");
      out.write("                border-radius: 8px;\r\n");
      out.write("                box-shadow: 0 2px 4px rgba(0,0,0,0.1);\r\n");
      out.write("                text-align: center;\r\n");
      out.write("            }\r\n");
      out.write("            .dashboard-card h4 {\r\n");
      out.write("                margin: 0 0 10px 0;\r\n");
      out.write("                color: #333;\r\n");
      out.write("            }\r\n");
      out.write("            .dashboard-card a {\r\n");
      out.write("                text-decoration: none;\r\n");
      out.write("                color: #007bff;\r\n");
      out.write("                font-weight: bold;\r\n");
      out.write("            }\r\n");
      out.write("            .dashboard-card a:hover {\r\n");
      out.write("                color: #0056b3;\r\n");
      out.write("            }\r\n");
      out.write("        </style>\r\n");
      out.write("    </head>\r\n");
      out.write("    <body>\r\n");
      out.write("        <div class=\"homepage\">\r\n");
      out.write("            <!-- Navbar -->\r\n");
      out.write("            <header class=\"navbar\">\r\n");
      out.write("                <div class=\"logo\">MRS</div>\r\n");
      out.write("                <nav class=\"nav-links\">\r\n");
      out.write("                    ");
 if (isLoggedIn) { 
      out.write("\r\n");
      out.write("                        <div class=\"user-info\">\r\n");
      out.write("                            <span>Xin chào, ");
      out.print( userName );
      out.write("!</span>\r\n");
      out.write("                            <span class=\"role-badge role-");
      out.print( userRole );
      out.write('"');
      out.write('>');
      out.print( userRole );
      out.write("</span>\r\n");
      out.write("                            <a href=\"LogoutController\" class=\"logout-btn\">Đăng xuất</a>\r\n");
      out.write("                        </div>\r\n");
      out.write("                    ");
 } else { 
      out.write("\r\n");
      out.write("                        <a href=\"login.jsp\">Login</a>\r\n");
      out.write("                    ");
 } 
      out.write("\r\n");
      out.write("                    <a href=\"menu.jsp\">Service</a>\r\n");
      out.write("                    <a href=\"#\">About Us</a>\r\n");
      out.write("                    <a href=\"#\" class=\"active\">Home</a>\r\n");
      out.write("                </nav>\r\n");
      out.write("            </header>\r\n");
      out.write("\r\n");
      out.write("            <!-- Role-specific Dashboard -->\r\n");
      out.write("            ");
 if (isLoggedIn) { 
      out.write("\r\n");
      out.write("                <section class=\"role-specific-content\">\r\n");
      out.write("                    ");
 if ("owner".equals(userRole)) { 
      out.write("\r\n");
      out.write("                        <div class=\"admin-panel\">\r\n");
      out.write("                            <h2>🏢 Owner Dashboard</h2>\r\n");
      out.write("                            <p>Chào mừng chủ sở hữu! Bạn có quyền truy cập toàn bộ hệ thống.</p>\r\n");
      out.write("                            <div class=\"dashboard-grid\">\r\n");
      out.write("                                <div class=\"dashboard-card\">\r\n");
      out.write("                                    <h4>👥 Quản lý nhân viên</h4>\r\n");
      out.write("                                    <p>Xem và quản lý tất cả nhân viên</p>\r\n");
      out.write("                                    <a href=\"owner/employees\">Xem chi tiết</a>\r\n");
      out.write("                                </div>\r\n");
      out.write("                                <div class=\"dashboard-card\">\r\n");
      out.write("                                    <h4>📊 Báo cáo doanh thu</h4>\r\n");
      out.write("                                    <p>Xem báo cáo tài chính tổng thể</p>\r\n");
      out.write("                                    <a href=\"owner/reports\">Xem báo cáo</a>\r\n");
      out.write("                                </div>\r\n");
      out.write("                                <div class=\"dashboard-card\">\r\n");
      out.write("                                    <h4>⚙️ Cài đặt hệ thống</h4>\r\n");
      out.write("                                    <p>Cấu hình hệ thống và dịch vụ</p>\r\n");
      out.write("                                    <a href=\"owner/settings\">Cài đặt</a>\r\n");
      out.write("                                </div>\r\n");
      out.write("                                <div class=\"dashboard-card\">\r\n");
      out.write("                                    <h4>🏪 Quản lý chi nhánh</h4>\r\n");
      out.write("                                    <p>Quản lý các chi nhánh và khu vực</p>\r\n");
      out.write("                                    <a href=\"owner/branches\">Quản lý</a>\r\n");
      out.write("                                </div>\r\n");
      out.write("                            </div>\r\n");
      out.write("                        </div>\r\n");
      out.write("                    ");
 } else if ("manager".equals(userRole)) { 
      out.write("\r\n");
      out.write("                        <div class=\"manager-panel\">\r\n");
      out.write("                            <h2>👔 Manager Dashboard</h2>\r\n");
      out.write("                            <p>Chào mừng quản lý! Quản lý các hoạt động hàng ngày của chi nhánh.</p>\r\n");
      out.write("                            <div class=\"dashboard-grid\">\r\n");
      out.write("                                <div class=\"dashboard-card\">\r\n");
      out.write("                                    <h4>📋 Quản lý đơn hàng</h4>\r\n");
      out.write("                                    <p>Theo dõi và xử lý các yêu cầu cứu hộ</p>\r\n");
      out.write("                                    <a href=\"manager/orders\">Xem đơn hàng</a>\r\n");
      out.write("                                </div>\r\n");
      out.write("                                <div class=\"dashboard-card\">\r\n");
      out.write("                                    <h4>👷 Quản lý nhân viên</h4>\r\n");
      out.write("                                    <p>Phân công và theo dõi nhân viên</p>\r\n");
      out.write("                                    <a href=\"manager/staff\">Quản lý nhân viên</a>\r\n");
      out.write("                                </div>\r\n");
      out.write("                                <div class=\"dashboard-card\">\r\n");
      out.write("                                    <h4>📈 Báo cáo chi nhánh</h4>\r\n");
      out.write("                                    <p>Xem hiệu suất chi nhánh</p>\r\n");
      out.write("                                    <a href=\"manager/branch-reports\">Xem báo cáo</a>\r\n");
      out.write("                                </div>\r\n");
      out.write("                                <div class=\"dashboard-card\">\r\n");
      out.write("                                    <h4>📞 Hỗ trợ khách hàng</h4>\r\n");
      out.write("                                    <p>Xử lý khiếu nại và phản hồi</p>\r\n");
      out.write("                                    <a href=\"manager/support\">Hỗ trợ</a>\r\n");
      out.write("                                </div>\r\n");
      out.write("                            </div>\r\n");
      out.write("                        </div>\r\n");
      out.write("                    ");
 } else if ("employee".equals(userRole)) { 
      out.write("\r\n");
      out.write("                        <div class=\"employee-panel\">\r\n");
      out.write("                            <h2>🔧 Employee Dashboard</h2>\r\n");
      out.write("                            <p>Chào mừng nhân viên! Xem các nhiệm vụ được giao và cập nhật trạng thái.</p>\r\n");
      out.write("                            <div class=\"dashboard-grid\">\r\n");
      out.write("                                <div class=\"dashboard-card\">\r\n");
      out.write("                                    <h4>📝 Nhiệm vụ của tôi</h4>\r\n");
      out.write("                                    <p>Xem các yêu cầu cứu hộ được giao</p>\r\n");
      out.write("                                    <a href=\"employee/my-tasks\">Xem nhiệm vụ</a>\r\n");
      out.write("                                </div>\r\n");
      out.write("                                <div class=\"dashboard-card\">\r\n");
      out.write("                                    <h4>📍 Cập nhật vị trí</h4>\r\n");
      out.write("                                    <p>Cập nhật vị trí hiện tại của bạn</p>\r\n");
      out.write("                                    <a href=\"employee/location\">Cập nhật vị trí</a>\r\n");
      out.write("                                </div>\r\n");
      out.write("                                <div class=\"dashboard-card\">\r\n");
      out.write("                                    <h4>⏰ Chấm công</h4>\r\n");
      out.write("                                    <p>Ghi nhận giờ làm việc</p>\r\n");
      out.write("                                    <a href=\"employee/timesheet\">Chấm công</a>\r\n");
      out.write("                                </div>\r\n");
      out.write("                                <div class=\"dashboard-card\">\r\n");
      out.write("                                    <h4>📊 Hiệu suất của tôi</h4>\r\n");
      out.write("                                    <p>Xem thống kê công việc</p>\r\n");
      out.write("                                    <a href=\"employee/performance\">Xem hiệu suất</a>\r\n");
      out.write("                                </div>\r\n");
      out.write("                            </div>\r\n");
      out.write("                        </div>\r\n");
      out.write("                    ");
 } else if ("customer".equals(userRole)) { 
      out.write("\r\n");
      out.write("                        <div class=\"customer-panel\">\r\n");
      out.write("                            <h2>🛵 Customer Dashboard</h2>\r\n");
      out.write("                            <p>Chào mừng khách hàng! Đặt dịch vụ cứu hộ và theo dõi trạng thái.</p>\r\n");
      out.write("                            <div class=\"dashboard-grid\">\r\n");
      out.write("                                <div class=\"dashboard-card\">\r\n");
      out.write("                                    <h4>🆘 Yêu cầu cứu hộ</h4>\r\n");
      out.write("                                    <p>Đặt dịch vụ cứu hộ khẩn cấp</p>\r\n");
      out.write("                                    <a href=\"request.jsp\">Yêu cầu ngay</a>\r\n");
      out.write("                                </div>\r\n");
      out.write("                                <div class=\"dashboard-card\">\r\n");
      out.write("                                    <h4>📋 Đơn hàng của tôi</h4>\r\n");
      out.write("                                    <p>Theo dõi trạng thái các yêu cầu</p>\r\n");
      out.write("                                    <a href=\"requestWaiting.jsp\">Xem đơn hàng</a>\r\n");
      out.write("                                </div>\r\n");
      out.write("                                <div class=\"dashboard-card\">\r\n");
      out.write("                                    <h4>💳 Thanh toán</h4>\r\n");
      out.write("                                    <p>Quản lý phương thức thanh toán</p>\r\n");
      out.write("                                    <a href=\"customer/payment\">Thanh toán</a>\r\n");
      out.write("                                </div>\r\n");
      out.write("                                <div class=\"dashboard-card\">\r\n");
      out.write("                                    <h4>⭐ Đánh giá dịch vụ</h4>\r\n");
      out.write("                                    <p>Đánh giá và phản hồi dịch vụ</p>\r\n");
      out.write("                                    <a href=\"customer/reviews\">Đánh giá</a>\r\n");
      out.write("                                </div>\r\n");
      out.write("                            </div>\r\n");
      out.write("                        </div>\r\n");
      out.write("                    ");
 } 
      out.write("\r\n");
      out.write("                </section>\r\n");
      out.write("            ");
 } else { 
      out.write("\r\n");
      out.write("                <!-- Hero Section for non-logged in users -->\r\n");
      out.write("                <section class=\"hero\">\r\n");
      out.write("                    <h1>Bạn gặp vấn đề về xe máy?</h1>\r\n");
      out.write("                    <p>Liên hệ ngay với đội ngũ nhân viên của chúng tôi.</p>\r\n");
      out.write("                    <a href=\"login.jsp\">\r\n");
      out.write("                        <button class=\"btn-join\">Join Us</button>\r\n");
      out.write("                    </a>\r\n");
      out.write("                    <div class=\"hero-image\">\r\n");
      out.write("                        <img src=\"images/dich-vu-cuu-ho-xe-may.png\" alt=\"Dịch vụ cứu hộ xe máy\" />\r\n");
      out.write("                    </div>\r\n");
      out.write("                </section>\r\n");
      out.write("            ");
 } 
      out.write("\r\n");
      out.write("\r\n");
      out.write("            <!-- Services Section -->\r\n");
      out.write("            <section class=\"services\">\r\n");
      out.write("                <h2>Các dịch vụ của chúng tôi</h2>\r\n");
      out.write("                <div class=\"service-grid\">\r\n");
      out.write("                    <div class=\"service-card\">\r\n");
      out.write("                        <img src=\"images/keo-xe.jpg\" alt=\"Kéo xe\" />\r\n");
      out.write("                        <p class=\"service-title\">Kéo xe</p>\r\n");
      out.write("                        ");
 if ("customer".equals(userRole)) { 
      out.write("\r\n");
      out.write("                            <p class=\"service-desc\">Dịch vụ kéo xe chuyên nghiệp 24/7</p>\r\n");
      out.write("                            <a href=\"customer/book-service?type=keo-xe\">Đặt dịch vụ</a>\r\n");
      out.write("                        ");
 } else { 
      out.write("\r\n");
      out.write("                            <p class=\"service-desc\">Dịch vụ này đang cập nhật vui lòng chọn dịch vụ khác</p>\r\n");
      out.write("                        ");
 } 
      out.write("\r\n");
      out.write("                    </div>\r\n");
      out.write("                    <div class=\"service-card\">\r\n");
      out.write("                        <img src=\"images/bom-vo.jpg\" alt=\"Bơm và vá lốp xe\" />\r\n");
      out.write("                        <p class=\"service-title\">Bơm và vá lốp xe</p>\r\n");
      out.write("                        ");
 if ("customer".equals(userRole)) { 
      out.write("\r\n");
      out.write("                            <p class=\"service-desc\">Sửa chữa lốp xe tại chỗ nhanh chóng</p>\r\n");
      out.write("                            <a href=\"customer/book-service?type=bom-lop\">Đặt dịch vụ</a>\r\n");
      out.write("                        ");
 } else { 
      out.write("\r\n");
      out.write("                            <p class=\"service-desc\">Dịch vụ này đang cập nhật vui lòng chọn dịch vụ khác</p>\r\n");
      out.write("                        ");
 } 
      out.write("\r\n");
      out.write("                    </div>\r\n");
      out.write("                    <div class=\"service-card\">\r\n");
      out.write("                        <img src=\"images/do-xang.jpg\" alt=\"Đổ xăng\" />\r\n");
      out.write("                        <p class=\"service-title\">Đổ xăng</p>\r\n");
      out.write("                        ");
 if ("customer".equals(userRole)) { 
      out.write("\r\n");
      out.write("                            <p class=\"service-desc\">Giao xăng tận nơi khi bạn cần</p>\r\n");
      out.write("                            <a href=\"customer/book-service?type=do-xang\">Đặt dịch vụ</a>\r\n");
      out.write("                        ");
 } else { 
      out.write("\r\n");
      out.write("                            <p class=\"service-desc\">Dịch vụ này đang cập nhật vui lòng chọn dịch vụ khác</p>\r\n");
      out.write("                        ");
 } 
      out.write("\r\n");
      out.write("                    </div>\r\n");
      out.write("                </div>\r\n");
      out.write("            </section>\r\n");
      out.write("\r\n");
      out.write("            <!-- Extra Utilities -->\r\n");
      out.write("            <section class=\"utilities\">\r\n");
      out.write("                <h3>Ngoài ra, chúng tôi còn có nhiều tiện ích:</h3>\r\n");
      out.write("                <ul>\r\n");
      out.write("                    <li>Tìm cứu hộ gần nhất (dựa trên định vị GPS)</li>\r\n");
      out.write("                    <li>Hợp tác với các gara để nhanh chóng hỗ trợ</li>\r\n");
      out.write("                    <li>Đội ngũ kỹ thuật trực tiếp đánh giá sự cố và sửa chữa tại chỗ</li>\r\n");
      out.write("                    <li>Thời gian phản hồi nhanh chóng</li>\r\n");
      out.write("                </ul>\r\n");
      out.write("                <div class=\"buttons\">\r\n");
      out.write("                    ");
 if (isLoggedIn) { 
      out.write("\r\n");
      out.write("                        ");
 if ("customer".equals(userRole)) { 
      out.write("\r\n");
      out.write("                            <a href=\"customer/emergency\"><button>Khẩn cấp</button></a>\r\n");
      out.write("                            <a href=\"customer/find-nearby\"><button class=\"secondary\">Tìm gần nhất</button></a>\r\n");
      out.write("                        ");
 } else { 
      out.write("\r\n");
      out.write("                            <button>Dashboard</button>\r\n");
      out.write("                            <button class=\"secondary\">Báo cáo</button>\r\n");
      out.write("                        ");
 } 
      out.write("\r\n");
      out.write("                    ");
 } else { 
      out.write("\r\n");
      out.write("                        <button>Button</button>\r\n");
      out.write("                        <button class=\"secondary\">Secondary button</button>\r\n");
      out.write("                    ");
 } 
      out.write("\r\n");
      out.write("                </div>\r\n");
      out.write("                <img src=\"images/map-pin.png\" alt=\"Map\" class=\"map-image\" />\r\n");
      out.write("            </section>\r\n");
      out.write("\r\n");
      out.write("            <!-- Partnership -->\r\n");
      out.write("            <section class=\"partnership\">\r\n");
      out.write("                <h3>Hợp tác với</h3>\r\n");
      out.write("                <div class=\"partner-card\">\r\n");
      out.write("                    <img src=\"images/honda-head.jpg\" alt=\"Honda Head\" />\r\n");
      out.write("                    <p class=\"partner-name\">Honda Head</p>\r\n");
      out.write("                    <p class=\"partner-desc\">Honda là thương hiệu xe của Nhật Bản uy tín hàng đầu Việt Nam... chất lượng tốt nhất.</p>\r\n");
      out.write("                </div>\r\n");
      out.write("            </section>\r\n");
      out.write("\r\n");
      out.write("            <!-- Feedback -->\r\n");
      out.write("            <section class=\"feedback\">\r\n");
      out.write("                <h3>Phản hồi từ khách hàng</h3>\r\n");
      out.write("                <div class=\"feedback-grid\">\r\n");
      out.write("                    <div class=\"feedback-card\">\r\n");
      out.write("                        <p><em>\"Dịch vụ hết nước chấm\"</em></p>\r\n");
      out.write("                        <p class=\"name\">Không phải Phong</p>\r\n");
      out.write("                    </div>\r\n");
      out.write("                    <div class=\"feedback-card\">\r\n");
      out.write("                        <p><em>\"Phản ứng cực kì nhanh\"</em></p>\r\n");
      out.write("                        <p class=\"name\">Bảo Long</p>\r\n");
      out.write("                    </div>\r\n");
      out.write("                    <div class=\"feedback-card\">\r\n");
      out.write("                        <p><em>\"Cho em xin info anh đeo kính\"</em></p>\r\n");
      out.write("                        <p class=\"name\">Minh Trí</p>\r\n");
      out.write("                    </div>\r\n");
      out.write("                </div>\r\n");
      out.write("            </section>\r\n");
      out.write("\r\n");
      out.write("            <!-- Footer -->\r\n");
      out.write("            <footer class=\"footer\">\r\n");
      out.write("                <div class=\"footer-content\">\r\n");
      out.write("                    <div>\r\n");
      out.write("                        <h4>Liên hệ chúng tôi</h4>\r\n");
      out.write("                        <p>Hotline: 19001234567</p>\r\n");
      out.write("                        <p>Zalo: Khoa</p>\r\n");
      out.write("                        <p>Địa chỉ: Trường đại học FPT</p>\r\n");
      out.write("                        <p>Email: KhoaNT123456@gmail.com</p>\r\n");
      out.write("                    </div>\r\n");
      out.write("                    <div>\r\n");
      out.write("                        <p>Topic</p>\r\n");
      out.write("                        <p>Page</p>\r\n");
      out.write("                        <p>Page</p>\r\n");
      out.write("                        <p>Page</p>\r\n");
      out.write("                    </div>\r\n");
      out.write("                </div>\r\n");
      out.write("            </footer>\r\n");
      out.write("\r\n");
      out.write("        </div>\r\n");
      out.write("    </body>\r\n");
      out.write("</html>");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
