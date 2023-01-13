package com.bimelody.ecommerceservice.repository;

import com.amazonaws.util.CollectionUtils;

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

import static com.bimelody.ecommerceservice.dataaccesslayer.Tables.STORE;
import static com.bimelody.ecommerceservice.dataaccesslayer.Tables.STORE_LOCATION;
import static com.bimelody.ecommerceservice.dataaccesslayer.Tables.STORE_CATEGORY;
import static com.bimelody.ecommerceservice.dataaccesslayer.Tables.STORE_CATEGORY_MAP;
import static java.util.stream.Collectors.toList;
import static org.jooq.impl.DSL.groupConcat;

@Slf4j
@Repository
@AllArgsConstructor
public class StoreRepositoryImpl implements StoreRepository {

    private static final String STORE_CATEGORY_SEPARATOR = ",";
    private static final String STORE_CATEGORY_FIELD = "StoreCategories";

    private static final List<SelectField<?>> SELECT_FIELDS =
            Arrays.asList(
                    STORE.STORE_ID,
                    STORE.STORE_NAME,
                    STORE.UNIQUE_STORE_NAME,
                    STORE.STORE_WEBSITE,
                    STORE.STORE_DESCRIPTION,
                    STORE.CONTACT_NUMBER,
                    STORE.CONTACT_EMAIL,
                    STORE.STORE_COVER_IMAGE,
                    STORE.YELP_LINK,
                    STORE.TWITTER_LINK,
                    STORE.FACEBOOK_LINK,
                    STORE.INSTAGRAM_LINK,
                    STORE_LOCATION.FORMATTED_ADDRESS,
                    STORE_LOCATION.LATITUDE,
                    STORE_LOCATION.LONGITUDE,
                    groupConcat(STORE_CATEGORY.CATEGORY_TYPE)
                            .separator(STORE_CATEGORY_SEPARATOR)
                            .as(STORE_CATEGORY_FIELD));
    private static final Table<?> FROM_CLAUSE =
            STORE
                    .leftJoin(STORE_LOCATION)
                    .on(STORE.STORE_ID.eq(STORE_LOCATION.STORE_ID))
                    .leftJoin(STORE_CATEGORY_MAP)
                    .on(STORE.STORE_ID.eq(STORE_CATEGORY_MAP.STORE_ID))
                    .leftJoin(STORE_CATEGORY)
                    .on(STORE_CATEGORY_MAP.STORE_CATEGORY_ID.eq(STORE_CATEGORY.STORE_CATEGORY_ID));

    private DSLContext jooqDslContext;

    @Override
    public void createStore(@NonNull final com.bimelody.ecommerceservice.model.Store store) {
        StoreRecord storeRecord = jooqDslContext.newRecord(STORE);
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
    public void updateStore(@NonNull final com.bimelody.ecommerceservice.model.Store store) {
        Result<StoreRecord> records =
                jooqDslContext
                        .selectFrom(STORE)
                        .where(STORE.UNIQUE_STORE_NAME.eq(store.getUniqueStoreName()))
                        .fetch();
        if (records.isEmpty()) {
            return;
        }
        if (records.size() > 1) {
            throw new IllegalStateException("More than 1 records are found for storeIdentifier= " + store.getUniqueStoreName() + "in " +
                    "database!");
        }
        StoreRecord storeRecord = records.get(0);
        jooqDslContext.transaction(
                transaction -> {
                    jooqDslContext.update(STORE)
                            .set(STORE.STORE_NAME, store.getStoreName())
                            .set(STORE.STORE_WEBSITE, store.getStoreWebsite())
                            .set(STORE.STORE_DESCRIPTION, store.getStoreDescription())
                            .set(STORE.CONTACT_EMAIL, store.getContactEmail())
                            .set(STORE.CONTACT_NUMBER, store.getContactNumber())
                            .where(STORE.STORE_ID.eq(storeRecord.getStoreId()))
                            .execute();
                    jooqDslContext.update(STORE_LOCATION)
                            .set(STORE_LOCATION.FORMATTED_ADDRESS, store.getStoreLocation())
                            .set(STORE_LOCATION.LATITUDE, store.getStoreLocationLatitude())
                            .set(STORE_LOCATION.LONGITUDE, store.getStoreLocationLongitude())
                            .where(STORE_LOCATION.STORE_ID.eq(storeRecord.getStoreId()))
                            .execute();
                }
        );
    }

    @Override
    public List<com.bimelody.ecommerceservice.model.Store> findStores(
            @NonNull final FindStoresRequest findStoresRequest) {
        Result<org.jooq.Record> records =
                jooqDslContext
                        .select(SELECT_FIELDS)
                        .from(FROM_CLAUSE)
                        .where(buildWhereCondition(findStoresRequest))
                        .groupBy(STORE.STORE_ID, STORE_LOCATION.STORE_LOCATION_ID)
                        .limit(findStoresRequest.getPageSize())
                        .offset(findStoresRequest.getPageSize() * (findStoresRequest.getPageNum() - 1))
                        .fetch();

        return buildStoreListWithReturnedDatabaseRecords(records);
    }

    @Override
    public Optional<com.bimelody.ecommerceservice.model.Store> getStoreInfo(@NonNull String uniqueStoreName) {

        Condition where = STORE.UNIQUE_STORE_NAME.eq(uniqueStoreName);

        Result<org.jooq.Record> records =
                jooqDslContext
                        .select(SELECT_FIELDS)
                        .from(FROM_CLAUSE)
                        .where(where)
                        .groupBy(STORE.STORE_ID, STORE_LOCATION.STORE_LOCATION_ID)
                        .fetch();

        List<com.bimelody.ecommerceservice.model.Store> stores = buildStoreListWithReturnedDatabaseRecords(records);

        if (stores.size() > 1) {
            throw new IllegalStateException(
                    "Find more than 1 store records for unique store name " + uniqueStoreName);
        }

        return Optional.ofNullable(stores.isEmpty() ? null : stores.get(0));
    }

    @Override
    public List<com.bimelody.ecommerceservice.model.StoreCategory> getAllStoreCategories() {
        Result<org.jooq.Record> records =
                jooqDslContext
                        .select(Arrays.asList(STORE_CATEGORY.STORE_CATEGORY_ID, STORE_CATEGORY.CATEGORY_TYPE))
                        .from(STORE_CATEGORY)
                        .fetch();
        return records.stream()
                .map(
                        record ->
                                com.bimelody.ecommerceservice.model.StoreCategory
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

    private List<com.bimelody.ecommerceservice.model.Store> buildStoreListWithReturnedDatabaseRecords(
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
                            return com.bimelody.ecommerceservice.model.Store
                                    .builder()
                                    .storeId(record.getValue(STORE.STORE_ID).toBigInteger().longValue())
                                    .storeName(record.getValue(STORE.STORE_NAME))
                                    .uniqueStoreName(record.getValue(STORE.UNIQUE_STORE_NAME))
                                    .storeWebsite(record.getValue(STORE.STORE_WEBSITE))
                                    .storeDescription(record.getValue(STORE.STORE_DESCRIPTION))
                                    .contactNumber(record.getValue(STORE.CONTACT_NUMBER))
                                    .contactEmail(record.getValue(STORE.CONTACT_EMAIL))
                                    .storeCoverImage(record.getValue(STORE.STORE_COVER_IMAGE))
                                    .yelpLink(record.getValue(STORE.YELP_LINK))
                                    .twitterLink(record.getValue(STORE.TWITTER_LINK))
                                    .facebookLink(record.getValue(STORE.FACEBOOK_LINK))
                                    .instagramLink(record.getValue(STORE.INSTAGRAM_LINK))
                                    .storeLocation(record.getValue(STORE_LOCATION.FORMATTED_ADDRESS))
                                    .storeLocationLatitude(record.getValue(STORE_LOCATION.LATITUDE))
                                    .storeLocationLongitude(record.getValue(STORE_LOCATION.LONGITUDE))
                                    .storeCategories(storeCategories)
                                    .build();
                        })
                .collect(toList());
    }

    // TODO: Maybe not create location when first onboarding a new store? TBD
    private void addLocationForStore(@NonNull final com.bimelody.ecommerceservice.model.Store store,
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
            @NonNull final com.bimelody.ecommerceservice.model.Store store, @NonNull StoreRecord storeRecord) {
        if (!CollectionUtils.isNullOrEmpty(store.getStoreCategories())) {
            Map<String, com.bimelody.ecommerceservice.model.StoreCategory> allStoreCategories =
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
