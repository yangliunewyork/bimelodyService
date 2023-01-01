package com.bimelody.ecommerceservice.service;

import com.bimelody.ecommerceservice.model.Product;
import com.bimelody.ecommerceservice.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public void createProduct(Product product) {
        productRepository.createProduct(product);
    }

    @Override
    public void updateProduct(Product product) {
        productRepository.updateProduct(product);
    }


    @Override
    public List<Product> searchProducts(
            final String storeIdentifier,
            final String productCategory,
            int pageNum,
            int pageSize) {
        return productRepository.searchProducts(storeIdentifier, productCategory, pageNum, pageSize);
    }

    @Override
    public Optional<Product> findProductInfoFromStore(String storeIdentifier, String productIdentifier) {
        return productRepository.findProductInfoFromStore(storeIdentifier, productIdentifier);
    }

    @Override
    public Optional<Product> deleteProductInStore(String storeIdentifier, String productIdentifier) {
        return productRepository.deleteProductInStore(storeIdentifier, productIdentifier);
    }
}
