package eCommerceService.service;

import eCommerceService.model.Store;
import eCommerceService.model.StoreCategory;
import eCommerceService.model.request.FindStoresRequest;
import eCommerceService.repository.StoreRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StoreServiceImpl implements StoreService {

  private final StoreRepository storeRepository;

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
}
