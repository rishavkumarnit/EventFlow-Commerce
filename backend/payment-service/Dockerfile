FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /workspace
COPY . .
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew
RUN ./gradlew :payment-service:bootJar --no-daemon

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /workspace/payment-service/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
