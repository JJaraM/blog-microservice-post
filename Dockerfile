FROM maven:3.8.5-openjdk-11 AS maven_build
RUN echo "Copying maven settings file"

COPY settings.xml /tmp/
COPY pom.xml /tmp/
COPY src /tmp/src/

# COPY /etc/secrets/settings.xml /tmp/

WORKDIR /tmp/

RUN mvn -DgithubRepoPass=${GITHUB_TOKEN} -s settings.xml clean install

##pull base image
#FROM openjdk
#
##maintainer
#MAINTAINER jonathan.jara.morales@gmail.com
#EXPOSE 8080
#
##copy hello world to docker image from builder image
#COPY --from=maven_build /tmp/target/post-microservice.jar /data/post-microservice.jar
#
##default command
#CMD java -jar /data/post-microservice.jar
