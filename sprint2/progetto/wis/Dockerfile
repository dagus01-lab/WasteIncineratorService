## ----------------------------------------------------------
## Dockerfile for wis
## ----------------------------------------------------------
## In order to start the container type the following commands:
## 1) gradlew distTar
## 2) docker build -t wis:1.0 .
## docker run -it --rm --name wis -v ./ca.crt:/etc/nginx/certs/ca.crt -v ./client.crt:/etc/nginx/certs/client.crt -v client.key:/etc/nginx/certs/client.key  -p 8125:8125/tcp 8125:8125/udp --privileged wis:1.0  sh
    FROM alpine:latest
    ## EXPOSE 8100
    ## ADD extracts the tar
    ADD ./build/distributions/waste_incinerator_service-1.0.tar /
    WORKDIR /waste_incinerator_service-1.0/bin
    COPY ./*.pl ./
    COPY ./*.bin ./
    COPY ./*.txt ./
    
    RUN apk update 
    ##RUN apk add --no-cache openjdk11-jdk nginx
    RUN apk add --no-cache openjdk11-jdk stunnel openssl
    ## COPY nginx.conf /etc/nginx/nginx.conf
    COPY ./stunnel.conf /etc/stunnel/stunnel.conf
    ##CMD ["sh", "-c", "nginx && ./waste_incinerator_service"]
    CMD ["sh", "-c", "stunnel /etc/stunnel/stunnel.conf && ./waste_incinerator_service"]
    #type docker_password.txt | docker login --username natbodocker --password-stdin
    #docker login --username natbodocker --password xyz
    #docker push natbodocker/basicrobot24:1.0
    
    
    # docker-compose -f wis.yaml  up