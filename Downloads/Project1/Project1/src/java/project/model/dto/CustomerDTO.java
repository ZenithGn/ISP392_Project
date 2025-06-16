/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.model.dto;

/**
 *
 * @author KhoaLe
 */

public class CustomerDTO {
    private String customerId;
    private String customerNickName;
    private String email;
    private String phone;
    private String accountId;
    
    // Default constructor
    public CustomerDTO() {}
    
    // Constructor with all parameters
    public CustomerDTO(String customerId, String customerNickName, String email, String phone, String accountId) {
        this.customerId = customerId;
        this.customerNickName = customerNickName;
        this.email = email;
        this.phone = phone;
        this.accountId = accountId;
    }
    
    // Constructor without customerId (for new customers)
    public CustomerDTO(String customerNickName, String email, String phone, String accountId) {
        this.customerNickName = customerNickName;
        this.email = email;
        this.phone = phone;
        this.accountId = accountId;
    }
    
    // Getters and Setters
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public String getCustomerNickName() {
        return customerNickName;
    }
    
    public void setCustomerNickName(String customerNickName) {
        this.customerNickName = customerNickName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getAccountId() {
        return accountId;
    }
    
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    
    @Override
    public String toString() {
        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", customerNickName='" + customerNickName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", accountId='" + accountId + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CustomerDTO customer = (CustomerDTO) obj;
        return customerId != null ? customerId.equals(customer.customerId) : customer.customerId == null;
    }
    
    @Override
    public int hashCode() {
        return customerId != null ? customerId.hashCode() : 0;
    }
}