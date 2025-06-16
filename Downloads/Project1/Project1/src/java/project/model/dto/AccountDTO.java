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
public class AccountDTO {

    private String id;           // Account_id
    private String userName;     // userName
    private String password;     // password
    private boolean isRegistered; // is_registered
    private String role;         // role

    // Default constructor
    public AccountDTO() {
    }

    // Full constructor
    public AccountDTO(String id, String userName, String password, boolean isRegistered, String role) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.isRegistered = isRegistered;
        this.role = role;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(boolean isRegistered) {
        this.isRegistered = isRegistered;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
