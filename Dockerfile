# -----------------------
# Stage 1: Build Stage
# -----------------------
FROM eclipse-temurin:17-jdk-alpine AS build

# Install bash (needed for Maven Wrapper script)
RUN apk add --no-cache bash

# Set working directory
WORKDIR /app

# Copy Maven Wrapper files first (for better layer caching)
COPY .mvn/ .mvn/
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .

# Make Maven Wrapper executable
RUN chmod +x mvnw

# Copy source code
COPY src/ ./src/

# Build the Spring Boot project (skip tests for faster builds)
RUN ./mvnw clean package -DskipTests

# -----------------------
# Stage 2: Runtime Stage
# -----------------------
FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app

# Copy the executable Spring Boot JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
