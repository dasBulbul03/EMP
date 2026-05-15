## Multi-stage Dockerfile

# Build stage: use Maven with Temurin 21
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml ./
RUN mvn -B -DskipTests=true dependency:go-offline
COPY src ./src
RUN mvn -B -DskipTests=true clean package

# Runtime stage: smaller, non-root runtime image
FROM eclipse-temurin:21-jre-jammy

RUN groupadd --system app && useradd --system --gid app --create-home app
WORKDIR /app
COPY --from=build /workspace/target/ems-0.0.1-SNAPSHOT.jar ./app.jar
RUN chown app:app /app/app.jar

USER app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
