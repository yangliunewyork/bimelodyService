package eCommerceService.jersey.exception;

import lombok.Builder;
import lombok.Data;

/**
 * This object represents the error output that’ll be send to the client.
 */
@Data
@Builder
public class Error {
    private int statusCode;
    private String statusDescription;
    private String errorMessage;
}