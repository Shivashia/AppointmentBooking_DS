# Use Java 17 base image
FROM openjdk:17-jdk-slim

# Set working directory in container
WORKDIR /app

# Copy the built jar from target folder into container
COPY target/Appointment-0.0.1-SNAPSHOT.jar app.jar

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
