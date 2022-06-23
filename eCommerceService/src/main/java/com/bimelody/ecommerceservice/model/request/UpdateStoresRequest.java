package com.bimelody.ecommerceservice.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStoresRequest {
    private String storeName;
    private String storeWebsite;
    private String storeDescription;
    private String contactNumber;
    private String contactEmail;
}
