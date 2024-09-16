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

# Function to check if the llama3.1 model is pulled for Ollama
check_llama3_model() {
    echo "========================================"
    echo "Checking if llama3.1 model is pulled:"
    echo "========================================"
    if command -v ollama &> /dev/null
    then
        if ollama list | grep -q "llama3.1"
        then
            echo "llama3.1 model is pulled and available."
        else
            echo "llama3.1 model is not pulled. Please pull it using 'ollama pull llama3.1'."
        fi
    else
        echo "Ollama is not installed, so the llama3.1 model cannot be checked."
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
check_docker
check_docker_image "pgvector/pgvector:pg16"
check_docker_image "dpage/pgadmin4:8.6"
check_httpie
