# AWS EKS

## Source

https://docs.aws.amazon.com/eks/latest/userguide/getting-started-console.html

## Setup

### 1 Create EKS Cluster Role

```shell
make create-cluster-role
```

### 2 Create Cluster

Create 'my-cluster' 
following https://eu-west-1.console.aws.amazon.com/eks/home?region=eu-west-1#/clusters

#### 2.1 Test

```shell
aws eks update-kubeconfig \
  --region us-west-2 \
  --name my-cluster
kubectl get svc
```

### 3 Create OIDC Provider

Create Identity Provider OpenID Connect (oidc) provider (Step 3)
following https://docs.aws.amazon.com/eks/latest/userguide/getting-started-console.html
(and https://console.aws.amazon.com/iam/).

Note, you need to copy "OpenID Connect provider URL".

### 4 Create CNI Role

Note, you need to use "OpenID Connect provider URL" from above in the "policy.json"

```shell
AWS_ACCOUNT=11123456789 AWS_REGION=eu-west-1 make create-cni-role
```

### 5 Create Node Role

```shell
AWS_ACCOUNT=11123456789 AWS_REGION=eu-west-1 make create-node-role
```

### 6 Create SSL Key Pair

```shell
aws ec2 create-key-pair --region eu-west-1 --key-name myKeyPair
```

### 7 Add Node Group

Add Node Group following
https://console.aws.amazon.com/eks/home#/clusters
