package com.bimelody.ecommerceservice.repository;

import com.bimelody.ecommerceservice.model.Store;
import com.bimelody.ecommerceservice.model.StoreCategory;
import com.bimelody.ecommerceservice.model.request.FindStoresRequest;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;

/**
 * The interface for Store repository.
 */
public interface StoreRepository {

  void createStore(@NonNull final Store store);

  void updateStore(@NonNull final Store store);

  List<Store> findStores(@NonNull final FindStoresRequest findStoresRequest);

  Optional<Store> getStoreInfo(@NonNull final String uniqueStoreName);

  List<StoreCategory> getAllStoreCategories();
}
