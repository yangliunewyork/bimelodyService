package com.bimelody.ecommerceservice.model;

import lombok.Builder;
import lombok.Data;

/**
 * A POJO class for store category.
 */
@Builder
@Data
public class StoreCategory {
  private long storeCategoryId;
  private String categoryType;
}
