package com.mycompany.customerapi.resources;

import com.google.gson.Gson;
import com.mycompany.customerapi.model.Customer;
import com.mycompany.customerapi.utils.CustomerOPR;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/customers")
public class CustomerResource {
    private final CustomerOPR customerOpr = new CustomerOPR();
    private final Gson gson = new Gson();

    // Get all customers
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCustomers() {
        try {
            return Response
                    .ok(gson.toJson(customerOpr.getCustomers()))
                    .build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // Get customer by account number
    @GET
    @Path("{accountNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomerByAccountNumber(@PathParam("accountNumber") String accountNumber) {
        try {
            Customer customer = customerOpr.getCustomerByAccountNumber(accountNumber);
            if (customer != null) {
                return Response.ok(gson.toJson(customer)).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Customer not found with account: " + accountNumber)
                        .build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // Create new customer
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCustomer(String json) {
        try {
            Customer customer = gson.fromJson(json, Customer.class);
            boolean isCreated = customerOpr.addCustomer(customer);
            if (isCreated) {
                return Response.status(Response.Status.CREATED)
                        .entity("Customer created successfully")
                        .build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Failed to create customer")
                        .build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // Update existing customer
    @PUT
    @Path("{accountNumber}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCustomer(
            String json, 
            @PathParam("accountNumber") String accountNumber) {
        try {
            Customer customer = gson.fromJson(json, Customer.class);
            // Ensure path account number matches customer object
            customer.setAccountNumber(accountNumber);
            
            boolean isUpdated = customerOpr.updateCustomer(customer);
            if (isUpdated) {
                return Response.ok("Customer updated successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Customer not found with account: " + accountNumber)
                        .build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // Delete customer
    @DELETE
    @Path("{accountNumber}")
    public Response deleteCustomer(@PathParam("accountNumber") String accountNumber) {
        try {
            boolean isDeleted = customerOpr.deleteCustomer(accountNumber);
            if (isDeleted) {
                return Response.ok("Customer deleted successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Customer not found with account: " + accountNumber)
                        .build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}