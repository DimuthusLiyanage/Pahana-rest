package com.mycompany.customerapi.resources;

import com.google.gson.Gson;
import com.mycompany.customerapi.model.User;
import com.mycompany.customerapi.utils.UserOPR;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
public class UserResource {
    private final UserOPR userOpr = new UserOPR();
    private final Gson gson = new Gson();

    // Get all users
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        try {
            return Response
                    .ok(gson.toJson(userOpr.getUsers()))
                    .build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // Get user by ID
    @GET
    @Path("{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("userId") int userId) {
        try {
            User user = userOpr.getUserById(userId);
            if (user != null) {
                return Response.ok(gson.toJson(user)).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("User not found with ID: " + userId)
                        .build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // Create new user
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(String json) {
        try {
            User user = gson.fromJson(json, User.class);
            boolean isCreated = userOpr.addUser(user);
            if (isCreated) {
                return Response.status(Response.Status.CREATED)
                        .entity("User created successfully")
                        .build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Failed to create user")
                        .build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // Update existing user
    @PUT
    @Path("{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(
            String json, 
            @PathParam("userId") int userId) {
        try {
            User user = gson.fromJson(json, User.class);
            // Ensure path user ID matches user object
            user.setUserId(userId);
            
            boolean isUpdated = userOpr.updateUser(user);
            if (isUpdated) {
                return Response.ok("User updated successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("User not found with ID: " + userId)
                        .build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // Delete user
    @DELETE
    @Path("{userId}")
    public Response deleteUser(@PathParam("userId") int userId) {
        try {
            boolean isDeleted = userOpr.deleteUser(userId);
            if (isDeleted) {
                return Response.ok("User deleted successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("User not found with ID: " + userId)
                        .build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // Additional endpoint to get user by username
    @GET
    @Path("username/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByUsername(@PathParam("username") String username) {
        try {
            User user = userOpr.getUserByUsername(username);
            if (user != null) {
                return Response.ok(gson.toJson(user)).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("User not found with username: " + username)
                        .build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}