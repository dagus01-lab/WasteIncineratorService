#/bin/bash
openssl genrsa -out /etc/mosquitto/certs/mosquitto.key 2048
openssl req -new -key /etc/mosquitto/certs/mosquitto.key -out /etc/mosquitto/certs/mosquitto.csr
openssl x509 -req -in /etc/mosquitto/certs/mosquitto.csr -signkey /etc/mosquitto/certs/mosquitto.key -out /etc/mosquitto/certs/mosquitto.crt -days 365
