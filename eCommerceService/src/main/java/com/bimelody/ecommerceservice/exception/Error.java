package com.bimelody.ecommerceservice.exception;

import lombok.Builder;
import lombok.Data;

/**
 * Represents the error payload sent to the client.
 */
@Data
@Builder
public class Error {

  private int statusCode;
  private String statusDescription;
  private String errorMessage;
}
