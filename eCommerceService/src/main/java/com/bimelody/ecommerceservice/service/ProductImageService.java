package com.bimelody.ecommerceservice.service;

import lombok.NonNull;

/**
 * Service class for handling product image operations.
 */
public interface ProductImageService {
    /**
     * Return a S3 PreSigned URL for a product image.
     *
     * @param storeIdentifier Store identifier.
     * @param productIdentifier Product identifier in a store.
     * @param imageOrder  The order of the image for the product.
     * @return an S3 PreSigned URL.
     */
    String getS3PreSignedUrl(
            @NonNull final String storeIdentifier,
            @NonNull final String productIdentifier,
            final int imageOrder);
}
