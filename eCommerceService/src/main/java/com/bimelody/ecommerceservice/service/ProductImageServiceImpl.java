package com.bimelody.ecommerceservice.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Slf4j
@Service
@AllArgsConstructor
public class ProductImageServiceImpl implements ProductImageService{

    private final Environment environment;
    private final S3Presigner s3Presigner;
    private final S3Client s3Client;
    @Override
    public String getS3PreSignedUrl(@NonNull final String storeIdentifier,
                                    @NonNull final String productIdentifier,
                                    final int pictureOrder) {
        log.info("storeIdentifier: {}, productIdentifier: {}", storeIdentifier, productIdentifier);
        String objectKey = storeIdentifier + pictureOrder;
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(environment.getProperty("PRODUCT_IMAGE_BUCKET"))
                .key(objectKey)
                .build();

        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(30))
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedPutObjectRequest = s3Presigner.presignPutObject(putObjectPresignRequest);
        return presignedPutObjectRequest.url().toString();
    }
}
