/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.model.dao;

import project.model.dto.AccountDTO;
import java.sql.*;
import project.utils.DBUtils;

public class AccountDAO {

    private static final String LOGIN = "SELECT a.* FROM Account a WHERE a.Account_id IN (SELECT account_id FROM Owner WHERE phone = ? UNION SELECT account_id FROM Manager WHERE phone = ? UNION SELECT account_id FROM Customer WHERE phone = ?) AND a.password = ?";
    private static final String CHECKDUPLICATE = "SELECT 1 FROM Account WHERE userName = ?";
    private static final String CREATEACCOUNT = "INSERT INTO Account (Account_id, userName, password, is_registered, role) VALUES (?, ?, ?, ?, ?)";
    private static final String CREATECUSTOMER = "INSERT INTO Customer (customer_id, customer_nickName, email, phone, account_id) VALUES (?, ?, ?, ?, ?)";

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
                ptm.setString(4, password);

                rs = ptm.executeQuery();
                if (rs.next()) {
                    account = new AccountDTO();
                    account.setId(rs.getString("Account_id"));
                    account.setUserName(rs.getString("userName"));
                    account.setPassword(rs.getString("password"));
                    account.setRole(rs.getString("role"));
                    account.setIsRegistered(rs.getBoolean("is_registered"));
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

    public boolean createAccount(AccountDTO account) throws Exception {
        if (account == null) {
            throw new Exception("Account object is null");
        }
        
        Connection conn = null;
        PreparedStatement ptm = null;
        boolean check = false;

        try {
            System.out.println("DEBUG DAO - Getting database connection");
            conn = DBUtils.getConnection();
            
            if (conn == null) {
                throw new Exception("Database connection is null");
            }
            
            System.out.println("DEBUG DAO - Preparing statement for account creation");
            ptm = conn.prepareStatement(CREATEACCOUNT);

            ptm.setString(1, account.getId());
            ptm.setString(2, account.getUserName());
            ptm.setString(3, account.getPassword());
            ptm.setBoolean(4, account.getIsRegistered());
            ptm.setString(5, account.getRole());
            
            System.out.println("DEBUG DAO - Executing account insert with ID: " + account.getId());
            int result = ptm.executeUpdate();
            check = result > 0;
            System.out.println("DEBUG DAO - Account insert result: " + result);
            
        } catch (Exception e) {
            System.err.println("ERROR in createAccount: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Failed to create account: " + e.getMessage(), e);
        } finally {
            closeResources(null, ptm, conn);
        }
        return check;
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

    public void createCustomer(String customerId, String nickname, String email, String phone, String accountId) throws Exception {
        if (customerId == null || accountId == null) {
            throw new Exception("Customer ID and Account ID cannot be null");
        }
        
        Connection conn = null;
        PreparedStatement ptm = null;

        try {
            System.out.println("DEBUG DAO - Creating customer with ID: " + customerId);
            conn = DBUtils.getConnection();

            if (conn == null) {
                throw new Exception("Database connection is null");
            }
            
            ptm = conn.prepareStatement(CREATECUSTOMER);
            ptm.setString(1, customerId);
            ptm.setString(2, nickname);
            ptm.setString(3, email);
            ptm.setString(4, phone);
            ptm.setString(5, accountId);
            
            int result = ptm.executeUpdate();
            System.out.println("DEBUG DAO - Customer insert result: " + result);
            
            if (result <= 0) {
                throw new Exception("Failed to insert customer record");
            }
            
        } catch (Exception e) {
            System.err.println("ERROR in createCustomer: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Failed to create customer: " + e.getMessage(), e);
        } finally {
            closeResources(null, ptm, conn);
        }
    }

    public String generateAccountId() throws Exception {
        String sql = "SELECT COUNT(*) AS total FROM Account";
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;

        try {
            System.out.println("DEBUG DAO - Generating account ID");
            conn = DBUtils.getConnection();
            
            if (conn == null) {
                throw new Exception("Database connection is null");
            }
            
            ptm = conn.prepareStatement(sql);
            rs = ptm.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt("total") + 1;
                String id = String.format("ACC%03d", count);
                System.out.println("DEBUG DAO - Generated account ID: " + id);
                return id;
            } else {
                return "ACC001";
            }
            
        } catch (Exception e) {
            System.err.println("ERROR in generateAccountId: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Failed to generate account ID: " + e.getMessage(), e);
        } finally {
            closeResources(rs, ptm, conn);
        }
    }

    public String generateCustomerId() throws Exception {
        String sql = "SELECT COUNT(*) AS total FROM Customer";
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;

        try {
            System.out.println("DEBUG DAO - Generating customer ID");
            conn = DBUtils.getConnection();
            
            if (conn == null) {
                throw new Exception("Database connection is null");
            }
            
            ptm = conn.prepareStatement(sql);
            rs = ptm.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt("total") + 1;
                String id = String.format("CUS%03d", count);
                System.out.println("DEBUG DAO - Generated customer ID: " + id);
                return id;
            } else {
                return "CUS001";
            }
            
        } catch (Exception e) {
            System.err.println("ERROR in generateCustomerId: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Failed to generate customer ID: " + e.getMessage(), e);
        } finally {
            closeResources(rs, ptm, conn);
        }
    }
    
    // Helper method to close resources
    private void closeResources(ResultSet rs, PreparedStatement ptm, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        } catch (Exception e) {
            System.err.println("ERROR closing resources: " + e.getMessage());
            e.printStackTrace();
        }
    }
}