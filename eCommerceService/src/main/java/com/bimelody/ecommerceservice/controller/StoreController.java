package com.bimelody.ecommerceservice.controller;

import com.amazonaws.util.CollectionUtils;
import com.bimelody.ecommerceservice.model.Product;
import com.bimelody.ecommerceservice.model.Store;
import com.bimelody.ecommerceservice.model.request.CreateProductRequest;
import com.bimelody.ecommerceservice.model.request.CreateStoreRequest;
import com.bimelody.ecommerceservice.model.request.FindStoresRequest;
import com.bimelody.ecommerceservice.model.request.UpdateProductRequest;
import com.bimelody.ecommerceservice.model.request.UpdateStoresRequest;
import com.bimelody.ecommerceservice.service.ProductAssetService;
import com.bimelody.ecommerceservice.service.ProductService;
import com.bimelody.ecommerceservice.service.StoreService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling store and product resources.
 */
@Slf4j
@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

  private final StoreService storeService;
  private final ProductService productService;
  private final ProductAssetService productAssetService;

  /**
   * Return a list of stores matching the given criteria.
   *
   * @param findStoresRequest query parameters for finding stores.
   * @return list of stores.
   */
  @GetMapping
  public ResponseEntity<List<Store>> getStores(FindStoresRequest findStoresRequest) {
    return ResponseEntity.ok(storeService.findStores(findStoresRequest));
  }

  /**
   * Create a new store.
   *
   * @param createStoresRequest the store creation request body.
   * @return empty 200 response on success.
   */
  @PostMapping
  public ResponseEntity<Void> createStore(@RequestBody CreateStoreRequest createStoresRequest) {
    Store store = Store.builder()
        .uniqueStoreName(createStoresRequest.getUniqueStoreName())
        .storeName(createStoresRequest.getStoreName())
        .storeWebsite(createStoresRequest.getStoreWebsite())
        .contactEmail(createStoresRequest.getContactEmail())
        .contactNumber(createStoresRequest.getContactNumber())
        .storeDescription(createStoresRequest.getStoreDescription())
        .build();
    storeService.createStore(store);
    return ResponseEntity.ok().build();
  }

  /**
   * Get information for a specific store.
   *
   * @param storeIdentifier the unique store name.
   * @return store info, or 404 if not found.
   */
  @GetMapping("/{storeIdentifier}")
  public ResponseEntity<Store> getStoreInfo(
      @PathVariable String storeIdentifier) {
    Optional<Store> storeOptional = storeService.getStoreInfo(storeIdentifier);
    return storeOptional
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * Update information for the specified store.
   *
   * @param storeIdentifier the unique store name.
   * @param updateStoresRequest the store update request body.
   * @return empty 200 response on success.
   */
  @PutMapping("/{storeIdentifier}")
  public ResponseEntity<Void> updateStore(
      @PathVariable String storeIdentifier,
      @RequestBody UpdateStoresRequest updateStoresRequest) {
    Store store = Store.builder()
        .uniqueStoreName(storeIdentifier)
        .storeName(updateStoresRequest.getStoreName())
        .storeWebsite(updateStoresRequest.getStoreWebsite())
        .contactEmail(updateStoresRequest.getContactEmail())
        .contactNumber(updateStoresRequest.getContactNumber())
        .storeDescription(updateStoresRequest.getStoreDescription())
        .storeLocation(updateStoresRequest.getStoreLocation())
        .storeLocationLatitude(updateStoresRequest.getStoreLocationLatitude())
        .storeLocationLongitude(updateStoresRequest.getStoreLocationLongitude())
        .build();
    storeService.updateStore(store);
    return ResponseEntity.ok().build();
  }

  /**
   * Return a list of products in the specified store.
   *
   * @param storeIdentifier the unique store name.
   * @param productCategory optional product category filter.
   * @param pageNum page number, defaults to 1.
   * @param pageSize number of results per page, defaults to 10.
   * @return list of products.
   */
  @GetMapping("/{storeIdentifier}/products")
  public ResponseEntity<List<Product>> getProductsInStore(
      @PathVariable String storeIdentifier,
      @RequestParam(required = false) String productCategory,
      @RequestParam(defaultValue = "1") int pageNum,
      @RequestParam(defaultValue = "10") int pageSize) {
    return ResponseEntity.ok(
        productService.searchProductsInStore(storeIdentifier, productCategory, pageNum, pageSize));
  }

  /**
   * Create a new product in the specified store.
   *
   * @param storeIdentifier the unique store name.
   * @param createProductRequest the product creation request body.
   * @return the created product.
   */
  @PostMapping("/{storeIdentifier}/products")
  public ResponseEntity<Product> createProduct(
      @PathVariable String storeIdentifier,
      @RequestBody CreateProductRequest createProductRequest) {
    Product product = Product.builder()
        .uniqueStoreName(storeIdentifier)
        .uniqueProductNameInStore(storeService.generateProductIdentifierInStore(
            storeIdentifier, createProductRequest.getProductName()))
        .productName(createProductRequest.getProductName())
        .productDescription(createProductRequest.getProductDescription())
        .priceInDollar(createProductRequest.getPriceInDollar())
        .productImageUrls(createProductRequest.getProductImageUrls())
        .build();
    productService.createProduct(product);
    return ResponseEntity.ok(product);
  }

  /**
   * Get information about a specific product in a store.
   *
   * @param storeIdentifier the unique store name.
   * @param productIdentifier the unique product name in the store.
   * @return the product, or 404 if not found.
   */
  @GetMapping("/{storeIdentifier}/products/{productIdentifier}")
  public ResponseEntity<Product> getProductInfoFromStore(
      @PathVariable String storeIdentifier,
      @PathVariable String productIdentifier) {
    Optional<Product> productOptional =
        productService.findProductInfoFromStore(storeIdentifier, productIdentifier);
    return productOptional
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * Update a product in the specified store.
   *
   * @param storeIdentifier the unique store name.
   * @param productIdentifier the unique product name in the store.
   * @param updateProductRequest the product update request body.
   * @return the updated product.
   */
  @PutMapping("/{storeIdentifier}/products/{productIdentifier}")
  public ResponseEntity<Product> updateProduct(
      @PathVariable String storeIdentifier,
      @PathVariable String productIdentifier,
      @RequestBody UpdateProductRequest updateProductRequest) {
    if (StringUtils.isBlank(storeIdentifier)) {
      throw new IllegalArgumentException("storeIdentifier is invalid: " + storeIdentifier);
    }
    if (StringUtils.isBlank(productIdentifier)) {
      throw new IllegalArgumentException("Invalid productIdentifier: " + productIdentifier);
    }
    if (CollectionUtils.isNullOrEmpty(updateProductRequest.getProductImageUrls())) {
      throw new IllegalArgumentException(
          "Product images can't be empty: " + updateProductRequest.getProductImageUrls());
    }
    Optional<Product> productOptional =
        productService.findProductInfoFromStore(storeIdentifier, productIdentifier);
    if (productOptional.isEmpty()) {
      throw new IllegalArgumentException(
          "Can't find product based on productIdentifier: " + productIdentifier);
    }
    List<String> assetLinksToBeDeletedFromS3 = productOptional.get().getProductImageUrls()
        .stream()
        .filter(link -> !updateProductRequest.getProductImageUrls().contains(link))
        .collect(Collectors.toList());
    Product product = Product.builder()
        .uniqueStoreName(storeIdentifier)
        .uniqueProductNameInStore(productIdentifier)
        .productName(updateProductRequest.getProductName())
        .productDescription(updateProductRequest.getProductDescription())
        .priceInDollar(updateProductRequest.getPriceInDollar())
        .productImageUrls(updateProductRequest.getProductImageUrls())
        .build();
    productService.updateProduct(product);
    assetLinksToBeDeletedFromS3.forEach(productAssetService::deleteS3Asset);
    return ResponseEntity.ok(product);
  }

  /**
   * Delete the specified product in the store.
   *
   * @param storeIdentifier the unique store name.
   * @param productIdentifier the unique product name in the store.
   * @return the deleted product, or 404 if not found.
   */
  @DeleteMapping("/{storeIdentifier}/products/{productIdentifier}")
  public ResponseEntity<Product> deleteProductInStore(
      @PathVariable String storeIdentifier,
      @PathVariable String productIdentifier) {
    log.info("Delete product with storeIdentifier={}, productIdentifier={}",
        storeIdentifier, productIdentifier);
    Optional<Product> productOptional =
        productService.deleteProductInStore(storeIdentifier, productIdentifier);
    return productOptional
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
}
