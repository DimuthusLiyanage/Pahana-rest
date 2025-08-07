package com.mycompany.customerapi.utils;

import com.mycompany.customerapi.model.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerOPR {
    private final DBUtils dbUtils = new DBUtils();

    public List<Customer> getCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        
        try (Connection conn = dbUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setAccountNumber(rs.getString("account_number"));
                customer.setName(rs.getString("name"));
                customer.setAddress(rs.getString("address"));
                customer.setTelephone(rs.getString("telephone"));
                customer.setEmail(rs.getString("email"));
                customer.setUnitsConsumed(rs.getInt("units_consumed"));
                customer.setRegisteredDate(rs.getDate("registered_date"));
                customer.setLastBilledDate(rs.getDate("last_billed_date"));
                customers.add(customer);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
        return customers;
    }

    public Customer getCustomerByAccountNumber(String accountNumber) {
        String sql = "SELECT * FROM customers WHERE account_number = ?";
        try (Connection conn = dbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Customer customer = new Customer();
                    customer.setAccountNumber(rs.getString("account_number"));
                    customer.setName(rs.getString("name"));
                    customer.setAddress(rs.getString("address"));
                    customer.setTelephone(rs.getString("telephone"));
                    customer.setEmail(rs.getString("email"));
                    customer.setUnitsConsumed(rs.getInt("units_consumed"));
                    customer.setRegisteredDate(rs.getDate("registered_date"));
                    customer.setLastBilledDate(rs.getDate("last_billed_date"));
                    return customer;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
        return null;
    }

    public boolean addCustomer(Customer customer) {
        String sql = "INSERT INTO customers (account_number, name, address, telephone, email, units_consumed, registered_date) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getAccountNumber());
            stmt.setString(2, customer.getName());
            stmt.setString(3, customer.getAddress());
            stmt.setString(4, customer.getTelephone());
            stmt.setString(5, customer.getEmail());
            stmt.setInt(6, customer.getUnitsConsumed());
            stmt.setDate(7, new java.sql.Date(customer.getRegisteredDate().getTime()));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }

    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE customers SET name = ?, address = ?, telephone = ?, email = ?, "
                   + "units_consumed = ?, last_billed_date = ? WHERE account_number = ?";
        try (Connection conn = dbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getAddress());
            stmt.setString(3, customer.getTelephone());
            stmt.setString(4, customer.getEmail());
            stmt.setInt(5, customer.getUnitsConsumed());
            stmt.setDate(6, customer.getLastBilledDate() != null ? 
                          new java.sql.Date(customer.getLastBilledDate().getTime()) : null);
            stmt.setString(7, customer.getAccountNumber());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }

    public boolean deleteCustomer(String accountNumber) {
        String sql = "DELETE FROM customers WHERE account_number = ?";
        try (Connection conn = dbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }
}