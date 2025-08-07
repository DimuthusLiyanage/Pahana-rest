package com.mycompany.customerapi.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Item {
    private int itemId;
    private String itemCode;
    private String description;
    private BigDecimal unitPrice;
    private String category;
    private int stockQuantity;
    private Timestamp lastUpdated;

    // Constructors
    public Item() {
    }

    public Item(int itemId, String itemCode, String description, BigDecimal unitPrice, 
                String category, int stockQuantity, Timestamp lastUpdated) {
        this.itemId = itemId;
        this.itemCode = itemCode;
        this.description = description;
        this.unitPrice = unitPrice;
        this.category = category;
        this.stockQuantity = stockQuantity;
        this.lastUpdated = lastUpdated;
    }

    // Getters and Setters
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}