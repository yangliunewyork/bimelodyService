package com.bimelody.ecommerceservice.resource;

import com.bimelody.ecommerceservice.model.request.CreateProductRequest;
import com.bimelody.ecommerceservice.model.request.CreateStoreRequest;
import com.bimelody.ecommerceservice.model.request.FindStoresRequest;
import com.bimelody.ecommerceservice.model.request.UpdateProductRequest;
import com.bimelody.ecommerceservice.model.request.UpdateStoresRequest;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

/**
 * Resource for store.
 */
@Path("/stores")
public interface StoreResource {

  /**
   * Return a list of stores.
   *
   * @param findStoresRequest An instance of {@link FindStoresRequest}
   * @return HTTP response contains stores information.
   */
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response getStores(@BeanParam final FindStoresRequest findStoresRequest);

  /**
   * Create a new store.
   *
   * @param createStoresRequest An instance of {@link CreateStoreRequest}
   * @return HTTP response confirming whether store creation succeed or not.
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response createStore(final CreateStoreRequest createStoresRequest);

  /**
   * Get information for a store.
   *
   * @param storeIdentifier Store identifier.
   * @return HTTP response containing the store info.
   */
  @Path(value = "/{storeIdentifier}")
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response getStoreInfo(@PathParam("storeIdentifier") final String storeIdentifier);

  /**
   * Update information for the specified store.
   *
   * @param storeIdentifier     Unique store name.
   * @param updateStoresRequest An instance of {@link UpdateStoresRequest}
   * @return HTTP response regarding update succeed or not.
   */
  @Path(value = "/{storeIdentifier}")
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response updateStore(@PathParam("storeIdentifier") final String storeIdentifier,
                       final UpdateStoresRequest updateStoresRequest);

  /**
   * Return a list of products in the specified store.
   *
   * @param storeIdentifier storeIdentifier: Unique store name.
   * @param productCategory Product category.
   * @param pageNum         Page number.
   * @param pageSize        Number of results per page.
   * @return A list of products in the specified store.
   */
  @Path("{storeIdentifier}/products")
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response getProductsInStore(
      @PathParam("storeIdentifier") final String storeIdentifier,
      @QueryParam("category") final String productCategory,
      @QueryParam("pageNum") @DefaultValue("1") int pageNum,
      @QueryParam("pageSize") @DefaultValue("10") int pageSize);

  /**
   * Create a new product under the specified store.
   *
   * @param storeIdentifier      Unique store name.
   * @param createProductRequest An instance of {@link CreateProductRequest}.
   * @return HTTP response confirming whether request succeed or not.
   */
  @Path("{storeIdentifier}/products")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response createProduct(@PathParam("storeIdentifier") final String storeIdentifier,
                         final CreateProductRequest createProductRequest);

  /**
   * Update the product in the store.
   *
   * @param storeIdentifier      Unique store name.
   * @param productIdentifier    Unique product name in the store.
   * @param updateProductRequest An instance of {@link UpdateProductRequest}.
   * @return HTTP response confirming whether request succeed or not.
   */
  @Path("{storeIdentifier}/products/{productIdentifier}")
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response updateProduct(@PathParam("storeIdentifier") final String storeIdentifier,
                         @PathParam("productIdentifier") final String productIdentifier,
                         final UpdateProductRequest updateProductRequest);


  /**
   * Get information about the specified product in the specified store.
   *
   * @param storeIdentifier   Unique store name.
   * @param productIdentifier Unique product name.
   * @return HTTP response confirming whether request succeed or not.
   */
  @Path("{storeIdentifier}/products/{productIdentifier}")
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response getProductInfoFromStore(
      @PathParam("storeIdentifier") final String storeIdentifier,
      @PathParam("productIdentifier") final String productIdentifier);

  /**
   * Delete the specified product in the store.
   *
   * @param storeIdentifier   Unique store name.
   * @param productIdentifier Unique product name.
   * @return HTTP response confirming whether request succeed or not.
   */
  @Path("{storeIdentifier}/products/{productIdentifier}")
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response deleteProductInStore(
      @PathParam("storeIdentifier") final String storeIdentifier,
      @PathParam("productIdentifier") final String productIdentifier);
}
