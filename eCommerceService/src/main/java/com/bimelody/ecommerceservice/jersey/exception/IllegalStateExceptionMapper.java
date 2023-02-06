package com.bimelody.ecommerceservice.jersey.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

/**
 * Map IllegalStateException to an HTTP response.
 */
@Slf4j
@Provider
public class IllegalStateExceptionMapper implements ExceptionMapper<IllegalStateException> {
  @Override
  public Response toResponse(IllegalStateException exception) {
    log.error("IllegalStateExceptionMapper:", exception);
    Error error =
        Error.builder()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .statusDescription(exception.getLocalizedMessage())
            .errorMessage(exception.getMessage())
            .build();
    return Response.status(Response.Status.BAD_REQUEST)
        .entity(error)
        .type(MediaType.APPLICATION_JSON)
        .build();
  }
}
