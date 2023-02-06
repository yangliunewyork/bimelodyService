package com.bimelody.ecommerceservice.controller;

import com.bimelody.ecommerceservice.model.StoreCategory;
import com.bimelody.ecommerceservice.resource.StoreCategoryResource;
import com.bimelody.ecommerceservice.service.StoreService;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

/**
 * Controller for handling StoreCategoryResource.
 */
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
