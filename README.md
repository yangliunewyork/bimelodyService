# eCommerceServiceWrapper

# Build

Build the whole project:  
```
./gradlew clean build 
```

Run Gradle task on one module only:  
```
./gradlew :eCommerceServiceDataAccessLayer:generate // Call JOOQ to re-generate based on database schema
./gradlew :eCommerceServiceDataAccessLayer:build  
```

List all Gradle tasks:  
``````
./gradlew tasks --all
```