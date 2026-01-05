# 1. Build Stage
FROM gradle:8.7-jdk21 AS builder
WORKDIR /app

# 루트 컨텍스트에서 backend 폴더의 내용을 복사
COPY backend/build.gradle backend/settings.gradle ./
COPY backend/src ./src

# 빌드 실행 (테스트 제외)
RUN gradle build -x test --no-daemon

# 2. Run Stage
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 실행 포트 노출
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
