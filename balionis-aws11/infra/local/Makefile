.PHONY: help docker-compose-up docker-compose-down

help:
	@grep -E '^[a-zA-Z0-9_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

docker-compose-up:	## start docker containers on localhost
	docker-compose -f docker-compose.yml build && \
	docker-compose -f docker-compose.yml up -d

docker-compose-down:	## stop docker containers on localhost
	docker-compose -f docker-compose.yml down
