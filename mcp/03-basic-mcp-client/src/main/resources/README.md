# Spring AI - Model Context Protocol (MCP) Client

This example demonstrates how to build a basic MCP client application using Spring AI's Model Context Protocol (MCP) client starter. The application showcases integration with multiple MCP servers, including the Brave Search server and a filesystem server, allowing the AI model to perform web searches and file operations.

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- npx package manager
- OpenAI API key (Get one at https://platform.openai.com/)

## Setup

1. Install npx (Node Package eXecute):
   First, make sure to install [npm](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm)
   and then run:
   ```bash
   npm install -g npx
   ```

2. Build the application:
   ```bash
   ./mvnw clean install
   ```

## Running the Application

Run the application using Maven:
```bash
./mvnw spring-boot:run
```

The application will execute a predefined query that demonstrates the MCP client's capabilities. It will:
1. Connect to the configured MCP servers
2. List the available tools
3. Execute a query about Spring IO 2025 conference sessions related to Spring AI and MCP
4. Use the available tools to search for information and create a summary file

## How it Works

The application integrates Spring AI with MCP servers through several components:

### MCP Client Configuration

1. Required dependencies in pom.xml:
```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-mcp-client</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-model-openai</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

2. Application properties (application.yaml):
```yaml
spring:
  ai:
    mcp:
      client:
        stdio:
          servers-configuration: classpath:/mcp-servers-config.json
  config:
    import: "optional:classpath:/creds.yaml"
server:
  port: 8181
```

3. MCP Server Configuration (mcp-servers-config.json):
```json
{
  "mcpServers": {
    "brave-search": {
      "command": "npx",
      "args": [
        "-y",
        "@modelcontextprotocol/server-brave-search"
      ],
      "env": {
      }
    },
    "filesystem": {
      "command": "npx",
      "args": [
        "-y",
        "@modelcontextprotocol/server-filesystem",
        "./mcp/03-basic-mcp-client/target"
      ]
    }
  }
}
```

4. Credentials Configuration (creds.yaml):
```yaml
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}
```

### Implementation

The main application class demonstrates how to:
1. Build a ChatClient with MCP tool integration
2. List available tools from the MCP servers
3. Execute a query that can utilize the MCP tools
4. Process and display the response

```java
@SpringBootApplication
public class BasicMcpClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(BasicMcpClientApplication.class, args).close();
  }

  @Bean
  public CommandLineRunner chatbot(
      ChatClient.Builder chatClientBuilder, ToolCallbackProvider tools) {

    return args -> {

      var chatClient = chatClientBuilder.build();

      System.out.println(
          "Provided Tools: \n"
              + Stream.of(tools.getToolCallbacks())
                  .map(tc -> tc.getToolDefinition().name())
                  .collect(Collectors.joining("\n")));

      String userQuestion =
          """
            Does the Spring IO 2025 conference has a sessions about the Srping AI and Model Context Protocol (MCP)? 
            Please provide references and write a summary as a summary.md file in the mcp/03-basic-mcp-client/target directory.
            """;

      System.out.println("\n\nQuestion: \n" + userQuestion);

      var response =
          chatClient
              .prompt()
              .system(
                  "You are useful assistant and can perform web searches to answer user questions.")
              .user(userQuestion)
              .toolCallbacks(tools)
              .call()
              .content();

      System.out.println("\nResponse: \n" + response);
    };
  }
}
```

## MCP Servers Used

This example integrates with two MCP servers:

1. **Brave Search MCP Server**
   - Provides web search capabilities
   - Allows the AI model to search for up-to-date information on the internet
   - Used to find information about Spring IO 2025 conference sessions

2. **Filesystem MCP Server**
   - Provides file system operations
   - Allows the AI model to create, read, update, and delete files
   - Used to write the summary.md file in the target directory

## Transport Types

The MCP client supports multiple transport types:

1. **STDIO Transport** (used in this example)
   - Communicates with MCP servers using standard input/output streams
   - Servers are started as child processes by the client
   - Configuration is provided in the mcp-servers-config.json file

2. **SSE Transport** (commented out in application.yaml)
   - Communicates with MCP servers using Server-Sent Events over HTTP
   - Servers must be running separately from the client
   - Configuration is provided in the application.yaml file

## Additional Resources

- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
- [MCP Client Boot Starter](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-client-boot-starter-docs.html)
- [Model Context Protocol Specification](https://modelcontextprotocol.github.io/specification/)
- [Brave Search MCP Server](https://github.com/modelcontextprotocol/servers/tree/main/src/brave-search)
- [Filesystem MCP Server](https://github.com/modelcontextprotocol/servers/tree/main/src/filesystem)
