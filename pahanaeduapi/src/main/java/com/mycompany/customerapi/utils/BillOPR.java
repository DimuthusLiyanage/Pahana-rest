// BillOPR.java
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
                bills.add(bill);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
        return bills;
    }

    public Bill getBillById(int billId) {
        String billSql = "SELECT * FROM bills WHERE bill_id = ?";
        String itemsSql = "SELECT * FROM bill_items WHERE bill_id = ?";
        
        try (Connection conn = dbUtils.getConnection();
             PreparedStatement billStmt = conn.prepareStatement(billSql)) {
            
            billStmt.setInt(1, billId);
            try (ResultSet billRs = billStmt.executeQuery()) {
                if (billRs.next()) {
                    Bill bill = new Bill();
                    bill.setBillId(billRs.getInt("bill_id"));
                    bill.setAccountNumber(billRs.getString("account_number"));
                    bill.setBillDate(billRs.getDate("bill_date"));
                    bill.setTotalUnits(billRs.getInt("total_units"));
                    bill.setAmountDue(billRs.getDouble("amount_due"));
                    bill.setPaymentStatus(billRs.getString("payment_status"));
                    
                    // Fetch line items
                    try (PreparedStatement itemsStmt = conn.prepareStatement(itemsSql)) {
                        itemsStmt.setInt(1, billId);
                        try (ResultSet itemsRs = itemsStmt.executeQuery()) {
                            List<BillItem> items = new ArrayList<>();
                            while (itemsRs.next()) {
                                BillItem item = new BillItem();
                                item.setBillId(itemsRs.getInt("bill_id"));
                                item.setItemId(itemsRs.getInt("item_id"));
                                item.setQuantity(itemsRs.getInt("quantity"));
                                item.setUnitPrice(itemsRs.getDouble("unit_price"));
                                items.add(item);
                            }
                            bill.setItems(items);
                        }
                    }
                    return bill;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
        return null;
    }

    public boolean addBill(Bill bill) {
        String billSql = "INSERT INTO bills (account_number, bill_date, total_units, amount_due, payment_status) "
                       + "VALUES (?, ?, ?, ?, ?)";
        String itemSql = "INSERT INTO bill_items (bill_id, item_id, quantity, unit_price) "
                       + "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbUtils.getConnection();
             PreparedStatement billStmt = conn.prepareStatement(billSql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Insert bill header
            billStmt.setString(1, bill.getAccountNumber());
            billStmt.setDate(2, new java.sql.Date(bill.getBillDate().getTime()));
            billStmt.setInt(3, bill.getTotalUnits());
            billStmt.setDouble(4, bill.getAmountDue());
            billStmt.setString(5, bill.getPaymentStatus());
            
            if (billStmt.executeUpdate() > 0) {
                // Get generated bill_id
                try (ResultSet rs = billStmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int billId = rs.getInt(1);
                        
                        // Insert line items
                        try (PreparedStatement itemStmt = conn.prepareStatement(itemSql)) {
                            for (BillItem item : bill.getItems()) {
                                itemStmt.setInt(1, billId);
                                itemStmt.setInt(2, item.getItemId());
                                itemStmt.setInt(3, item.getQuantity());
                                itemStmt.setDouble(4, item.getUnitPrice());
                                itemStmt.addBatch();
                            }
                            itemStmt.executeBatch();
                        }
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }

    public boolean updateBill(Bill bill) {
        String billSql = "UPDATE bills SET account_number = ?, bill_date = ?, total_units = ?, "
                       + "amount_due = ?, payment_status = ? WHERE bill_id = ?";
        String deleteItemsSql = "DELETE FROM bill_items WHERE bill_id = ?";
        
        try (Connection conn = dbUtils.getConnection();
             PreparedStatement billStmt = conn.prepareStatement(billSql);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteItemsSql)) {
            
            // Update bill header
            billStmt.setString(1, bill.getAccountNumber());
            billStmt.setDate(2, new java.sql.Date(bill.getBillDate().getTime()));
            billStmt.setInt(3, bill.getTotalUnits());
            billStmt.setDouble(4, bill.getAmountDue());
            billStmt.setString(5, bill.getPaymentStatus());
            billStmt.setInt(6, bill.getBillId());
            
            // Delete existing items
            deleteStmt.setInt(1, bill.getBillId());
            deleteStmt.executeUpdate();
            
            // Insert new items
            if (billStmt.executeUpdate() > 0) {
                try (PreparedStatement itemStmt = conn.prepareStatement(
                        "INSERT INTO bill_items VALUES (?, ?, ?, ?)")) {
                    for (BillItem item : bill.getItems()) {
                        itemStmt.setInt(1, bill.getBillId());
                        itemStmt.setInt(2, item.getItemId());
                        itemStmt.setInt(3, item.getQuantity());
                        itemStmt.setDouble(4, item.getUnitPrice());
                        itemStmt.addBatch();
                    }
                    itemStmt.executeBatch();
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }

    public boolean deleteBill(int billId) {
        String deleteItemsSql = "DELETE FROM bill_items WHERE bill_id = ?";
        String deleteBillSql = "DELETE FROM bills WHERE bill_id = ?";
        
        try (Connection conn = dbUtils.getConnection();
             PreparedStatement itemsStmt = conn.prepareStatement(deleteItemsSql);
             PreparedStatement billStmt = conn.prepareStatement(deleteBillSql)) {
            
            // Delete items first (foreign key constraint)
            itemsStmt.setInt(1, billId);
            itemsStmt.executeUpdate();
            
            // Delete bill
            billStmt.setInt(1, billId);
            return billStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }
}