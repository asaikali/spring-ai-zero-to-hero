#!/bin/bash

# Function to check if Java is installed and print its version
check_java() {
    echo "============================"
    echo "Checking Java installation:"
    echo "============================"
    if command -v java &> /dev/null
    then
        echo "Java is installed. Version details:"
        java -version
    else
        echo "Java is not installed."
    fi
    echo ""
}

# Function to check if Ollama is installed and print its version
check_ollama() {
    echo "==============================="
    echo "Checking Ollama installation:"
    echo "==============================="
    if command -v ollama &> /dev/null
    then
        echo "Ollama is installed. Version details:"
        ollama --version
    else
        echo "Ollama is not installed."
    fi
    echo ""
}

# Function to check if the llama3.2 model is pulled for Ollama
check_llama3_model() {
    echo "========================================"
    echo "Checking if llama3.2 model is pulled:"
    echo "========================================"
    if command -v ollama &> /dev/null
    then
        if ollama list | grep -q "llama3.2"
        then
            echo "llama3.2 model is pulled and available."
        else
            echo "llama3.2 model is not pulled. Please pull it using 'ollama pull llama3.2'."
        fi
    else
        echo "Ollama is not installed, so the llama3.2 model cannot be checked."
    fi
    echo ""
}

# Function to check if the mxbai-embed-large model is pulled for Ollama
check_mxbai_embed_large_model() {
    echo "========================================"
    echo "Checking if mxbai-embed-large model is pulled:"
    echo "========================================"
    if command -v ollama &> /dev/null
    then
        if ollama list | grep -q "mxbai-embed-large"
        then
            echo "mxbai-embed-large model is pulled and available."
        else
            echo "mxbai-embed-large model is not pulled. Please pull it using 'ollama pull mxbai-embed-large'."
        fi
    else
        echo "Ollama is not installed, so the mxbai-embed-large model cannot be checked."
    fi
    echo ""
}

# Function to check if the llava model is pulled for Ollama
check_llava_model() {
    echo "========================================"
    echo "Checking if llava model is pulled:"
    echo "========================================"
    if command -v ollama &> /dev/null
    then
        if ollama list | grep -q "llava"
        then
            echo "llava model is pulled and available."
        else
            echo "llava model is not pulled. Please pull it using 'ollama pull llava'."
        fi
    else
        echo "Ollama is not installed, so the llava model cannot be checked."
    fi
    echo ""
}


# Function to check if Docker is installed and print its version
check_docker() {
    echo "=============================="
    echo "Checking Docker installation:"
    echo "=============================="
    if command -v docker &> /dev/null
    then
        echo "Docker is installed. Version details:"
        docker --version
    else
        echo "Docker is not installed."
    fi
    echo ""
}

# Function to check if a Docker image is pulled
check_docker_image() {
    local image=$1
    echo "Checking Docker image: $image"
    if docker images --format "{{.Repository}}:{{.Tag}}" | grep -q "$image"
    then
        echo "Docker image $image is pulled."
    else
        echo "Docker image $image is not pulled. Please pull it using 'docker pull $image'."
    fi
    echo ""
}

# Function to check if HTTPie is installed and print its version
check_httpie() {
    echo "==============================="
    echo "Checking HTTPie installation:"
    echo "==============================="
    if command -v http &> /dev/null
    then
        echo "HTTPie is installed. Version details:"
        http --version
    else
        echo "HTTPie is not installed."
    fi
    echo ""
}

# Run the functions to check for each software, the llama3 model, and Docker images
check_java
check_ollama
check_llama3_model
check_mxbai_embed_large_model
check_llava_model
check_docker
check_docker_image "pgvector/pgvector:pg17"
check_docker_image "dpage/pgadmin4:9.8.0"
check_httpie
