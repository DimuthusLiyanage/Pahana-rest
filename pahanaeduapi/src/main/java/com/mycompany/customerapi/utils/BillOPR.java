package com.mycompany.customerapi.utils;

import com.mycompany.customerapi.model.Bill;
import com.mycompany.customerapi.model.BillItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillOPR {
    private final DBUtils dbUtils = new DBUtils();

    public List<Bill> getBills() {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM bills";
        
        try (Connection conn = dbUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Bill bill = new Bill();
                bill.setBillId(rs.getInt("bill_id"));
                bill.setAccountNumber(rs.getString("account_number"));
                bill.setBillDate(rs.getDate("bill_date"));
                bill.setTotalUnits(rs.getInt("total_units"));
                bill.setAmountDue(rs.getDouble("amount_due"));
                bill.setPaymentStatus(rs.getString("payment_status"));
                // Optionally load items here with another query
                bills.add(bill);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
        return bills;
    }

    public Bill getBillById(int billId) {
        String sql = "SELECT * FROM bills WHERE bill_id = ?";
        try (Connection conn = dbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, billId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Bill bill = new Bill();
                    bill.setBillId(rs.getInt("bill_id"));
                    bill.setAccountNumber(rs.getString("account_number"));
                    bill.setBillDate(rs.getDate("bill_date"));
                    bill.setTotalUnits(rs.getInt("total_units"));
                    bill.setAmountDue(rs.getDouble("amount_due"));
                    bill.setPaymentStatus(rs.getString("payment_status"));
                    // Load items
                    bill.setItems(getBillItems(billId));
                    return bill;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
        return null;
    }

    public List<Bill> getBillsByAccount(String accountNumber) {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM bills WHERE account_number = ?";
        
        try (Connection conn = dbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Bill bill = new Bill();
                    bill.setBillId(rs.getInt("bill_id"));
                    bill.setAccountNumber(rs.getString("account_number"));
                    bill.setBillDate(rs.getDate("bill_date"));
                    bill.setTotalUnits(rs.getInt("total_units"));
                    bill.setAmountDue(rs.getDouble("amount_due"));
                    bill.setPaymentStatus(rs.getString("payment_status"));
                    bills.add(bill);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
        return bills;
    }

    public int createBill(Bill bill) {
        String sql = "INSERT INTO bills (account_number, bill_date, total_units, amount_due, payment_status) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, bill.getAccountNumber());
            stmt.setDate(2, new java.sql.Date(bill.getBillDate().getTime()));
            stmt.setInt(3, bill.getTotalUnits());
            stmt.setDouble(4, bill.getAmountDue());
            stmt.setString(5, bill.getPaymentStatus());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }

    public boolean updateBillStatus(int billId, String status) {
        String sql = "UPDATE bills SET payment_status = ? WHERE bill_id = ?";
        try (Connection conn = dbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, billId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }

    public Bill generateBill(String accountNumber) {
        // First get customer's current units
        String customerSql = "SELECT units_consumed FROM customers WHERE account_number = ?";
        try (Connection conn = dbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(customerSql)) {
            stmt.setString(1, accountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int units = rs.getInt("units_consumed");
                    
                    // Calculate bill amount (simplified calculation)
                    double amount = calculateBillAmount(units);
                    
                    // Create bill
                    Bill bill = new Bill();
                    bill.setAccountNumber(accountNumber);
                    bill.setBillDate(new java.util.Date());
                    bill.setTotalUnits(units);
                    bill.setAmountDue(amount);
                    bill.setPaymentStatus("pending");
                    
                    // Save to database
                    int billId = createBill(bill);
                    if (billId > 0) {
                        bill.setBillId(billId);
                        
                        // Update customer's last billed date
                        updateCustomerLastBilledDate(accountNumber);
                        
                        return bill;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
        return null;
    }

    private double calculateBillAmount(int units) {
        // Implement your actual billing calculation logic here
        // This is a simplified example
        if (units <= 100) {
            return units * 10.0;
        } else if (units <= 200) {
            return 100 * 10.0 + (units - 100) * 15.0;
        } else {
            return 100 * 10.0 + 100 * 15.0 + (units - 200) * 20.0;
        }
    }

    private void updateCustomerLastBilledDate(String accountNumber) {
        String sql = "UPDATE customers SET last_billed_date = CURRENT_DATE WHERE account_number = ?";
        try (Connection conn = dbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }

    public boolean addBillItem(BillItem item) {
        String sql = "INSERT INTO bill_items (bill_id, item_id, quantity, unit_price) "
                   + "VALUES (?, ?, ?, ?)";
        try (Connection conn = dbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, item.getBillId());
            stmt.setInt(2, item.getItemId());
            stmt.setInt(3, item.getQuantity());
            stmt.setDouble(4, item.getUnitPrice());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }

    public List<BillItem> getBillItems(int billId) {
        List<BillItem> items = new ArrayList<>();
        String sql = "SELECT bi.*, i.name as item_name FROM bill_items bi " +
                     "JOIN items i ON bi.item_id = i.item_id " +
                     "WHERE bi.bill_id = ?";
        try (Connection conn = dbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, billId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BillItem item = new BillItem();
                    item.setBillId(rs.getInt("bill_id"));
                    item.setItemId(rs.getInt("item_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setUnitPrice(rs.getDouble("unit_price"));
                    item.setItemName(rs.getString("item_name"));
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
        return items;
    }
}