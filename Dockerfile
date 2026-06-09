FROM eclipse-temurin:21-jdk
COPY ./target/saberpro-1.jar app.jar
EXPOSE 8132
CMD ["java", "-jar", "app.jar"]
