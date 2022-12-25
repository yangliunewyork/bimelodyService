package com.bimelody.ecommerceservice.resource;

import com.bimelody.ecommerceservice.model.request.CreateProductRequest;
import com.bimelody.ecommerceservice.model.request.FindStoresRequest;
import com.bimelody.ecommerceservice.model.request.UpdateProductRequest;
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

  /**
   * Return a list of stores.
   * @param findStoresRequest An instance of {@link FindStoresRequest}
   * @return HTTP response contains stores information.
   */
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response getStores(@BeanParam final FindStoresRequest findStoresRequest);

  /**
   * Create a new store.
   * @param createStoresRequest An instance of {@link CreateStoreRequest}
   * @return  HTTP response confirming whether store creation succeed or not.
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response createStore(final CreateStoreRequest createStoresRequest);

  /**
   * Get information for a store.
   * @param unique_store_name Store identifier.
   * @return  HTTP response containing the store info.
   */
  @Path(value = "/{unique_store_name}")
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response getStoreInfo(@PathParam("unique_store_name") final String unique_store_name);

  /**
   * Update information for the specified store.
   *
   * @param unique_store_name Unique store name.
   * @param updateStoresRequest An instance of {@link UpdateStoresRequest}
   * @return HTTP response regarding update succeed or not.
   */
  @Path(value = "/{unique_store_name}")
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response updateStore(@PathParam("unique_store_name") final String unique_store_name,
                       final UpdateStoresRequest updateStoresRequest);

  /**
   * Return a list of products in the specified store.
   *
   * @param unique_store_name Unique store name.
   * @param productCategory Product category.
   * @param pageNum Page number.
   * @param pageSize Number of results per page.
   * @return  A list of products in the specified store.
   */
  @Path("{unique_store_name}/products")
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response getProductsInStore(
      @PathParam("unique_store_name") final String unique_store_name,
      @QueryParam("category") final String productCategory,
      @QueryParam("pageNum")  @DefaultValue("1") int pageNum,
      @QueryParam("pageSize") @DefaultValue("10") int pageSize);


  /**
   * Some operations not related to any resource.
   *
   * @param operationType
   * @return
   */
  @Path("{storeIdentifier}/operation")
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response operation(@PathParam("storeIdentifier") final String storeIdentifier,
                     @QueryParam("productName") final String productName,
                     @QueryParam("operationType") final String operationType);

  /**
   * Create a new product under the specified store.
   *
   * @param unique_store_name Unique store name.
   * @param createProductRequest An instance of {@link CreateProductRequest}.
   * @return HTTP response confirming whether request succeed or not.
   */
  @Path("{unique_store_name}/products")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response createProduct(@PathParam("unique_store_name") final String unique_store_name,
                          final CreateProductRequest createProductRequest);

  /**
   * Update the product in the store.
   *
   * @param unique_store_name Unique store name.
   * @param updateProductRequest An instance of {@link UpdateProductRequest}.
   * @return HTTP response confirming whether request succeed or not.
   */
  @Path("{unique_store_name}/products")
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response updateProduct(@PathParam("unique_store_name") final String unique_store_name,
                         final UpdateProductRequest updateProductRequest);


  /**
   * Get information about the specified product in the specified store.
   *
   * @param unique_store_name Unique store name.
   * @param unique_product_name Unique product name.
   * @return HTTP response confirming whether request succeed or not.
   */
  @Path("{unique_store_name}/products/{unique_product_name}")
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response getProductInfoFromStore(
      @PathParam("unique_store_name") final String unique_store_name,
      @PathParam("unique_product_name") final String unique_product_name);
}
