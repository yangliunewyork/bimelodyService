package eCommerceService.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindStoresRequest {

  @QueryParam("storeCategories")
  private String storeCategories;

  @QueryParam("latitude")
  private Double latitude;

  @QueryParam("longitude")
  private Double longitude;

  @QueryParam("distance")
  private Double distance;

  @QueryParam("pageNum")
  @DefaultValue("1")
  private Integer pageNum;

  @QueryParam("pageSize")
  @DefaultValue("10")
  private Integer pageSize;
}
