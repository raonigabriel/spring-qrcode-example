FROM openjdk:8-jre-alpine

ARG JAR_FILE=target/*.jar

EXPOSE 8080

COPY ${JAR_FILE} /app.jar

ENTRYPOINT ["java","-jar","/app.jar"]