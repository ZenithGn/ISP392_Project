package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class menu_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      out.write("<html>\n");
      out.write("    <head>\n");
      out.write("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
      out.write("        <title>JSP Page</title>\n");
      out.write("        <link rel=\"stylesheet\" type=\"text/css\" href=\"css/menu.css\">\n");
      out.write("    </head>\n");
      out.write("    <body>\n");
      out.write("    <div class=\"menu-container\">\n");
      out.write("        <h1>DANH SÁCH DỊCH VỤ CỨU HỘ XE MÁY</h1>\n");
      out.write("\n");
      out.write("        <table class=\"service-table\">\n");
      out.write("            <thead>\n");
      out.write("                <tr>\n");
      out.write("                    <th>Dịch vụ</th>\n");
      out.write("                    <th>Mô tả</th>\n");
      out.write("                    <th>Giá tham khảo (VNĐ)</th>\n");
      out.write("                </tr>\n");
      out.write("            </thead>\n");
      out.write("            <tbody>\n");
      out.write("                <tr>\n");
      out.write("                    <td>Vá xe tại chỗ</td>\n");
      out.write("                    <td>Vá lốp xe máy tận nơi</td>\n");
      out.write("                    <td>30.000 - 50.000</td>\n");
      out.write("                </tr>\n");
      out.write("                <tr>\n");
      out.write("                    <td>Thay lốp</td>\n");
      out.write("                    <td>Thay lốp xe mới, tùy loại xe</td>\n");
      out.write("                    <td>150.000 - 350.000</td>\n");
      out.write("                </tr>\n");
      out.write("                <tr>\n");
      out.write("                    <td>Cứu hộ kéo xe</td>\n");
      out.write("                    <td>Kéo xe về garage gần nhất</td>\n");
      out.write("                    <td>300.000 - 700.000</td>\n");
      out.write("                </tr>\n");
      out.write("                <tr>\n");
      out.write("                    <td>Nạp bình ắc quy</td>\n");
      out.write("                    <td>Hỗ trợ khởi động xe khi hết bình</td>\n");
      out.write("                    <td>100.000 - 200.000</td>\n");
      out.write("                </tr>\n");
      out.write("                <tr>\n");
      out.write("                    <td>Thay bugi / lọc gió / dầu nhớt</td>\n");
      out.write("                    <td>Bảo trì các bộ phận cơ bản của xe</td>\n");
      out.write("                    <td>50.000 - 250.000</td>\n");
      out.write("                </tr>\n");
      out.write("                <tr>\n");
      out.write("                    <td>Dịch vụ khác</td>\n");
      out.write("                    <td>Liên hệ để được tư vấn cụ thể</td>\n");
      out.write("                    <td>Liên hệ</td>\n");
      out.write("                </tr>\n");
      out.write("            </tbody>\n");
      out.write("        </table>\n");
      out.write("\n");
      out.write("        <div class=\"hotline\">\n");
      out.write("            <p>📞 Hotline hỗ trợ: <strong>1900 9999</strong></p>\n");
      out.write("        </div>\n");
      out.write("    </div>\n");
      out.write("</body>\n");
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
