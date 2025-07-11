/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import project.model.dto.FeedBackDTO;
import project.utils.DBUtils;

/**
 *
 * @author Khanh
 */
public class FeedbackDAO {
     public boolean insertFeedback(FeedBackDTO feedback) throws Exception {
    Connection conn = null;
    PreparedStatement ps = null;
    try {
        conn = DBUtils.getConnection();
        String sql = "INSERT INTO Feedback (request_id, rating, comment) " +
                     "VALUES (?, ?, ?)";
        ps = conn.prepareStatement(sql);
        ps.setString(1, feedback.getRequestId());
        ps.setInt(2, feedback.getRating());
        ps.setString(3, feedback.getComment());
        

        return ps.executeUpdate() > 0;
    } finally {
        if (ps != null) ps.close();
        if (conn != null) conn.close();
    }
}
     
     public List<FeedBackDTO> getAllFeedback() throws Exception {
    List<FeedBackDTO> list = new ArrayList<>();
    Connection conn = null;
    PreparedStatement ptm = null;
    ResultSet rs = null;

    try {
        conn = DBUtils.getConnection();
        if (conn != null) {
            String sql = "SELECT f.feedback_id, f.request_id, f.rating, f.comment " +
                        "FROM Feedback f ";

            ptm = conn.prepareStatement(sql);
            rs = ptm.executeQuery();

            while (rs.next()) {
                FeedBackDTO dto = new FeedBackDTO();
                dto.setFeedbackId(rs.getInt("feedback_id"));
                dto.setRequestId(rs.getString("request_id"));
                dto.setRating(rs.getInt("rating"));
                dto.setComment(rs.getString("comment"));
                

                list.add(dto);
            }
        }
    } finally {
        if (rs != null) rs.close();
        if (ptm != null) ptm.close();
        if (conn != null) conn.close();
    }

    return list;
}

}
