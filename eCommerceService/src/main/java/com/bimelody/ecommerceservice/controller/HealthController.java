package com.bimelody.ecommerceservice.controller;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for handling service health.
 */
@Path("/")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class HealthController {
  @GET
  @Path("ping")
  public Response ping() {
    return Response.ok().entity("health").build();
  }
}
