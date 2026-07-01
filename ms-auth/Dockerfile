FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew build -x test
EXPOSE 8081
CMD ["java", "-jar", "build/libs/ms-auth-0.0.1-SNAPSHOT.jar"]
