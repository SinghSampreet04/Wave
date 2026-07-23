FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src

RUN mvn -B -q clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/* \
    && groupadd --system wave \
    && useradd --system --gid wave --home-dir /app wave \
    && mkdir -p /app/uploads \
    && chown -R wave:wave /app

COPY --from=builder /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

USER wave

EXPOSE 8082

HEALTHCHECK --interval=20s --timeout=5s --start-period=45s --retries=5 \
  CMD curl --fail --silent http://localhost:8082/actuator/health || exit 1

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-XX:+ExitOnOutOfMemoryError", "-jar", "app.jar"]
