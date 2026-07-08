package com.bimelody.ecommerceservice.controller;

import com.bimelody.ecommerceservice.service.ProductAssetService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling product image assets.
 */
@Slf4j
@RestController
@RequestMapping("/product-image-assets")
@RequiredArgsConstructor
public class ProductImageAssetController {

  private final ProductAssetService productAssetService;

  /**
   * Return a presigned S3 URL for direct frontend upload.
   *
   * @param storeIdentifier the store identifier.
   * @param imageName the name of the image.
   * @param imageOrder the order of the image in the product image list.
   * @return a map containing the presigned URL.
   */
  @GetMapping
  public ResponseEntity<Map<String, String>> getPresignedUrl(
      @RequestParam String storeIdentifier,
      @RequestParam String imageName,
      @RequestParam int imageOrder) {
    String preSignedUrl = productAssetService
        .getS3PreSignedUrl(storeIdentifier, imageName, imageOrder);
    return ResponseEntity.ok(Map.of("presignedUrl", preSignedUrl));
  }
}
