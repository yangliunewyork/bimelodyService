package eCommerceService.jersey.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.SQLStateClass;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Slf4j
@Provider
public class DatabaseAccessExceptionMapper implements ExceptionMapper<DataAccessException> {
  @Override
  public Response toResponse(DataAccessException exception) {
    Error error =
        Error.builder()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
            .statusDescription(exception.getLocalizedMessage())
            .errorMessage(exception.getMessage())
            .build();
    // Reset status code to bad request
    if (SQLStateClass.C23_INTEGRITY_CONSTRAINT_VIOLATION.equals(exception.sqlStateClass())) {
      error.setStatusCode(HttpStatus.SC_BAD_REQUEST);
    }
    log.error("DataAccessException is thrown!", exception);
    return Response.status(Response.Status.BAD_REQUEST)
        .entity(error)
        .type(MediaType.APPLICATION_JSON)
        .build();
  }
}
