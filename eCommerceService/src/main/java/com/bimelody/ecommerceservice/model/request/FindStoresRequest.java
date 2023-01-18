package com.bimelody.ecommerceservice.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindStoresRequest {

  @QueryParam("storeCategories")
  private String storeCategories;

  @QueryParam("latitude")
  private BigDecimal latitude;

  @QueryParam("longitude")
  private BigDecimal longitude;

  @QueryParam("meters")
  private Long meters;

  @QueryParam("pageNum")
  @DefaultValue("1")
  private Integer pageNum;

  @QueryParam("pageSize")
  @DefaultValue("10")
  private Integer pageSize;
}
