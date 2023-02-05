package com.bimelody.ecommerceservice.resource;

import com.bimelody.ecommerceservice.model.request.SearchProductsRequest;
import lombok.NonNull;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ProductResource {

  @GET
  Response searchProducts(@BeanParam final SearchProductsRequest searchProductsRequest);
}
