package com.bimelody.ecommerceservice.jersey.exception;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * GenericExceptionMapper will be returned to client when a general exception is thrown.
 */
@Slf4j
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable ex) {
        log.error("GenericExceptionMapper!", ex);
        Response.StatusType type = getStatusType(ex);

        Error error = Error.builder()
                .statusCode(type.getStatusCode())
                .statusDescription(type.getReasonPhrase())
                .errorMessage(ex.getLocalizedMessage())
                .build();

        return Response.status(error.getStatusCode())
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private Response.StatusType getStatusType(Throwable ex) {
        if (ex instanceof WebApplicationException) {
            return((WebApplicationException)ex).getResponse().getStatusInfo();
        } else {
            return Response.Status.INTERNAL_SERVER_ERROR;
        }
    }
}