package com.bimelody.ecommerceservice.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.List;

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