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

public class OwnerDTO {
    private String ownerId;
    private String ownerNickName;
    private String phone;
    private String email;
    private String contact;
    private String operatingHours;
    private String accountId;
    
    // Default constructor
    public OwnerDTO() {}
    
    // Constructor with all parameters
    public OwnerDTO(String ownerId, String ownerNickName, String phone, String email, 
                 String contact, String operatingHours, String accountId) {
        this.ownerId = ownerId;
        this.ownerNickName = ownerNickName;
        this.phone = phone;
        this.email = email;
        this.contact = contact;
        this.operatingHours = operatingHours;
        this.accountId = accountId;
    }
    
    // Constructor without ownerId (for new owners)
    public OwnerDTO(String ownerNickName, String phone, String email, 
                 String contact, String operatingHours, String accountId) {
        this.ownerNickName = ownerNickName;
        this.phone = phone;
        this.email = email;
        this.contact = contact;
        this.operatingHours = operatingHours;
        this.accountId = accountId;
    }
    
    // Getters and Setters
    public String getOwnerId() {
        return ownerId;
    }
    
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
    
    public String getOwnerNickName() {
        return ownerNickName;
    }
    
    public void setOwnerNickName(String ownerNickName) {
        this.ownerNickName = ownerNickName;
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
    
    public String getContact() {
        return contact;
    }
    
    public void setContact(String contact) {
        this.contact = contact;
    }
    
    public String getOperatingHours() {
        return operatingHours;
    }
    
    public void setOperatingHours(String operatingHours) {
        this.operatingHours = operatingHours;
    }
    
    public String getAccountId() {
        return accountId;
    }
    
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    
    @Override
    public String toString() {
        return "Owner{" +
                "ownerId='" + ownerId + '\'' +
                ", ownerNickName='" + ownerNickName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", contact='" + contact + '\'' +
                ", operatingHours='" + operatingHours + '\'' +
                ", accountId='" + accountId + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        OwnerDTO owner = (OwnerDTO) obj;
        return ownerId != null ? ownerId.equals(owner.ownerId) : owner.ownerId == null;
    }
    
    @Override
    public int hashCode() {
        return ownerId != null ? ownerId.hashCode() : 0;
    }
}