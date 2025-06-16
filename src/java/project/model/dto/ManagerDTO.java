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
public class ManagerDTO {
    private String managerId;
    private String managerNickName;
    private String phone;
    private String email;
    private String accountId;
    
    // Default constructor
    public ManagerDTO() {}
    
    // Constructor with all parameters
    public ManagerDTO(String managerId, String managerNickName, String phone, String email, String accountId) {
        this.managerId = managerId;
        this.managerNickName = managerNickName;
        this.phone = phone;
        this.email = email;
        this.accountId = accountId;
    }
    
    // Constructor without managerId (for new managers)
    public ManagerDTO(String managerNickName, String phone, String email, String accountId) {
        this.managerNickName = managerNickName;
        this.phone = phone;
        this.email = email;
        this.accountId = accountId;
    }
    
    // Getters and Setters
    public String getManagerId() {
        return managerId;
    }
    
    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }
    
    public String getManagerNickName() {
        return managerNickName;
    }
    
    public void setManagerNickName(String managerNickName) {
        this.managerNickName = managerNickName;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getAccountId() {
        return accountId;
    }
    
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    
    @Override
    public String toString() {
        return "Manager{" +
                "managerId='" + managerId + '\'' +
                ", managerNickName='" + managerNickName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", accountId='" + accountId + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ManagerDTO manager = (ManagerDTO) obj;
        return managerId != null ? managerId.equals(manager.managerId) : manager.managerId == null;
    }
    
    @Override
    public int hashCode() {
        return managerId != null ? managerId.hashCode() : 0;
    }
}