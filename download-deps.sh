ollama pull llama3.2
ollama pull llava
ollama pull mxbai-embed-large
docker-compose  -f docker/postgres/docker-compose.yml pull
docker-compose  -f docker/observability-stack/docker-compose.yml pull
./mvnw clean package
