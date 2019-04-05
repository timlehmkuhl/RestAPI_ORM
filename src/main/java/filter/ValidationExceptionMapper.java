package filter;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException e) {
        ConstraintViolation violation = e.getConstraintViolations().stream().findFirst().get();
        String message = violation.getPropertyPath() + " " + violation.getMessage();
        return Response.status(Response.Status.BAD_REQUEST).entity(message).type("text/plain").build();
    }
}