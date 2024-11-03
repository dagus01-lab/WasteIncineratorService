#first you have to ensure swap is disabled:
#sudo swapoff -a
#then you can start the kubeadm daemon:
#sudo kubeadm init ----config=kubeadm_config.yaml
#join on the two raspberry

#in order to be able to access to the cluster with kubectl, run:
#sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
#sudo chown $(id -u):$(id -g) $HOME/.kube/config

#if not installed, you need to install a CNI plugin to be able to manage the network:
#kubectl apply -f https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml

#then it is important to remove the taint that prevents scheduling (substitute the manager-node name with ther right name!!):
#kubectl taint nodes <manager-node> node-role.kubernetes.io/control-plane:NoSchedule-

# kubectl create secret generic stunnel-secrets \
#   --from-file=certs/client.crt \
#   --from-file=certs/client.key \
#   --from-file=certs/ca.crt \
#   --from-file=certs/basicrobot.key \
#   --from-file=certs/basicrobot.crt \
#   --from-file=certs/server.crt \
#   --from-file=certs/server.key
kubectl delete configmap config-files
kubectl create configmap config-files \
  --from-file=configs/stunnel_wis.conf \
  --from-file=configs/mosquitto.conf \
  --from-file=configs/scale_conf.json \
  --from-file=configs/wis_conf.json \
  --from-file=configs/basicrobotConfig.json \
  --from-file=configs/facadeConfig.json \
  --from-file=configs/monitoringdevice_conf.json \
  --from-file=configs/raspberryMockFacadeConfig.json

kubectl apply -f wis.yaml
#to show service logs: kubectl logs -f -l app=<label> 
#and to show what services are running: kubectl get all
# to show addresses: kubectl get svc

#then write on /etc/hosts (on windows write the address mappings on file C:\Windows\System32\drivers\etc\hosts): 
#<wisfacade-ingress-ip> wisfacade
#<virtualrobot-ingress-ip> virtualrobot
#where you specify the ips of the two services

#to terminate services: kubectl delete -f deployment.yaml

#to expose certain services to the outside it is necessary to use ingress component
#for this reason we must make sure a nginx controller is installed in our cluster:
#kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/cloud/deploy.yaml
#then, execute:
kubectl apply -f ingress.yaml