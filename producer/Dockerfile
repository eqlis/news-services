FROM mcr.microsoft.com/playwright/java:v1.57.0-jammy

COPY target/producer-0.0.1-SNAPSHOT.jar producer.jar

ENTRYPOINT ["java", "-jar", "/producer.jar"]