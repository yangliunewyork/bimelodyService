package eCommerceService.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

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
}
