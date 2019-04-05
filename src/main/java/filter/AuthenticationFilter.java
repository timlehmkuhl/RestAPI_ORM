package filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import filter.AuthenticationFilter.Secured;
import model.JWTClaim;

import javax.annotation.Priority;
import javax.ws.rs.NameBinding;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.ParseException;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    public static final String SECRET = "my-very-private-secret:32-symbols=256-bit";
    static final String TOKEN_TYPE = "Bearer";

    @NameBinding @Retention(RetentionPolicy.RUNTIME)
    public @interface Secured { }

    @Override
    public void filter(ContainerRequestContext request) {
        // validate HTTP authorization header
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(TOKEN_TYPE + " ")) {
            request.abortWith(Response.status(401).entity("JWT not found in HTTP header").build());
        }
        // extract the token from the HTTP authorization header
        String jwt = authHeader.substring(TOKEN_TYPE.length()).trim();

        // validate JWT signature
        try {
            JWSObject jwsObject = JWSObject.parse(jwt);
            JWSVerifier verifier = new MACVerifier(SECRET);
            if (jwsObject.verify(verifier)) {
                String payload = jwsObject.getPayload().toString();
                JWTClaim claim = new ObjectMapper().readValue(payload, JWTClaim.class);
                System.out.println("User verified: " + claim.name);
                return;
            }
            else {
                request.abortWith(Response.status(401).entity("Invalid JWT").build());
            }
        } catch (ParseException | JOSEException | IOException e) {
            request.abortWith(Response.status(400).entity(e.getMessage()).build());
        }
    }
}
