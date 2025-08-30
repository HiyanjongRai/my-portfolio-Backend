# ---- Build stage ----
FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests clean package

# ---- Run stage ----
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Render uses PORT=10000 for web services
ENV PORT=10000
EXPOSE 10000

# Memory-friendly flags for free tier
ENTRYPOINT ["java",
  "-XX:MaxRAMPercentage=70.0",
  "-XX:+UseSerialGC",
  "-Dserver.port=${PORT}",
  "-jar","/app/app.jar"
]
