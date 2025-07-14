/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import project.model.dto.CustomerDTO;
import project.utils.DBUtils;

/**
 *
 * @author TUAN
 */
public class CustomerDAO {

    private static final String FIND_BY_EMAIL = "SELECT customer_id, customer_nickName, email, phone, account_id FROM Customer WHERE LOWER(LTRIM(RTRIM(email))) = ?";
    private static final String UPDATE_PASSWORD = "UPDATE Account SET password = ? WHERE account_id = (SELECT account_id FROM Customer WHERE LOWER(LTRIM(RTRIM(email))) = ?)";

    public CustomerDTO findByEmail(String email) throws Exception {
        CustomerDTO customer = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(FIND_BY_EMAIL);
            ps.setString(1, email.trim().toLowerCase());
            rs = ps.executeQuery();

            if (rs.next()) {
                customer = new CustomerDTO();
                customer.setCustomerId(rs.getString("customer_id"));
                customer.setCustomerNickName(rs.getString("customer_nickName"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                customer.setAccountId(rs.getString("account_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return customer;
    }

    public boolean updatePassword(String email, String newPassword) throws Exception {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(UPDATE_PASSWORD);
            ps.setString(1, newPassword); // Bạn nên hash mật khẩu
            ps.setString(2, email.trim().toLowerCase());
            result = ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return result;
    }
}
