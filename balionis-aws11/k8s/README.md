# k8s

## Prepare

### Kind
```
curl -Lo ~/tmp/kind https://kind.sigs.k8s.io/dl/v0.11.0/kind-linux-amd64 \
    && chmod +x ~/tmp/kind \
    && sudo mv ~/tmp/kind /usr/local/bin/kind \
    && kind --version
```

### Kubeval
```
curl -Lo ~/tmp/kubeval-linux-amd64.tar.gz https://github.com/instrumenta/kubeval/releases/download/v0.16.1/kubeval-linux-amd64.tar.gz \
    && tar xf ~/tmp/kubeval-linux-amd64.tar.gz --directory ~/tmp \
    && chmod +x ~/tmp/kubeval \
    && sudo mv ~/tmp/kubeval /usr/local/bin/kubeval \
    && kubeval --version
```

### Kustomize
```
curl -Lo ~/tmp/kustomize_v4.1.3_linux_amd64.tar.gz https://github.com/kubernetes-sigs/kustomize/releases/download/kustomize/v4.1.3/kustomize_v4.1.3_linux_amd64.tar.gz \
    && tar xf ~/tmp/kustomize_v4.1.3_linux_amd64.tar.gz --directory ~/tmp \
    && chmod +x ~/tmp/kustomize \
    && sudo mv ~/tmp/kustomize /usr/local/bin/kustomize \ 
    && kustomize version
```

### Kubectl
```
curl -Lo ~/tmp/kubectl https://amazon-eks.s3-us-west-2.amazonaws.com/1.18.9/2020-11-02/bin/linux/amd64/kubectl \
    && chmod +x ~/tmp/kubectl \
    && sudo mv ~/tmp/kubectl /usr/local/bin/kubectl \
    && kubectl version
```

## Build & Deploy (Zero-To-Hero)
```
cd k8s \ 
  && make kind-create-cluster \
  && ENVIRONMENT=local NAMESPACE=ingress-nginx MODULE=ingress-nginx make apply \
  && cd ..

./gradlew clean mock-sync-service:dockerPush

cd k8s \ 
  && ENVIRONMENT=local NAMESPACE=default MODULE=mock-sync-service make apply \
  && ENVIRONMENT=local NAMESPACE=default MODULE=mock-ingress make apply \
  && cd ..  
```

## Cleanup
```
cd k8s \ 
  && make kind-delete-cluster \
  && docker system prune -f \
  && docker volume prune -f
```

## Test

### Test cluster
```
kind get clusters
wsl2
```

```
kubectl cluster-info --context kind-wsl2
Kubernetes master is running at https://127.0.0.1:40471
...
```

### Test service
```
kubectl port-forward service/mock-sync-service 8180:8080
curl -X GET localhost:8180/api/scenario/s0 
```

### Test ingress
```
curl -X GET http://localhost/api/scenario/s0
```

## Troubleshoot
```
docker exec -it wsl2-control-plane bash
crictl images
ps auxf
```
