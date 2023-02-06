package com.bimelody.ecommerceservice.model.request;

import java.math.BigDecimal;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A POJO request class for searching products.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchProductsRequest {

  /**
   * Latitude of address for searching stores.
   */
  @QueryParam("latitude")
  private BigDecimal latitude;

  /**
   * Longitude of address for searching stores.
   */
  @QueryParam("longitude")
  private BigDecimal longitude;

  /**
   * Distance in meters used to find store location.
   */
  @QueryParam("meters")
  private Long meters;

  /**
   * Northeast bound latitude used to find store location.
   */
  @QueryParam("neLat")
  private BigDecimal neLat;

  /**
   * Northeast bound longitude used to find store location.
   */
  @QueryParam("neLng")
  private BigDecimal neLng;

  /**
   * Southwest bound latitude used to find store location.
   */
  @QueryParam("swLat")
  private BigDecimal swLat;

  /**
   * Southwest bound longitude used to find store location.
   */
  @QueryParam("swLng")
  private BigDecimal swLng;

  /**
   * search results page number.
   */
  @QueryParam("pageNum")
  @DefaultValue("1")
  private Integer pageNum;

  /**
   * Number of search results returned per page.
   */
  @QueryParam("pageSize")
  @DefaultValue("10")
  private Integer pageSize;

  @QueryParam("productTags")
  private String productTags;

  /**
   * I don't think we need use both productCategories and productTags table.
   * Searching products should primarily be done with full text search.
   * Therefore, it makes more use to use productTag table, and deprecate
   * productCategories table.
   */
  @Deprecated
  @QueryParam("productCategories")
  private String productCategories;

}
