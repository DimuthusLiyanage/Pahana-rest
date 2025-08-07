package com.mycompany.customerapi.resources;

import com.google.gson.Gson;
import com.mycompany.customerapi.model.Item;
import com.mycompany.customerapi.utils.ItemOPR;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/items")
public class ItemResource {
    private final ItemOPR itemOpr = new ItemOPR();
    private final Gson gson = new Gson();

    // Get all items
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllItems() {
        try {
            return Response
                    .ok(gson.toJson(itemOpr.getItems()))
                    .build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // Get item by ID
    @GET
    @Path("{itemId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItemById(@PathParam("itemId") int itemId) {
        try {
            Item item = itemOpr.getItemById(itemId);
            if (item != null) {
                return Response.ok(gson.toJson(item)).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Item not found with ID: " + itemId)
                        .build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // Get item by code
    @GET
    @Path("code/{itemCode}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItemByCode(@PathParam("itemCode") String itemCode) {
        try {
            Item item = itemOpr.getItemByCode(itemCode);
            if (item != null) {
                return Response.ok(gson.toJson(item)).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Item not found with code: " + itemCode)
                        .build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // Create new item
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createItem(String json) {
        try {
            Item item = gson.fromJson(json, Item.class);
            boolean isCreated = itemOpr.addItem(item);
            if (isCreated) {
                return Response.status(Response.Status.CREATED)
                        .entity("Item created successfully")
                        .build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Failed to create item")
                        .build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // Update existing item
    @PUT
    @Path("{itemId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateItem(
            String json, 
            @PathParam("itemId") int itemId) {
        try {
            Item item = gson.fromJson(json, Item.class);
            // Ensure path item ID matches item object
            item.setItemId(itemId);
            
            boolean isUpdated = itemOpr.updateItem(item);
            if (isUpdated) {
                return Response.ok("Item updated successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Item not found with ID: " + itemId)
                        .build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // Delete item
    @DELETE
    @Path("{itemId}")
    public Response deleteItem(@PathParam("itemId") int itemId) {
        try {
            boolean isDeleted = itemOpr.deleteItem(itemId);
            if (isDeleted) {
                return Response.ok("Item deleted successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Item not found with ID: " + itemId)
                        .build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // Update stock quantity
    @PATCH
    @Path("{itemId}/stock")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateStockQuantity(
            @PathParam("itemId") int itemId,
            String quantityChangeJson) {
        try {
            int quantityChange = gson.fromJson(quantityChangeJson, Integer.class);
            boolean isUpdated = itemOpr.updateStockQuantity(itemId, quantityChange);
            if (isUpdated) {
                return Response.ok("Stock quantity updated successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Item not found with ID: " + itemId)
                        .build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}