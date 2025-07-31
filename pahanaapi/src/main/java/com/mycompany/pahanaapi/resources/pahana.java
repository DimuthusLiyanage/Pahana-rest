package com.mycompany.pahanaapi.resources;
import com.google.gson.Gson;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@path("Pahana")
public class pahana {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudents() {
        Utils utils = new Utils();
        Gson gson = new Gson();
        return Response
                .ok(gson.toJson(utils.getStudents()))
                .build();
    }
}
