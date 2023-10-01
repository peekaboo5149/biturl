# Bring up the Docker containers in the background using docker-compose.
up:
	@docker-compose up -d --remove-orphans
# Bring down and stop the Docker containers created by docker-compose.
down:
	@docker-compose down

# TODO: Add gradle related stuff
# TODO: Docker compatible

# PHONY targets do not correspond to actual files; they are used for automation.
.PHONY: up down