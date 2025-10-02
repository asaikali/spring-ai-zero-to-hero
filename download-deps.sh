ollama pull llama3.2
ollama pull llava
ollama pull mxbai-embed-large
docker-compose  -f docker/postgres/docker-compose.yaml pull
docker-compose  -f docker/observability-stack/docker-compose.yaml pull
./mvnw clean package
