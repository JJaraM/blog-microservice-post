FROM maven:3.6.3-openjdk-14 AS maven_build

COPY pom.xml /tmp/
COPY src /tmp/src/

WORKDIR /tmp/

# RUN --mount=type=secret,id=cloud_properties,dst=/etc/secrets/cloud.properties \
#   cat /etc/secrets/cloud.properties > /tmp/cloud.properties
  
# RUN cat /tmp/cloud.properties

RUN --mount=type=secret,id=cloud_properties,dst=/etc/secrets/cloud.properties \
  cat /etc/secrets/cloud.properties | grep "spring.profiles.active" | cut -d "=" -f2 > /tmp/cloud_grep.properties
  
RUN cat /tmp/cloud_grep.properties
  
RUN --mount=type=secret,id=settings_xml,dst=/etc/secrets/settings.xml \
  mvn -s /etc/secrets/settings.xml clean install

FROM openjdk
EXPOSE 8080

COPY --from=maven_build /tmp/target/post-microservice.jar /data/post-microservice.jar

ENTRYPOINT ["java","-jar", "/data/post-microservice.jar", "--spring.config.additional-location=/tmp/cloud.properties"]
