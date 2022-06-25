# eCommerceServiceWrapper

# Build

### Build Project

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
```
./gradlew tasks --all
```

### Build Docker

Build the docker image:

```
docker build . -t ecommerce-service
```

# Run

Run the service without docker:

```
java -jar ./build/libs/eCommerceService-0.0.1.jar
```

Run the service in a docker:

```
docker run --rm -ti -p 8080:8080 ecommerce-service
```

# Debug 

### Debug docker

```
docker build --progress=plain --no-cache . 
```
