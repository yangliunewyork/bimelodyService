package com.bimelody.ecommerceservice.model.request;

import java.math.BigDecimal;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A POJO request class for finding stores.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindStoresRequest {

  @QueryParam("storeCategories")
  private String storeCategories;

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
}
