package project.filter;

import project.model.dto.AccountDTO;

/**
 *
 * Author KhoaLe
 */

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {

    // Pages that don't require authentication
    private static final List<String> PUBLIC_PAGES = Arrays.asList(
            "/homepage.jsp",
            "/MainController",
            "/LoginController",  
            "/login.jsp",
            "/register.jsp",
            "/css/",
            "/js/",
            "/images/",
            "/"  // Nếu cần add thêm
    );

    // Role-based access control
    private static final List<String> OWNER_PAGES = Arrays.asList(
            "/owner/"
    );

    private static final List<String> MANAGER_PAGES = Arrays.asList(
            "/manager/"
    );

    private static final List<String> CUSTOMER_PAGES = Arrays.asList(
            "/customer/"
    );

    private static final List<String> EMPLOYEE_PAGES = Arrays.asList(
            "/employee/"
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = requestURI.substring(contextPath.length());

        // Default tới Homepage
        if (path.isEmpty()) {
            path = "/";
        }

        // Check xem trang JSP có public không
        if (isPublicPage(path)) {
            chain.doFilter(request, response);
            return;
        }

        // Check authentication
        HttpSession session = httpRequest.getSession(false);
        AccountDTO loginUser = null;

        if (session != null) {
            // Try both attribute names for compatibility
            loginUser = (AccountDTO) session.getAttribute("LOGIN_USER");
            if (loginUser == null) {
                loginUser = (AccountDTO) session.getAttribute("account");
            }
        }

        if (loginUser == null) {
            // User not logged in, redirect to login page
            httpResponse.sendRedirect(contextPath + "/login.jsp");
            return;
        }

        // Check role-based access
        if (!hasRoleAccess(loginUser.getRole(), path)) {
            // User doesn't have permission to access this page
            httpRequest.setAttribute("ERROR_MESSAGE", "You don't have permission to access this page");
            httpRequest.getRequestDispatcher("/homepage.jsp").forward(request, response);
            return;
        }

        // User is authenticated and has permission, continue
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup code if needed
    }

    private boolean isPublicPage(String path) {
        return PUBLIC_PAGES.stream().anyMatch(publicPage ->
                path.equals(publicPage) || path.startsWith(publicPage)
        );
    }

    private boolean hasRoleAccess(String userRole, String path) {
        if (userRole == null) {
            return false;
        }

        String role = userRole.toLowerCase();

        // Check owner access
        if (OWNER_PAGES.stream().anyMatch(path::startsWith)) {
            return "owner".equals(role);
        }

        // Check manager access
        if (MANAGER_PAGES.stream().anyMatch(path::startsWith)) {
            return "manager".equals(role) || "owner".equals(role); // Owner can access manager pages
        }

        // Check customer access
        if (CUSTOMER_PAGES.stream().anyMatch(path::startsWith)) {
            return "customer".equals(role);
        }

        // Check employee access
        if (EMPLOYEE_PAGES.stream().anyMatch(path::startsWith)) {
            return "employee".equals(role) || "manager".equals(role) || "owner".equals(role);
        }

        // For general pages, all authenticated users have access
        return true;
    }
}