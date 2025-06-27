/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.model.dao;

import project.model.dto.EmployeeDTO;
import project.utils.DBUtils;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lehan
 */
public class EmployeeDAO {

    /**
     * Get all employees with their associated manager and owner information
     */
    public List<EmployeeDTO> getAllEmployees() throws SQLException {
        List<EmployeeDTO> employees = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(
                        "SELECT e.employee_id, e.name, e.FirstName, e.LastName, e.phone, e.email, e.role, e.working_hours, "
                        + "e.owner_id, e.manager_id, e.account_id, m.manager_nickName as manager_name, "
                        + "o.owner_nickName as owner_name, a.username as account_username "
                        + "FROM Employee e "
                        + "LEFT JOIN Manager m ON e.manager_id = m.manager_id "
                        + "LEFT JOIN Owner o ON e.owner_id = o.owner_id "
                        + "LEFT JOIN Account a ON e.account_id = a.account_id "
                        + "ORDER BY e.name"
                );
                rs = ptm.executeQuery();
                while (rs.next()) {
                    EmployeeDTO employee = mapResultSetToEmployee(rs);
                    employees.add(employee);
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
        return employees;
    }

    /**
     * Get employee by ID
     */
    public EmployeeDTO getEmployeeById(int employeeId) throws SQLException {
        EmployeeDTO employee = null;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(
                        "SELECT e.employee_id, e.name, e.FirstName, e.LastName, e.phone, e.email, e.role, e.working_hours, "
                        + "e.owner_id, e.manager_id, e.account_id, m.manager_nickName as manager_name, "
                        + "o.owner_nickName as owner_name, a.username as account_username "
                        + "FROM Employee e "
                        + "LEFT JOIN Manager m ON e.manager_id = m.manager_id "
                        + "LEFT JOIN Owner o ON e.owner_id = o.owner_id "
                        + "LEFT JOIN Account a ON e.account_id = a.account_id "
                        + "WHERE e.employee_id = ?"
                );
                ptm.setInt(1, employeeId);
                rs = ptm.executeQuery();
                if (rs.next()) {
                    employee = mapResultSetToEmployee(rs);
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
        return employee;
    }

    /**
     * Add new employee
     */
    public boolean addEmployee(EmployeeDTO employee) throws SQLException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(
                        "INSERT INTO Employee (name, FirstName, LastName, phone, email, role, working_hours, owner_id, manager_id, account_id) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                );
                ptm.setString(1, employee.getName());
                ptm.setString(2, employee.getFirstName());
                ptm.setString(3, employee.getLastName());
                ptm.setString(4, employee.getPhone());
                ptm.setString(5, employee.getEmail());
                ptm.setString(6, employee.getRole());
                ptm.setString(7, employee.getWorkingHours());
                
                // Handle nullable foreign keys
                if (employee.getOwnerId() > 0) {
                    ptm.setInt(8, employee.getOwnerId());
                } else {
                    ptm.setNull(8, Types.INTEGER);
                }
                
                if (employee.getManagerId() > 0) {
                    ptm.setInt(9, employee.getManagerId());
                } else {
                    ptm.setNull(9, Types.INTEGER);
                }
                
                if (employee.getAccountId() > 0) {
                    ptm.setInt(10, employee.getAccountId());
                } else {
                    ptm.setNull(10, Types.INTEGER);
                }

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

    /**
     * Update existing employee
     */
    public boolean updateEmployee(EmployeeDTO employee) throws SQLException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(
                        "UPDATE Employee SET name = ?, FirstName = ?, LastName = ?, phone = ?, email = ?, role = ?, "
                        + "working_hours = ?, owner_id = ?, manager_id = ?, account_id = ? WHERE employee_id = ?"
                );
                ptm.setString(1, employee.getName());
                ptm.setString(2, employee.getFirstName());
                ptm.setString(3, employee.getLastName());
                ptm.setString(4, employee.getPhone());
                ptm.setString(5, employee.getEmail());
                ptm.setString(6, employee.getRole());
                ptm.setString(7, employee.getWorkingHours());
                
                // Handle nullable foreign keys
                if (employee.getOwnerId() > 0) {
                    ptm.setInt(8, employee.getOwnerId());
                } else {
                    ptm.setNull(8, Types.INTEGER);
                }
                
                if (employee.getManagerId() > 0) {
                    ptm.setInt(9, employee.getManagerId());
                } else {
                    ptm.setNull(9, Types.INTEGER);
                }
                
                if (employee.getAccountId() > 0) {
                    ptm.setInt(10, employee.getAccountId());
                } else {
                    ptm.setNull(10, Types.INTEGER);
                }
                
                ptm.setInt(11, employee.getEmployeeId());

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

    /**
     * Delete employee by ID
     */
    public boolean deleteEmployee(int employeeId) throws SQLException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement("DELETE FROM Employee WHERE employee_id = ?");
                ptm.setInt(1, employeeId);
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

    /**
     * Search employees by name or role
     */
    public List<EmployeeDTO> searchEmployees(String searchTerm) throws SQLException {
        List<EmployeeDTO> employees = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(
                        "SELECT e.employee_id, e.name, e.FirstName, e.LastName, e.phone, e.email, e.role, e.working_hours, "
                        + "e.owner_id, e.manager_id, e.account_id, m.manager_nickName as manager_name, "
                        + "o.owner_nickName as owner_name, a.username as account_username "
                        + "FROM Employee e "
                        + "LEFT JOIN Manager m ON e.manager_id = m.manager_id "
                        + "LEFT JOIN Owner o ON e.owner_id = o.owner_id "
                        + "LEFT JOIN Account a ON e.account_id = a.account_id "
                        + "WHERE e.name LIKE ? OR e.role LIKE ? OR e.email LIKE ? "
                        + "ORDER BY e.name"
                );
                String searchPattern = "%" + searchTerm + "%";
                ptm.setString(1, searchPattern);
                ptm.setString(2, searchPattern);
                ptm.setString(3, searchPattern);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    EmployeeDTO employee = mapResultSetToEmployee(rs);
                    employees.add(employee);
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
        return employees;
    }

    /**
     * Get employees by manager ID
     */
    public List<EmployeeDTO> getEmployeesByManagerId(int managerId) throws SQLException {
        List<EmployeeDTO> employees = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(
                        "SELECT e.employee_id, e.name, e.FirstName, e.LastName, e.phone, e.email, e.role, e.working_hours, "
                        + "e.owner_id, e.manager_id, e.account_id, m.manager_nickName as manager_name, "
                        + "o.owner_nickName as owner_name, a.username as account_username "
                        + "FROM Employee e "
                        + "LEFT JOIN Manager m ON e.manager_id = m.manager_id "
                        + "LEFT JOIN Owner o ON e.owner_id = o.owner_id "
                        + "LEFT JOIN Account a ON e.account_id = a.account_id "
                        + "WHERE e.manager_id = ? "
                        + "ORDER BY e.name"
                );
                ptm.setInt(1, managerId);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    EmployeeDTO employee = mapResultSetToEmployee(rs);
                    employees.add(employee);
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
        return employees;
    }

    /**
     * Helper method to map ResultSet to EmployeeDTO
     */
    private EmployeeDTO mapResultSetToEmployee(ResultSet rs) throws SQLException {
        EmployeeDTO employee = new EmployeeDTO();
        employee.setEmployeeId(rs.getInt("employee_id"));
        employee.setName(rs.getString("name"));
        employee.setFirstName(rs.getString("FirstName"));
        employee.setLastName(rs.getString("LastName"));
        employee.setPhone(rs.getString("phone"));
        employee.setEmail(rs.getString("email"));
        employee.setRole(rs.getString("role"));
        employee.setWorkingHours(rs.getString("working_hours"));
        employee.setOwnerId(rs.getInt("owner_id"));
        employee.setManagerId(rs.getInt("manager_id"));
        employee.setAccountId(rs.getInt("account_id"));
        employee.setManagerName(rs.getString("manager_name"));
        employee.setOwnerName(rs.getString("owner_name"));
        employee.setAccountUsername(rs.getString("account_username"));
        return employee;
    }
    
    
    public EmployeeDTO getEmployeeByAccountId(int accountId) throws Exception {
    EmployeeDTO employee = null;
    Connection conn = null;
    PreparedStatement ptm = null;
    ResultSet rs = null;

    try {
        conn = DBUtils.getConnection();
        if (conn != null) {
            ptm = conn.prepareStatement(
                "SELECT e.employee_id, e.name, e.FirstName, e.LastName, e.phone, e.email, e.role, e.working_hours, " +
                "e.owner_id, e.manager_id, e.account_id, m.manager_nickName AS manager_name, " +
                "o.owner_nickName AS owner_name, a.username AS account_username " +
                "FROM Employee e " +
                "LEFT JOIN Manager m ON e.manager_id = m.manager_id " +
                "LEFT JOIN Owner o ON e.owner_id = o.owner_id " +
                "LEFT JOIN Account a ON e.account_id = a.account_id " +
                "WHERE e.account_id = ?"
            );
            ptm.setInt(1, accountId);
            rs = ptm.executeQuery();
            if (rs.next()) {
                employee = mapResultSetToEmployee(rs);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        if (rs != null) rs.close();
        if (ptm != null) ptm.close();
        if (conn != null) conn.close();
    }

    return employee;
}
    
}