package com.bimelody.ecommerceservice.controller;

import com.bimelody.ecommerceservice.model.StoreCategory;
import com.bimelody.ecommerceservice.service.StoreService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling store categories.
 */
@RestController
@RequestMapping("/store-categories")
@RequiredArgsConstructor
public class StoreCategoryController {

  private final StoreService storeService;

  /**
   * Return all store categories.
   *
   * @return list of store categories.
   */
  @GetMapping
  public ResponseEntity<List<StoreCategory>> getAllStoreCategories() {
    return ResponseEntity.ok(storeService.getAllStoreCategories());
  }
}
