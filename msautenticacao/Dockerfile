FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /msautenticacao
COPY . .
RUN mvn clean package -DskipTests


FROM openjdk:17
WORKDIR /msautenticacao
COPY --from=build ./msautenticacao/target/*.jar ./msautenticacao.jar
EXPOSE 8085
ENTRYPOINT java -jar msautenticacao.jar --spring.profiles.active=docker