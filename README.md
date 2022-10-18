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

Running service requires connection to the MySql instance behind it. 

### Run the service without docker

We need specify the profile to use `application-local.properties`.

The service can be run locally in your laptop. You just need to run the MySql instance in docker container.

```
java -jar ./build/libs/eCommerceService-0.0.1.jar
```

### Run the service in local docker

We need specify the profile to use `application-docker.properties`.

In order to make the service in one docker to call the MySQL instance in another docker, we need to use Docker 
network, and run containers in the same network.

Check [eCommerceDatabaseSchema](https://github.com/yangliunewyork/eCommerceDatabaseSchema) to see how to run the 
required MySQL instance in the Docker network.

Start the service container in the docker network. Below command will read `application-docker.properties` file.

```
docker run --net=ecommerce-service-network --name=ecommerce-service -e "SPRING_PROFILES_ACTIVE=docker" --rm -ti -p 
8080:8080 ecommerce-service
```

You can check the network with `docker network inspect ecommerce-service-network`.

# Debug 

### Debug docker

```
docker build --progress=plain --no-cache . 
```
