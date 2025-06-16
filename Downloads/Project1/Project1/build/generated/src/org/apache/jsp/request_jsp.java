package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class request_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      out.write("        <link rel=\"stylesheet\" type=\"text/css\" href=\"css/request.css\">\n");
      out.write("    </head>\n");
      out.write("   <body>\n");
      out.write("    <div class=\"request-form\">\n");
      out.write("        <h2>Request information</h2>\n");
      out.write("\n");
      out.write("        <!-- FORM BẮT ĐẦU -->\n");
      out.write("        <form action=\"requestWaiting.jsp\" method=\"post\">\n");
      out.write("            <div class=\"form-group\">\n");
      out.write("                <label for=\"service\">Service</label>\n");
      out.write("                <select id=\"service\" name=\"service\" required>\n");
      out.write("                    <option value=\"\">Select service</option>\n");
      out.write("                    <option value=\"tire patching\">Vá xe tại chỗ</option>\n");
      out.write("                    <option value=\"Tire replacement\">Thay lốp</option>\n");
      out.write("                    <option value=\"Towing service\">Cứu hộ kéo xe</option>\n");
      out.write("                    <option value=\"Battery jump-start\">Nạp bình ắc quy</option>\n");
      out.write("                    <option value=\"Spark plug / filter / oil replacement\">Thay bugi / lọc gió / dầu nhớt</option>\n");
      out.write("                    <option value=\"Other\">Dịch vụ khác</option>\n");
      out.write("                    <div class=\"form-group\" id=\"other-service-box\">\n");
      out.write("    <label for=\"otherService\">Nhập dịch vụ khác</label>\n");
      out.write("    <textarea id=\"otherService\" name=\"otherService\" placeholder=\"Vui lòng ghi rõ dịch vụ...\"></textarea>\n");
      out.write("</div>\n");
      out.write("                </select>\n");
      out.write("            </div>\n");
      out.write("\n");
      out.write("            <div class=\"form-group\">\n");
      out.write("                <label for=\"location\">Enter location (if GPS enable please ignore)</label>\n");
      out.write("                <textarea id=\"location\" name=\"location\" placeholder=\"Your location...\"></textarea>\n");
      out.write("            </div>\n");
      out.write("\n");
      out.write("            <div class=\"form-group\">\n");
      out.write("                <label for=\"note\">Request note</label>\n");
      out.write("                <textarea id=\"note\" name=\"note\" placeholder=\"Your note...\"></textarea>\n");
      out.write("            </div>\n");
      out.write("\n");
      out.write("            <div class=\"terms-box\">\n");
      out.write("                <input type=\"checkbox\" id=\"terms\" name=\"terms\" required>\n");
      out.write("                <label for=\"terms\">Tôi đồng ý với điều khoản</label>\n");
      out.write("            </div>\n");
      out.write("\n");
      out.write("            <button type=\"submit\">Send request information</button>\n");
      out.write("        </form>\n");
      out.write("        <!-- FORM KẾT THÚC -->\n");
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
