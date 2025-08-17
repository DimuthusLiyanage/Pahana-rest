/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package functionTest;

import com.mycompany.customerapi.model.Item;
import com.mycompany.customerapi.utils.BillOPR;

import com.mycompany.customerapi.utils.CustomerOPR;
import com.mycompany.customerapi.utils.DBUtils;
import com.mycompany.customerapi.utils.ItemOPR;
import com.mycompany.customerapi.utils.UserOPR;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import org.apiguardian.api.API;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Dimuthu
 */
public class Test {
    public DBUtils utils;
    public com.mycompany.customerapi.utils.CustomerOPR customerOPR;
    public com.mycompany.customerapi.utils.BillOPR BillOPR;
    public com.mycompany.customerapi.utils.ItemOPR ItemOPR;
    public com.mycompany.customerapi.utils.UserOPR UserOPR;
    
    @BeforeEach
    public void setUp() {
        utils = new DBUtils();
        customerOPR = new CustomerOPR();
        BillOPR = new BillOPR();
        ItemOPR = new ItemOPR();
        UserOPR=new UserOPR();
        System.out.println("Utils instance created: " + utils);
    }
    @org.junit.jupiter.api.Test
    public void testGetCustomers() {
        List<com.mycompany.customerapi.model.Customer> customers = customerOPR.getCustomers();
        assertNotNull(customers);
        assertTrue(customers.size() >= 0);
    }
    
    @org.junit.jupiter.api.Test
    public void testGetCustomerById() {
        com.mycompany.customerapi.model.Customer customer = customerOPR.getCustomerByAccountNumber("CUST-1001");
        assertNotNull(customer);
        assertEquals("CUST-1001", customer.getAccountNumber());
        assertNotNull(customer.getName());
    }
    
    @org.junit.jupiter.api.Test
    public void testCreateUpdateDeleteCustomer() {
        com.mycompany.customerapi.model.Customer customer = new com.mycompany.customerapi.model.Customer();
       customer.setAccountNumber("2");
        customer.setName("Test Customer");
        customer.setEmail("test@example.com");
        customer.setAddress("123 Test St");
        customer.setTelephone("1234567890");
        customer.setUnitsConsumed(0);
        LocalDate today = LocalDate.now();
customer.setRegisteredDate(java.sql.Date.valueOf(today));

        boolean created =customerOPR.addCustomer(customer);
        assertTrue(created);
        assertTrue("2".equals(customer.getAccountNumber()));

        customer.setName("Updated Customer");
        customer.setUnitsConsumed(10);
        boolean updated = customerOPR.updateCustomer(customer);
        assertTrue(updated);

        com.mycompany.customerapi.model.Customer updatedCustomer = customerOPR.getCustomerByAccountNumber(customer.getAccountNumber());
        assertEquals("Updated Customer", updatedCustomer.getName());
        assertEquals(10, updatedCustomer.getUnitsConsumed());

        boolean deleted = customerOPR.deleteCustomer(customer.getAccountNumber());
        boolean del=customerOPR.deleteCustomer(customer.getAccountNumber());
        assertFalse(del);

        assertNull(customerOPR.getCustomerByAccountNumber(customer.getAccountNumber()));
    }
    //item
     @org.junit.jupiter.api.Test
    public void testGetItems() {
        List<com.mycompany.customerapi.model.Item> items = ItemOPR.getItems();
        assertNotNull(items);
        assertTrue(items.size() >= 0);
    }

    @org.junit.jupiter.api.Test
    public void testGetItemById() {
        com.mycompany.customerapi.model.Item item = ItemOPR.getItemById(2);
        assertNotNull(item);
        assertEquals(2, item.getItemId());
        assertNotNull(item.getItemId());
    }
    
   
}
