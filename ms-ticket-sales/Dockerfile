FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew build -x test
EXPOSE 8083
CMD ["java", "-jar", "build/libs/ms-ticket-sales-0.0.1-SNAPSHOT.jar"]
