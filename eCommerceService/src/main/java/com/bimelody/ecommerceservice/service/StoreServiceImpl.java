package com.bimelody.ecommerceservice.service;

import com.bimelody.ecommerceservice.model.Product;
import com.bimelody.ecommerceservice.model.StoreCategory;
import com.bimelody.ecommerceservice.model.request.FindStoresRequest;
import com.bimelody.ecommerceservice.repository.ProductRepository;
import com.bimelody.ecommerceservice.repository.StoreRepository;
import com.bimelody.ecommerceservice.model.Store;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class StoreServiceImpl implements StoreService {

  private final StoreRepository storeRepository;
  private final ProductRepository productRepository;

  @Override
  public void createStore(@NonNull Store store) {
    storeRepository.createStore(store);
  }

  @Override
  public void updateStore(@NonNull Store store) {
    storeRepository.updateStore(store);
  }

  @Override
  public List<Store> findStores(@NonNull final FindStoresRequest findStoresRequest) {

    return storeRepository.findStores(findStoresRequest);
  }

  @Override
  public Optional<Store> getStoreInfo(@NonNull String uniqueStoreName) {
    return storeRepository.getStoreInfo(uniqueStoreName);
  }

  @Override
  public List<StoreCategory> getAllStoreCategories() {
    return storeRepository.getAllStoreCategories();
  }

  @Override
  public String generateProductIdentifierInStore(@NonNull final String uniqueStoreName,
                                                 @NonNull final String productName) {
    final StringBuilder uniqueProductNameInStoreStringBuilder = new StringBuilder();
    // Replacing all non-alphanumeric characters with empty strings
    uniqueProductNameInStoreStringBuilder.append(productName.replaceAll("[^A-Za-z0-9]", ""));
    String randomSuffix = "";
    for (int i = 0; i < 3; ++i) {
      Optional<Product> productOptional = productRepository
              .findProductInfoFromStore(uniqueStoreName,
                      uniqueProductNameInStoreStringBuilder.toString() + randomSuffix);
      if (productOptional.isPresent()) {
        randomSuffix = UUID.randomUUID().toString().substring(0,6);
      } else {
        return uniqueProductNameInStoreStringBuilder.toString() + randomSuffix;
      }
    }
    log.error("Failed to generate productIdentifier in store {}",uniqueStoreName);
    throw new IllegalStateException(String.format("Failed to generate productIdentifier in store: %s!", uniqueStoreName));
  }
}
