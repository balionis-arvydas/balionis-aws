.PHONY: help docker-compose-up docker-compose-down

help:
	@grep -E '^[a-zA-Z0-9_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

kind-create-cluster:	## create kubernetes 'kind' cluster under docker
	kind create cluster --name wsl2 --config kind/wsl2-config.yaml

kind-delete-cluster:	## delete kubernetes 'kind' cluster under docker
	kind delete cluster --name wsl2
