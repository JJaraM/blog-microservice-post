FROM maven:3.8.5-openjdk-11 AS maven_build

RUN --mount=type=secret,id=settings_xml,dst=/etc/secrets/settings.xml cat /etc/secrets/settings.xml

COPY pom.xml /tmp/
COPY src /tmp/src/

WORKDIR /tmp/
RUN --mount=type=secret,id=settings_xml,dst=/etc/secrets/settings.xml mvn -s /etc/secrets/settings.xml clean install
