package com.bimelody.ecommerceservice.repository;

import com.amazonaws.util.CollectionUtils;
import com.bimelody.ecommerceservice.model.Store;
import com.bimelody.ecommerceservice.model.StoreCategory;
import com.bimelody.ecommerceservice.model.request.FindStoresRequest;
import com.bimelody.ecommerceservice.dataaccesslayer.tables.StoreCategoryMap;
import com.bimelody.ecommerceservice.dataaccesslayer.tables.StoreLocation;
import com.bimelody.ecommerceservice.dataaccesslayer.tables.records.StoreCategoryMapRecord;
import com.bimelody.ecommerceservice.dataaccesslayer.tables.records.StoreLocationRecord;
import com.bimelody.ecommerceservice.dataaccesslayer.tables.records.StoreRecord;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.SQLStateClass;
import org.jooq.impl.DSL;
import org.jooq.types.UInteger;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.jooq.impl.DSL.groupConcat;

@Slf4j
@Repository
@AllArgsConstructor
public class StoreRepositoryImpl implements StoreRepository {

  private static final String STORE_CATEGORY_SEPARATOR = ",";
  private static final String STORE_CATEGORY_FIELD = "StoreCategories";

  private static final com.bimelody.ecommerceservice.dataaccesslayer.tables.Store STORE_TABLE
          = com.bimelody.ecommerceservice.dataaccesslayer.tables.Store.STORE;
  private static final StoreLocation STORE_LOCATION = StoreLocation.STORE_LOCATION;
  private static final com.bimelody.ecommerceservice.dataaccesslayer.tables.StoreCategory STORE_CATEGORY
          = com.bimelody.ecommerceservice.dataaccesslayer.tables.StoreCategory.STORE_CATEGORY;
  private static final StoreCategoryMap STORE_CATEGORY_MAP = StoreCategoryMap.STORE_CATEGORY_MAP;
  private static final List<SelectField<?>> SELECT_FIELDS =
      Arrays.asList(
          STORE_TABLE.STORE_ID,
          STORE_TABLE.STORE_NAME,
          STORE_TABLE.UNIQUE_STORE_NAME,
          STORE_TABLE.STORE_WEBSITE,
          STORE_TABLE.STORE_DESCRIPTION,
          STORE_TABLE.CONTACT_NUMBER,
          STORE_TABLE.CONTACT_EMAIL,
          STORE_TABLE.STORE_COVER_IMAGE,
          STORE_TABLE.YELP_LINK,
          STORE_TABLE.TWITTER_LINK,
          STORE_TABLE.FACEBOOK_LINK,
          STORE_TABLE.INSTAGRAM_LINK,
          STORE_LOCATION.FORMATTED_ADDRESS,
          STORE_LOCATION.LATITUDE,
          STORE_LOCATION.LONGITUDE,
          groupConcat(STORE_CATEGORY.CATEGORY_TYPE)
              .separator(STORE_CATEGORY_SEPARATOR)
              .as(STORE_CATEGORY_FIELD));
  private static final Table<?> FROM_CLAUSE =
      STORE_TABLE
          .leftJoin(STORE_LOCATION)
          .on(STORE_TABLE.STORE_ID.eq(STORE_LOCATION.STORE_ID))
          .leftJoin(STORE_CATEGORY_MAP)
          .on(STORE_TABLE.STORE_ID.eq(STORE_CATEGORY_MAP.STORE_ID))
          .leftJoin(STORE_CATEGORY)
          .on(STORE_CATEGORY_MAP.STORE_CATEGORY_ID.eq(STORE_CATEGORY.STORE_CATEGORY_ID));

  private DSLContext jooqDslContext;

  @Override
  public void createStore(@NonNull final Store store) {
    StoreRecord storeRecord = jooqDslContext.newRecord(STORE_TABLE);
    storeRecord.setStoreName(store.getStoreName());
    storeRecord.setUniqueStoreName(store.getUniqueStoreName());
    storeRecord.setContactNumber(store.getContactNumber());
    storeRecord.setContactEmail(store.getContactEmail());
    storeRecord.setStoreWebsite(store.getStoreWebsite());
    storeRecord.setStoreDescription(store.getStoreDescription());
    storeRecord.setFacebookLink(store.getFacebookLink());
    storeRecord.setInstagramLink(store.getInstagramLink());
    storeRecord.setTwitterLink(store.getTwitterLink());
    try {
      storeRecord.store();
    } catch (final DataAccessException exception) {
      log.error("Failed to create a Store record!", exception);
      //log.info("exception.sqlStateClass(), {}", exception.sqlStateClass());
      if (SQLStateClass.C23_INTEGRITY_CONSTRAINT_VIOLATION.equals(exception.sqlStateClass())) {
        log.info("hello~~~");
        //throw new RuntimeException("Just a normal IllegalArgumentException!");
        throw new IllegalStateException("IllegalStateException: " + exception.getMessage());
      }
      throw exception;
    }

    // TODO: These two steps may be tedious when onboard a new store.
    addLocationForStore(store, storeRecord);
    addCategoriesForStore(store, storeRecord);
  }

  @Override
  public void updateStore(@NonNull final Store store) {
    jooqDslContext.update(STORE_TABLE)
            .set(STORE_TABLE.STORE_NAME, store.getStoreName())
            .set(STORE_TABLE.STORE_WEBSITE, store.getStoreWebsite())
            .set(STORE_TABLE.STORE_DESCRIPTION, store.getStoreDescription())
            .set(STORE_TABLE.CONTACT_EMAIL, store.getContactEmail())
            .set(STORE_TABLE.CONTACT_NUMBER, store.getContactNumber())
            .where(STORE_TABLE.UNIQUE_STORE_NAME.eq(store.getUniqueStoreName()))
            .execute();
  }

  @Override
  public List<Store> findStores(
      @NonNull final FindStoresRequest findStoresRequest) {
    Result<org.jooq.Record> records =
        jooqDslContext
            .select(SELECT_FIELDS)
            .from(FROM_CLAUSE)
            .where(buildWhereCondition(findStoresRequest))
            .groupBy(STORE_TABLE.STORE_ID, STORE_LOCATION.STORE_LOCATION_ID)
            .limit(findStoresRequest.getPageSize())
            .offset(findStoresRequest.getPageSize() * (findStoresRequest.getPageNum() - 1))
            .fetch();

    return buildStoreListWithReturnedDatabaseRecords(records);
  }

  @Override
  public Optional<Store> getStoreInfo(@NonNull String uniqueStoreName) {

    Condition where = STORE_TABLE.UNIQUE_STORE_NAME.eq(uniqueStoreName);

    Result<org.jooq.Record> records =
        jooqDslContext
            .select(SELECT_FIELDS)
            .from(FROM_CLAUSE)
            .where(where)
            .groupBy(STORE_TABLE.STORE_ID, STORE_LOCATION.STORE_LOCATION_ID)
            .fetch();

    List<Store> stores = buildStoreListWithReturnedDatabaseRecords(records);

    if (stores.size() > 1) {
      throw new IllegalStateException(
          "Find more than 1 store records for unique store name " + uniqueStoreName);
    }

    return Optional.ofNullable(stores.isEmpty() ? null : stores.get(0));
  }

  @Override
  public List<StoreCategory> getAllStoreCategories() {
    Result<org.jooq.Record> records =
        jooqDslContext
            .select(Arrays.asList(STORE_CATEGORY.STORE_CATEGORY_ID, STORE_CATEGORY.CATEGORY_TYPE))
            .from(STORE_CATEGORY)
            .fetch();
    return records.stream()
        .map(
            record ->
                StoreCategory
                    .builder()
                    .storeCategoryId(record.getValue(STORE_CATEGORY.STORE_CATEGORY_ID).longValue())
                    .categoryType(record.getValue(STORE_CATEGORY.CATEGORY_TYPE))
                    .build())
        .collect(toList());
  }

  private Condition buildWhereCondition(@NonNull final FindStoresRequest findStoresRequest) {
    List<Condition> conditions = new ArrayList<>();
    conditions.add(
            STORE_LOCATION.LATITUDE.isNotNull()
    );
    conditions.add(
            STORE_LOCATION.LONGITUDE.isNotNull()
    );
    if (findStoresRequest.getStoreCategories() != null) {
      conditions.add(
          STORE_CATEGORY.CATEGORY_TYPE.in(
              findStoresRequest.getStoreCategories().split(STORE_CATEGORY_SEPARATOR)));
    }
    Condition whereCondition = DSL.and(conditions);
    return whereCondition;
  }

  private List<Store> buildStoreListWithReturnedDatabaseRecords (
      @NonNull final Result<org.jooq.Record> records) {
    return records.stream()
        .map(
            record -> {
              final Object storeCategoriesValue = record.getValue(STORE_CATEGORY_FIELD);
              List<String> storeCategories =
                  storeCategoriesValue != null
                      ? Arrays.asList(
                          storeCategoriesValue.toString().split(STORE_CATEGORY_SEPARATOR))
                      : Collections.emptyList();
              return Store
                  .builder()
                  .storeId(record.getValue(STORE_TABLE.STORE_ID).toBigInteger().longValue())
                  .storeName(record.getValue(STORE_TABLE.STORE_NAME))
                  .uniqueStoreName(record.getValue(STORE_TABLE.UNIQUE_STORE_NAME))
                  .storeWebsite(record.getValue(STORE_TABLE.STORE_WEBSITE))
                  .storeDescription(record.getValue(STORE_TABLE.STORE_DESCRIPTION))
                  .contactNumber(record.getValue(STORE_TABLE.CONTACT_NUMBER))
                  .contactEmail(record.getValue(STORE_TABLE.CONTACT_EMAIL))
                  .storeCoverImage(record.getValue(STORE_TABLE.STORE_COVER_IMAGE))
                  .yelpLink(record.getValue(STORE_TABLE.YELP_LINK))
                  .twitterLink(record.getValue(STORE_TABLE.TWITTER_LINK))
                  .facebookLink(record.getValue(STORE_TABLE.FACEBOOK_LINK))
                  .instagramLink(record.getValue(STORE_TABLE.INSTAGRAM_LINK))
                  .storeLocation(record.getValue(STORE_LOCATION.FORMATTED_ADDRESS))
                  .storeLocationLatitude(record.getValue(STORE_LOCATION.LATITUDE))
                  .storeLocationLongitude(record.getValue(STORE_LOCATION.LONGITUDE))
                  .storeCategories(storeCategories)
                  .build();
            })
        .collect(toList());
  }

  // TODO: Maybe not create location when first onboarding a new store? TBD
  private void addLocationForStore(@NonNull final Store store,
                                   @NonNull StoreRecord storeRecord) {
    if (store.getStoreLocation() != null) {
      StoreLocationRecord storeLocationRecord = jooqDslContext.newRecord(STORE_LOCATION);
      storeLocationRecord.setStoreId(storeRecord.getStoreId());
      storeLocationRecord.setFormattedAddress(store.getStoreLocation());
      storeLocationRecord.setLatitude(store.getStoreLocationLatitude());
      storeLocationRecord.setLongitude(store.getStoreLocationLongitude());
      storeLocationRecord.store();
    }
  }

  // TODO: Maybe not create store categories when first onboarding a new store? TBD
  private void addCategoriesForStore(
          @NonNull final Store store, @NonNull StoreRecord storeRecord) {
    if (!CollectionUtils.isNullOrEmpty(store.getStoreCategories())) {
      Map<String, StoreCategory> allStoreCategories =
          getAllStoreCategories().stream()
              .collect(
                  Collectors.toMap(item -> item.getCategoryType().toUpperCase(), item -> item));
      store
          .getStoreCategories()
          .forEach(
              storeCategory -> {
                if (allStoreCategories.containsKey(storeCategory.toUpperCase())) {
                  StoreCategoryMapRecord storeCategoryMapRecord =
                      jooqDslContext.newRecord(STORE_CATEGORY_MAP);
                  storeCategoryMapRecord.setStoreId(storeRecord.getStoreId());
                  storeCategoryMapRecord.setStoreCategoryId(
                      UInteger.valueOf(allStoreCategories.get(storeCategory).getStoreCategoryId()));
                }
              });
    }
  }
}
