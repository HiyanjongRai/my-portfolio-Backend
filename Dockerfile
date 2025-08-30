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

# Render requires binding to PORT (default=10000)
ENV PORT=10000
EXPOSE 10000
ENTRYPOINT ["java","-XX:MaxRAMPercentage=75.0","-Dserver.port=${PORT}","-jar","/app/app.jar"]
