## ----------------------------------------------------------
## Dockerfile for wis
## ----------------------------------------------------------
## In order to start the container type the following commands:
## 1) gradlew build
## 2) docker build -t wisfacade:1.0 .
## docker run -it --rm --name wisfacade -v ./ca.crt:/etc/nginx/certs/ca.crt -v ./client.crt:/etc/nginx/certs/client.crt -v client.key:/etc/nginx/certs/client.key  -p 8050:8050/tcp 8050:8050/udp --privileged wisfacade:1.0  sh

#MULTI-STAGE build to integrate tests with github actions
FROM dagus01/qakimage:1.0 AS build
WORKDIR /wisfacade
COPY . .
RUN apk add openjdk17-jdk
RUN ./gradlew build
RUN ./gradlew test 

FROM alpine:latest
COPY --from=build /wisfacade/build/libs/wisFacade-0.0.1-SNAPSHOT.jar wisFacade-0.0.1-SNAPSHOT.jar
COPY ./*.json ./
RUN apk add --no-cache stunnel openssl openjdk17-jdk
COPY ./stunnel.conf /etc/stunnel/stunnel.conf
CMD ["sh", "-c", "java -jar ./wisFacade-0.0.1-SNAPSHOT.jar"]    

    #type docker_password.txt | docker login --username natbodocker --password-stdin
    #docker login --username natbodocker --password xyz
    #docker push natbodocker/basicrobot24:1.0
    
    
    # docker-compose -f wis.yaml  up