FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /mseureka
COPY . .
RUN mvn clean package -DskipTests


FROM openjdk:17
WORKDIR /mseureka
COPY --from=build ./mseureka/target/*.jar ./mseureka.jar
EXPOSE 8761
ENTRYPOINT java -jar mseureka.jar