package com.mycompany.customerapi.resources;

import com.google.gson.Gson;
import com.mycompany.customerapi.model.Bill;
import com.mycompany.customerapi.model.BillItem;
import com.mycompany.customerapi.utils.BillOPR;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/bills")
public class BillResource {
    private final BillOPR billOpr = new BillOPR();
    private final Gson gson = new Gson();

    // Get all bills
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBills() {
        try {
            return Response
                    .ok(gson.toJson(billOpr.getBills()))
                    .build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // Get bill by ID
    @GET
    @Path("{billId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBillById(@PathParam("billId") int billId) {
        try {
            Bill bill = billOpr.getBillById(billId);
            if (bill != null) {
                return Response.ok(gson.toJson(bill)).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Bill not found with ID: " + billId)
                        .build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // Get bills by account number
    @GET
    @Path("account/{accountNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBillsByAccount(@PathParam("accountNumber") String accountNumber) {
        try {
            return Response.ok(gson.toJson(billOpr.getBillsByAccount(accountNumber))).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // Create new bill
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createBill(String json) {
        try {
            Bill bill = gson.fromJson(json, Bill.class);
            int billId = billOpr.createBill(bill);
            if (billId > 0) {
                return Response.status(Response.Status.CREATED)
                        .entity("Bill created successfully with ID: " + billId)
                        .build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Failed to create bill")
                        .build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // Update bill payment status
    @PUT
    @Path("{billId}/status")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBillStatus(
            @PathParam("billId") int billId, 
            String json) {
        try {
            String status = gson.fromJson(json, String.class);
            boolean isUpdated = billOpr.updateBillStatus(billId, status);
            if (isUpdated) {
                return Response.ok("Bill status updated successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Bill not found with ID: " + billId)
                        .build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // Generate bill for customer (calculate based on units)
    @POST
    @Path("generate/{accountNumber}")
    public Response generateBill(@PathParam("accountNumber") String accountNumber) {
        try {
            Bill bill = billOpr.generateBill(accountNumber);
            if (bill != null) {
                return Response.status(Response.Status.CREATED)
                        .entity(gson.toJson(bill))
                        .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Customer not found or unable to generate bill")
                        .build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // Bill items operations
    @POST
    @Path("{billId}/items")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addBillItem(
            @PathParam("billId") int billId, 
            String json) {
        try {
            BillItem item = gson.fromJson(json, BillItem.class);
            item.setBillId(billId);
            boolean isAdded = billOpr.addBillItem(item);
            if (isAdded) {
                return Response.status(Response.Status.CREATED)
                        .entity("Bill item added successfully")
                        .build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Failed to add bill item")
                        .build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("{billId}/items")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBillItems(@PathParam("billId") int billId) {
        try {
            return Response.ok(gson.toJson(billOpr.getBillItems(billId))).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}