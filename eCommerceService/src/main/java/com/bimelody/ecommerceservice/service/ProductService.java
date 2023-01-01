package com.bimelody.ecommerceservice.service;

import com.bimelody.ecommerceservice.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Service class for handling product operations.
 */
public interface ProductService {

    void createProduct(final Product product);

    void updateProduct(final Product product);

    List<Product> searchProducts(
            final String uniqueStoreName, final String productCategory, int pageNum, int pageSize);

    Optional<Product> findProductInfoFromStore(
            final String uniqueStoreName, final String productIdentifier);

    Optional<Product> deleteProductInStore(
            final String uniqueStoreName, final String productIdentifier);
}
