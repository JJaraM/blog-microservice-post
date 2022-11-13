FROM maven:3.6.3-openjdk-14 AS maven_build

COPY pom.xml /tmp/
COPY src /tmp/src/

WORKDIR /tmp/

# File that contains the environment variables required to start the appplication
RUN --mount=type=secret,id=_cloud,dst=/etc/secrets/.cloud \
  cat /etc/secrets/.cloud > /tmp/src/main/resources/bootstrap.properties  

# File that contains the github maven repositories
RUN --mount=type=secret,id=settings_xml,dst=/etc/secrets/settings.xml \
  mvn -s /etc/secrets/settings.xml clean install
  
# RUN cat /tmp/src/main/resources/properties.properties

FROM openjdk
EXPOSE 8080

COPY --from=maven_build /tmp/target/post-microservice.jar /data/post-microservice.jar

# ENTRYPOINT ["java","-jar", "/data/post-microservice.jar", "--spring.profiles.active=prd", "--spring.cloud.config.username=${cloud_user_new}", "--spring.cloud.config.password=pass"]
ENTRYPOINT ["java","-jar", "/data/post-microservice.jar"]
