FROM openjdk:17
COPY "./target/saberpro-1.jar" "app.jar"
EXPOSE 8132
ENTRYPOINT [ "java", "-jar", "app.jar" ]