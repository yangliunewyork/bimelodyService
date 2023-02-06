package com.bimelody.ecommerceservice.model;

import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * A POJO class for Store information.
 */
@Builder
@Data
public class Store {
  private long storeId;
  private String uniqueStoreName;
  private String storeName;
  private String storeWebsite;
  private String storeDescription;
  private String contactNumber;
  private String contactEmail;
  private String storeCoverImage;
  private String yelpLink;
  private String facebookLink;
  private String instagramLink;
  private String twitterLink;
  private String storeLocation;
  private BigDecimal storeLocationLatitude;
  private BigDecimal storeLocationLongitude;
  private List<String> storeCategories;
}
