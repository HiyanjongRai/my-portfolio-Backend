# ---- Build stage ----
FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app

# Cache deps
COPY pom.xml ./
RUN mvn -B -q -DskipTests dependency:go-offline

# Build
COPY src ./src
RUN mvn -B -DskipTests clean package

# ---- Run stage ----
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy fat jar
COPY --from=build /app/target/*.jar app.jar

# The platform (Koyeb/Render/etc.) will inject PORT; Spring reads it via server.port=${PORT:8080}
EXPOSE 8080

# Optional: small timezone & curl for health/debug (remove if you want a slimmer image)
RUN apk add --no-cache tzdata curl

# JVM tuned for small memory plans
ENTRYPOINT ["java", \
  "-XX:MaxRAMPercentage=70.0", \
  "-XX:+UseSerialGC", \
  "-jar","/app/app.jar" \
]
