FROM maven:3.6.3-openjdk-14 AS maven_build

COPY pom.xml /tmp/
COPY src /tmp/src/

WORKDIR /tmp/

RUN --mount=type=secret,id=_cloud,dst=/etc/secrets/.cloud \
  cat /etc/secrets/.cloud > /tmp/src/main/resources/cloud.yml  
RUN cat /tmp/src/main/resources/cloud.yml

# Cloud Config Server: User
RUN --mount=type=secret,id=_user,dst=/etc/secrets/.user \
  cat /etc/secrets/.user > /tmp/user.property
  
ARG cloud_user
RUN cloud_user=$(cat /tmp/user.property)
RUN echo $cloud_user
RUN cat /tmp/user.property

RUN --mount=type=secret,id=settings_xml,dst=/etc/secrets/settings.xml \
  mvn -s /etc/secrets/settings.xml clean install

FROM openjdk
EXPOSE 8080

COPY --from=maven_build /tmp/target/post-microservice.jar /data/post-microservice.jar

ENTRYPOINT ["java","-jar", "/data/post-microservice.jar", "--spring.profiles.active=prd", "--spring.cloud.config.username=${cloud_user}", "--spring.cloud.config.password=pass"]
