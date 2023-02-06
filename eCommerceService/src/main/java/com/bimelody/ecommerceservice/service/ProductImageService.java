package com.bimelody.ecommerceservice.service;

import lombok.NonNull;

/**
 * Service class that contains business logic for handling product image.
 */
public interface ProductImageService {
  /**
   * Return a S3 PreSigned URL for a product image.
   *
   * @param storeIdentifier Store identifier.
   * @param imageOrder      The order of the image for the product.
   * @return an S3 PreSigned URL.
   */
  String getS3PreSignedUrl(
      @NonNull final String storeIdentifier,
      @NonNull final String imageName,
      final int imageOrder);
}
