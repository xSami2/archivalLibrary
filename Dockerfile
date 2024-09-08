FROM openjdk:17-jdk-slim

WORKDIR /app

ENV SPRING_PROFILES_ACTIVE=prod
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/TheArchivalLibrary
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=admin
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update
##
ENV FILES_PATH=src/main/resources


COPY target/archivalLibrary-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9091

ENTRYPOINT ["java", "-jar", "app.jar"]
