# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Install runtimes: Python, C++, Node.js
RUN apt-get update && apt-get install -y \
    g++ \
    python3 \
    nodejs \
    && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/online-compiler-0.0.1-SNAPSHOT.jar app.jar

# Set environment variable to indicate we are in a container with pre-installed runtimes
ENV RUNTIME_ENVIRONMENT=CONTAINER

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
