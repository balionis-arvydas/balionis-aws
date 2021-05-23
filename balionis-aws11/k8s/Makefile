.PHONY: help docker-compose-up docker-compose-down

BUILD_DIR=build
K8S_VERSION=1.18.0
MODULE_DIR=$(BUILD_DIR)/$(NAMESPACE)/$(MODULE)/$(ENVIRONMENT)
MODULE_YAML=$(addprefix $(MODULE_DIR), module.yaml)

help:
	@grep -E '^[a-zA-Z0-9_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

kind-create-cluster:	## create kubernetes 'kind' cluster under docker
	kind create cluster --name wsl2 --config kind/wsl2-config.yaml

kind-delete-cluster:	## delete kubernetes 'kind' cluster under docker
	kind delete cluster --name wsl2

check-namespace-env-var:	## check if NAMESPACE env variable is defined
ifndef NAMESPACE
	$(error NAMESPACE is undefined)
endif

check-module-env-var:	## check if MODULE env variable is defined
ifndef MODULE
	$(error MODULE is undefined)
endif

check-environment-env-var:	## check if ENVIRONMENT env variable is defined
ifndef ENVIRONMENT
	$(error ENVIRONMENT is undefined)
endif

$(MODULE_DIR):
	mkdir -p $(MODULE_DIR)

$(MODULE_YAML): check-namespace-env-var check-module-env-var check-environment-env-var | $(MODULE_DIR)
	kustomize build --load-restrictor LoadRestrictionsNone  $(NAMESPACE)/$(MODULE)/$(ENVIRONMENT) > $(MODULE_YAML)

validate: $(MODULE_YAML)	## Validate generated descriptors using kubeval
	kubeval $(MODULE_YAML) -v $(K8S_VERSION) --ignore-missing-schemas

dry-run: $(MODULE_YAML)	## Run kubectl apply with dry-run
	kubectl apply --dry-run=client -f $(NAMESPACE)/namespace.yaml && \
	kubectl apply --dry-run=client -f $(MODULE_YAML)

apply: $(MODULE_YAML)	## Run kubectl apply with dry-run
	kubectl apply --wait=true -f $(NAMESPACE)/namespace.yaml && \
	kubectl apply --wait=true -f $(MODULE_YAML)
