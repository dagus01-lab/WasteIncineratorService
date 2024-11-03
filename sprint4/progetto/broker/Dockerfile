FROM alpine:latest
## Instructions for setup
## 1) docker build -t mqtt_broker:1.0 .
## Per eseguire (in alternatia a docker-compose): docker run -it --rm --name mqtt_broker -v ./ca.crt:/etc/mosquitto/certs/ca.crt -v ./server.crt:/etc/mosquitto/certs/server.crt -v ./server.key:/etc/mosquitto/certs/server.key -p 8081:8081 mqtt_broker:1.0 sh

RUN apk update && apk add mosquitto mosquitto-clients
COPY ./mosquitto.conf /etc/mosquitto/

CMD ["mosquitto", "-c", "/etc/mosquitto/mosquitto.conf"]