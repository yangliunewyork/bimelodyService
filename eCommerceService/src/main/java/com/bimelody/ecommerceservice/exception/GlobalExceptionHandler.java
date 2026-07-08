package com.bimelody.ecommerceservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.SQLStateClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler mapping exceptions to HTTP responses.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handle database access exceptions.
   *
   * @param ex the DataAccessException thrown.
   * @return an error response.
   */
  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<Error> handleDataAccessException(DataAccessException ex) {
    log.error("DataAccessException is thrown!", ex);
    int statusCode = SQLStateClass.C23_INTEGRITY_CONSTRAINT_VIOLATION.equals(ex.sqlStateClass())
        ? HttpStatus.BAD_REQUEST.value()
        : HttpStatus.INTERNAL_SERVER_ERROR.value();
    return ResponseEntity.status(statusCode).body(Error.builder()
        .statusCode(statusCode)
        .statusDescription(ex.getLocalizedMessage())
        .errorMessage(ex.getMessage())
        .build());
  }

  /**
   * Handle illegal state exceptions.
   *
   * @param ex the IllegalStateException thrown.
   * @return an error response.
   */
  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<Error> handleIllegalStateException(IllegalStateException ex) {
    log.error("IllegalStateException:", ex);
    return ResponseEntity.badRequest().body(Error.builder()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .statusDescription(ex.getLocalizedMessage())
        .errorMessage(ex.getMessage())
        .build());
  }

  /**
   * Handle all other exceptions.
   *
   * @param ex the Throwable thrown.
   * @return an error response.
   */
  @ExceptionHandler(Throwable.class)
  public ResponseEntity<Error> handleGeneric(Throwable ex) {
    log.error("Unhandled exception!", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Error.builder()
        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .errorMessage(ex.getLocalizedMessage())
        .build());
  }
}
