#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package


FROM adoptopenjdk/openjdk14:alpine-jre
COPY --from=build /home/app/target/post-microservice.jar /usr/local/lib/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]




# For Java 11, try this
# FROM adoptopenjdk/openjdk14:alpine-jre

# Refer to Maven build -> finalName
# ARG JAR_FILE=target/post-microservice.jar

# Create the dir
# RUN mkdir -p /opt/app

# cd /opt/app
# WORKDIR /opt/app

# cp target/spring-boot-web.jar /opt/app/app.jar
# COPY ${JAR_FILE} app.jar

# java -jar /opt/app/app.jar
# ENTRYPOINT ["java","-jar","app.jar"]
