FROM maven:3.8.5-openjdk-11 AS maven_build
RUN echo "Copying maven settings file"
#COPY .travis.settings.xml /tmp/
COPY settings.xml /root/.m2
COPY pom.xml /tmp/


COPY src /tmp/src/

WORKDIR /tmp/

RUN mvn package

#pull base image

FROM openjdk

#maintainer 
MAINTAINER jonathan.jara.morales@gmail.com
#expose port 8080
EXPOSE 8080

#default command
CMD java -jar /data/post-microservice.jar

#copy hello world to docker image from builder image

COPY --from=maven_build /tmp/target/post-microservice.jar /data/post-microservice.jar




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
