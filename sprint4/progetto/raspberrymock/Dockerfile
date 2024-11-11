## ----------------------------------------------------------
## Dockerfile raspberrymock
## ----------------------------------------------------------
## In order to start the container type the following commands:
## 1) gradlew distTar
## 2) docker build -t raspberrymock:1.0 .
## docker run -it --rm --name raspberrymock -v ./ca.crt:/etc/nginx/certs/ca.crt -v ./client.crt:/etc/nginx/certs/client.crt -v client.key:/etc/nginx/certs/client.key  -p 8125:8125/tcp 8125:8125/udp --privileged raspberrymock:1.0  sh
FROM dagus01/raspberrymockfacade:1.0 as facade

FROM dagus01/qakimage:1.0 AS build
WORKDIR /raspberrymock
COPY . .
COPY --from=facade ./*.jar /raspberrymockfacade/build/libs/
RUN ./gradlew eclipse
RUN ./gradlew build && ./gradlew distTar
RUN ./gradlew test

FROM alpine:latest
## EXPOSE 8100
## ADD extracts the tar
COPY --from=build /raspberrymock/build/distributions/raspberrymock-1.0.tar /
RUN tar -xf /raspberrymock-1.0.tar -C /
WORKDIR /raspberrymock-1.0/bin
COPY ./*.pl ./
COPY ./*.txt ./
COPY ./*.json ./

RUN apk update 
RUN apk add --no-cache openjdk11-jdk stunnel openssl \
    && apk add --no-cache mosquitto mosquitto-clients  
COPY ./stunnel.conf /etc/stunnel/stunnel_raspberry.conf
CMD ["sh", "-c", "./raspberrymock"]
#type docker_password.txt | docker login --username natbodocker --password-stdin
#docker login --username natbodocker --password xyz
#docker push natbodocker/basicrobot24:1.0


# docker-compose -f wis.yaml  up
