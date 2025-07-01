/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.model.dao;

/**
 *
 * @author Khanh
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import project.model.dto.ServiceDTO;
import project.utils.DBUtils;

public class ServiceDAO {

    public List<ServiceDTO> getAllServices() throws Exception {
        List<ServiceDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;

        try {
            conn = DBUtils.getConnection();
            String sql = "SELECT service_id, service_name, price, garage_id, is_active FROM Service WHERE is_active = 1";
            ptm = conn.prepareStatement(sql);
            rs = ptm.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("service_id");
                String name = rs.getString("service_name");
                BigDecimal price = rs.getBigDecimal("price");
                int garageId = rs.getInt("garage_id");
                boolean isActive = rs.getBoolean("is_active");

                ServiceDTO dto = new ServiceDTO(id, name, price, garageId, isActive);
                list.add(dto);
            }
        } finally {
            if (rs != null) rs.close();
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }

        return list;
    }

    public BigDecimal getServicePriceById(int serviceId) throws Exception {
        BigDecimal price = null;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;

        try {
            conn = DBUtils.getConnection();
            String sql = "SELECT price FROM Service WHERE service_id = ?";
            ptm = conn.prepareStatement(sql);
            ptm.setInt(1, serviceId);
            rs = ptm.executeQuery();

            if (rs.next()) {
                price = rs.getBigDecimal("price");
            }
        } finally {
            if (rs != null) rs.close();
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }

        return price;
    }
}

