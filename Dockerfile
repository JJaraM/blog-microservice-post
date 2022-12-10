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

FROM openjdk
EXPOSE 8761

COPY --from=maven_build /tmp/target/app.jar /data/app.jar
ENTRYPOINT ["java","-jar", "/data/app.jar"]
