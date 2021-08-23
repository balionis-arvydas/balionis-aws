.PHONY: help version

help:
	@grep -E '^[a-zA-Z0-9_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

version:	## - print aws cli version
	aws --version

has-environment:	## - make sure environment variable is defined.
ifndef ENVIRONMENT
	$(error ENVIRONMENT is undefined)
endif

has-project:	## - make sure project variable is defined.
ifndef PROJECT
	$(error PROJECT is undefined)
endif

has-account:	## - make sure AWS account variable is defined.
ifndef AWS_ACCOUNT
	$(error AWS_ACCOUNT is undefined)
endif

has-region:	## - make sure AWS region variable is defined.
ifndef AWS_REGION
	$(error AWS_REGION is undefined)
endif

create-s3-bucket:	## - create stack of s3 bucket which stores terraform state
	aws cloudformation deploy --stack-name s3-bucket-terraform-state --template-file ./s3.yaml

create-dynamodb:	## - create stack of dynamodb table which locks terraform state
	aws cloudformation deploy --stack-name dynamodb-table-terraform-state-lock --template-file ./dynamodb.yaml

create-core-s3-bucket: 	## - create stack of s3 bucket which stores core templates
	aws cloudformation deploy --stack-name s3-bucket-core-templates --template-file ./core-templates-s3.yaml

reload-core-s3-bucket: has-account has-region 	## - reload core templates
	aws s3 cp core-templates s3://core-templates-$(AWS_REGION)-$(AWS_ACCOUNT) --recursive

create-core-vpc: has-project has-environment 	## - create stack of core vpc settings
	aws cloudformation deploy --stack-name core-vpc-$(PROJECT)-$(ENVIRONMENT) --template-file ./core.yaml \
    --parameter-overrides "ProjectName=$(PROJECT)" "EnvironmentName=$(ENVIRONMENT)"
