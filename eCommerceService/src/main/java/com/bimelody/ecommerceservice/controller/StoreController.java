package com.bimelody.ecommerceservice.controller;

import com.bimelody.ecommerceservice.model.Product;
import com.bimelody.ecommerceservice.model.Store;
import com.bimelody.ecommerceservice.model.request.CreateProductRequest;
import com.bimelody.ecommerceservice.model.request.CreateStoreRequest;
import com.bimelody.ecommerceservice.model.request.FindStoresRequest;
import com.bimelody.ecommerceservice.model.request.UpdateProductRequest;
import com.bimelody.ecommerceservice.model.request.UpdateStoresRequest;
import com.bimelody.ecommerceservice.resource.StoreResource;
import com.bimelody.ecommerceservice.service.ProductService;
import com.bimelody.ecommerceservice.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class StoreController implements StoreResource {

  private final StoreService storeService;

  private final ProductService productService;

  public Response getStores(final FindStoresRequest findStoresRequest) {
    final List<Store>  stores = storeService.findStores(findStoresRequest);
    return Response.status(Response.Status.OK)
            .entity(stores)
            .type(MediaType.APPLICATION_JSON)
            .build();
  }

  public Response createStore(final CreateStoreRequest createStoresRequest) {
    Store store =
        Store.builder()
            .uniqueStoreName(createStoresRequest.getUniqueStoreName())
            .storeName(createStoresRequest.getStoreName())
            .storeWebsite(createStoresRequest.getStoreWebsite())
            .contactEmail(createStoresRequest.getContactEmail())
            .contactNumber(createStoresRequest.getContactNumber())
            .storeDescription(createStoresRequest.getStoreDescription())
            .build();
    storeService.createStore(store);
    return Response.status(Response.Status.OK)
            .type(MediaType.APPLICATION_JSON)
            .build();
  }

  public Response getStoreInfo(final String unique_store_name){
    final Optional<Store> storeOptional = storeService.getStoreInfo(unique_store_name);
    if (storeOptional.isPresent()) {
      return Response.status(Response.Status.OK)
              .entity(storeOptional.get())
              .type(MediaType.APPLICATION_JSON)
              .build();
    } else {
      return Response.status(Response.Status.NOT_FOUND)
              .type(MediaType.APPLICATION_JSON)
              .build();
    }
  }

  @Override
  public Response updateStore(String unique_store_name, final UpdateStoresRequest updateStoresRequest) {
    log.info("createStoresRequest is {}", updateStoresRequest);
    Store store =
            Store.builder()
                    .uniqueStoreName(unique_store_name)
                    .storeName(updateStoresRequest.getStoreName())
                    .storeWebsite(updateStoresRequest.getStoreWebsite())
                    .contactEmail(updateStoresRequest.getContactEmail())
                    .contactNumber(updateStoresRequest.getContactNumber())
                    .storeDescription(updateStoresRequest.getStoreDescription())
                    .build();
    storeService.updateStore(store);
    return Response.status(Response.Status.OK)
            .type(MediaType.APPLICATION_JSON)
            .build();
  }

  public Response createProduct(final String uniqueStoreName,
                                final CreateProductRequest createProductRequest) {
    Product product = Product.builder()
            .uniqueStoreName(uniqueStoreName)
            .uniqueProductNameInStore(generateUniqueProductNameInStore(uniqueStoreName, createProductRequest.getProductName()))
            .productName(createProductRequest.getProductName())
            .productDescription(createProductRequest.getProductDescription())
            .priceInDollar(createProductRequest.getPriceInDollar())
            .productImageUrls(createProductRequest.getProductImageUrls())
            .build();

    productService.createProduct(product);
    return Response.status(Response.Status.OK)
            .type(MediaType.APPLICATION_JSON)
            .entity(product)
            .build();
  }

  public Response updateProduct(final String uniqueStoreName,
                                final UpdateProductRequest updateProductRequest) {
    if (StringUtils.isBlank(uniqueStoreName)) {
      throw new IllegalArgumentException("uniqueStoreName is invalid: " + uniqueStoreName);
    }
    if (StringUtils.isBlank(updateProductRequest.getUniqueProductNameInStore())) {
      throw new IllegalArgumentException("Invalid uniqueProductNameInStore: " + updateProductRequest.getUniqueProductNameInStore());
    }
    Optional<Product> productOptional = productService
            .findProductInfoFromStore(uniqueStoreName, updateProductRequest.getUniqueProductNameInStore());
    if (productOptional.isEmpty()) {
      throw new IllegalArgumentException("Can't find product based on uniqueProductNameInStore: " + updateProductRequest.getUniqueProductNameInStore());
    }

    Product product = Product.builder()
            .uniqueStoreName(uniqueStoreName)
            .uniqueProductNameInStore(updateProductRequest.getUniqueProductNameInStore())
            .productName(updateProductRequest.getProductName())
            .productDescription(updateProductRequest.getProductDescription())
            .priceInDollar(updateProductRequest.getPriceInDollar())
            .build();

    productService.updateProduct(product);
    return Response.status(Response.Status.OK)
            .type(MediaType.APPLICATION_JSON)
            .entity(product)
            .build();
  }

  public Response getProductsInStore(
      final String unique_store_name, final String productCategory, int pageNum, int pageSize) {
    List<Product> products = productService.searchProducts(unique_store_name, productCategory, pageNum, pageSize);
    return Response.status(Response.Status.OK)
            .entity(products)
            .type(MediaType.APPLICATION_JSON)
            .build();
  }

  public Response getProductInfoFromStore(
      String storeIdentifier, String productIdentifierInStore) {
    Optional<Product> productOptional = productService
            .findProductInfoFromStore(storeIdentifier, productIdentifierInStore);
    if (productOptional.isPresent()) {
      return Response.status(Response.Status.OK)
              .entity(productOptional.get())
              .type(MediaType.APPLICATION_JSON)
              .build();
    } else {
      return Response.status(Response.Status.NOT_FOUND)
              .type(MediaType.APPLICATION_JSON)
              .build();
    }
  }

  private String generateUniqueProductNameInStore(final String uniqueStoreName,
                                                  final String productName) {
    final StringBuilder uniqueProductNameInStoreStringBuilder = new StringBuilder();
    // Replacing all non-alphanumeric characters with empty strings
    uniqueProductNameInStoreStringBuilder.append(productName.replaceAll("[^A-Za-z0-9]", ""));
    String randomSuffix = "";
    for (int i = 0; i < 3; ++i) {
      Optional<Product> productOptional = productService
              .findProductInfoFromStore(uniqueStoreName,
                      uniqueProductNameInStoreStringBuilder.toString() + randomSuffix);
      if (productOptional.isPresent()) {
        randomSuffix = UUID.randomUUID().toString().substring(0,6);
      } else {
        return uniqueProductNameInStoreStringBuilder.toString() + randomSuffix;
      }
    }
    log.error("Failed to create product in store {}",uniqueStoreName);
    throw new IllegalStateException(String.format("Failed to create product in store: %s!", uniqueStoreName));
  }

}
