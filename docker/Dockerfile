FROM gradle:8.12-jdk21 AS build
ARG MODULE
WORKDIR /app
COPY . .
RUN gradle :${MODULE}:build -x test

FROM eclipse-temurin:21-jre
ARG MODULE
WORKDIR /app
COPY --from=build /app/${MODULE}/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]