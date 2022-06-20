package eCommerceService.service;

import eCommerceService.model.Store;
import eCommerceService.model.StoreCategory;
import eCommerceService.model.request.FindStoresRequest;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface StoreService {

  void createStore(@NonNull final Store store);

  void updateStore(@NonNull final Store store);

  List<Store> findStores(@NonNull final FindStoresRequest findStoresRequest);

  Optional<Store> getStoreInfo(@NonNull final String uniqueStoreName);

  List<StoreCategory> getAllStoreCategories();
}
