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
           data-unit="<%= s.getUnitPrice() != 0 ? s.getUnitPrice() : 0 %>"
           data-base="<%= s.getBasePrice() != 0 ? s.getBasePrice() : 0 %>">

    <label for="service_<%= s.getServiceId() %>">
        <%= s.getServiceName() %><br>
        <small>Phí cố định: <%= s.getBasePrice() %> đ | Đơn giá: <%= s.getUnitPrice() %> đ</small>
    </label>

    <!-- Đặt input ra ngoài label để đảm bảo hiển thị -->
    <div class="quantity-wrapper">
    <label for="quantity_<%= s.getServiceId() %>">Số lượng:</label>
    <input type="number"
           name="quantity_<%= s.getServiceId() %>"
           id="quantity_<%= s.getServiceId() %>"
           class="quantity-input"
           value="1"
           min="1"
           data-for="service_<%= s.getServiceId() %>">
</div>
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
window.addEventListener('DOMContentLoaded', () => {
  const checkboxes = document.querySelectorAll('input[name="serviceType"]');
  const totalPriceSpan = document.getElementById('totalPrice');
  const totalPriceInput = document.getElementById('totalPriceInput');

  function calculateTotal() {
    let total = 0;

    checkboxes.forEach(cb => {
      const serviceWrapper = cb.closest('.service-option');
      const quantityWrapper = serviceWrapper.querySelector('.quantity-wrapper');
      const quantityInput = serviceWrapper.querySelector('.quantity-input');

      if (cb.checked) {
        quantityWrapper.classList.add('active');
        const base = parseFloat(cb.dataset.base) || 0;
        const unit = parseFloat(cb.dataset.unit) || 0;
        const quantity = parseInt(quantityInput.value) || 1;
        total += base + unit * (quantity - 1); // hoặc unit * quantity nếu bạn muốn
      } else {
        quantityWrapper.classList.remove('active');
      }
    });

    totalPriceSpan.textContent = total.toFixed(0);
    totalPriceInput.value = total.toFixed(0);
  }

  // Khi checkbox thay đổi
  checkboxes.forEach(cb => cb.addEventListener('change', calculateTotal));

  // Khi số lượng thay đổi, vẫn cần gọi calculateTotal
  const quantityInputs = document.querySelectorAll('.quantity-input');
  quantityInputs.forEach(inp => {
    inp.addEventListener('input', calculateTotal);
  });

  // Tính toán ban đầu
  calculateTotal();
});
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