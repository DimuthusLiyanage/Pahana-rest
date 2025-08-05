/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.customerapi.resources;

/**
 *
 * @author Dimuthu
 */


import com.google.gson.Gson;
import com.mycompany.customerapi.model.Customer;
import com.mycompany.customerapi.utils.DBUtils;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/customers")
public class CustomerResource {
    private final DBUtils dbUtils = new DBUtils();
    private final Gson gson = new Gson();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomers() {
        return Response
                .ok(gson.toJson(dbUtils.getCustomers()))
                .build();
    }

    // Implement other endpoints (GET by ID, POST, PUT, DELETE) 
    // similar to your StudentResources class
}
