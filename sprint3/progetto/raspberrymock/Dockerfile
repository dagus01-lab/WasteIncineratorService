## ----------------------------------------------------------
## Dockerfile raspberrymock
## ----------------------------------------------------------
## In order to start the container type the following commands:
## 1) gradlew distTar
## 2) docker build -t raspberrymock:1.0 .
## docker run -it --rm --name raspberrymock -v ./ca.crt:/etc/nginx/certs/ca.crt -v ./client.crt:/etc/nginx/certs/client.crt -v client.key:/etc/nginx/certs/client.key  -p 8125:8125/tcp 8125:8125/udp --privileged raspberrymock:1.0  sh

FROM openjdk:17 AS build
WORKDIR /raspberrymock
COPY . .
RUN ./gradlew build && ./gradlew distTar

FROM openjdk:17 AS test-stage
WORKDIR /raspberrymock
COPY --from=build /raspberrymock /raspberrymock
RUN ./gradlew test 
FROM alpine:latest
## EXPOSE 8100
## ADD extracts the tar
ADD --from=test-stage /raspberrymock/build/distributions/raspberrymock-1.0.tar /
WORKDIR /raspberrymock-1.0/bin
COPY ./*.pl ./
COPY ./*.txt ./
COPY ./*.json ./

RUN apk update 
RUN apk add --no-cache openjdk11-jdk stunnel openssl
COPY ./stunnel.conf /etc/stunnel/stunnel_raspberry.conf
##CMD ["sh", "-c", "nginx && ./waste_incinerator_service"]
CMD ["sh", "-c", "stunnel /etc/stunnel/stunnel.conf && ./raspberrymock"]
#type docker_password.txt | docker login --username natbodocker --password-stdin
#docker login --username natbodocker --password xyz
#docker push natbodocker/basicrobot24:1.0


# docker-compose -f wis.yaml  up
