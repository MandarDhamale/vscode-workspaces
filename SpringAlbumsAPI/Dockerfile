# Use a maintained JDK image
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy your Spring Boot JAR
COPY target/SpringRestApi-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app uses
EXPOSE 8080

# Optional: Pass JVM options
ENV JAVA_OPTS=""

# Entrypoint
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]
