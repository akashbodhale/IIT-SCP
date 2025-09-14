# -----------------------
# Stage 1: Build Stage
# -----------------------
FROM openjdk:17-jdk-slim AS build

# Set working directory
WORKDIR /app

# Copy Maven Wrapper and pom.xml
COPY .mvn/ .mvn/
COPY mvnw .
COPY pom.xml .

# Copy source code
COPY src/ ./src/

# Make Maven Wrapper executable
RUN chmod +x mvnw

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
