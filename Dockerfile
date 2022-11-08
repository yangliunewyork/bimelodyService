########################################################################################
# We are multi-stage builds here to build the docker image.
# https://docs.docker.com/develop/develop-images/multistage-build/
########################################################################################

########################################################################################
# Build Stage
########################################################################################
FROM public.ecr.aws/amazoncorretto/amazoncorretto:11 AS BUILD_ARTIFACT

# Declare argument variables
ARG ARG_DATABASE_URL
ARG ARG_DATABASE_USERNAME
ARG ARG_DATABASE_PASSWORD

# Pass value from docker build arguments to environment variables.
ENV DATABASE_URL=$ARG_DATABASE_URL
ENV DATABASE_USERNAME=$ARG_DATABASE_USERNAME
ENV DATABASE_PASSWORD=$ARG_DATABASE_PASSWORD

# Set working directory
ARG GROUP_NAME=bimelody
ARG APP_NAME=ecommerce-service
ENV APP_HOME=/home/$GROUP_NAME/$APP_NAME/
RUN yum install -y shadow-utils && yum clean all
RUN groupadd $GROUP_NAME
WORKDIR $APP_HOME


COPY build.gradle settings.gradle gradlew $APP_HOME
COPY gradle $APP_HOME/gradle

# https://stackoverflow.com/questions/25873971/docker-cache-gradle-dependencies
RUN ./gradlew build || return 0
COPY . .
RUN ./gradlew build

RUN ls $APP_HOME/eCommerceService/build/libs/

########################################################################################
# Run Stage
########################################################################################

FROM public.ecr.aws/amazoncorretto/amazoncorretto:11 AS RUN_ARTIFACT
# Run as a non-root user to mitigate security risks
# https://security.stackexchange.com/questions/106860/can-a-root-user-inside-a-docker-lxc-break-the-security-of-the-whole-system

# Declare argument variables
ARG ARG_DATABASE_URL
ARG ARG_DATABASE_USERNAME
ARG ARG_DATABASE_PASSWORD

# Pass value from docker build arguments to environment variables.
ENV DATABASE_URL=$ARG_DATABASE_URL
ENV DATABASE_USERNAME=$ARG_DATABASE_USERNAME
ENV DATABASE_PASSWORD=$ARG_DATABASE_PASSWORD

ARG GROUP_NAME=bimelody
ARG APP_NAME=ecommerce-service
ENV APP_HOME=/home/$GROUP_NAME/$APP_NAME/
RUN yum install -y shadow-utils && yum clean all
RUN groupadd $GROUP_NAME
WORKDIR $APP_HOME

RUN useradd -G $GROUP_NAME $APP_NAME --home $APP_HOME
USER $APP_NAME

# Copy the artifact from BUILD_ARTIFACT stage
COPY --from=BUILD_ARTIFACT $APP_HOME/eCommerceService/build/libs/eCommerceService-0.0.1.jar eCommerceService-0.0.1.jar

EXPOSE 8080
EXPOSE 8443

# Set ENTRYPOINT in exec form to run the container as an executable
ENTRYPOINT ["java", "-jar", "eCommerceService-0.0.1.jar"]