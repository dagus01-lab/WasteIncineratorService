## ----------------------------------------------------------
## Dockerfile for wis
## ----------------------------------------------------------
## In order to start the container type the following commands:
## 1) gradlew distTar
## 2) docker build -t wis:1.0 .
## docker run -it --rm --name wis -v ./ca.crt:/etc/nginx/certs/ca.crt -v ./client.crt:/etc/nginx/certs/client.crt -v client.key:/etc/nginx/certs/client.key  -p 8125:8125/tcp 8125:8125/udp --privileged wis:1.0  sh



    FROM dagus01/qakimage:1.0 AS build
	WORKDIR /wis
    COPY . .
    RUN ./gradlew eclipse
    RUN ./gradlew build && ./gradlew distTar
	RUN ./gradlew test

    FROM nginx:alpine
    ## EXPOSE 8100
    ## ADD extracts the tar
    COPY --from=build /wis/build/distributions/waste_incinerator_service-1.0.tar /
    RUN tar -xf /waste_incinerator_service-1.0.tar -C /
    WORKDIR /waste_incinerator_service-1.0/bin
    COPY ./*.pl ./
    COPY ./*.bin ./
    COPY ./*.txt ./
    COPY ./*.json ./
    
    RUN apk update 
    RUN apk add --no-cache libcoap openjdk11-jdk stunnel openssl \
        && apk add --no-cache mosquitto mosquitto-clients
    RUN apk add --no-cache nginx && \
        mkdir -p /run/nginx
    COPY ./stunnel.conf /etc/stunnel/stunnel_wis.conf 
    ##CMD ["sh", "-c", "nginx && ./waste_incinerator_service"]
    CMD ["sh", "-c", "./waste_incinerator_service"]
    #type docker_password.txt | docker login --username natbodocker --password-stdin
    #docker login --username natbodocker --password xyz
    #docker push natbodocker/basicrobot24:1.0
    
    
    # docker-compose -f wis.yaml  up