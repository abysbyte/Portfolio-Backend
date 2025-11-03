# Use OpenJDK as base image
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR file built by Maven/Gradle
COPY target/*.jar app.jar

# Expose the port (Render will auto-detect)
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
