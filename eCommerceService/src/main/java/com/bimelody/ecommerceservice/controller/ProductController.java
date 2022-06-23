package com.bimelody.ecommerceservice.controller;

import com.bimelody.ecommerceservice.model.Product;
import com.bimelody.ecommerceservice.resource.ProductResource;
import com.bimelody.ecommerceservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ProductController implements ProductResource {

  private final ProductService productService;

  public Response getProducts(
      final String uniqueStoreName, final String productCategory, int pageNum, int pageSize) {
    List<Product> products =
        productService.searchProducts(uniqueStoreName, productCategory, pageNum, pageSize);
    return Response.status(Response.Status.OK)
        .entity(products)
        .type(MediaType.APPLICATION_JSON)
        .build();
  }
}
