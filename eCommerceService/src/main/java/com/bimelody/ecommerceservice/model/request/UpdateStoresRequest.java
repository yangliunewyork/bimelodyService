package com.bimelody.ecommerceservice.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStoresRequest {
    @NonNull
    @Size(min=4, message="storeName should have at least 4 characters")
    private String storeName;
    @NonNull
    @Size(min=10, message="storeWebsite should have at least 10 characters")
    private String storeWebsite;
    @NonNull
    @Size(min=10, message="storeDescription should have at least 10 characters")
    private String storeDescription;
    @NonNull
    @Size(min=7, message="contactNumber should have at least 7 characters")
    private String contactNumber;
    @NonNull
    @Size(min=10, message="contactEmail should have at least 10 characters")
    private String contactEmail;
    @NonNull
    @Size(min=10, message="storeLocation should have at least 10 characters")
    private String storeLocation;

    /**
     * Use double for now as database use
     * https://stackoverflow.com/questions/6754881/java-double-vs-bigdecimal-for-latitude-longitude
     */
    @NonNull
    private BigDecimal storeLocationLatitude;

    @NonNull
    private BigDecimal storeLocationLongitude;
}
