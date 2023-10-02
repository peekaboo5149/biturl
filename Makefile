# Bring up the Docker containers in the background using docker-compose.
up:
	@docker-compose up -d --remove-orphans
# Bring down and stop the Docker containers created by docker-compose.
down:
	@docker-compose down

build-jar:
	@./gradlew clean build --no-daemon -x test

image:
	@docker build -t biturl:1.0.0 .
	@docker build -t biturl:1.0.0 .

# PHONY targets do not correspond to actual files; they are used for automation.
.PHONY: up down
