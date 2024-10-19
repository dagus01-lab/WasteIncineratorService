## ----------------------------------------------------------
## Dockerfile for wis
## ----------------------------------------------------------
## In order to start the container type the following commands:
## 1) gradlew build
## 2) docker build -t wisfacade:1.0 .
## docker run -it --rm --name wisfacade -v ./ca.crt:/etc/nginx/certs/ca.crt -v ./client.crt:/etc/nginx/certs/client.crt -v client.key:/etc/nginx/certs/client.key  -p 8125:8125/tcp 8125:8125/udp --privileged wis:1.0  sh
FROM openjdk:17-jdk-alpine
COPY build/libs/wisFacade-0.0.1-SNAPSHOT.jar wisFacade-0.0.1-SNAPSHOT.jar
RUN apk add --no-cache stunnel openssl
COPY ./stunnel.conf /etc/stunnel/stunnel.conf

ENTRYPOINT ["sh", "-c", "stunnel /etc/stunnel/stunnel.conf && java -jar ./wisFacade-0.0.1-SNAPSHOT.jar"]    

    #type docker_password.txt | docker login --username natbodocker --password-stdin
    #docker login --username natbodocker --password xyz
    #docker push natbodocker/basicrobot24:1.0
    
    
    # docker-compose -f wis.yaml  up