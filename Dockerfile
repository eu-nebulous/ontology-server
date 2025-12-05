FROM maven:3.9.5-eclipse-temurin AS build

WORKDIR /ontology-server
COPY src/ src/
COPY pom.xml pom.xml

RUN mvn package -Dmaven.test.skip

WORKDIR /ontology-server/target

#
# Package stage
#
FROM docker.io/library/eclipse-temurin:17-jre
COPY --from=build /ontology-server/target/nebulous-ont-0.0.1-SNAPSHOT.jar nebulous-ont.jar
COPY nebulous.ttl nebulous.ttl


CMD ["java", "-jar", "nebulous-ont.jar", "nebulous.ttl"]

EXPOSE 80