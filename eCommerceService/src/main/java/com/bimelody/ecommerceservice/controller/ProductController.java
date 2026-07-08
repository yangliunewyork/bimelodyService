package com.bimelody.ecommerceservice.controller;

import com.bimelody.ecommerceservice.model.Product;
import com.bimelody.ecommerceservice.model.request.SearchProductsRequest;
import com.bimelody.ecommerceservice.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling product search.
 */
@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  /**
   * Search for products.
   *
   * @param searchProductsRequest query parameters for searching products.
   * @return list of matching products.
   */
  @GetMapping
  public ResponseEntity<List<Product>> searchProducts(
      SearchProductsRequest searchProductsRequest) {
    return ResponseEntity.ok(productService.searchProducts(searchProductsRequest));
  }
}
