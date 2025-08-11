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

    @OPTIONS
    @Path("{path: .*}")
    public Response options() {
        return Response.ok().build();
    }

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
                String jsonResponse = gson.toJson(loginResponse);

                return Response.ok(jsonResponse).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED)
                               .entity("Invalid username or password")
                               .build();
            }
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // (other endpoints can remain as-is from your original file)

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
