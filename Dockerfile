FROM maven:3.9.5-eclipse-temurin

WORKDIR /ontology-server

COPY src/ src/
COPY pom.xml .

RUN mvn package -Dmaven.test.skip

WORKDIR /ontology-server/target
COPY nebulous.ttl .


CMD ["java", "-jar", "nebulous-ont-0.0.1-SNAPSHOT.jar", "nebulous.ttl"]

EXPOSE 80