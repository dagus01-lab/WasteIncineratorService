#first you have to ensure swap is disabled:
#sudo swapoff -a
#then you can start the kubeadm daemon:
#curl -sfL https://get.k3s.io | sh -

#after installing it for the first time, you can enable/disable it just with:
#sudo systemctl start k3s
#or
#sudo systemctl stop k3s

#join on the two raspberry by retrieving the Node Token and Master IP Address On the master node, get the node token, 
#which will be used for workers to join the cluster:
#cat /var/lib/rancher/k3s/server/node-token
#and then join on the raspberry devices:
#curl -sfL https://get.k3s.io | K3S_URL=https://<master-ip>:6443 K3S_TOKEN=<node-token> sh -

#after installing it for the first time, you can first copy from the manager node the configuration file:
#sudo k3s agent --server https://<control-plane-ip>:6443 --token <node-token>


#in order to be able to access to the cluster with kubectl, run:
#sudo cp /etc/rancher/k3s/k3s.yaml $HOME/.kube/config
#sudo chown $(id -u):$(id -g) $HOME/.kube/config

#with k3s there is no need to install a CNI plugin, as we have flannel already installed

#then it is important to remove the taint that prevents scheduling (substitute the manager-node name with ther right name!!):
#kubectl taint nodes <manager-node> node-role.kubernetes.io/control-plane:NoSchedule-

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