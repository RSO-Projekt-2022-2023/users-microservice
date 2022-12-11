package si.fri.rso.project.user.api.v1.resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import si.fri.rso.project.user.lib.User;
import si.fri.rso.project.user.services.beans.UserBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;



@ApplicationScoped
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private Logger log = Logger.getLogger(UserResource.class.getName());

    @Inject
    private UserBean userBean;

    @Context
    protected UriInfo uriInfo;

    @Operation(description = "Get all users.", summary = "Get all users.")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "List of users",
                    content = @Content(schema = @Schema(implementation = User.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Number of objects in list")}
            )})
    @GET
    public Response getUsers() {

        List<User> user = userBean.getUser();

        return Response.status(Response.Status.OK).entity(user).build();
    }


    @Operation(description = "Get data for user.", summary = "Get data for user")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "User data",
                    content = @Content(
                            schema = @Schema(implementation = User.class))
            )})
    @GET
    @Path("/{userId}")
    public Response getUser(@Parameter(description = "User ID.", required = true)
                                     @PathParam("userId") Integer userId) {

        User user = userBean.getUser(userId);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(user).build();
    }

    @Operation(description = "Add user data.", summary = "Add user data")
    @APIResponses({
            @APIResponse(responseCode = "201",
                    description = "User successfully added."
            ),
            @APIResponse(responseCode = "405", description = "Validation error .")
    })
    @POST
    public Response createUser(@RequestBody(
            description = "DTO object with user data.",
            required = true, content = @Content(
            schema = @Schema(implementation = User.class))) User user) {

        if (user.getUsername() == null || user.getPassword() == null || user.getEmail() == null || user.getFirstName() == null || user.getLastName() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            user = userBean.createUser(user);
        }

        return Response.status(Response.Status.OK).entity(user).build();

    }


    @Operation(description = "Update data for a user.", summary = "Update user")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "User successfully updated."
            )
    })
    @PUT
    @Path("{userId}")
    public Response putUser(@Parameter(description = "User ID.", required = true)
                                     @PathParam("userId") Integer userId,
                                     @RequestBody(
                                             description = "DTO object with user data.",
                                             required = true, content = @Content(
                                             schema = @Schema(implementation = User.class)))
                                             User user){

        user = userBean.putUser(userId, user);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.NOT_MODIFIED).build();

    }

    @Operation(description = "Delete user data.", summary = "Delete user data")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "User successfully deleted."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Not found."
            )
    })
    @DELETE
    @Path("{userId}")
    public Response deleteUser(@Parameter(description = "User ID.", required = true)
                                        @PathParam("userId") Integer userId){

        boolean deleted = userBean.deleteUser(userId);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }





}