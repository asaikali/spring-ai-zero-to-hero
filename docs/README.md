# spring-ai-zero-to-hero

Example applications showing how to use Spring AI to build Generative 
AI projects. Each sample application contains a `readme.md` with instructions
on how to run the application and areas of the code to pay attention to.

## Learning Resources

### Prompt Engineering
Effective prompting is critical to writing apps that makes effective use of 
AI models. There is a lot of content online about this topic of varing 
quality. We found [ChatGPT Prompt Engineering for Developers](https://learn.deeplearning.ai/courses/chatgpt-prompt-eng) 
to be excellent because it is delivered by the AI pioneer Andrew Ng's team 
at deeplearning.ai.

## Software Prerequisites

### Java development tooling
* Java 21 you can use [sdkman.io](https://sdkman.io/)
* [Maven](https://maven.apache.org/index.html)
* Favourite Java IDE one of
    * [IntelliJ](https://www.jetbrains.com/idea/download)
    * [VSCode](https://code.visualstudio.com/)
    * [Eclipse Spring Tool Suite](https://spring.io/tools)

### Http Client
* We recommend getting a HTTP client like [httpie](https://httpie.io/) or [curl](https://curl.se/)
* Alternatively, you could use a standard web browser.

### Containerization tools
* [Docker](https://www.docker.com/products/docker-desktop) so we can use test containers & for local dependencies  

### OpenAI
* You need OpenAI API key to run the examples with OpenAI.
* Refer to [this page](https://help.openai.com/en/articles/4936850-where-do-i-find-my-openai-api-key) to get an API key.

### Azure OpenAI
* You need Azure OpenAI service instance in the Azure portal. This requires to fill out a form at the moment, which usually
takes at most 24h to process.
* Create the service at https://portal.azure.com/#create/Microsoft.CognitiveServicesOpenAI
* Create an Azure OpenAI deployment at https://oai.azure.com/portal

### Local AI Models

[ollama](https://ollama.com/)  makes running models on your laptop easy and 
very educational. You can run the models locally and learn how they work. 

* Install ollama by following the instructions on the [ollama website]
  (https://ollama.com/) this [YouTube video](https://www.youtube.com/watch?
  v=3Q6J6J7Q1Zo) shows the ollama install process.
* Download the `llama3.1`, `mxbai-embed-large` and `llava` models with `ollama pull llama3.1` , `ollama pull mxbai-embed-large` and `ollama pull llava`.


## Save the conference wifi

Please run the `download-deps.sh` script to download the dependencies for the
workshop before the conference starts so you can follow along without 
getting stuck downloading stuff during the workshop with slow conference wifi.

## Run the Examples

The examples are tested with three AI providers:
* Azure OpenAI
* OpenAI
* Ollama (local LLM)

### Prepare the AI service credentials

Please copy the credentials from the template files, e.g.

```bash
cp ./providers/azure/src/main/resources/creds-template.yaml ./providers/azure/src/main/resources/creds.yaml
cp ./providers/openai/src/main/resources/creds-template.yaml ./providers/openai/src/main/resources/creds.yaml
```

Update the corresponding API keys, API endpoints and model names.

### Compile the code

Your favourite IDE will probably compile code as you change it. If you prefer running things from the command line, we need to build JAR packages, e.g.

```bash
./mvnw clean package
```

This will build the following JAR files, e.g.
```
./providers/azure/target/azure-0.0.1-SNAPSHOT.jar
./providers/ollama/target/ollama-0.0.1-SNAPSHOT.jar
./providers/openai/target/openai-0.0.1-SNAPSHOT.jar
./chat/target/chat-0.0.1-SNAPSHOT.jar
./embedding/target/embedding-0.0.1-SNAPSHOT.jar
./image/target/image-0.0.1-SNAPSHOT.jar
./audio/target/audio-0.0.1-SNAPSHOT.jar
```

### Run the Azure OpenAI Application

You can run the application from your favourite IDE, e.g. `providers/azure/src/main/java/com/example/AzureApplication.java`

Also, you can run this from command line, e.g.
```bash
java -jar ./providers/azure/target/azure-0.0.1-SNAPSHOT.jar
```

You can test the application locally, e.g.
```
http localhost:8080/debug
```

### Run the OpenAI Application

You can run the application from your favourite IDE, e.g. `providers/openai/src/main/java/com/example/OpenAiApplication.java`

Also, you can run this from command line, e.g.
```bash
java -jar ./providers/openai/target/openai-0.0.1-SNAPSHOT.jar
```

You can test the application locally, e.g.
```
http localhost:8080/debug
```

### Run the Ollama Application

You can run the application from your favourite IDE, e.g. `providers/ollama/src/main/java/com/example/OllamaApplication.java`

Also, you can run this from command line, e.g.
```bash
java -jar ./providers/ollama/target/ollama-0.0.1-SNAPSHOT.jar
```

You can test the application locally, e.g.
```
http localhost:8080/debug
```

## Examples

All examples are located in the corresponding packages, e.g.
* `chat`
* `embedding`
* `image`
* `audio`

Each example is running as a controller in each of the provider applications, e.g.
```
chat/src/main/java/com/example/chat_01/BasicPromptController.java
```

You can inspect each of the controller and infer the example URL, e.g.
```
http://localhost:8080/chat/01/joke
```

See [Chat Examples](./examples_chat.md)

See [Embedding Examples](./examples_embedding.md)

See [Image Examples](./examples_image.md)

See [Audio Examples](./examples_audio.md)

