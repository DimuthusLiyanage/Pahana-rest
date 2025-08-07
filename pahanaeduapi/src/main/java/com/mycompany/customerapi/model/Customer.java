package com.mycompany.customerapi.model;

import java.util.Date;

public class Customer {
    private String accountNumber;
    private String name;
    private String address;
    private String telephone;
    private String email;
    private int unitsConsumed;
    private Date registeredDate;
    private Date lastBilledDate;

    // Getters and setters for all fields
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public int getUnitsConsumed() { return unitsConsumed; }
    public void setUnitsConsumed(int unitsConsumed) { this.unitsConsumed = unitsConsumed; }
    
    public Date getRegisteredDate() { return registeredDate; }
    public void setRegisteredDate(Date registeredDate) { this.registeredDate = registeredDate; }
    
    public Date getLastBilledDate() { return lastBilledDate; }
    public void setLastBilledDate(Date lastBilledDate) { this.lastBilledDate = lastBilledDate; }
}