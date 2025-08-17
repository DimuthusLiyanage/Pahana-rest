package com.mycompany.customerapi.resources;

import com.google.gson.Gson;
import com.mycompany.customerapi.model.User;
import com.mycompany.customerapi.utils.UserOPR;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/users")
public class UserResource {
    private final UserOPR userOpr = new UserOPR();
    private final Gson gson = new Gson();

    // CORS Support
    @OPTIONS
    @Path("{path: .*}")
    public Response options() {
        return Response.ok()
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
            .header("Access-Control-Allow-Headers", "Content-Type, Authorization")
            .build();
    }

    // Login Endpoint
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(String json) {
        try {
            User credentials = gson.fromJson(json, User.class);
            if (userOpr.verifyUserCredentials(credentials.getUsername(), credentials.getPassword())) {
                User user = userOpr.getUserByUsername(credentials.getUsername());
                String token = "sample-token-" + System.currentTimeMillis();
                LoginResponse loginResponse = new LoginResponse(token, user.getRole(), user.getUsername());
                return Response.ok(gson.toJson(loginResponse))
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid username or password")
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage())
                .header("Access-Control-Allow-Origin", "*")
                .build();
        }
    }

    // Get All Users
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        try {
            List<User> users = userOpr.getAllUsers();
            return Response.ok(gson.toJson(users))
                .header("Access-Control-Allow-Origin", "*")
                .build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage())
                .header("Access-Control-Allow-Origin", "*")
                .build();
        }
    }

    // Create User
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUser(String json) {
        try {
            User user = gson.fromJson(json, User.class);
            if (userOpr.addUser(user)) {
                return Response.status(Response.Status.CREATED)
                    .entity(gson.toJson(user))
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Failed to create user")
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage())
                .header("Access-Control-Allow-Origin", "*")
                .build();
        }
    }

    // Update User
    @PUT
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("userId") int userId, String json) {
        try {
            User user = gson.fromJson(json, User.class);
            user.setUserId(userId);
            if (userOpr.updateUser(user)) {
                return Response.ok(gson.toJson(user))
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found")
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage())
                .header("Access-Control-Allow-Origin", "*")
                .build();
        }
    }

    // Delete User
    @DELETE
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("userId") int userId) {
        try {
            if (userOpr.deleteUser(userId)) {
                return Response.ok("{\"message\":\"User deleted successfully\"}")
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found")
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage())
                .header("Access-Control-Allow-Origin", "*")
                .build();
        }
    }

    // Login Response Class
    public static class LoginResponse {
        private String token;
        private String role;
        private String username;

        public LoginResponse(String token, String role, String username) {
            this.token = token;
            this.role = role;
            this.username = username;
        }

        public String getToken() { return token; }
        public String getRole() { return role; }
        public String getUsername() { return username; }
    }
}