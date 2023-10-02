# Stage 1: Build the Spring Boot application with Gradle
FROM gradle:8.3.0-jdk17-alpine AS build

WORKDIR /app
COPY build.gradle settings.gradle /app/

COPY src /app/src/
RUN gradle clean build --no-daemon -x test

# Stage 2: Create a lightweight runtime image
FROM openjdk:17-oracle
WORKDIR /app

# Copy the JAR file built in the previous stage
COPY --from=build /app/build/libs/biturl-1.0.0.jar app.jar

# Expose the port your Spring Boot application listens on (default is 8080)
EXPOSE 9000

# Command to run the Spring Boot application when the container starts
CMD ["java", "-jar", "app.jar"]
