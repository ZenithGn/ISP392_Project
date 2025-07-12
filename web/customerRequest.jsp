<%-- 
    Document   : customerRequest
    Created on : Jun 27, 2025, 8:02:46 AM
    Author     : Khanh
--%>

<%@page import="java.util.List"%>
<%@page import="project.model.dao.ServiceDAO"%>
<%@page import="project.model.dto.ServiceDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="project.model.dto.AccountDTO" %>
<%
    AccountDTO user = (AccountDTO) session.getAttribute("LOGIN_USER");
    if (user == null || !"customer".equals(user.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="css/customerRequest.css" />
    <title>Tạo Yêu Cầu Cứu Hộ - MRS</title>
</head>
<body>
    <div class="container">
        <div class="header">
            <!--h1-->
            <h1>Yêu Cầu Cứu Hộ</h1> 
            <p>Điền thông tin để được hỗ trợ nhanh nhất</p>
        </div>
        
        <div class="form-container">
            <% String success = (String) request.getAttribute("SUCCESS"); %>
            <% if (success != null) { %>
                <div class="alert alert-success">
                    <%= success %>
                </div>
            <% } %>
            
            <% String error = (String) request.getAttribute("ERROR"); %>
            <% if (error != null) { %>
                <div class="alert alert-error">
                    <%= error %>
                </div>
            <% } %>
            
            <form action="MainController" method="POST">
                
                <%
    ServiceDAO serviceDAO = new ServiceDAO();
    List<ServiceDTO> services = serviceDAO.getAllServices();
%>

                <div class="form-group">
    <label>Chọn Dịch Vụ <span class="required">*</span></label>
    <div class="service-grid">
        <% for (ServiceDTO s : services) { %>
            <div class="service-option">
                <input type="checkbox" id="service_<%= s.getServiceId() %>" 
                       name="serviceType" value="<%= s.getServiceId() %>" 
                       data-price="<%= s.getPrice() %>">
                <label for="service_<%= s.getServiceId() %>">
                    
                    <%= s.getServiceName() %><br>
                    <small><%= s.getPrice() %> đ</small>
                </label>
            </div>
        <% } %>
    </div>
</div>

<div class="total-price-card">
    <div class="price-label">
        <span class="price-icon">💰</span> Tổng tiền:
    </div>
    <div class="price-amount">
        <span id="totalPrice">0</span> đ
        <input type="hidden" name="totalPrice" id="totalPriceInput">
    </div>
</div>

<script>
    const checkboxes = document.querySelectorAll('input[name="serviceType"]');
    const totalPriceSpan = document.getElementById('totalPrice');
    const totalPriceInput = document.getElementById('totalPriceInput');

    function calculateTotal() {
        let total = 0;
        checkboxes.forEach(cb => {
            if (cb.checked) {
                total += parseFloat(cb.dataset.price);
            }
        });
        totalPriceSpan.textContent = total.toFixed(2);
        totalPriceInput.value = total.toFixed(2); // Gán giá trị cho input hidden
    }

    checkboxes.forEach(cb => {
        cb.addEventListener('change', calculateTotal);
    });

    // Gọi hàm một lần để cập nhật khi trang load lại
    calculateTotal();
</script>

                
                <div class="form-group">
                    <label for="location">Vị Trí Hiện Tại <span class="required">*</span></label>
                    <input type="text" id="location" name="location" 
                           placeholder="Nhập địa chỉ cụ thể..." required>
                </div>
                
                <div class="form-group">
                    <label for="description">Mô Tả Sự Cố</label>
                    <textarea id="description" name="description" 
                              placeholder="Mô tả chi tiết tình trạng xe của bạn..."></textarea>
                </div>
                
                <div class="form-group">
    <label>Mức Độ Khẩn Cấp <span class="required">*</span></label>
    <div class="urgency-options">
        <div class="urgency-option">
            <input type="radio" id="low" name="urgency" value="low" required>
            <label for="low">
                <span class="icon">🕐</span>
                Bình thường
            </label>
        </div>
        <div class="urgency-option">
            <input type="radio" id="medium" name="urgency" value="medium" required>
            <label for="medium">
                <span class="icon">⚠️</span>
                Khẩn cấp
            </label>
        </div>
        <div class="urgency-option">
            <input type="radio" id="high" name="urgency" value="high" required>
            <label for="high">
                <span class="icon">🚨</span>
                Rất khẩn cấp
            </label>
        </div>
    </div>
</div>
                
                <div class="btn-container">
                    <a href="homepage.jsp" >
                    
                
                    <button type="button" class="btn btn-secondary" onclick="history.back()">
                        Quay lại
                    </button>
                    </a>
                    <button type="submit" name="action" value="CustomerRequest" class="btn btn-primary">
                        Gửi Yêu Cầu
                    </button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>