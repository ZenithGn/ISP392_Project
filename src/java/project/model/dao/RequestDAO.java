/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.model.dao;

import project.model.dto.RequestDTO;
import project.utils.DBUtils;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import project.model.dto.RequestDetailDTO;

/**
 *
 * @author KhoaLe
 */
public class RequestDAO {

    public boolean createRequest(RequestDTO request) throws Exception {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(
   "INSERT INTO Request (request_id, status, customer_id, location, urgency, total_price, created_at) "
           + "VALUES (?, ?, ?, ?, ?, ?, GETDATE())"
                );

ptm.setString(1, request.getRequestId());
ptm.setString(2, request.getStatus());
ptm.setString(3, request.getCustomerId());
ptm.setString(4, request.getLocation());
ptm.setString(5, request.getUrgency());
ptm.setDouble(6, request.getTotalPrice());

                check = ptm.executeUpdate() > 0;
            }
        } catch (Exception e) {
        System.err.println("❌ Error when inserting request: " + e.getMessage());
        e.printStackTrace(); // In toàn bộ lỗi
        throw e; // propagate lỗi lên controller
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

    public List<RequestDTO> getAllPendingRequests() throws SQLException {
        List<RequestDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
              ptm = conn.prepareStatement(
    "SELECT r.request_id, r.status, r.customer_id, r.created_at, " +
    "r.location, r.urgency, " +
    "c.customer_nickName, c.phone, c.email " +
    "FROM Request r " +
    "JOIN Customer c ON r.customer_id = c.customer_id " +
    "WHERE r.status = 'pending' " +
    "ORDER BY r.created_at DESC"
);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    RequestDTO request = new RequestDTO();
                    request.setRequestId(rs.getString("request_id"));
                    request.setStatus(rs.getString("status"));
                    request.setCustomerId(rs.getString("customer_id"));
                    request.setCreatedAt(rs.getTimestamp("created_at"));
                    request.setCustomerName(rs.getString("customer_nickName"));
                    request.setCustomerPhone(rs.getString("phone"));
                    request.setCustomerEmail(rs.getString("email"));
                    request.setLocation(rs.getString("location")); // bổ sung
    request.setUrgency(rs.getString("urgency"));   // bổ sung
                    list.add(request);
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
        return list;
    }

    public boolean updateRequestStatus(String requestId, String status) throws SQLException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(
                        "UPDATE Request SET status = ? WHERE request_id = ?"
                );
                ptm.setString(1, status);
                ptm.setString(2, requestId);

                check = ptm.executeUpdate() > 0;
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

    public List<RequestDTO> getRequestsByCustomerId(String customerId) throws SQLException {
        List<RequestDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(
                        "SELECT request_id, status, customer_id, created_at "
                        + "FROM Request WHERE customer_id = ? ORDER BY created_at DESC"
                );
                ptm.setString(1, customerId);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    RequestDTO request = new RequestDTO();
                    request.setRequestId(rs.getString("request_id"));
                    request.setStatus(rs.getString("status"));
                    request.setCustomerId(rs.getString("customer_id"));
                    request.setCreatedAt(rs.getTimestamp("created_at"));
                    list.add(request);
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
        return list;
    }

    public String generateRequestId() throws SQLException {
        String newId = "REQ001";
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(
                        "SELECT TOP 1 request_id FROM Request ORDER BY request_id DESC"
                );
                rs = ptm.executeQuery();
                if (rs.next()) {
                    String lastId = rs.getString("request_id");
                    int num = Integer.parseInt(lastId.substring(3)) + 1;
                    newId = String.format("REQ%03d", num);
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
        return newId;
    }
public boolean assignEmployeeToRequest(String requestId, int employeeId) throws Exception {
    String sql = "UPDATE RequestDetail SET employee_id = ? WHERE request_id = ? AND employee_id IS NULL";
    try (Connection conn = DBUtils.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, employeeId);
        ps.setString(2, requestId);
        return ps.executeUpdate() > 0;
    }
}
public boolean insertRequestDetail(String requestId, int serviceId, Integer employeeId,String notes) throws Exception {
    String sql = "INSERT INTO RequestDetail (request_id, service_id, employee_id, notes) VALUES (?, ?, ?, ?)";
    try (Connection conn = DBUtils.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, requestId);
        ps.setInt(2, serviceId);
        if (employeeId == null) {
            ps.setNull(3, java.sql.Types.INTEGER);
        } else {
            ps.setInt(3, employeeId);
        }
        ps.setString(4, notes);
        return ps.executeUpdate() > 0;
    }
}



public List<RequestDetailDTO> getTasksByEmployeeId(int employeeId) throws Exception {
    List<RequestDetailDTO> list = new ArrayList<>();
    Connection conn = null;
    PreparedStatement ptm = null;
    ResultSet rs = null;

    try {
        conn = DBUtils.getConnection();
        if (conn != null) {
            String sql = "SELECT r.request_id, r.location, r.urgency, r.created_at, r.status, " +
                         "rd.service_id, rd.notes, s.service_name, c.customer_nickName " +
                         "FROM RequestDetail rd " +
                         "JOIN Request r ON rd.request_id = r.request_id " +
                         "JOIN Service s ON rd.service_id = s.service_id " +
                         "JOIN Customer c ON r.customer_id = c.customer_id " +
                         "WHERE rd.employee_id = ?";

            ptm = conn.prepareStatement(sql);
            ptm.setInt(1, employeeId);
            rs = ptm.executeQuery();

            while (rs.next()) {
                RequestDetailDTO dto = new RequestDetailDTO();
                dto.setRequestId(rs.getString("request_id"));
                dto.setServiceId(rs.getInt("service_id"));
                dto.setServiceName(rs.getString("service_name"));
                dto.setNotes(rs.getString("notes"));
                dto.setStatus(rs.getString("status"));
                dto.setLocation(rs.getString("location"));
                dto.setUrgency(rs.getString("urgency"));
                dto.setCreatedAt(rs.getTimestamp("created_at"));
                dto.setCustomerName(rs.getString("customer_nickName"));
                list.add(dto);
            }
        }
    } catch (Exception e) {
        System.err.println("❌ Error when retrieving tasks: " + e.getMessage());
        e.printStackTrace();
        throw e;
    } finally {
        try {
            if (rs != null) rs.close();
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    return list;
}





}
