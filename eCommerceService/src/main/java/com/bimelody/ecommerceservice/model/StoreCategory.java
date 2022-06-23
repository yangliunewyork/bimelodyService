package com.bimelody.ecommerceservice.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StoreCategory {
    private long storeCategoryId;
    private String categoryType;
}
