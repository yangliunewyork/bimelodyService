package com.bimelody.ecommerceservice.model.request;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A POJO request class for searching products.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchProductsRequest {

  private BigDecimal latitude;
  private BigDecimal longitude;
  private Long meters;
  private BigDecimal neLat;
  private BigDecimal neLng;
  private BigDecimal swLat;
  private BigDecimal swLng;
  @Builder.Default
  private Integer pageNum = 1;
  @Builder.Default
  private Integer pageSize = 10;
  private String productTags;

  @Deprecated
  private String productCategories;
}
