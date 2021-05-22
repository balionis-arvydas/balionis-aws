# balionis-aws11

This sandbox is to remind me how to run kubernetes 'kind' on localhost using
1) nginx

## Setup

```
curl -Lo ./tmp/kind https://kind.sigs.k8s.io/dl/v0.11.0/kind-linux-amd64
chmod +x ./tmp/kind
sudo mv ./tmp/kind /usr/local/bin/kind
kind --version
```

## Test

```
kind get clusters
wsl2
```

```
kubectl cluster-info --context kind-wsl2
Kubernetes master is running at https://127.0.0.1:40471
...
```
