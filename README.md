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
*  Command line http client  [httpie](https://httpie.io/) is recommended, the instructions use it, if you don't have it please install it. If you are handy with [curl](https://curl.se/) you can use that too. 

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

## API Keys

You will be provided with API keys for online AI services during the 
workshop, these keys will only be valid during the workshop. Highly 
recommend you get your own keys to continue experimenting after the workshop.

### OpenAI
* You need OpenAI API key to run the examples with OpenAI.
* Refer to [this page](https://help.openai.com/en/articles/4936850-where-do-i-find-my-openai-api-key) to get an API key.

### Azure OpenAI
* You need Azure OpenAI service instance in the Azure portal. This requires to fill out a form at the moment, which usually
  takes at most 24h to process.
* Create the service at https://portal.azure.com/#create/Microsoft.CognitiveServicesOpenAI
* Create an Azure OpenAI deployment at https://oai.azure.com/portal

# Outline

Generative AI is a transformational technology impacting our world in profound ways and creating unprecedented opportunities. This workshop is designed for Spring developers looking to add generative AI to existing applications or to implement brand new AI apps using the Spring AI project.

We assume no previous AI experience. The workshop will teach you key AI concepts and how to apply them in your applications, using the Spring AI project.

The workshop is hands-on. Bring your laptop and a willingness to learn. We will provide Spring AI based sample code and the API keys for the AI services. By the end of the day you will know how to add generative AI features to your Spring apps.

### Key Concepts covered:
- A Concise History of AI/ML
- Introduction to Generative AI Models
- Prompt Engineering Techniques & Best Practices
- Vector Databases
- RAG: Retrieval Augmented Generation
- Extending LLMs with Function Calling
- Evaluation: How to tell if the AI is doing what you think it should be doing
- Architecture of AI powered applications
- How to integrate AI into existing applications
- The landscape of tools and libraries of building AI powered applications

### Hands on Code Exercises with Spring AI:
- Quickstart: Creating a “Hello World” application for Generative AI in just minutes
- Prompt Engineering Techniques using Prompt Templating and Roles
- Mapping AI output to POJOs
- Implementing RAG (Retrieval Augmented Generation)
- Exploring Function calling: Enable the AI to access APIs on demand
- Evaluation Driven Development
- Using Spring AI with GraalVM
