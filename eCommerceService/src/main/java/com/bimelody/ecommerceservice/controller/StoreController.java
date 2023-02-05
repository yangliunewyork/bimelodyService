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
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class StoreController implements StoreResource {

  private enum OperationType {
    GenerateProductIdentifierInStore
  }

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

  public Response getStoreInfo(final String storeIdentifier){
    final Optional<Store> storeOptional = storeService.getStoreInfo(storeIdentifier);
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
  public Response updateStore(String storeIdentifier, final UpdateStoresRequest updateStoresRequest) {
    //log.info("updateStoresRequest is {}", updateStoresRequest);
    Store store =
            Store.builder()
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
    return Response.status(Response.Status.OK)
            .type(MediaType.APPLICATION_JSON)
            .build();
  }

  public Response createProduct(final String storeIdentifier,
                                final CreateProductRequest createProductRequest) {
    Product product = Product.builder()
            .uniqueStoreName(storeIdentifier)
            .uniqueProductNameInStore(storeService .generateProductIdentifierInStore(storeIdentifier,
                    createProductRequest.getProductName()))
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

  public Response updateProduct(final String storeIdentifier,
                                final UpdateProductRequest updateProductRequest) {
    if (StringUtils.isBlank(storeIdentifier)) {
      throw new IllegalArgumentException("storeIdentifier is invalid: " + storeIdentifier);
    }
    if (StringUtils.isBlank(updateProductRequest.getUniqueProductNameInStore())) {
      throw new IllegalArgumentException("Invalid uniqueProductNameInStore: " + updateProductRequest.getUniqueProductNameInStore());
    }
    Optional<Product> productOptional = productService
            .findProductInfoFromStore(storeIdentifier, updateProductRequest.getUniqueProductNameInStore());
    if (productOptional.isEmpty()) {
      throw new IllegalArgumentException("Can't find product based on uniqueProductNameInStore: " + updateProductRequest.getUniqueProductNameInStore());
    }

    Product product = Product.builder()
            .uniqueStoreName(storeIdentifier)
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
      final String storeIdentifier, final String productCategory, int pageNum, int pageSize) {
    List<Product> products = productService.searchProductsInStore(storeIdentifier, productCategory, pageNum, pageSize);
    return Response.status(Response.Status.OK)
            .entity(products)
            .type(MediaType.APPLICATION_JSON)
            .build();
  }

  /**
   * This API is not being used.
   * @param storeIdentifier
   * @param productName
   * @param operationType
   * @return
   */
  @Deprecated
  @Override
  public Response operation(final String storeIdentifier,
                            final String productName,
                            final String operationType) {
    if (OperationType.GenerateProductIdentifierInStore
            .equals(OperationType.valueOf(operationType))) {
      if (StringUtils.isBlank(productName)) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("productName parameter is missing :" + productName)
                .type(MediaType.APPLICATION_JSON)
                .build();
      }
      final String productIdentifier = storeService.generateProductIdentifierInStore(storeIdentifier, productName);
      return Response.status(Response.Status.OK)
              .entity(Map.entry("productIdentifier", productIdentifier))
              .type(MediaType.APPLICATION_JSON)
              .build();
    }
    return Response.status(Response.Status.BAD_REQUEST)
            .entity("Bad operation type:" + operationType)
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

  @Override
  public Response deleteProductInStore(String storeIdentifier, String productIdentifier) {
    log.info("Delete product with storeIdentifier={}, productIdentifier={}", storeIdentifier, productIdentifier);

    Optional<Product> productOptional = productService
            .deleteProductInStore(storeIdentifier, productIdentifier);
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
}
