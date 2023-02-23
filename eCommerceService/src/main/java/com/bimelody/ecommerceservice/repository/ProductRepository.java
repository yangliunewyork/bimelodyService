package com.bimelody.ecommerceservice.repository;

import com.bimelody.ecommerceservice.model.Product;
import com.bimelody.ecommerceservice.model.request.SearchProductsRequest;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.lang.Nullable;

/**
 * Provide access to product storage.
 */
public interface ProductRepository {

  void createProduct(@NonNull final Product product);

  void updateProduct(@NonNull final Product product);

  List<Product> searchProducts(@NonNull final SearchProductsRequest searchProductsRequest);

  List<Product> searchProductsInStore(
      @Nullable final String uniqueStoreName,
      @Nullable final String productCategory,
      int pageNum,
      int pageSize);

  Optional<Product> findProductInfoFromStore(
      @NonNull final String storeIdentifier, @NonNull final String productIdentifier);

  Optional<Product> deleteProductInStore(
      @NonNull final String storeIdentifier, @NonNull final String productIdentifier);
}
