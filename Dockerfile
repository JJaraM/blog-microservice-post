FROM maven:3.8.5-openjdk-11 AS maven_build
RUN echo "Copying maven settings file"
RUN mkdir -p /root/.m2 
RUN mkdir /root/.m2/repository
    
COPY settings.xml /usr/share/maven/ref/
COPY pom.xml /tmp/
COPY settings.xml /tmp/

COPY src /tmp/src/
COPY settings.xml /root/.m2/
COPY settings.xml /usr/share/maven/ref/

ADD settings.xml /root/.m2/settings.xml

WORKDIR /tmp/



#RUN mvn --settings /usr/share/maven/ref/settings.xml clean install
# RUN mvn clean install

RUN echo "Getting current location"
RUN echo "$(pwd)"
RUN mvn -settings settings.xml clean install

#pull base image
FROM openjdk

#maintainer 
MAINTAINER jonathan.jara.morales@gmail.com
EXPOSE 8080

#copy hello world to docker image from builder image
COPY --from=maven_build /tmp/target/post-microservice.jar /data/post-microservice.jar

#default command
CMD java -jar /data/post-microservice.jar

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
