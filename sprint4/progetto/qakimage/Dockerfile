FROM alpine:latest
COPY ./unibolibs/*.jar /unibolibs/
RUN apk update 
RUN apk add --no-cache openjdk11-jdk
RUN apk add --no-cache mosquitto mosquitto-clients
ENV GRADLE_USER_HOME=/opt/gradle
ENV PATH=$PATH:$GRADLE_USER_HOME/bin

COPY . .

RUN chmod +x gradlew \
    && ./gradlew --no-daemon --info tasks

# Verify installation
RUN ./gradlew --version

# Final base image
CMD ["gradlew"]