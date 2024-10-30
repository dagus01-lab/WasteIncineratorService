kubectl create secret generic stunnel-secrets \
  --from-file=certs/client.crt \
  --from-file=certs/client.key \
  --from-file=certs/ca.crt \
  --from-file=certs/basicrobot.key \
  --from-file=certs/basicrobot.crt \
  --from-file=certs/server.crt \
  --from-file=certs/server.key

kubectl create configmap config-files \
  --from-file=configs/stunnel_wis.conf \
  --from-file=configs/stunnel_raspberry.conf \
  --from-file=configs/mosquitto.conf \
  --from-file=configs/scale_conf.json \
  --from-file=configs/basicrobotConfig.json \
  --from-file=configs/facadeConfig.json \
  --from-file=configs/monitoringdevice_conf.json

kubectl apply -f deployment.yaml
#to show service logs: kubectl logs -f -l app=<label> 
#and to show what services are running: kubectl get all

#to terminate services: kubectl delete -f deployment.yaml