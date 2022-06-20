package eCommerceService.controller;

import eCommerceService.model.StoreCategory;
import eCommerceService.resource.StoreCategoryResource;
import eCommerceService.service.StoreService;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class StoreCategoryController implements StoreCategoryResource {

  private final StoreService storeService;

  @Override
  public Response getAllStoreCategories() {
    List<StoreCategory> categories = storeService.getAllStoreCategories();
    return Response.status(Response.Status.OK)
            .entity(categories)
            .type(MediaType.APPLICATION_JSON)
            .build();
  }
}
