package com.bimelody.ecommerceservice.service;

import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

/**
 * Implementation for {@link ProductAssetService}.
 */
@Slf4j
@Service
@AllArgsConstructor
public class ProductAssetServiceImpl implements ProductAssetService {

  private final Environment environment;

  @Qualifier("AssetAwsS3Presigner")
  private final S3Presigner s3Presigner;

  @Qualifier("AssetAwsS3Client")
  private final S3Client s3Client;

  private final StoreService storeService;

  private static final String IMAGE_FOLDER_NAME = "images";

  @Override
  public String getS3PreSignedUrl(@NonNull final String storeIdentifier,
                                  @NonNull final String imageName,
                                  final int imageOrder) {
    String objectKey = IMAGE_FOLDER_NAME + '/' + storeIdentifier + '/' + imageName;
    log.info("objectKey is {}", objectKey);
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(environment.getProperty("PRODUCT_IMAGE_BUCKET"))
        .key(objectKey)
        .build();

    PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(30))
        .putObjectRequest(putObjectRequest)
        .build();
    PresignedPutObjectRequest presignedPutObjectRequest =
        s3Presigner.presignPutObject(putObjectPresignRequest);
    return presignedPutObjectRequest.url().toString();
  }

  @Override
  public void deleteS3Asset(final String assetLink) {
    if (StringUtils.isBlank(assetLink)
        || !assetLink.contains("images/")) {
      throw new IllegalArgumentException("Invalid asset link: " + assetLink);
    }
    final String objectKey = assetLink.substring(assetLink.indexOf("images/"));
    DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
        .bucket(environment.getProperty("PRODUCT_IMAGE_BUCKET"))
        .key(objectKey)
        .build();
    s3Client.deleteObject(deleteObjectRequest);
  }
}
