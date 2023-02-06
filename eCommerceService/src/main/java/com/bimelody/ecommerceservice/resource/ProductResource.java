package com.bimelody.ecommerceservice.resource;

import com.bimelody.ecommerceservice.model.request.SearchProductsRequest;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Resource for product.
 */
@Path("/products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ProductResource {

  /**
   * Return product information.
   *
   * @param searchProductsRequest Request class.
   * @return search results for product.
   */
  @GET
  Response searchProducts(@BeanParam final SearchProductsRequest searchProductsRequest);
}
