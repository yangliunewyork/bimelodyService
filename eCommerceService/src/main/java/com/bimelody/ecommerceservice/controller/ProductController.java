package com.bimelody.ecommerceservice.controller;

import com.bimelody.ecommerceservice.model.Product;
import com.bimelody.ecommerceservice.model.request.SearchProductsRequest;
import com.bimelody.ecommerceservice.resource.ProductResource;
import com.bimelody.ecommerceservice.service.ProductService;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for handling Product resource.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ProductController implements ProductResource {

  private final ProductService productService;

  @Override
  public Response searchProducts(@NonNull SearchProductsRequest searchProductsRequest) {
    List<Product> products =
        productService.searchProducts(searchProductsRequest);
    return Response.status(Response.Status.OK)
        .entity(products)
        .type(MediaType.APPLICATION_JSON)
        .build();
  }
}
