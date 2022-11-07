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
docker build . -t ecommerce-service \
--build-arg ARG_DATABASE_URL=jdbc:mysql://host.docker.internal:3305/Catbirdnyc \
--build-arg ARG_DATABASE_USERNAME=root \
--build-arg ARG_DATABASE_PASSWORD=password
```

# Run

Running service requires connection to the MySql instance behind it.

### Run the service in local docker

We use `application-docker.properties` to set environment variables(in AWS CodeBuild we have to pass arguments 
explicitly when build the docker image).

In order to make the service in one docker to call the MySQL instance in another docker, we need to use Docker 
network, and run containers in the same network.

Check [eCommerceDatabaseSchema](https://github.com/yangliunewyork/eCommerceDatabaseSchema) to see how to run the 
required MySQL instance in the Docker network.

Start the service container in the docker network. Below command will read `application-docker.properties` file.

```
docker run --net=ecommerce-service-network --name=ecommerce-service \
--rm -ti -p 8080:8080 ecommerce-service  

```

You can check the network with `docker network inspect ecommerce-service-network`.

# Debug 

### Debug docker

```
docker build --progress=plain --no-cache . 
```

### Get docker image from ECR

Log in ECR:  
```
aws ecr get-login-password --region us-east-1 --profile eCommerceService-Alpha | docker login --username AWS 
--password-stdin 529378696789.dkr.ecr.us-east-1.amazonaws.com
```

Pull latest image:

```
docker pull 529378696789.dkr.ecr.us-east-1.amazonaws.com/ecommerce-service-ecr-repository:latest
```

Inspect image:

```
docker image inspect 2078daf2dd36 
```