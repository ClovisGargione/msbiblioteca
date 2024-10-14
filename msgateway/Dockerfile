FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /gateway
COPY . .
RUN mvn clean package -DskipTests


FROM openjdk:17
WORKDIR /gateway
COPY --from=build ./gateway/target/*.jar ./gateway.jar
EXPOSE 8080
ENTRYPOINT java -jar gateway.jar --spring.profiles.active=docker