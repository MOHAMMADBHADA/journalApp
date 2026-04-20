# Step 1: Build using Maven and Eclipse Temurin (More stable)
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Run using Eclipse Temurin (Industry Standard)
FROM eclipse-temurin:17-jdk-jammy
COPY --from=build /target/*.jar app.jar
# ... baaki purana code (FROM, COPY, etc.) ...

EXPOSE 8080

# Is line ko update karo:
ENTRYPOINT ["java", "-Dserver.port=${PORT:8080}", "-jar", "app.jar"]