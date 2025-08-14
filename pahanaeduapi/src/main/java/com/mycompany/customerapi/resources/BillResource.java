// BillResource.java
package com.mycompany.customerapi.resources;

import com.google.gson.Gson;
import com.mycompany.customerapi.model.Bill;
import com.mycompany.customerapi.utils.BillOPR;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/bills")
public class BillResource {
    private final BillOPR billOpr = new BillOPR();
    private final Gson gson = new Gson();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBills() {
        try {
            return Response.ok(gson.toJson(billOpr.getBills())).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

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
                        .entity("Bill not found with ID: " + billId).build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createBill(String json) {
        try {
            Bill bill = gson.fromJson(json, Bill.class);
            boolean isCreated = billOpr.addBill(bill);
            if (isCreated) {
                return Response.status(Response.Status.CREATED)
                        .entity("Bill created successfully").build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Failed to create bill").build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("{billId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBill(String json, @PathParam("billId") int billId) {
        try {
            Bill bill = gson.fromJson(json, Bill.class);
            bill.setBillId(billId); // Ensure path ID matches object
            boolean isUpdated = billOpr.updateBill(bill);
            if (isUpdated) {
                return Response.ok("Bill updated successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Bill not found with ID: " + billId).build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("{billId}")
    public Response deleteBill(@PathParam("billId") int billId) {
        try {
            boolean isDeleted = billOpr.deleteBill(billId);
            if (isDeleted) {
                return Response.ok("Bill deleted successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Bill not found with ID: " + billId).build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}