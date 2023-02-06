package com.bimelody.ecommerceservice.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * A POJO request class for creating store.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateStoreRequest {
  @NonNull
  private String uniqueStoreName;
  @NonNull
  private String storeName;
  @NonNull
  private String storeWebsite;
  @NonNull
  private String storeDescription;
  @NonNull
  private String contactNumber;
  @NonNull
  private String contactEmail;
  @NonNull
  private String storeLocation;
  private double storeLatitude;
  private double storeLongitude;
}
