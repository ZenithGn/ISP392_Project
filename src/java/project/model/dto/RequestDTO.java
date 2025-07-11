/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.model.dto;

import java.sql.Timestamp;

/**
 *
 * @author KhoaLe
 */

public class RequestDTO {
    private String requestId;
    private String status;
    private String customerId;
    private Timestamp createdAt;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String serviceType;
    private String serviceName;
    private String description;
    private String location;
    private String urgency;
    private Double totalPrice;

    // Constructors
    public RequestDTO() {}

    public RequestDTO(String requestId, String status, String customerId, Timestamp createdAt) {
        this.requestId = requestId;
        this.status = status;
        this.customerId = customerId;
        this.createdAt = createdAt;
    }

    public RequestDTO(String requestId, String status, String customerId, Timestamp createdAt, String customerName, String customerPhone, String customerEmail, String serviceType, String serviceName, String description, String location, String urgency, Double totalPrice) {
        this.requestId = requestId;
        this.status = status;
        this.customerId = customerId;
        this.createdAt = createdAt;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
        this.serviceType = serviceType;
        this.serviceName = serviceName;
        this.description = description;
        this.location = location;
        this.urgency = urgency;
        this.totalPrice = totalPrice;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    

    

    
    
    
}