FROM gradle:8.14-jdk21 AS builder

WORKDIR /app

COPY build.gradle* ./
COPY settings.gradle* ./
COPY gradle ./gradle
COPY gradlew ./

RUN chmod +x gradlew

RUN ./gradlew dependencies --no-daemon || true

COPY src ./src

RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS ="-Xms224m -Xmx736m -XX:MetaspaceSize=96m -XX:MaxMetaspaceSize=128m -XX:+UseZGC -XX:+ZGenerational -XX:+ExitOnOutOfMemoryError -XX:+DisableExplicitGC -Djava.security.egd=file:/dev/./urandom -XX:+UseContainerSupport -XX:ActiveProcessorCount=2 -XX:ParallelGCThreads=2 -XX:ConcGCThreads=1"


ENTRYPOINT ["sh", "-c", "java -jar app.jar"]