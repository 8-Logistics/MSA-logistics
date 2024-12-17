# 베이스 이미지 설정
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Wrapper 복사
COPY gradlew ./
COPY gradle gradle
COPY build.gradle ./
COPY settings.gradle ./
COPY src src

# Gradle 빌드 실행
RUN chmod +x gradlew
RUN ./gradlew clean build

# JAR 파일 복사
COPY build/libs/*.jar app.jar

# 컨테이너 실행 시 JAR 파일 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
