ollama pull llama4
ollama pull mxbai-embed-large
docker-compose  -f docker/postgres/docker-compose.yaml pull
docker-compose  -f docker/observability-stack/docker-compose.yaml pull
./mvnw clean package
