package com.mycompany.customerapi.utils;

import com.mycompany.customerapi.model.Item;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemOPR {
    private final DBUtils dbUtils = new DBUtils();

    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items";
        
        try (Connection conn = dbUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Item item = new Item();
                item.setItemId(rs.getInt("item_id"));
                item.setItemCode(rs.getString("item_code"));
                item.setDescription(rs.getString("description"));
                item.setUnitPrice(rs.getBigDecimal("unit_price"));
                item.setCategory(rs.getString("category"));
                item.setStockQuantity(rs.getInt("stock_quantity"));
                item.setLastUpdated(rs.getTimestamp("last_updated"));
                items.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
        return items;
    }

    public Item getItemById(int itemId) {
        String sql = "SELECT * FROM items WHERE item_id = ?";
        try (Connection conn = dbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, itemId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Item item = new Item();
                    item.setItemId(rs.getInt("item_id"));
                    item.setItemCode(rs.getString("item_code"));
                    item.setDescription(rs.getString("description"));
                    item.setUnitPrice(rs.getBigDecimal("unit_price"));
                    item.setCategory(rs.getString("category"));
                    item.setStockQuantity(rs.getInt("stock_quantity"));
                    item.setLastUpdated(rs.getTimestamp("last_updated"));
                    return item;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
        return null;
    }

    public Item getItemByCode(String itemCode) {
        String sql = "SELECT * FROM items WHERE item_code = ?";
        try (Connection conn = dbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, itemCode);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Item item = new Item();
                    item.setItemId(rs.getInt("item_id"));
                    item.setItemCode(rs.getString("item_code"));
                    item.setDescription(rs.getString("description"));
                    item.setUnitPrice(rs.getBigDecimal("unit_price"));
                    item.setCategory(rs.getString("category"));
                    item.setStockQuantity(rs.getInt("stock_quantity"));
                    item.setLastUpdated(rs.getTimestamp("last_updated"));
                    return item;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
        return null;
    }

    public boolean addItem(Item item) {
        String sql = "INSERT INTO items (item_code, description, unit_price, category, stock_quantity) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getItemCode());
            stmt.setString(2, item.getDescription());
            stmt.setBigDecimal(3, item.getUnitPrice());
            stmt.setString(4, item.getCategory());
            stmt.setInt(5, item.getStockQuantity());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }

    public boolean updateItem(Item item) {
        String sql = "UPDATE items SET item_code = ?, description = ?, unit_price = ?, "
                   + "category = ?, stock_quantity = ? WHERE item_id = ?";
        try (Connection conn = dbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getItemCode());
            stmt.setString(2, item.getDescription());
            stmt.setBigDecimal(3, item.getUnitPrice());
            stmt.setString(4, item.getCategory());
            stmt.setInt(5, item.getStockQuantity());
            stmt.setInt(6, item.getItemId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }

    public boolean deleteItem(int itemId) {
        String sql = "DELETE FROM items WHERE item_id = ?";
        try (Connection conn = dbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, itemId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }

    public boolean updateStockQuantity(int itemId, int quantityChange) {
        String sql = "UPDATE items SET stock_quantity = stock_quantity + ? WHERE item_id = ?";
        try (Connection conn = dbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantityChange);
            stmt.setInt(2, itemId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }
}