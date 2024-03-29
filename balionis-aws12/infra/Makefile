.PHONY: help version

help:
	@grep -E '^[a-zA-Z0-9_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

version:	## - print aws cli version
	aws --version

has-account:	## - make sure AWS account variable is defined.
ifndef AWS_ACCOUNT
	$(error AWS_ACCOUNT is undefined)
endif

has-region:	## - make sure AWS region variable is defined.
ifndef AWS_REGION
	$(error AWS_REGION is undefined)
endif

has-environment:	## - make sure environment variable is defined.
ifndef ENVIRONMENT
	$(error ENVIRONMENT is undefined)
endif

has-project:	## - make sure project variable is defined.
ifndef PROJECT
	$(error PROJECT is undefined)
endif

create-cluster-role: has-environment has-project 	## - create eks cluster role with cloud formation
	aws cloudformation deploy --stack-name eks-cluster-role-$(PROJECT)-$(ENVIRONMENT) \
	--template-file ./eks-cluster-role.yaml \
	--capabilities CAPABILITY_NAMED_IAM \
	--parameter-overrides "ProjectName=$(PROJECT)" "EnvironmentName=$(ENVIRONMENT)"

create-cluster-role-deprecated:	## - create eks cluster role with aws cli
	aws iam create-role \
	  --role-name myAmazonEKSClusterRole \
	  --assume-role-policy-document file://"resources/cluster-role-trust-policy.json" && \
	aws iam attach-role-policy \
	  --policy-arn arn:aws:iam::aws:policy/AmazonEKSClusterPolicy \
	  --role-name myAmazonEKSClusterRole

create-cni-role: has-account has-region		## - create eks cni role
	aws iam create-role \
	  --role-name myAmazonEKSCNIRole \
	  --assume-role-policy-document file://"resources/cni-role-trust-policy.json" && \
	aws iam attach-role-policy \
	  --policy-arn arn:aws:iam::aws:policy/AmazonEKS_CNI_Policy \
	  --role-name myAmazonEKSCNIRole && \
	aws eks update-addon \
	  --cluster-name my-cluster \
	  --addon-name vpc-cni \
	  --service-account-role-arn arn:aws:iam::$AWS_ACCOUNT:role/myAmazonEKSCNIRole

create-node-role: has-account has-region	## - create eks node role
	aws iam create-role \
	  --role-name myAmazonEKSNodeRole \
	  --assume-role-policy-document file://"resources/node-role-trust-policy.json" && \
	aws iam attach-role-policy \
	  --policy-arn arn:aws:iam::aws:policy/AmazonEKSWorkerNodePolicy \
	  --role-name myAmazonEKSNodeRole && \
	aws iam attach-role-policy \
	  --policy-arn arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly \
	  --role-name myAmazonEKSNodeRole
