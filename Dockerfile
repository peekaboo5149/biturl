# Stage 1: Build the application
FROM gradle:7.2-jdk11 AS builder

WORKDIR /app

# Copy the project's build.gradle and settings.gradle
COPY build.gradle settings.gradle ./

# Copy the Gradle wrapper files
COPY gradlew ./
COPY gradle gradle

# Resolve dependencies (this step caches dependencies to speed up builds)
RUN ./gradlew dependencies

# Copy the source code and build the application
COPY src src
#RUN ./gradlew build -x test
RUN ./gradlew buildBitUrlJar -x test

# Stage 2: Create a minimal runtime image
FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /app

ARG JAR_NAME
ARG JAR_VERSION

# Copy the JAR file from the builder stage
COPY --from=builder /app/build/libs/biturl.jar ./${JAR_NAME}-${JAR_VERSION}.jar

# Expose the port that your Spring Boot application will run on
EXPOSE 9000

# Run the Spring Boot application as a non-root user
RUN adduser -D bit
USER bit

# Specify the command to run your Spring Boot application
CMD ["java", "-jar", "biturl.jar"]
