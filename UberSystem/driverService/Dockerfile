FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copy the JAR from local machine (you need to build it first locally)
COPY driverService/target/driverService-1.0-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]