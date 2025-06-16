/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.model.dao;

/**
 *
 * @author KhoaLe
 */
import project.model.dto.AccountDTO;
import java.sql.*;
import project.utils.DBUtils;

public class AccountDAO {

    private static final String LOGIN = "SELECT a.* FROM Account a WHERE a.Account_id IN (SELECT account_id FROM Owner WHERE phone = ? UNION SELECT account_id FROM Manager WHERE phone = ? UNION SELECT account_id FROM Customer WHERE phone =  ?) AND a.password = ?";
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

                // set phone for Owner, Manager, Customer
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
            e.printStackTrace();
        } finally {
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
                e.printStackTrace();
            }
        }
        return account;
    }

    public boolean createCustomer(AccountDTO account) throws Exception {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ptm = null;

        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(CREATEACCOUNT);

                ptm.setString(1, account.getId());
                ptm.setString(2, account.getUserName());
                ptm.setString(3, account.getPassword());
                ptm.setBoolean(4, account.getIsRegistered());
                ptm.setString(5, account.getRole());
                check = ptm.executeUpdate() > 0 ? true : false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return check;
    }

    public boolean checkDuplicate(String userName) throws SQLException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;

        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(CHECKDUPLICATE);
                ptm.setString(1, userName);
                rs = ptm.executeQuery();
                if (rs.next()) {
                    check = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return check;
    }

    public void createCustomer(String customerId, String nickname, String email, String phone, String accountId) throws Exception {
        Connection conn = null;
        PreparedStatement ptm = null;

        try {
            conn = DBUtils.getConnection();
            
            if (conn != null) {
                ptm = conn.prepareStatement(CREATECUSTOMER);
                ptm.setString(1, customerId);
                ptm.setString(2, nickname);
                ptm.setString(3, email);
                ptm.setString(4, phone);
                ptm.setString(5, accountId);
                ptm.executeUpdate();
            }
        } finally {
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    
}
