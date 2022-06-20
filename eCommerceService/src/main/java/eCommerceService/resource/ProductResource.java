package eCommerceService.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/products")
public interface ProductResource {

  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response getProducts(
      @QueryParam("storeName") final String uniqueStoreName,
      @QueryParam("category") final String productCategory,
      @QueryParam("pageNum") @DefaultValue("1") int pageNum,
      @QueryParam("pageSize") @DefaultValue("10") int pageSize);
}
