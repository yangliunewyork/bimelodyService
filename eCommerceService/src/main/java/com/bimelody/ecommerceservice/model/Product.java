package com.bimelody.ecommerceservice.model;

import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * A POJO class for product.
 */
@Builder
@Data
public class Product {
  private long productId;
  private String productName;
  private String uniqueProductNameInStore;
  private String productDescription;
  private BigDecimal priceInDollar;
  private List<String> productImageUrls;
  private List<String> productCategories;
  private List<String> productBrands;
  private String timestamp;

  // Add product store information below
  private String uniqueStoreName;
  private String storeName;
  private String storeLocation;
  private BigDecimal storeLocationLatitude;
  private BigDecimal storeLocationLongitude;
}
