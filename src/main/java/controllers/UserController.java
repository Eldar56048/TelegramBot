package controllers;

import domain.models.User;
import filters.customAnnotations.OnlyAdmin;
import services.UserService;
import services.interfaces.IUserService;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("users")
public class UserController {

    private IUserService userService = new UserService();

    @GET
    public String hello() {
        return "Hello world!";
    }

    @GET
    @Path("/{param}")
    public Response getUserByID(@PathParam("param") long id) {
        User user;
        try {
            user = userService.getUserByID(id);
        } catch (ServerErrorException ex) {
            return Response
                    .serverError().build();
        } catch (BadRequestException ex) {
            return Response
                    .status(Response.Status.ACCEPTED)
                    .entity("This user cannot be created").build();
        }



        if (user == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("User does not exist!")
                    .build();
        } else {
            return Response
                    .status(Response.Status.OK)
                    .entity(user)
                    .build();
        }
    }


    @OnlyAdmin
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/add")
    public Response createNewUser(User user) {

        try {
            userService.addUser(user);
        } catch (ServerErrorException ex) {
            return Response
                    .serverError().entity(ex.getMessage()).build();
        } catch (BadRequestException ex) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ex.getMessage()).build();
        }

        return Response.
                status(Response.Status.CREATED)
                .entity("User created successfully!")
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/update")
    public Response updateUser(User user, @Context ContainerRequestContext requestContext) {
        if (!requestContext.getSecurityContext().isUserInRole("admin") &&
                !requestContext.getSecurityContext().getUserPrincipal().getName().equals(user.getUsername())) {
            return Response
                    .status(Response.Status.FORBIDDEN)
                    .build();
        }

        try {
            userService.updateUser(user);
        } catch (ServerErrorException ex) {
            return Response
                    .serverError().entity(ex.getMessage()).build();
        } catch (BadRequestException ex) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ex.getMessage()).build();
        }

        return Response.
                status(Response.Status.CREATED)
                .entity("User updated successfully!")
                .build();
    }


}
