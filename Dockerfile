# ---- Build stage ----
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests clean package

# ---- Run stage ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Render will set PORT at runtime; default to 8080 locally
ENV PORT=8080
EXPOSE 8080

# Run with memory-friendly flags; shell form so $PORT expands
CMD sh -c 'java -XX:MaxRAMPercentage=70.0 -XX:+UseSerialGC -Dserver.port=$PORT -jar /app/app.jar'
