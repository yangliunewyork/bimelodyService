package eCommerceService.repository;

import eCommerceService.model.Product;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

/** Provide access to audio file storage. */
public interface ProductRepository {

  void createProduct(@NonNull final Product product);

  List<Product> searchProducts(
      @Nullable final String uniqueStoreName,
      @Nullable final String productCategory,
      int pageNum,
      int pageSize);

  Optional<Product> findProductInfoFromStore(
          @NonNull final String uniqueStoreName, @NonNull final String uniqueProductNameInStore);
}
