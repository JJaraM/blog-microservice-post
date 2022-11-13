FROM maven:3.6.3-openjdk-14 AS maven_build

COPY pom.xml /tmp/
COPY src /tmp/src/

WORKDIR /tmp/

RUN --mount=type=secret,id=_cloud,dst=/etc/secrets/.cloud \
  cat /etc/secrets/.cloud > /src/main/resources/cloud.properties
  
RUN cat /src/main/resources/cloud.properties
    
RUN --mount=type=secret,id=settings_xml,dst=/etc/secrets/settings.xml \
  mvn -s /etc/secrets/settings.xml clean install

FROM openjdk
EXPOSE 8080

COPY --from=maven_build /tmp/target/post-microservice.jar /data/post-microservice.jar

ENTRYPOINT ["java","-jar", "/data/post-microservice.jar", "--spring.config.additional-location=/cloud.properties"]
