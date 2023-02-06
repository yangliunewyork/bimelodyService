package com.bimelody.ecommerceservice.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

/**
 * Provide beans for S3 access of assets AWS account.
 */
@Configuration
public class AssetS3ClientModule {

  /**
   * Provide the AWS credential provider bean of the assets AWS account .
   *
   * @param environment The environment in which the current application is running
   * @return A StaticCredentialsProvider instance.
   */
  @Bean("AssetAwsCredentialsProvider")
  public static StaticCredentialsProvider getAwsCredentialsProvider(Environment environment) {
    return StaticCredentialsProvider.create(
        AwsBasicCredentials.create(
            environment.getProperty("ASSET_AWS_ACCOUNT_ACCESS_KEY_ID"),
            environment.getProperty("ASSET_AWS_ACCOUNT_SECRET_ACCESS_KEY")
        )
    );
  }

  /**
   * Provide S3Client bean  for accessing S3 buckets in assets AWS account.
   *
   * @param awsCredentialsProvider AWS credential provider.
   * @return An instance of S3Client for accessing S3 buckets in assets AWS account.
   */
  @Bean("AssetAwsS3Client")
  public static S3Client provideS3Client(
      @Qualifier("AssetAwsCredentialsProvider") AwsCredentialsProvider awsCredentialsProvider) {
    return S3Client.builder().credentialsProvider(awsCredentialsProvider).build();
  }

  /**
   * Provide S3Presigner bean of assets AWS account.
   *
   * @param awsCredentialsProvider  AWS credential provider.
   * @return An instance of S3Presigner.
   */
  @Bean("AssetAwsS3Presigner")
  public static S3Presigner provideS3Presigner(
      @Qualifier("AssetAwsCredentialsProvider") AwsCredentialsProvider awsCredentialsProvider) {
    return S3Presigner.builder().credentialsProvider(awsCredentialsProvider).build();
  }

}

