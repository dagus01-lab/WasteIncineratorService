## ----------------------------------------------------------
## Dockerfile monitoringdevice
## ----------------------------------------------------------
## In order to build the container type the following commands:
## 1) gradlew distTar
## 2) Essendo l'immagine pensata per essere eseguita su raspberry, che ha architettura arm, occorre 
##    fare build per l'opportuna piattaforma su cui il container eseguirà. 
##    A tal scopo il comando docker buildx ci viene in ausilio. Per abilitare il build
##    su altre architetture occorre anzitutto creare un opportuno builder:
##      docker buildx create --use --name multiarch-builder
##      docker buildx inspect --bootstrap
##    Per poter fare la build di architetture diverse è necessario QEMU, che può essere 
##    installato digitando:
##      docker run --privileged --rm tonistiigi/binfmt --install all
##    (se questi step per configurare buildx sono già stati effettuati non è necessario ripeterli)
##    Infine, l'immagine può essere costruita:
##      docker buildx build --platform linux/arm64/v8 -t dagus01/monitoringdevice --load .
## 3) è possibile condividere l'immagine con un raspberry sulla stessa LAN:
##      docker save monitoringdevice:arm | ssh user@<MD-ip> 'sudo docker load'
##    oppure, facendo push su docker hub. In tal caso occorre fare tag dell'immagine locale:
##      docker tag monitoringdevice <username>/monitoringdevice
##    dove <username> denota il nome utente del proprio repository dockerhub.
##    Occorre poi fare login:
##      docker login --username <username>
##    e infine fare push:
##      docker push <username>/monitoringdevice:arm
##
## docker run -it --rm --name monitoringdevice -p 8100:8100/tcp  -monitoringdevice:1.0  sh
FROM arm64v8/alpine

## EXPOSE 8100
RUN apk update
RUN set -xe && apk add --no-cache libgpiod gcc build-base linux-headers musl-dev libgpiod-dev stunnel
RUN apk add --no-cache openjdk11-jdk 

## ADD extracts the tar
ADD ./build/distributions/monitoringdevice-1.0.tar /
WORKDIR /monitoringdevice-1.0/bin
COPY ./*.pl ./
COPY ./*.txt ./
COPY ./resources/* ./
COPY ./*.json ./

RUN gcc sonar.c -o sonar -lgpiod
RUN gcc ledDevice.c -o ledDevice -lgpiod

CMD ["sh", "-c", "./monitoringdevice"]
