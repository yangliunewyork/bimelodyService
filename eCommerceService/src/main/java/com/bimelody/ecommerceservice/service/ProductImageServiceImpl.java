package com.bimelody.ecommerceservice.service;

import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

/**
 * Implementation for {@link ProductImageService}.
 */
@Slf4j
@Service
@AllArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {

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
}
