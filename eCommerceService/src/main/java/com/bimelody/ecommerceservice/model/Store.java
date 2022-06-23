package com.bimelody.ecommerceservice.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

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
  private Double storeLocationLatitude;
  private Double storeLocationLongitude;
  private List<String> storeCategories;
}
