# spring-ai-zero-to-hero

Example applications showing how to use Spring AI to build Generative
AI projects.

## Software Prerequisites

**You need the following software installed: Java 21, docker, ollama, httpie,
and your favourite Java IDE. This is a lot of GBs to download so please make
sure to have all this stuff installed before the conference workshop, as the
conference wifi may be slow, so you might not be able to run the samples.**

### Java development tooling

* Java 21 you can use [sdkman.io](https://sdkman.io/)
* [Maven](https://maven.apache.org/index.html)
* Favourite Java IDE one of
    * [IntelliJ](https://www.jetbrains.com/idea/download)
    * [VSCode](https://code.visualstudio.com/)
    * [Eclipse Spring Tool Suite](https://spring.io/tools)

### Http Client

* Command line http client  [httpie](https://httpie.io/) is recommended, the instructions use it, if
  you don't have it please install it. If you are handy with [curl](https://curl.se/) you can use
  that too.

### Containerization tools

* [Docker](https://www.docker.com/products/docker-desktop) so we can use test containers & for local
  dependencies

### Local AI Models

[ollama](https://ollama.com/)  makes running models on your laptop easy and
very educational. You can run the models locally and learn how they work.

* Install ollama by following the instructions on the [ollama website](https://ollama.com/)
  this [YouTube video](https://www.youtube.com/watch?v=3Q6J6J7Q1Zo) shows the ollama install
  process.

## Save the conference Wi-Fi

Please make sure that the software list above is installed on your laptop
before the workshop starts. After install:

1. clone this repo to your laptop
2. Run the `./download-deps.sh` script pull local AI models, and container
   images.
2. Run the `check-deps.sh` script to check that the all the required
   software is installed, the output of the script on my machine looks like.

```text
./check-deps.sh 
============================
Checking Java installation:
============================
Java is installed. Version details:
openjdk version "21.0.7" 2025-04-15 LTS
OpenJDK Runtime Environment Temurin-21.0.7+6 (build 21.0.7+6-LTS)
OpenJDK 64-Bit Server VM Temurin-21.0.7+6 (build 21.0.7+6-LTS, mixed mode, sharing)

===============================
Checking Ollama installation:
===============================
Ollama is installed. Version details:
ollama version is 0.6.5

========================================
Checking if llama4 model is pulled:
========================================
llama4 model is not pulled. Please pull it using 'ollama pull llama4'.

========================================
Checking if mxbai-embed-large model is pulled:
========================================
mxbai-embed-large model is pulled and available.

========================================
Checking if llava model is pulled:
========================================
llava model is pulled and available.

==============================
Checking Docker installation:
==============================
Docker is installed. Version details:
Docker version 28.1.1, build 4eba377

Checking Docker image: pgvector/pgvector:pg17
Docker image pgvector/pgvector:pg17 is pulled.

Checking Docker image: dpage/pgadmin4:9.3
Docker image dpage/pgadmin4:9.3 is pulled.

===============================
Checking HTTPie installation:
===============================
HTTPie is installed. Version details:
3.2.4
```

if you run into issues try running the commands in the `check-deps.sh`
script one at a time.

## API Keys

You will be provided with API keys for online AI services during the
workshop, these keys will only be valid during the workshop. Highly
recommend you get your own keys to continue experimenting after the workshop.

### OpenAI

* You need an OpenAI API key to run the examples with OpenAI.
* Refer
  to [this page](https://help.openai.com/en/articles/4936850-where-do-i-find-my-openai-api-key) to
  get an API key.

# Outline

Generative AI is a transformational technology impacting our world in profound ways and creating
unprecedented opportunities. This workshop is designed for Spring developers looking to add
generative AI to existing applications or to implement brand new AI apps using the Spring AI
project.

We assume no previous AI experience. The workshop will teach you key AI concepts and how to apply
them in your applications, using the Spring AI project.

The workshop is hands-on. Bring your laptop and a willingness to learn. We will provide Spring AI
based sample code and the API keys for the AI services. By the end of the day you will know how to
add generative AI features to your Spring apps.

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

## Repo Organization

Spring AI provides a consistent API to work with many different types of AI
providers. For example, the same code wil work with OpenAI, Google Vertex AI,
Azure OpenAI, and local AI models. The major directories in this repo are:

- **/components/data/** this directory contains various types of example data sets used
    by the examples in the repo.
- **/components/api/** this directory contains the code that interacts with the AI
  providers. The code in this directory is the same for all the AI providers.
  Each project in this directory focuses on a different aspect of the Spring
  AI API, within a project you will see that the package names end with
  numbers indicating the order in which the code in each project should be
  studied.
- **/components/patterns/** this directory contains the code that demonstrates how to
  use the Spring AI API to implement common AI application patterns such as
  retrieval augmented generation. The code in this directory is the same for
  all the AI providers.
- **/applications/** this directory contains the spring boot applications  
  that interact with the specific AI providers. The configuration of each
  project in this directory is different, for example, setting API keys and
  configuring the AI service with the correct endpoint. To try out the samples
  in this repo you will be launching the apps in this directory. Each
  subdirectory contains a readme.md file with instructions on how to run the
  application.
  -**/pgvector/** this directory contains a docker compose file to launch
  postgres with the pgvector extension. This is used to demonstrate how to
  use vector databases with Spring AI.

- **docs/** this directory contains the documentation for the repo.

## Recommendations to get the most out of the repo

1. Run the samples with the different AI providers to see how the same code
   works with different providers.
2. Run the gateway application and inspect the API requests/responses to see
   what interaction with the AI projects looks like on the wire.
3. Make sure to run ollama and download the llama3 model to see how easy it
   is to run local AI models.
4. The code in this repo is designed to be read in order, so start with the
   code in the api directory and work your way through the projects. Once
   you have looked at the code in the api directory move on to the code in
   the patterns' directory.
5. Spring AI project is evolving quickly, it is possible that the code in
   this repo will be using a snapshot release of the Spring AI project, or
   that it falls behind the latest version. If you run into problem with
   this repo, send a pull request or open an issue. 
