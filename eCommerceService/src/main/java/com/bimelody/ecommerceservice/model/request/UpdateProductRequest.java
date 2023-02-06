package com.bimelody.ecommerceservice.model.request;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * A POJO request class for updating product information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {
  @NonNull
  private String uniqueProductNameInStore;
  @NonNull
  private String productName;
  @NonNull
  private String productDescription;
  @NonNull
  private BigDecimal priceInDollar;
}