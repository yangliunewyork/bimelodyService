package com.bimelody.ecommerceservice.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Resource class for product image.
 */
@Path("/product-image-assets")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ProductImageAssetResource {

  /**
   * Return presigned URL so that frontend can use it to upload asset directly to S3 bucket.
   *
   * @param storeIdentifier Store identifier.
   * @param imageName       the name of the image.
   * @param imageOrder      the order of the image in the image list of the product.
   * @return A Response instance.
   */
  @GET
  Response getPresignedUrl(
      @QueryParam("storeIdentifier") final String storeIdentifier,
      @QueryParam("imageName") final String imageName,
      @QueryParam("imageOrder") final int imageOrder);

}
