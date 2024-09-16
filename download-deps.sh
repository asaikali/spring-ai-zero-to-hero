ollama pull llama3.1
docker-compose  -f pgvector/docker-compose.yml pull
./mvnw clean package
ollama pull mxbai-embed-large
