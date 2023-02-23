package com.bimelody.ecommerceservice.repository;

import static com.bimelody.ecommerceservice.dataaccesslayer.Tables.PRODUCT;
import static com.bimelody.ecommerceservice.dataaccesslayer.Tables.PRODUCT_BRAND;
import static com.bimelody.ecommerceservice.dataaccesslayer.Tables.PRODUCT_BRAND_MAP;
import static com.bimelody.ecommerceservice.dataaccesslayer.Tables.PRODUCT_CATEGORY;
import static com.bimelody.ecommerceservice.dataaccesslayer.Tables.PRODUCT_CATEGORY_MAP;
import static com.bimelody.ecommerceservice.dataaccesslayer.Tables.PRODUCT_IMAGE;
import static com.bimelody.ecommerceservice.dataaccesslayer.Tables.PRODUCT_TAG_MAP;
import static com.bimelody.ecommerceservice.dataaccesslayer.Tables.STORE;
import static com.bimelody.ecommerceservice.dataaccesslayer.Tables.STORE_LOCATION;
import static org.jooq.impl.DSL.groupConcat;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.val;

import com.bimelody.ecommerceservice.dataaccesslayer.tables.records.ProductImageRecord;
import com.bimelody.ecommerceservice.dataaccesslayer.tables.records.ProductRecord;
import com.bimelody.ecommerceservice.dataaccesslayer.tables.records.StoreRecord;
import com.bimelody.ecommerceservice.model.request.SearchProductsRequest;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
import org.jooq.types.UInteger;
import org.springframework.stereotype.Repository;

/**
 * Implementation for product repository. Used for retrieval, and search product information.
 */
@Slf4j
@Repository
@AllArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

  private static final String GROUP_RESULT_SEPARATOR = "@@@";
  private static final String PRODUCT_IMAGES_FIELD = "ProductImages";
  private static final String PRODUCT_CATEGORIES_FIELD = "ProductCategories";
  private static final String PRODUCT_BRANDS_FIELD = "ProductBrands";

  private static final List<SelectField<?>> SELECTED_FIELDS =
      Arrays.asList(
          PRODUCT.PRODUCT_ID,
          PRODUCT.PRODUCT_NAME,
          PRODUCT.UNIQUE_PRODUCT_NAME_IN_STORE,
          PRODUCT.PRODUCT_DESCRIPTION,
          PRODUCT.PRICE_IN_DOLLAR,
          STORE.UNIQUE_STORE_NAME,
          STORE.STORE_NAME,
          STORE_LOCATION.FORMATTED_ADDRESS,
          STORE_LOCATION.LONGITUDE,
          STORE_LOCATION.LATITUDE,
          groupConcat(PRODUCT_IMAGE.PRODUCT_IMAGE_LINK)
              .separator(GROUP_RESULT_SEPARATOR)
              .as(PRODUCT_IMAGES_FIELD),
          groupConcat(PRODUCT_CATEGORY.CATEGORY_TYPE)
              .separator(GROUP_RESULT_SEPARATOR)
              .as(PRODUCT_CATEGORIES_FIELD),
          groupConcat(PRODUCT_BRAND.BRAND_NAME)
              .separator(GROUP_RESULT_SEPARATOR)
              .as(PRODUCT_BRANDS_FIELD), PRODUCT.CREATION_TIME);

  private static final Table<?> SEARCH_PRODUCTS_FROM_CLAUSE =
      PRODUCT
          .join(STORE)
          .on(PRODUCT.STORE_ID.eq(STORE.STORE_ID))
          .join(STORE_LOCATION).on(STORE.STORE_ID.eq(STORE_LOCATION.STORE_ID))
          .leftJoin(PRODUCT_IMAGE)
          .on(PRODUCT.PRODUCT_ID.eq(PRODUCT_IMAGE.PRODUCT_ID))
          .leftJoin(PRODUCT_BRAND_MAP)
          .on(PRODUCT.PRODUCT_ID.eq(PRODUCT_BRAND_MAP.PRODUCT_ID))
          .leftJoin(PRODUCT_BRAND)
          .on(PRODUCT_BRAND_MAP.PRODUCT_BRAND_ID.eq(PRODUCT_BRAND.PRODUCT_BRAND_ID))
          .leftJoin(PRODUCT_CATEGORY_MAP)
          .on(PRODUCT.PRODUCT_ID.eq(PRODUCT_CATEGORY_MAP.PRODUCT_ID))
          .leftJoin(PRODUCT_CATEGORY)
          .on(PRODUCT_CATEGORY_MAP.PRODUCT_CATEGORY_ID.eq(PRODUCT_CATEGORY.PRODUCT_CATEGORY_ID));

  private DSLContext jooqDslContext;

  @Override
  public void createProduct(@NonNull com.bimelody.ecommerceservice.model.Product product) {
    jooqDslContext.transaction(
        transaction -> {
          StoreRecord storeRecord =
              jooqDslContext.fetchOne(
                  STORE, STORE.UNIQUE_STORE_NAME.eq(product.getUniqueStoreName()));
          if (storeRecord == null) {
            throw new IllegalStateException(
                "Can't find store " + product.getUniqueStoreName() + "in database!");
          }

          ProductRecord productRecord = jooqDslContext.newRecord(PRODUCT);
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
                        jooqDslContext.newRecord(PRODUCT_IMAGE);
                    productImageRecord.setProductId(productRecord.getProductId());
                    productImageRecord.setProductImageLink(imageUrl);
                    productImageRecord.store();
                  });
        });
  }

  @Override
  public void updateProduct(@NonNull com.bimelody.ecommerceservice.model.Product product) {
    Optional<com.bimelody.ecommerceservice.model.Product> productInfoInDatabaseOptional =
        findProductInfoFromStore(
            product.getUniqueStoreName(),
            product.getUniqueProductNameInStore());
    if (productInfoInDatabaseOptional.isEmpty()) {
      log.info("Product is not found for identifier:{}", product.getProductId());
      return;
    }
    com.bimelody.ecommerceservice.model.Product productInfoInDatabase =
        productInfoInDatabaseOptional.get();
    List<String> productImagesInDatabase = productInfoInDatabase.getProductImageUrls();

    // Find new URLs
    List<String> newUrls = product.getProductImageUrls().stream()
        .filter(StringUtils::isNotBlank)
        .filter(imageUrl -> !productImagesInDatabase.contains(imageUrl))
        .collect(Collectors.toList());
    // Find URLs in database that aren't needed any more.
    List<String> oldUrls = productInfoInDatabase.getProductImageUrls().stream()
        .filter(imageUrl -> !product.getProductImageUrls().contains(imageUrl))
        .collect(Collectors.toList());
    jooqDslContext.transaction(
        transaction -> {
          jooqDslContext.update(PRODUCT)
              .set(PRODUCT.PRODUCT_NAME, product.getProductName())
              .set(PRODUCT.PRODUCT_DESCRIPTION, product.getProductDescription())
              .set(PRODUCT.PRICE_IN_DOLLAR, product.getPriceInDollar())
              .where(PRODUCT.UNIQUE_PRODUCT_NAME_IN_STORE.eq(product.getUniqueProductNameInStore()))
              .execute();

          // Only create asset table record for new urls.
          newUrls.forEach(
              imageUrl -> {
                ProductImageRecord productImageRecord =
                    jooqDslContext.newRecord(PRODUCT_IMAGE);
                productImageRecord.setProductId(
                    UInteger.valueOf(productInfoInDatabase.getProductId()));
                productImageRecord.setProductImageLink(imageUrl);
                productImageRecord.store();
              }
          );

          // Delete unused asset urls.
          oldUrls.forEach(
              imageUrl -> {
                jooqDslContext.deleteFrom(PRODUCT_IMAGE)
                    .where(PRODUCT_IMAGE.PRODUCT_IMAGE_LINK
                        .eq(imageUrl))
                    .execute();
              }
          );
        }
    );
  }

  @Override
  public List<com.bimelody.ecommerceservice.model.Product> searchProducts(
      final SearchProductsRequest searchProductsRequest) {
    Condition whereCondition = buildSqlConditionForGisSearch(searchProductsRequest);
    Result<org.jooq.Record> records =
        jooqDslContext
            .select(SELECTED_FIELDS)
            .from(SEARCH_PRODUCTS_FROM_CLAUSE)
            .where(whereCondition)
            .groupBy(PRODUCT.PRODUCT_ID, STORE_LOCATION.STORE_LOCATION_ID)
            .orderBy(PRODUCT.CREATION_TIME.desc())
            .limit(searchProductsRequest.getPageSize())
            .offset(searchProductsRequest.getPageSize() * (searchProductsRequest.getPageNum() - 1))
            .fetch();

    return buildProductListWithReturnedDatabaseRecords(records);
  }

  @Override
  public List<com.bimelody.ecommerceservice.model.Product> searchProductsInStore(
      final String uniqueStoreName, final String productCategory, int pageNum, int pageSize) {
    List<Condition> conditions = new ArrayList<>();
    if (uniqueStoreName != null) {
      conditions.add(STORE.UNIQUE_STORE_NAME.eq(uniqueStoreName));
    }
    if (productCategory != null) {
      conditions.add(PRODUCT_CATEGORY.CATEGORY_TYPE.eq(productCategory));
    }
    Condition where = conditions.isEmpty() ? noCondition() : DSL.and(conditions);

    Result<org.jooq.Record> records =
        jooqDslContext
            .select(SELECTED_FIELDS)
            .from(SEARCH_PRODUCTS_FROM_CLAUSE)
            .where(where)
            .groupBy(PRODUCT.PRODUCT_ID, STORE_LOCATION.STORE_LOCATION_ID)
            .orderBy(PRODUCT.CREATION_TIME.desc())
            .limit(pageSize)
            .offset(pageSize * (pageNum - 1))
            .fetch();

    return buildProductListWithReturnedDatabaseRecords(records);
  }

  @Override
  public Optional<com.bimelody.ecommerceservice.model.Product> findProductInfoFromStore(
      final String uniqueStoreName, final String uniqueProductNameInStore) {
    List<Condition> conditions = new ArrayList<>();
    conditions.add(STORE.UNIQUE_STORE_NAME.eq(uniqueStoreName));
    conditions.add(PRODUCT.UNIQUE_PRODUCT_NAME_IN_STORE.eq(uniqueProductNameInStore));
    Condition where = DSL.and(conditions);
    Result<org.jooq.Record> records =
        jooqDslContext
            .select(SELECTED_FIELDS)
            .from(SEARCH_PRODUCTS_FROM_CLAUSE)
            .where(where)
            .groupBy(PRODUCT.PRODUCT_ID, STORE_LOCATION.STORE_LOCATION_ID)
            .fetch();
    List<com.bimelody.ecommerceservice.model.Product> products =
        buildProductListWithReturnedDatabaseRecords(records);

    if (products.size() > 1) {
      throw new IllegalStateException(
          String.format(
              "Find more than 1 product records for store name %s , product name %s!",
              uniqueStoreName, uniqueProductNameInStore));
    }

    return products.isEmpty() ? Optional.empty() : Optional.of(products.get(0));
  }

  @Override
  public Optional<com.bimelody.ecommerceservice.model.Product> deleteProductInStore(
      @NonNull String storeIdentifier, @NonNull String productIdentifier) {
    Result<ProductRecord> queryResult =
        jooqDslContext
            .selectFrom(PRODUCT)
            .where(PRODUCT.UNIQUE_PRODUCT_NAME_IN_STORE.eq(productIdentifier))
            .fetch();
    if (queryResult.isEmpty()) {
      return Optional.empty();
    } else if (queryResult.size() > 1) {
      throw new IllegalStateException(
          String.format("More than 1 records are found for storeIdentifier=%s, "
              + "productIdentifier=%s", storeIdentifier, productIdentifier));
    } else {
      ProductRecord productRecord = queryResult.get(0);
      jooqDslContext.transaction(
          transaction -> {
            jooqDslContext.deleteFrom(PRODUCT_IMAGE)
                .where(PRODUCT_IMAGE.PRODUCT_ID
                    .eq(productRecord.getValue(PRODUCT.PRODUCT_ID)))
                .execute();

            jooqDslContext.deleteFrom(PRODUCT_CATEGORY_MAP)
                .where(PRODUCT_CATEGORY_MAP.PRODUCT_ID
                    .eq(productRecord.getValue(PRODUCT.PRODUCT_ID)))
                .execute();
            ;
            jooqDslContext.deleteFrom(PRODUCT_TAG_MAP)
                .where(PRODUCT_TAG_MAP.PRODUCT_ID
                    .eq(productRecord.getValue(PRODUCT.PRODUCT_ID)))
                .execute();
            ;
            jooqDslContext.deleteFrom(PRODUCT_BRAND_MAP)
                .where(PRODUCT_BRAND_MAP.PRODUCT_ID
                    .eq(productRecord.getValue(PRODUCT.PRODUCT_ID)))
                .execute();
            jooqDslContext.deleteFrom(PRODUCT)
                .where(PRODUCT.PRODUCT_ID
                    .eq(productRecord.getValue(PRODUCT.PRODUCT_ID)))
                .execute();

          }
      );
      return Optional.of(com.bimelody.ecommerceservice.model.Product
          .builder()
          .productId(productRecord.getValue(PRODUCT.PRODUCT_ID).longValue())
          .productName(productRecord.getValue(PRODUCT.PRODUCT_NAME))
          .build());
    }


  }

  private Condition buildSqlConditionForGisSearch(
      @NonNull final SearchProductsRequest searchProductsRequest) {
    List<Condition> conditions = new ArrayList<>();
    conditions.add(
        STORE_LOCATION.LATITUDE.isNotNull()
    );
    conditions.add(
        STORE_LOCATION.LONGITUDE.isNotNull()
    );
    if (searchProductsRequest.getLatitude() != null
        && searchProductsRequest.getLongitude() != null) {
      long distanceInMeters = searchProductsRequest.getMeters() != null
          ? searchProductsRequest.getMeters() : 10000L; // Default distance 10KM.

      // ST_Distance_Sphere return distance in meters.
      conditions.add(DSL.condition(
          "(ST_Distance_Sphere(point(longitude, latitude), point({0}, {1}))) < {2}",
          val(searchProductsRequest.getLongitude()),
          val(searchProductsRequest.getLatitude()),
          val(distanceInMeters)));
    } else if (
        searchProductsRequest.getNeLat() != null
            && searchProductsRequest.getNeLng() != null
            && searchProductsRequest.getSwLat() != null
            && searchProductsRequest.getSwLng() != null) {
      conditions.add(
          DSL.condition(
              "ST_CONTAINS(ST_MakeEnvelope(\n"
                  + "Point({0}, {1}), \n"
                  + "Point({2}, {3})\n"
                  + "), Point(latitude, longitude))",
              val(searchProductsRequest.getNeLat()),
              val(searchProductsRequest.getNeLng()),
              val(searchProductsRequest.getSwLat()),
              val(searchProductsRequest.getSwLng()))
      );
    }

    Condition whereCondition = DSL.and(conditions);
    return whereCondition;
  }

  private List<com.bimelody.ecommerceservice.model.Product>
      buildProductListWithReturnedDatabaseRecords(@NonNull final Result<org.jooq.Record> records) {
    return records.stream()
        .map(
            record -> {
              return com.bimelody.ecommerceservice.model.Product
                  .builder()
                  .productId(record.getValue(PRODUCT.PRODUCT_ID).longValue())
                  .productName(record.getValue(PRODUCT.PRODUCT_NAME))
                  .uniqueProductNameInStore(
                      record.getValue(PRODUCT.UNIQUE_PRODUCT_NAME_IN_STORE))
                  .productDescription(record.getValue(PRODUCT.PRODUCT_DESCRIPTION))
                  .priceInDollar(record.getValue(PRODUCT.PRICE_IN_DOLLAR))
                  .productBrands(concatenateGroupValues(record, PRODUCT_BRANDS_FIELD))
                  .productCategories(concatenateGroupValues(record, PRODUCT_BRANDS_FIELD))
                  .productImageUrls(concatenateGroupValues(record, PRODUCT_IMAGES_FIELD))
                  .timestamp(
                      record
                          .getValue(PRODUCT.CREATION_TIME)
                          .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                  .storeName(record.getValue(STORE.STORE_NAME))
                  .uniqueStoreName(record.getValue(STORE.UNIQUE_STORE_NAME))
                  .storeLocation(record.getValue(STORE_LOCATION.FORMATTED_ADDRESS))
                  .storeLocationLatitude(record.getValue(STORE_LOCATION.LATITUDE))
                  .storeLocationLongitude(record.getValue(STORE_LOCATION.LONGITUDE))
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
