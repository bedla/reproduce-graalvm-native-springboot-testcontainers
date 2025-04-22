
docker run \
-e LOGGING_LEVEL_ORG_TESTCONTAINERS=DEBUG \
-e DOCKER_HOST=tcp://host.docker.internal:2375 \
demo:0.0.1-SNAPSHOT
