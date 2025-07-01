/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.model.dto;

/**
 *
 * @author Khanh
 */
public class FeedBackDTO {
    private int feedbackId;
    private String requestId;
    private int rating;
    private String comment;
    private int serviceId;
    private String serviceName;
    private String customerId;
    private String customerName;

    public FeedBackDTO(int feedbackId, String requestId, int rating, String comment, int serviceId, String serviceName, String customerId, String customerName) {
        this.feedbackId = feedbackId;
        this.requestId = requestId;
        this.rating = rating;
        this.comment = comment;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.customerId = customerId;
        this.customerName = customerName;
    }

    public FeedBackDTO(String requestId, int rating, String comment, int serviceId, String customerId) {
        this.requestId = requestId;
        this.rating = rating;
        this.comment = comment;
        this.serviceId = serviceId;
        this.customerId = customerId;
    }
    
    

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

   
    
    
    
}
