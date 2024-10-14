FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /msestante
COPY . .
RUN mvn clean package -DskipTests


FROM openjdk:17
WORKDIR /msestante
COPY --from=build ./msestante/target/*.jar ./msestante.jar
ENTRYPOINT java -jar msestante.jar --spring.profiles.active=docker