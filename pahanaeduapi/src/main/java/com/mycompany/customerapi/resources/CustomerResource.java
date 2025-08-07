package com.mycompany.customerapi.resources;

import com.google.gson.Gson;
import com.mycompany.customerapi.model.Customer;
import com.mycompany.customerapi.utils.CustomerOPR;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/customers")
public class CustomerResource {
    private final CustomerOPR cus = new CustomerOPR();
    private final Gson gson = new Gson();

    // Get all customers
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomers() {
        return Response
                .ok(gson.toJson(cus.getCustomers()))
                .build();
    }

    // Get customer by ID
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomerById(@PathParam("id") int id) {
        Customer customer = cus.getCustomerById(id);
        if (customer != null) {
            return Response.ok(gson.toJson(customer)).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // Add new customer
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCustomer(String json) {
        Customer customer = gson.fromJson(json, Customer.class);
        boolean isAdded = cus.addCustomer(customer);
        if (isAdded) {
            return Response.status(Response.Status.CREATED).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Update existing customer
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCustomer(String json, @PathParam("id") int id) {
        Customer customer = gson.fromJson(json, Customer.class);
        customer.setId(id); // Ensure path ID matches the customer ID
        boolean isUpdated = cus.updateCustomer(customer);
        if (isUpdated) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // Delete customer
    @DELETE
    @Path("{id}")
    public Response deleteCustomer(@PathParam("id") int id) {
        boolean isDeleted = cus.deleteCustomer(id);
        if (isDeleted) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}