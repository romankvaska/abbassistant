# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Render.com sets PORT environment variable
ENV PORT=8080
EXPOSE ${PORT}

# JVM memory settings for Render free tier (512MB RAM)
ENV JAVA_OPTS="-Xms128m -Xmx384m -XX:+UseSerialGC -XX:MaxRAM=512m"

# Use shell form to expand environment variables
CMD java $JAVA_OPTS -Dserver.port=$PORT -Dspring.profiles.active=postgres -jar app.jar
