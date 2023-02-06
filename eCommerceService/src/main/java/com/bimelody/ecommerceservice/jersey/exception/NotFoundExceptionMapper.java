package com.bimelody.ecommerceservice.jersey.exception;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

/**
 * Map NotFoundException to an HTTP response.
 */
@Slf4j
@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
  @Override
  public Response toResponse(NotFoundException exception) {
    log.error("NotFoundExceptionMapper:", exception);
    Error error = Error.builder()
        .statusCode(HttpStatus.SC_NOT_FOUND)
        .errorMessage("The resource is not found.")
        .build();
    return Response.status(Response.Status.NOT_FOUND)
        .entity(error)
        .type(MediaType.APPLICATION_JSON)
        .build();
  }
}
