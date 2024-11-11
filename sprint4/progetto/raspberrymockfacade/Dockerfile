FROM alpine:latest
COPY ./build/libs/* ./
COPY ./*.json ./
RUN apk add --no-cache stunnel openssl openjdk11-jdk
CMD ["sh", "-c", "java -jar ./raspberrymockfacade-0.0.1-SNAPSHOT.jar"]  