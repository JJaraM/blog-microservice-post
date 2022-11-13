FROM openjdk:18-jdk-oraclelinux8 AS maven_build

COPY pom.xml /tmp/
COPY src /tmp/src/

RUN --mount=type=secret,id=settings_xml,dst=/etc/secrets/settings.xml \
 cp /etc/secrets/settings.xml /tmp/settings.xml

RUN cat /tmp/settings.xml

WORKDIR /tmp/
RUN --mount=type=secret,id=settings_xml,dst=/etc/secrets/settings.xml \
 mvn -s /etc/secrets/settings.xml clean install
