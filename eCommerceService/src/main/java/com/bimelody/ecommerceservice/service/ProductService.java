package com.bimelody.ecommerceservice.service;

import com.bimelody.ecommerceservice.model.Product;
import com.bimelody.ecommerceservice.model.request.SearchProductsRequest;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;

/**
 * Service class that contains business logic for handling product.
 */
public interface ProductService {

  void createProduct(final Product product);

  void updateProduct(final Product product);

  List<Product> searchProducts(
      @NonNull final SearchProductsRequest searchProductsRequest);

  List<Product> searchProductsInStore(
      final String uniqueStoreName, final String productCategory, int pageNum, int pageSize);

  Optional<Product> findProductInfoFromStore(
      final String uniqueStoreName, final String productIdentifier);

  Optional<Product> deleteProductInStore(
      final String uniqueStoreName, final String productIdentifier);
}
