FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /msleitor
COPY . .
RUN mvn clean package -DskipTests


FROM openjdk:17
WORKDIR /msleitor
COPY --from=build ./msleitor/target/*.jar ./msleitor.jar
ENTRYPOINT java -jar msleitor.jar --spring.profiles.active=docker