/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.model.dao;

import project.model.dto.AccountDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
    
    public List<AccountDTO> getAllManagers() throws Exception {
    List<AccountDTO> list = new ArrayList<>();
    Connection conn = DBUtils.getConnection();
   String sql = 
    "SELECT a.account_id, a.username, a.isregistered, a.role, " +
    "m.manager_nickName, m.phone, m.email " +  // <-- dấu cách sau dấu phẩy
    "FROM Account a " +                       // <-- dấu cách cuối mỗi dòng
    "LEFT JOIN Manager m ON a.account_id = m.account_id " +
    "WHERE a.role = 'manager'";
    

    PreparedStatement ps = conn.prepareStatement(sql);
    ResultSet rs = ps.executeQuery();

    while (rs.next()) {
        AccountDTO acc = new AccountDTO();
        acc.setId(rs.getString("account_id"));
        acc.setUserName(rs.getString("username"));
        acc.setIsRegistered(rs.getBoolean("isregistered"));
        acc.setRole(rs.getString("role"));
        acc.setNickName(rs.getString("manager_nickName")); // <-- thêm 3 dòng này
        acc.setPhone(rs.getString("phone"));
        acc.setEmail(rs.getString("email"));
        list.add(acc);
    }

    rs.close();
    ps.close();
    conn.close();
    return list;
}

    // 2. Tạo mới manager
   public boolean createManager(String username, String password, String nickname, String phone, String email) throws Exception {
    Connection conn = null;
    PreparedStatement accountStmt = null;
    PreparedStatement managerStmt = null;
    ResultSet rs = null;

    boolean success = false;

    try {
        conn = DBUtils.getConnection();
        conn.setAutoCommit(false); // Giao dịch để rollback nếu có lỗi

        // 1. Thêm vào bảng Account
        String insertAccountSQL = "INSERT INTO Account (username, password, isregistered, role) VALUES (?, ?, ?, ?)";
        accountStmt = conn.prepareStatement(insertAccountSQL, Statement.RETURN_GENERATED_KEYS);
        accountStmt.setString(1, username);
        accountStmt.setString(2, password);
        accountStmt.setBoolean(3, false); // isregistered = false khi mới tạo
        accountStmt.setString(4, "manager");
        int affected = accountStmt.executeUpdate();

        if (affected == 0) {
            throw new SQLException("Creating account failed, no rows affected.");
        }

        rs = accountStmt.getGeneratedKeys();
        int generatedAccountId = -1;
        if (rs.next()) {
            generatedAccountId = rs.getInt(1);
        } else {
            throw new SQLException("Creating account failed, no ID obtained.");
        }

        // 2. Thêm vào bảng Manager
        String insertManagerSQL = "INSERT INTO Manager (manager_nickName, phone, email, account_id) VALUES (?, ?, ?, ?)";
        managerStmt = conn.prepareStatement(insertManagerSQL);
        managerStmt.setString(1, nickname);
        managerStmt.setString(2, phone);
        managerStmt.setString(3, email);
        managerStmt.setInt(4, generatedAccountId);
        managerStmt.executeUpdate();

        conn.commit(); // Nếu cả hai câu lệnh đều thành công thì commit
        success = true;

    } catch (Exception e) {
        if (conn != null) {
            try {
                conn.rollback(); // Nếu có lỗi thì rollback toàn bộ
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        throw new Exception("Failed to create manager: " + e.getMessage(), e);
    } finally {
        if (rs != null) rs.close();
        if (accountStmt != null) accountStmt.close();
        if (managerStmt != null) managerStmt.close();
        if (conn != null) conn.close();
    }

    return success;
}


    // 3. Cập nhật manager
    public boolean updateManager(AccountDTO acc, String nickname, String phone, String email) throws Exception {
    Connection conn = null;
    PreparedStatement psAccount = null;
    PreparedStatement psManager = null;
    boolean success = false;

    try {
        conn = DBUtils.getConnection();
        conn.setAutoCommit(false); // Bắt đầu transaction

        // Cập nhật bảng Account
        String sqlAccount = "UPDATE Account SET username=?, password=?, isregistered=? WHERE account_id=?";
        psAccount = conn.prepareStatement(sqlAccount);
        psAccount.setString(1, acc.getUserName());
        psAccount.setString(2, acc.getPassword());
        psAccount.setBoolean(3, acc.getIsRegistered());
        psAccount.setString(4, acc.getId());
        psAccount.executeUpdate();

        // Cập nhật bảng Manager
        String sqlManager = "UPDATE Manager SET manager_nickName=?, phone=?, email=? WHERE account_id=?";
        psManager = conn.prepareStatement(sqlManager);
        psManager.setString(1, nickname);
        psManager.setString(2, phone);
        psManager.setString(3, email);
        psManager.setInt(4, Integer.parseInt(acc.getId()));
        psManager.executeUpdate();

        conn.commit();
        success = true;

    } catch (Exception e) {
        if (conn != null) conn.rollback(); // Nếu lỗi, rollback
        throw new Exception("Error updating manager: " + e.getMessage(), e);
    } finally {
        if (psAccount != null) psAccount.close();
        if (psManager != null) psManager.close();
        if (conn != null) conn.close();
    }

    return success;
}



   public boolean deleteManager(String accountId) throws Exception {
    Connection conn = null;
    PreparedStatement psManager = null;
    PreparedStatement psAccount = null;
    boolean success = false;

    try {
        conn = DBUtils.getConnection();
        conn.setAutoCommit(false); // transaction

        // Xoá ở bảng Manager trước
        String sqlManager = "DELETE FROM Manager WHERE account_id=?";
        psManager = conn.prepareStatement(sqlManager);
        psManager.setString(1, accountId);
        psManager.executeUpdate();

        // Sau đó xoá ở bảng Account
        String sqlAccount = "DELETE FROM Account WHERE account_id=? AND role='manager'";
        psAccount = conn.prepareStatement(sqlAccount);
        psAccount.setString(1, accountId);
        psAccount.executeUpdate();

        conn.commit();
        success = true;

    } catch (Exception e) {
        if (conn != null) conn.rollback();
        throw new Exception("Error deleting manager: " + e.getMessage(), e);
    } finally {
        if (psManager != null) psManager.close();
        if (psAccount != null) psAccount.close();
        if (conn != null) conn.close();
    }

    return success;
}
   
   public boolean createManagerInfo(String nickname, String phone, String email, int accountId) throws Exception {
    Connection conn = null;
    PreparedStatement ptm = null;
    boolean success = false;

    try {
        conn = DBUtils.getConnection();
        String sql = "INSERT INTO Manager (manager_nickName, phone, email, account_id) VALUES (?, ?, ?, ?)";
        ptm = conn.prepareStatement(sql);
        ptm.setString(1, nickname);
        ptm.setString(2, phone);
        ptm.setString(3, email);
        ptm.setInt(4, accountId);

        int result = ptm.executeUpdate();
        success = result > 0;
    } finally {
        if (ptm != null) ptm.close();
        if (conn != null) conn.close();
    }

    return success;
}
}
