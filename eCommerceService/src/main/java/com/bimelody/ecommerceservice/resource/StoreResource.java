package com.bimelody.ecommerceservice.resource;

import com.bimelody.ecommerceservice.model.request.CreateProductRequest;
import com.bimelody.ecommerceservice.model.request.FindStoresRequest;
import com.bimelody.ecommerceservice.model.request.UpdateStoresRequest;
import com.bimelody.ecommerceservice.model.request.CreateStoreRequest;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/stores")
public interface StoreResource {

  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response getStores(@BeanParam final FindStoresRequest findStoresRequest);

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response createStore(final CreateStoreRequest createStoresRequest);

  @Path(value = "/{unique_store_name}")
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response getStoreInfo(@PathParam("unique_store_name") final String unique_store_name);

  @Path(value = "/{unique_store_name}")
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response updateStore(@PathParam("unique_store_name") final String unique_store_name,
                       final UpdateStoresRequest updateStoresRequest);

  @Path("{unique_store_name}/products")
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response getProductsInStore(
      @PathParam("unique_store_name") final String unique_store_name,
      @QueryParam("category") final String productCategory,
      @QueryParam("pageNum")  @DefaultValue("1") int pageNum,
      @QueryParam("pageSize") @DefaultValue("10") int pageSize);

  @Path("{unique_store_name}/products")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response createProduct(@PathParam("unique_store_name") final String unique_store_name,
                          final CreateProductRequest createProductRequest);


  @Path("{unique_store_name}/products/{unique_product_name}")
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response getProductInfoFromStore(
      @PathParam("unique_store_name") final String unique_store_name,
      @PathParam("unique_product_name") final String unique_product_name);
}
