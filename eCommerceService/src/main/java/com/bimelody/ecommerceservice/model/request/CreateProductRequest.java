package com.bimelody.ecommerceservice.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {
    private String uniqueProductNameInStore;
    private String productName;
    private String productDescription;
    private BigDecimal priceInDollar;
    private List<String> productImageUrls;
}
