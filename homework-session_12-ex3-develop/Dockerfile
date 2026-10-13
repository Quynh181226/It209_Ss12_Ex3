# ==========================================
# Stage 1: Build Stage (Gradle + JDK 17)
# ==========================================
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copy Gradle wrapper and build configuration files
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Copy source code
COPY src src

# Fix line endings (if built from Windows checkout) and build Spring Boot executable JAR
RUN sed -i 's/\r$//' gradlew && \
    chmod +x gradlew && \
    ./gradlew bootJar -x test --no-daemon

# ==========================================
# Stage 2: Runtime Stage (JRE 17 Alpine)
# ==========================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy executable JAR from build stage
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
