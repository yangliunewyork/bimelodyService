package com.bimelody.ecommerceservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling service health.
 */
@Slf4j
@RestController
public class HealthController {

  /**
   * Health check endpoint.
   *
   * @return a simple health string.
   */
  @GetMapping("/ping")
  public ResponseEntity<String> ping() {
    return ResponseEntity.ok("health");
  }
}
