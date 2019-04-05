package resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import filter.AuthenticationFilter;
import model.JWTClaim;
import model.User;
import service.UserService;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/login")
public class AuthenticationResource {

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(User userClaim) {
        Optional<User> user = UserService.queryByCredentials(userClaim.name, userClaim.passwordHash); // lookup user in database
        if (user.isPresent()) {
            try {
                // map claim object to JSON
                JWTClaim claim = new JWTClaim(String.valueOf(user.get().id), user.get().name);
                String claimJson = new ObjectMapper().writeValueAsString(claim);

                // create JWS object with claim as payload
                JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(claimJson));

                // use a secret key for HS256 and sign the JWS object using HMAC
                byte[] secret = AuthenticationFilter.SECRET.getBytes();
                jwsObject.sign(new MACSigner(secret));

                // provide token to the client
                return Response.ok(jwsObject.serialize()).build();

            } catch (JsonProcessingException | JOSEException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return Response.status(401).entity("Login failed").build();
    }
}
