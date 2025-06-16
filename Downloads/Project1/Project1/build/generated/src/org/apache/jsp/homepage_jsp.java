package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

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

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE html>\n");
      out.write("<html lang=\"vi\">\n");
      out.write("    <head>\n");
      out.write("        <meta charset=\"UTF-8\" />\n");
      out.write("        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n");
      out.write("        <title>MRS - Dịch vụ cứu hộ xe máy</title>\n");
      out.write("        <link rel=\"stylesheet\" href=\"css/homepage.css\" />\n");
      out.write("    </head>\n");
      out.write("    <body>\n");
      out.write("        <div class=\"homepage\">\n");
      out.write("\n");
      out.write("            <!-- Navbar -->\n");
      out.write("            <header class=\"navbar\">\n");
      out.write("                <div class=\"logo\">MRS</div>\n");
      out.write("                <nav class=\"nav-links\">\n");
      out.write("                    <a href=\"#\">Register | Login</a>\n");
      out.write("                    <a href=\"#\">Service</a>\n");
      out.write("                    <a href=\"#\">About Us</a>\n");
      out.write("                    <a href=\"#\" class=\"active\">Home</a>\n");
      out.write("                </nav>\n");
      out.write("            </header>\n");
      out.write("\n");
      out.write("            <!-- Hero Section -->\n");
      out.write("            <section class=\"hero\">\n");
      out.write("                <h1>Bạn gặp vấn đề về xe máy?</h1>\n");
      out.write("                <p>Liên hệ ngay với đội ngũ nhân viên của chúng tôi.</p>\n");
      out.write("                <button class=\"btn-join\">Join Us</button>\n");
      out.write("                <div class=\"hero-image\">\n");
      out.write("                    <img src=\"images/dich-vu-cuu-ho-xe-may.png\" alt=\"Dịch vụ cứu hộ xe máy\" />\n");
      out.write("                </div>\n");
      out.write("            </section>\n");
      out.write("\n");
      out.write("            <!-- Services Section -->\n");
      out.write("            <section class=\"services\">\n");
      out.write("                <h2>Các dịch vụ của chúng tôi</h2>\n");
      out.write("                <div class=\"service-grid\">\n");
      out.write("                    <div class=\"service-card\">\n");
      out.write("                        <img src=\"images/keo-xe.jpg\" alt=\"Kéo xe\" />\n");
      out.write("                        <p class=\"service-title\">Kéo xe</p>\n");
      out.write("                        <p class=\"service-desc\">Dịch vụ này đang cập nhật vui lòng chọn dịch vụ khác</p>\n");
      out.write("                    </div>\n");
      out.write("                    <div class=\"service-card\">\n");
      out.write("                        <img src=\"images/bom-vo.jpg\" alt=\"Bơm và vá lốp xe\" />\n");
      out.write("                        <p class=\"service-title\">Bơm và vá lốp xe</p>\n");
      out.write("                        <p class=\"service-desc\">Dịch vụ này đang cập nhật vui lòng chọn dịch vụ khác</p>\n");
      out.write("                    </div>\n");
      out.write("                    <div class=\"service-card\">\n");
      out.write("                        <img src=\"images/do-xang.jpg\" alt=\"Đổ xăng\" />\n");
      out.write("                        <p class=\"service-title\">Đổ xăng</p>\n");
      out.write("                        <p class=\"service-desc\">Dịch vụ này đang cập nhật vui lòng chọn dịch vụ khác</p>\n");
      out.write("                    </div>\n");
      out.write("                </div>\n");
      out.write("            </section>\n");
      out.write("\n");
      out.write("            <!-- Extra Utilities -->\n");
      out.write("            <section class=\"utilities\">\n");
      out.write("                <h3>Ngoài ra, chúng tôi còn có nhiều tiện ích:</h3>\n");
      out.write("                <ul>\n");
      out.write("                    <li>Tìm cứu hộ gần nhất (dựa trên định vị GPS)</li>\n");
      out.write("                    <li>Hợp tác với các gara để nhanh chóng hỗ trợ</li>\n");
      out.write("                    <li>Đội ngũ kỹ thuật trực tiếp đánh giá sự cố và sửa chữa tại chỗ</li>\n");
      out.write("                    <li>Thời gian phản hồi nhanh chóng</li>\n");
      out.write("                </ul>\n");
      out.write("                <div class=\"buttons\">\n");
      out.write("                    <button>Button</button>\n");
      out.write("                    <button class=\"secondary\">Secondary button</button>\n");
      out.write("                </div>\n");
      out.write("                <img src=\"images/map-pin.png\" alt=\"Map\" class=\"map-image\" />\n");
      out.write("            </section>\n");
      out.write("\n");
      out.write("            <!-- Partnership -->\n");
      out.write("            <section class=\"partnership\">\n");
      out.write("                <h3>Hợp tác với</h3>\n");
      out.write("                <div class=\"partner-card\">\n");
      out.write("                    <img src=\"images/honda-head.jpg\" alt=\"Honda Head\" />\n");
      out.write("                    <p class=\"partner-name\">Honda Head</p>\n");
      out.write("                    <p class=\"partner-desc\">Honda là thương hiệu xe của Nhật Bản uy tín hàng đầu Việt Nam... chất lượng tốt nhất.</p>\n");
      out.write("                </div>\n");
      out.write("            </section>\n");
      out.write("\n");
      out.write("            <!-- Feedback -->\n");
      out.write("            <section class=\"feedback\">\n");
      out.write("                <h3>Phản hồi từ khách hàng</h3>\n");
      out.write("                <div class=\"feedback-grid\">\n");
      out.write("                    <div class=\"feedback-card\">\n");
      out.write("                        <p><em>“Dịch vụ hết nước chấm”</em></p>\n");
      out.write("                        <p class=\"name\">Không phải Phong</p>\n");
      out.write("                    </div>\n");
      out.write("                    <div class=\"feedback-card\">\n");
      out.write("                        <p><em>“Phản ứng cực kì nhanh”</em></p>\n");
      out.write("                        <p class=\"name\">Bảo Long</p>\n");
      out.write("                    </div>\n");
      out.write("                    <div class=\"feedback-card\">\n");
      out.write("                        <p><em>“Cho em xin info anh đeo kính”</em></p>\n");
      out.write("                        <p class=\"name\">Minh Trí</p>\n");
      out.write("                    </div>\n");
      out.write("                </div>\n");
      out.write("            </section>\n");
      out.write("\n");
      out.write("            <!-- Footer -->\n");
      out.write("            <footer class=\"footer\">\n");
      out.write("                <div class=\"footer-content\">\n");
      out.write("                    <div>\n");
      out.write("                        <h4>Liên hệ chúng tôi</h4>\n");
      out.write("                        <p>Hotline: 19001234567</p>\n");
      out.write("                        <p>Zalo: Khoa</p>\n");
      out.write("                        <p>Địa chỉ: Trường đại học FPT</p>\n");
      out.write("                        <p>Email: KhoaNT123456@gmail.com</p>\n");
      out.write("                    </div>\n");
      out.write("                    <div>\n");
      out.write("                        <p>Topic</p>\n");
      out.write("                        <p>Page</p>\n");
      out.write("                        <p>Page</p>\n");
      out.write("                        <p>Page</p>\n");
      out.write("                    </div>\n");
      out.write("                </div>\n");
      out.write("            </footer>\n");
      out.write("\n");
      out.write("        </div>\n");
      out.write("    </body>\n");
      out.write("</html>\n");
      out.write("\n");
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
