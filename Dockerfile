FROM openjdk:8-jre-alpine

COPY ./target/spring-qrcode-example-1.3.0.jar /spring-qrcode-example.jar

CMD ["./spring-qrcode-example.jar"]