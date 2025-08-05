package com.mycompany.customerapi.utils;

import com.mycompany.customerapi.model.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBUtils {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }
    
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/batch13?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "root";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    public List<Customer> getCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setName(rs.getString("name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                customers.add(customer);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
        return customers;
    }
}