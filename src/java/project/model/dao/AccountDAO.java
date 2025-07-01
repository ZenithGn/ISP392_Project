/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.model.dao;

import project.model.dto.AccountDTO;
import java.sql.*;
import project.utils.DBUtils;

/**
 *
 * @author KhoaLe
 */

public class AccountDAO {

    private static final String LOGIN = "SELECT a.account_id, a.username, a.password, a.isregistered, a.role FROM Account a WHERE a.account_id IN (SELECT account_id FROM Owner WHERE phone = ? UNION SELECT account_id FROM Manager WHERE phone = ? UNION SELECT account_id FROM Customer WHERE phone = ? UNION SELECT account_id FROM Employee WHERE phone = ?) AND a.password = ?";
    private static final String CHECKDUPLICATE = "SELECT 1 FROM Account WHERE username = ?";
    private static final String CREATEACCOUNT = "INSERT INTO Account (username, password, isregistered, role) VALUES (?, ?, ?, ?)";
    private static final String CREATECUSTOMER = "INSERT INTO Customer (customer_nickName, email, phone, account_id) VALUES (?, ?, ?, ?)";

    public AccountDTO checkLoginByPhone(String phone, String password) {
        AccountDTO account = null;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(LOGIN);
                ptm.setString(1, phone); 
                ptm.setString(2, phone); 
                ptm.setString(3, phone); 
                ptm.setString(4, phone); 
                ptm.setString(5, password); 

                rs = ptm.executeQuery();
                if (rs.next()) {
                    account = new AccountDTO();
                    // Convert int to String for compatibility with existing DTO
                    account.setId(String.valueOf(rs.getInt("account_id")));
                    account.setUserName(rs.getString("username"));
                    account.setPassword(rs.getString("password"));
                    account.setRole(rs.getString("role"));
                    account.setIsRegistered(rs.getBoolean("isregistered"));
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR in checkLoginByPhone: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(rs, ptm, conn);
        }
        return account;
    }

    public int createAccount(AccountDTO account) throws Exception {
        if (account == null) {
            throw new Exception("Account object is null");
        }

        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        int generatedAccountId = -1;

        try {
            System.out.println("DEBUG DAO - Getting database connection");
            conn = DBUtils.getConnection();

            if (conn == null) {
                throw new Exception("Database connection is null");
            }

            System.out.println("DEBUG DAO - Preparing statement for account creation");
            // Use RETURN_GENERATED_KEYS to get the auto-generated ID
            ptm = conn.prepareStatement(CREATEACCOUNT, Statement.RETURN_GENERATED_KEYS);

            ptm.setString(1, account.getUserName());
            ptm.setString(2, account.getPassword());
            ptm.setBoolean(3, account.getIsRegistered());
            ptm.setString(4, account.getRole());

            System.out.println("DEBUG DAO - Executing account insert");
            int result = ptm.executeUpdate();
            
            if (result > 0) {
                // Get the generated account_id
                rs = ptm.getGeneratedKeys();
                if (rs.next()) {
                    generatedAccountId = rs.getInt(1);
                    System.out.println("DEBUG DAO - Generated account ID: " + generatedAccountId);
                }
            }
            
            System.out.println("DEBUG DAO - Account insert result: " + result);

        } catch (Exception e) {
            System.err.println("ERROR in createAccount: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Failed to create account: " + e.getMessage(), e);
        } finally {
            closeResources(rs, ptm, conn);
        }
        return generatedAccountId;
    }

    public boolean checkDuplicate(String userName) throws Exception {
        if (userName == null || userName.trim().isEmpty()) {
            throw new Exception("Username cannot be null or empty");
        }

        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        boolean check = false;

        try {
            System.out.println("DEBUG DAO - Checking duplicate for username: " + userName);
            conn = DBUtils.getConnection();

            if (conn == null) {
                throw new Exception("Database connection is null");
            }

            ptm = conn.prepareStatement(CHECKDUPLICATE);
            ptm.setString(1, userName);
            rs = ptm.executeQuery();

            if (rs.next()) {
                check = true;
                System.out.println("DEBUG DAO - Username already exists");
            } else {
                System.out.println("DEBUG DAO - Username is available");
            }

        } catch (Exception e) {
            System.err.println("ERROR in checkDuplicate: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Error checking duplicate username: " + e.getMessage(), e);
        } finally {
            closeResources(rs, ptm, conn);
        }
        return check;
    }

    public boolean createCustomer(String nickname, String email, String phone, int accountId) throws Exception {
        if (accountId <= 0) {
            throw new Exception("Valid Account ID is required");
        }

        Connection conn = null;
        PreparedStatement ptm = null;
        boolean success = false;

        try {
            System.out.println("DEBUG DAO - Creating customer for account ID: " + accountId);
            conn = DBUtils.getConnection();

            if (conn == null) {
                throw new Exception("Database connection is null");
            }

            ptm = conn.prepareStatement(CREATECUSTOMER);
            ptm.setString(1, nickname);
            ptm.setString(2, email);
            ptm.setString(3, phone);
            ptm.setInt(4, accountId);

            int result = ptm.executeUpdate();
            System.out.println("DEBUG DAO - Customer insert result: " + result);

            success = result > 0;

        } catch (Exception e) {
            System.err.println("ERROR in createCustomer: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Failed to create customer: " + e.getMessage(), e);
        } finally {
            closeResources(null, ptm, conn);
        }
        return success;
    }

    // These methods are no longer needed since we're using IDENTITY columns
    // But keeping them for backward compatibility, returning dummy values
    public String generateAccountId() throws Exception {
        System.out.println("DEBUG DAO - generateAccountId called (using IDENTITY, this method is deprecated)");
        return "AUTO_GENERATED";
    }

    public String generateCustomerId() throws Exception {
        System.out.println("DEBUG DAO - generateCustomerId called (using IDENTITY, this method is deprecated)");
        return "AUTO_GENERATED";
    }

    // Helper method to close resources
    private void closeResources(ResultSet rs, PreparedStatement ptm, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            System.err.println("ERROR closing resources: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public String getCustomerIdByAccountId(String accountId) throws Exception {
        String customerId = null;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            ptm = conn.prepareStatement("SELECT customer_id FROM Customer WHERE account_id = ?");
            ptm.setString(1, accountId);
            rs = ptm.executeQuery();
            if (rs.next()) {
                customerId = rs.getString("customer_id");
            }
        } finally {
            if (rs != null) rs.close();
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }
        return customerId;
    }
}