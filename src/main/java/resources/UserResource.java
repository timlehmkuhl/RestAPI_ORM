package resources;

//import filter.AuthenticationFilter.Secured;
import model.User;
import service.UserService;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Collection;
import java.util.Optional;

@Path("/users")
public class UserResource {

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getUser(@PathParam("id") int id) {
        User user = getUserById(id);
        return Response.ok(user).build();
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_OCTET_STREAM, "image/png", "image/jpeg"})
    public Response getUserImage(@PathParam("id") int id) throws IOException {
        User user = getUserById(id);
        InputStream is = new ByteArrayInputStream(user.image);
        String type = URLConnection.guessContentTypeFromStream(is); // get MIME type
        String ext = type.substring(type.lastIndexOf("/") + 1); // get file extension
        return Response.ok(is).type(type).header("Content-Disposition", "inline; filename=\"" + user.name + "." + ext + "\"").build();
    }

    private User getUserById(int id) {
        Optional<User> user = UserService.queryById(id); // query user object from database
        if (user.isPresent()) {
            return user.get();
        }
        throw new NotFoundException("User not found.");
    }

  /*  @GET @Secured
    public Collection<User> getUsers() {
        return UserService.queryAllUsers();
    }*/
}
