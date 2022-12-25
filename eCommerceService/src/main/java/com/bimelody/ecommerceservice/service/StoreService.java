package com.bimelody.ecommerceservice.service;

import com.bimelody.ecommerceservice.model.StoreCategory;
import com.bimelody.ecommerceservice.model.request.FindStoresRequest;
import com.bimelody.ecommerceservice.model.Store;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface StoreService {

  void createStore(@NonNull final Store store);

  void updateStore(@NonNull final Store store);

  List<Store> findStores(@NonNull final FindStoresRequest findStoresRequest);

  Optional<Store> getStoreInfo(@NonNull final String uniqueStoreName);

  List<StoreCategory> getAllStoreCategories();

  String generateProductIdentifierInStore(@NonNull final String uniqueStoreName,
                                          @NonNull final String productName);
}
