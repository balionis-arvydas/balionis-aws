# AWS EKS

## Source

https://docs.aws.amazon.com/eks/latest/userguide/getting-started-console.html

## Setup


```shell

#1

aws iam create-role \
  --role-name myAmazonEKSClusterRole \
  --assume-role-policy-document file://"cluster-role-trust-policy.json"

aws iam attach-role-policy \
  --policy-arn arn:aws:iam::aws:policy/AmazonEKSClusterPolicy \
  --role-name myAmazonEKSClusterRole

#2 

curl https://eu-west-1.console.aws.amazon.com/eks/home?region=eu-west-1#/clusters
# create 'my-cluster'

aws eks update-kubeconfig \
  --region us-west-2 \
  --name my-cluster

kubectl get svc

#3

curl https://console.aws.amazon.com/iam/
# create Identity Provider OpenID Connect (oidc) provider


#4 

aws iam create-role \
  --role-name myAmazonEKSCNIRole \
  --assume-role-policy-document file://"cni-role-trust-policy.json"

aws iam attach-role-policy \
  --policy-arn arn:aws:iam::aws:policy/AmazonEKS_CNI_Policy \
  --role-name myAmazonEKSCNIRole

aws eks update-addon \
  --cluster-name my-cluster \
  --addon-name vpc-cni \
  --service-account-role-arn arn:aws:iam::$AWS_ACCOUNT_ID:role/myAmazonEKSCNIRole 


aws iam create-role \
  --role-name myAmazonEKSNodeRole \
  --assume-role-policy-document file://"node-role-trust-policy.json"

aws iam attach-role-policy \
  --policy-arn arn:aws:iam::aws:policy/AmazonEKSWorkerNodePolicy \
  --role-name myAmazonEKSNodeRole
aws iam attach-role-policy \
  --policy-arn arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly \
  --role-name myAmazonEKSNodeRole

aws ec2 create-key-pair --region eu-west-1 --key-name myKeyPair


```

