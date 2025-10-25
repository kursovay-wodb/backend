# Stage 1: Build
FROM maven:3.8.6-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime  
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/demo-*.jar app.jar
RUN groupadd -r spring && useradd -r -g spring spring
USER spring
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]