package eCommerceService.service;

import eCommerceService.model.Product;
import eCommerceService.repository.ProductRepository;
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
    public List<Product> searchProducts(
            final String uniqueStoreName,
            final String productCategory,
            int pageNum,
            int pageSize) {
        return productRepository.searchProducts(uniqueStoreName, productCategory, pageNum, pageSize);
    }

    @Override
    public Optional<Product> findProductInfoFromStore(String uniqueStoreName, String uniqueProductNameInStore) {
        return productRepository.findProductInfoFromStore(uniqueStoreName, uniqueProductNameInStore);
    }
}
