# bimelodyService

# Project Structure

```
bimelodyService/
├── eCommerceService/               # Spring Boot application (controllers, services, repositories)
├── eCommerceServiceDataAccessLayer/ # jOOQ-generated database access code
└── eCommerceDatabaseSchema/        # Flyway SQL migration files and MySQL Dockerfiles
```

### eCommerceService
The main Spring Boot service exposing a REST API. Uses jOOQ for database access and Spring MVC for request handling.

### eCommerceServiceDataAccessLayer
Auto-generated jOOQ code derived from the SQL schema. Re-generate after schema changes:
```
./gradlew :eCommerceServiceDataAccessLayer:generate
```

### eCommerceDatabaseSchema
Flyway migration files defining the database schema. Also contains Dockerfiles for running MySQL and Flyway locally.

# Build

### Build Project

Build the whole project:
```
./gradlew clean build
```

Run Gradle task on one module only:
```
./gradlew :eCommerceServiceDataAccessLayer:generate  # Re-generate jOOQ code from schema
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

### Run locally (without Docker)

**Step 1 — Start MySQL and run Flyway migrations**

From the `eCommerceDatabaseSchema/` directory:

Create the Docker network (once — check first if it already exists):
```
docker network inspect ecommerce-service-network
docker network create ecommerce-service-network
```

Build and run MySQL:
```
docker build . -t ecommerce-mysql-instance -f Dockerfile.MySQL
docker run --net=ecommerce-service-network -p 3305:3306 \
  -e MYSQL_DATABASE=Catbirdnyc \
  -e MYSQL_ROOT_PASSWORD=password \
  ecommerce-mysql-instance
```

Once the container is up you can connect to it via MySQL Workbench on `127.0.0.1:3305`.
See: https://stackoverflow.com/questions/33827342/how-to-connect-mysql-workbench-to-running-mysql-inside-docker

Build and run Flyway migrations:
```
docker build . -t ecommerce-mysql-flyway -f Dockerfile.Flyway.Local
docker run --platform linux/amd64 --net=ecommerce-service-network \
  ecommerce-mysql-flyway \
  -url=jdbc:mysql://host.docker.internal:3305/Catbirdnyc \
  -user=root \
  -password=password
```

To debug a failing Flyway migration with verbose output:
```
docker build . -t ecommerce-mysql-flyway -f Dockerfile.Flyway.Local \
  --build-arg ARG_USERNAME="root" \
  --build-arg ARG_PASSWORD="password" \
  --build-arg ARG_DATABASE_ENDPOINT="jdbc:mysql://host.docker.internal:3305/Catbirdnyc?&allowPublicKeyRetrieval=true" \
  --progress=plain --no-cache
```

**Step 2 — Configure local properties**

Copy `eCommerceService/src/main/resources/application-local.properties.template` to
`application-local.properties` and fill in your AWS credentials.

**Step 3 — Start the service**
```
./gradlew :eCommerceService:bootRun --args='--spring.profiles.active=local'
```

The service starts on port 8080.

**Swagger UI:** http://localhost:8080/swagger-ui/index.html

### Run in Docker

Requires a `.env` file in the project root (see `.env` format in `MySqlDataSource.java` and `AssetS3ClientModule.java`).

Check [eCommerceDatabaseSchema](https://github.com/yangliunewyork/eCommerceDatabaseSchema) to see how to run the required MySQL instance in the Docker network.

```
docker run --net=ecommerce-service-network --name=ecommerce-service \
  --env-file .env \
  --rm -ti -p 8080:8080 ecommerce-service
```

**Swagger UI:** http://localhost:8080/swagger-ui/index.html

# Debug

### Debug Docker build
```
docker build --progress=plain --no-cache .
```

### Get Docker image from ECR

Log in to ECR:
```
aws ecr get-login-password --region us-east-1 --profile ecommerceservice-Alpha | docker login --username AWS \
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
