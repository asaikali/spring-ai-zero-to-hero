# spring-ai-zero-to-hero

Example applications showing how to use Spring AI to build Generative 
AI projects.

## Software Prerequisites

**You need the following software installed: Java 21, docker, ollama, httpie, 
ard your favourite Java IDE. This is a lot of GBs to download so please make 
user to have all this stuff installed before the conference workshop, as the 
conference wifi may be slow, so you might not be able to run the samples.**

### Java development tooling
* Java 21 you can use [sdkman.io](https://sdkman.io/)
* [Maven](https://maven.apache.org/index.html)
* Favourite Java IDE one of
    * [IntelliJ](https://www.jetbrains.com/idea/download)
    * [VSCode](https://code.visualstudio.com/)
    * [Eclipse Spring Tool Suite](https://spring.io/tools)

### Http Client
*  Command line http client  [httpie](https://httpie.
  io/) is recommend, the instructions use it, if you don't have it please 
   install it. If you are handy with [curl](https://curl.se/) you can use 
   that too. 

### Containerization tools
* [Docker](https://www.docker.com/products/docker-desktop) so we can use test containers & for local dependencies  

### Local AI Models

[ollama](https://ollama.com/)  makes running models on your laptop easy and 
very educational. You can run the models locally and learn how they work. 

* Install ollama by following the instructions on the [ollama website]
  (https://ollama.com/) this [YouTube video](https://www.youtube.com/watch?
  v=3Q6J6J7Q1Zo) shows the ollama install process.

## Save the conference Wi-Fi

Please make sure that the software list above is installed on your laptop 
before the workshop starts. After install: 

1. clone this repo to your laptop 
2. Run the `./download-deps.sh` script pull local AI models, and container 
   images. 
2. Run the `check-deps.sh` script to check that the all the required 
   software is installed, the output of the script on my machine looks like.

```shell
./check-deps.sh
============================
Checking Java installation:
============================
Java is installed. Version details:
openjdk version "21.0.1" 2023-10-17 LTS
OpenJDK Runtime Environment Temurin-21.0.1+12 (build 21.0.1+12-LTS)
OpenJDK 64-Bit Server VM Temurin-21.0.1+12 (build 21.0.1+12-LTS, mixed mode)

===============================
Checking Ollama installation:
===============================
Ollama is installed. Version details:
ollama version is 0.1.38

========================================
Checking if llama3 model is pulled:
========================================
llama3 model is pulled and available.

==============================
Checking Docker installation:
==============================
Docker is installed. Version details:
Docker version 26.1.1, build 4cf5afa

Checking Docker image: pgvector/pgvector:pg16
Docker image pgvector/pgvector:pg16 is pulled.

Checking Docker image: dpage/pgadmin4:8.6
Docker image dpage/pgadmin4:8.6 is pulled.

===============================
Checking HTTPie installation:
===============================
HTTPie is installed. Version details:
3.2.2
```

if you run into issues try running the commands in the `check-deps.sh` 
script one at a time. 