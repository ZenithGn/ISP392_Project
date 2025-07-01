/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.model.dto;

import java.sql.Timestamp;

/**
 *
 * @author Khanh
 */


public class RequestDetailDTO {
    private String requestId;
private int serviceId;
private String serviceName;
private String notes;
private String status;
private String location;
private String urgency;
private Timestamp createdAt;
private String customerName;

    public RequestDetailDTO() {
    }

    public RequestDetailDTO(String requestId, int serviceId, String serviceName, String notes, String status, String location, String urgency, Timestamp createdAt, String customerName) {
        this.requestId = requestId;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.notes = notes;
        this.status = status;
        this.location = location;
        this.urgency = urgency;
        this.createdAt = createdAt;
        this.customerName = customerName;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    

   
}

