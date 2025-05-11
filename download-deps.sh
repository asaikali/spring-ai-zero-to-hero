ollama pull llama4
ollama pull mxbai-embed-large
docker-compose  -f docker/postgres/docker-compose.yml pull
docker-compose  -f docker/observability-stack/docker-compose.yml pull
./mvnw clean package
