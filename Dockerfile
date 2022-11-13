FROM maven:3.6.3-openjdk-14 AS maven_build

COPY pom.xml /tmp/
COPY src /tmp/src/

WORKDIR /tmp/

RUN --mount=type=secret,id=cloud_yml,dst=/etc/secrets/cloud.yml \
  cp /etc/secrets/cloud.yml /data/cloud.yml
  
RUN --mount=type=secret,id=settings_xml,dst=/etc/secrets/settings.xml \
  mvn -s /etc/secrets/settings.xml clean install

FROM openjdk
EXPOSE 8080

COPY --from=maven_build /tmp/target/post-microservice.jar /data/post-microservice.jar
# CMD java -jar /data/post-microservice.jar

ENV SPRING_PROFILE="prd"
ENV CLOUD_CONFIG_USER="admin"
ENV CLOUD_CONFIG_PASS="costa rica"

ENTRYPOINT ["java","-jar", "/data/post-microservice.jar", "--spring.config.additional-location=/data/cloud.yml"]
