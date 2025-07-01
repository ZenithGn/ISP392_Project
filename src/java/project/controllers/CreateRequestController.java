package project.controllers;

import project.model.dao.RequestDAO;
import project.model.dto.RequestDTO;
import project.model.dto.AccountDTO;
import project.model.dto.CustomerDTO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "CreateRequestController", urlPatterns = {"/CreateRequestController"})
public class CreateRequestController extends HttpServlet {

    private static final String ERROR = "customerRequest.jsp";
    private static final String SUCCESS = "requestWaiting.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR;
        try {
            HttpSession session = request.getSession();
            AccountDTO loginUser = (AccountDTO) session.getAttribute("LOGIN_USER");
            
            if (loginUser == null || !"customer".equals(loginUser.getRole())) {
                request.setAttribute("ERROR", "Bạn cần đăng nhập với tài khoản khách hàng!");
                return;
            }

            String[] serviceType = request.getParameterValues("serviceType");
            String description = request.getParameter("description");
            String location = request.getParameter("location");
            String urgency = request.getParameter("urgency");
            String totalPriceStr = request.getParameter("totalPrice");

            if (serviceType != null && serviceType.length > 0) {
                RequestDAO dao = new RequestDAO();
                
                // Generate new request ID
                String requestId = dao.generateRequestId();
                
                // Get customer ID from session (you'll need to store this when customer logs in)
                String customerId = (String) session.getAttribute("CUSTOMER_ID");
                 String joinedServiceIds = String.join(",", serviceType);
                RequestDTO newRequest = new RequestDTO();
                newRequest.setRequestId(requestId);
                newRequest.setStatus("pending");
                newRequest.setCustomerId(customerId);
                newRequest.setServiceType(joinedServiceIds);
                newRequest.setDescription(description);
                newRequest.setLocation(location);
                newRequest.setUrgency(urgency);
               
                 if (totalPriceStr != null && !totalPriceStr.isEmpty()) {
                try {
                    double totalPrice = Double.parseDouble(totalPriceStr);
                    newRequest.setTotalPrice(totalPrice);
                } catch (NumberFormatException e) {
                    // Có thể log hoặc bỏ qua
                }
            }

                boolean check = dao.createRequest(newRequest);
                if (check) {
    // Thêm từng dòng vào RequestDetail
    for (String serviceIdStr : serviceType) {
        int serviceId = Integer.parseInt(serviceIdStr);
        boolean added = dao.insertRequestDetail(requestId, serviceId,null, description); // chưa có nhân viên
        if (!added) {
            request.setAttribute("ERROR", "Tạo chi tiết yêu cầu thất bại!");
            break; // hoặc có thể rollback nếu muốn an toàn hơn
        }
    }

    
    session.setAttribute("JUST_CREATED_REQUEST", true);
                    session.setAttribute("JUST_CREATED_CUSTOMER_ID", customerId);
                    response.sendRedirect(SUCCESS); // ✅ redirect thay vì forward
                    return;
                } else {
                    request.setAttribute("ERROR", "Tạo yêu cầu thất bại.");
                }
            } else {
                request.setAttribute("ERROR", "Vui lòng chọn loại dịch vụ!");
            }
        } catch (Exception e) {
            log("Error at CreateRequestController: " + e.toString());
            request.setAttribute("ERROR", "Có lỗi xảy ra: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}