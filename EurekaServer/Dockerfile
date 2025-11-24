# Use Java 17 base image
FROM openjdk:17-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy built jar into container
COPY target/EurekaServer-0.0.1-SNAPSHOT.jar app.jar

# Expose Eureka default port
EXPOSE 8761

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
