# ---------- Build stage ----------
FROM gradle:8.10.2-jdk17 AS build
WORKDIR /workspace

COPY build.gradle settings.gradle ./
COPY gradle gradle
RUN gradle --version
RUN gradle dependencies --no-daemon || true

COPY . .

RUN gradle clean bootJar --no-daemon

# ---------- Run stage ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /workspace/build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","/app/app.jar"]

