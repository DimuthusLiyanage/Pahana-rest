// Bill.java
package com.mycompany.customerapi.model;

import java.util.Date;
import java.util.List;

public class Bill {
    private int billId;
    private String accountNumber;
    private Date billDate;
    private int totalUnits;
    private double amountDue;
    private String paymentStatus;
    private List<BillItem> items; // For nested line items

    // Getters and Setters
    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }
    
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    
    public Date getBillDate() { return billDate; }
    public void setBillDate(Date billDate) { this.billDate = billDate; }
    
    public int getTotalUnits() { return totalUnits; }
    public void setTotalUnits(int totalUnits) { this.totalUnits = totalUnits; }
    
    public double getAmountDue() { return amountDue; }
    public void setAmountDue(double amountDue) { this.amountDue = amountDue; }
    
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    
    public List<BillItem> getItems() { return items; }
    public void setItems(List<BillItem> items) { this.items = items; }
}
