package com.bimelody.ecommerceservice.repository;

import com.bimelody.ecommerceservice.model.Product;
import com.bimelody.ecommerceservice.dataaccesslayer.tables.ProductBrand;
import com.bimelody.ecommerceservice.dataaccesslayer.tables.ProductBrandMap;
import com.bimelody.ecommerceservice.dataaccesslayer.tables.ProductCategory;
import com.bimelody.ecommerceservice.dataaccesslayer.tables.ProductCategoryMap;
import com.bimelody.ecommerceservice.dataaccesslayer.tables.ProductImage;
import com.bimelody.ecommerceservice.dataaccesslayer.tables.Store;
import com.bimelody.ecommerceservice.dataaccesslayer.tables.records.ProductImageRecord;
import com.bimelody.ecommerceservice.dataaccesslayer.tables.records.ProductRecord;
import com.bimelody.ecommerceservice.dataaccesslayer.tables.records.StoreRecord;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.groupConcat;
import static org.jooq.impl.DSL.noCondition;

@Slf4j
@Repository
@AllArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

  private DSLContext jooqDslContext;
  private static final String GROUP_RESULT_SEPARATOR = "@@@";
  private static final String PRODUCT_IMAGES_FIELD = "ProductImages";
  private static final String PRODUCT_CATEGORIES_FIELD = "ProductCategories";
  private static final String PRODUCT_BRANDS_FIELD = "ProductBrands";

  private static final com.bimelody.ecommerceservice.dataaccesslayer.tables.Product PRODUCT_TABLE
          = com.bimelody.ecommerceservice.dataaccesslayer.tables.Product.PRODUCT;
  private static final Store STORE_TABLE = Store.STORE;
  private static final ProductImage PRODUCT_IMAGE_TABLE = ProductImage.PRODUCT_IMAGE;
  private static final ProductBrand PRODUCT_BRAND_TABLE = ProductBrand.PRODUCT_BRAND;
  private static final ProductBrandMap PRODUCT_BRAND_MAP_TABLE = ProductBrandMap.PRODUCT_BRAND_MAP;
  private static final ProductCategory PRODUCT_CATEGORY_TABLE = ProductCategory.PRODUCT_CATEGORY;
  private static final ProductCategoryMap PRODUCT_CATEGORY_MAP_TABLE =
      ProductCategoryMap.PRODUCT_CATEGORY_MAP;

  private static final List<SelectField<?>> SELECTED_FIELDS =
      Arrays.asList(
          PRODUCT_TABLE.PRODUCT_ID,
          PRODUCT_TABLE.PRODUCT_NAME,
          PRODUCT_TABLE.UNIQUE_PRODUCT_NAME_IN_STORE,
          PRODUCT_TABLE.PRODUCT_DESCRIPTION,
          PRODUCT_TABLE.PRICE_IN_DOLLAR,
          STORE_TABLE.UNIQUE_STORE_NAME,
          STORE_TABLE.STORE_NAME,
          groupConcat(PRODUCT_IMAGE_TABLE.PRODUCT_IMAGE_LINK)
              .separator(GROUP_RESULT_SEPARATOR)
              .as(PRODUCT_IMAGES_FIELD),
          groupConcat(PRODUCT_CATEGORY_TABLE.CATEGORY_TYPE)
              .separator(GROUP_RESULT_SEPARATOR)
              .as(PRODUCT_CATEGORIES_FIELD),
          groupConcat(PRODUCT_BRAND_TABLE.BRAND_NAME)
              .separator(GROUP_RESULT_SEPARATOR)
              .as(PRODUCT_BRANDS_FIELD),
          PRODUCT_TABLE.CREATION_TIME);

  private static final Table<?> FROM_CLAUSE =
      PRODUCT_TABLE
          .join(STORE_TABLE)
          .on(PRODUCT_TABLE.STORE_ID.eq(STORE_TABLE.STORE_ID))
          .leftJoin(PRODUCT_IMAGE_TABLE)
          .on(PRODUCT_TABLE.PRODUCT_ID.eq(PRODUCT_IMAGE_TABLE.PRODUCT_ID))
          .leftJoin(PRODUCT_BRAND_MAP_TABLE)
          .on(PRODUCT_BRAND_MAP_TABLE.PRODUCT_ID.eq(PRODUCT_TABLE.PRODUCT_ID))
          .leftJoin(PRODUCT_BRAND_TABLE)
          .on(PRODUCT_BRAND_MAP_TABLE.PRODUCT_BRAND_ID.eq(PRODUCT_BRAND_TABLE.PRODUCT_BRAND_ID))
          .leftJoin(PRODUCT_CATEGORY_MAP_TABLE)
          .on(PRODUCT_CATEGORY_MAP_TABLE.PRODUCT_ID.eq(PRODUCT_TABLE.PRODUCT_ID))
          .leftJoin(PRODUCT_CATEGORY_TABLE)
          .on(
              PRODUCT_CATEGORY_MAP_TABLE.PRODUCT_CATEGORY_ID.eq(
                  PRODUCT_CATEGORY_TABLE.PRODUCT_CATEGORY_ID));

  @Override
  public void createProduct(@NonNull Product product) {
    jooqDslContext.transaction(
        transaction -> {
          StoreRecord storeRecord =
              jooqDslContext.fetchOne(
                  STORE_TABLE, STORE_TABLE.UNIQUE_STORE_NAME.eq(product.getUniqueStoreName()));
          if (storeRecord == null) {
              throw new IllegalStateException("Can't find store " + product.getUniqueStoreName() + "in database!");
          }

          ProductRecord productRecord = jooqDslContext.newRecord(PRODUCT_TABLE);
          productRecord.setStoreId(storeRecord.getStoreId());
          productRecord.setUniqueProductNameInStore(product.getUniqueProductNameInStore());
          productRecord.setProductName(product.getProductName());
          productRecord.setProductDescription(product.getProductDescription());
          productRecord.setPriceInDollar(product.getPriceInDollar());
          productRecord.store();

          product.getProductImageUrls().stream()
              .filter(StringUtils::isNotBlank)
              .forEach(
                  imageUrl -> {
                    ProductImageRecord productImageRecord =
                        jooqDslContext.newRecord(PRODUCT_IMAGE_TABLE);
                    productImageRecord.setProductId(productRecord.getProductId());
                    productImageRecord.setProductImageLink(imageUrl);
                    productImageRecord.store();
                  });
        });
  }

  @Override
  public List<Product> searchProducts(
      final String uniqueStoreName, final String productCategory, int pageNum, int pageSize) {
    List<Condition> conditions = new ArrayList<>();
    if (uniqueStoreName != null) {
      conditions.add(STORE_TABLE.UNIQUE_STORE_NAME.eq(uniqueStoreName));
    }
    if (productCategory != null) {
      conditions.add(ProductCategory.PRODUCT_CATEGORY.CATEGORY_TYPE.eq(productCategory));
    }
    Condition where = conditions.isEmpty() ? noCondition() : DSL.and(conditions);

    Result<org.jooq.Record> records =
        jooqDslContext
            .select(SELECTED_FIELDS)
            .from(FROM_CLAUSE)
            .where(where)
            .groupBy(PRODUCT_TABLE.PRODUCT_ID)
            .orderBy(PRODUCT_TABLE.CREATION_TIME.desc())
            .limit(pageSize)
            .offset(pageSize * (pageNum - 1))
            .fetch();

    return buildProductListWithReturnedDatabaseRecords(records);
  }

  @Override
  public Optional<Product> findProductInfoFromStore(
      final String uniqueStoreName, final String uniqueProductNameInStore) {
    List<Condition> conditions = new ArrayList<>();
    conditions.add(STORE_TABLE.UNIQUE_STORE_NAME.eq(uniqueStoreName));
    conditions.add(PRODUCT_TABLE.UNIQUE_PRODUCT_NAME_IN_STORE.eq(uniqueProductNameInStore));
    Condition where = DSL.and(conditions);
    Result<org.jooq.Record> records =
        jooqDslContext
            .select(SELECTED_FIELDS)
            .from(FROM_CLAUSE)
            .where(where)
            .groupBy(PRODUCT_TABLE.PRODUCT_ID)
            .fetch();
    List<Product> products =
        buildProductListWithReturnedDatabaseRecords(records);

    if (products.size() > 1) {
      throw new IllegalStateException(
          String.format(
              "Find more than 1 product records for store name %s , product name %s!",
              uniqueStoreName, uniqueProductNameInStore));
    }

    return products.isEmpty() ? Optional.empty() : Optional.of(products.get(0));
  }

  private List<Product> buildProductListWithReturnedDatabaseRecords(
      @NonNull final Result<org.jooq.Record> records) {
    return records.stream()
        .map(
            record -> {
              return Product
                  .builder()
                  .productId(record.getValue(PRODUCT_TABLE.PRODUCT_ID).longValue())
                  .productName(record.getValue(PRODUCT_TABLE.PRODUCT_NAME))
                  .uniqueProductNameInStore(
                      record.getValue(PRODUCT_TABLE.UNIQUE_PRODUCT_NAME_IN_STORE))
                  .productDescription(record.getValue(PRODUCT_TABLE.PRODUCT_DESCRIPTION))
                  .priceInDollar(record.getValue(PRODUCT_TABLE.PRICE_IN_DOLLAR))
                  .productBrands(concatenateGroupValues(record, PRODUCT_BRANDS_FIELD))
                  .productCategories(concatenateGroupValues(record, PRODUCT_BRANDS_FIELD))
                  .productImageUrls(concatenateGroupValues(record, PRODUCT_IMAGES_FIELD))
                  .timestamp(
                      record
                          .getValue(PRODUCT_TABLE.CREATION_TIME)
                          .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                  .storeName(record.getValue(STORE_TABLE.STORE_NAME))
                  .uniqueStoreName(record.getValue(STORE_TABLE.UNIQUE_STORE_NAME))
                  .build();
            })
        .collect(Collectors.toList());
  }

  private List<String> concatenateGroupValues(
      @NonNull final org.jooq.Record record, @NonNull final String fieldName) {
    final Object value = record.getValue(fieldName);
    return value != null
        ? Arrays.asList(value.toString().split(GROUP_RESULT_SEPARATOR))
        : Collections.emptyList();
  }
}
