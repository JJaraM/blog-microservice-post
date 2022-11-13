FROM maven:3.8.5-openjdk-11 AS maven_build

RUN --mount=type=secret,id=_maven_settings,dst=/etc/secrets/settings.xml cat /etc/secrets/settings.xml
RUN echo "Copying maven settings file"

COPY settings.xml /tmp/
COPY pom.xml /tmp/
COPY src /tmp/src/

# COPY /etc/secrets/settings.xml /tmp/

# RUN echo "env1 ${env.GITHUB_TOKEN} env2 ${GITHUB_TOKEN}"

WORKDIR /tmp/

RUN mvn -s settings.xml clean install

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
