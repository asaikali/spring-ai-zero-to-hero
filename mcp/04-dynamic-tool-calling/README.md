# Dynamic Tool Update Example

This example demonstrates how a Model Context Protocol (MCP) server can dynamically update its available tools at runtime, and how a client can detect and use these updated tools.

## Overview

The MCP protocol allows AI models to access external tools and resources through a standardized interface. This example showcases a key feature of MCP: the ability to dynamically update the available tools on the server side, with the client detecting and utilizing these changes without requiring a restart or reinitialization.

## Key Components

### Server

The server application consists of:

1. **WeatherService**: Initially provides a weather forecast tool that retrieves temperature data for a specific location using the Open-Meteo API.
2. **MathTools**: Contains mathematical operations (sum, multiply, divide) that are added dynamically to the server after receiving an update signal.
3. **ServerApplication**: Manages the server lifecycle and handles the dynamic tool update process through a REST endpoint.

### Client

The client application:

1. Connects to the MCP server
2. Retrieves the initial list of available tools
3. Triggers the server to update its tools via a REST call
4. Receives a notification when tools change through the MCP protocol
5. Retrieves the updated list of tools to confirm the changes

## How It Works

1. **Initial Setup**: When the server starts, it only exposes the weather forecast tool.

2. **Dynamic Update Process**:
   - The client sends a request to the server's `/updateTools` endpoint
   - The server receives this signal and adds the math tools to its available tools
   - The server notifies connected clients about the tool changes via the MCP protocol
   - The client receives the notification through a registered callback and can now use the new tools

3. **Tool Discovery**: The client can query the available tools at any time, demonstrating that the tool list has been updated.

## Implementation Details

### Server-Side Implementation

The server uses Spring AI's MCP server implementation with the following key components:

```java
// Register the initial weather tools
@Bean
public ToolCallbackProvider weatherTools(WeatherService weatherService) {
  return MethodToolCallbackProvider.builder().toolObjects(weatherService).build();
}

// Handle the dynamic tool update
@Bean
public CommandLineRunner commandRunner(McpSyncServer mcpSyncServer) {
  return args -> {
    logger.info("Server: " + mcpSyncServer.getServerInfo());

    // Wait for update signal
    latch.await();

    // Convert MathTools to MCP tool specifications
    List<SyncToolSpecification> newTools =
        McpToolUtils.toSyncToolSpecifications(ToolCallbacks.from(new MathTools()));

    // Add each new tool to the server
    for (SyncToolSpecification newTool : newTools) {
      logger.info("Add new tool: " + newTool);
      mcpSyncServer.addTool(newTool);
    }

    logger.info("Tools updated: ");
  };
}

// REST endpoint to trigger the tool update
@GetMapping("/updateTools")
public String greeting() {
  latch.countDown();
  return "Update signal received!";
}
```

The `WeatherService` class provides a tool for retrieving weather data:

```java
@Tool(description = "Get the temperature (in celsius) for a specific location")
public WeatherResponse weatherForecast(
    @ToolParam(description = "The location latitude") double latitude,
    @ToolParam(description = "The location longitude") double longitude,
    ToolContext toolContext) {
  // Implementation details...
}
```

The `MathTools` class provides mathematical operations that are added dynamically:

```java
@Tool(description = "Adds two numbers")
public int sumNumbers(int number1, int number2) {
  return number1 + number2;
}

@Tool(description = "Multiplies two numbers")
public int multiplyNumbers(int number1, int number2) {
  return number1 * number2;
}

@Tool(description = "Divide two numbers")
public double divideNumbers(double number1, double number2) {
  return number1 / number2;
}
```

### Client-Side Implementation

The client connects to the server and registers a callback to be notified when tools change:

```java
@Bean
McpSyncClientCustomizer customizeMcpClient() {
  return (name, mcpClientSpec) -> {
    mcpClientSpec.toolsChangeConsumer(
        tv -> {
          logger.info("\nMCP TOOLS CHANGE: " + tv);
          latch.countDown();
        });
  };
}
```

The client retrieves the available tools before and after the update:

```java
// Get initial tools
List<ToolDescription> toolDescriptions =
    chatClientBuilder
        .build()
        .prompt(
            "What tools are available? Please list them and avoid any additional comments. Only JSON format.")
        .toolCallbacks(tools)
        .call()
        .entity(new ParameterizedTypeReference<List<ToolDescription>>() {});

// Signal the server to update tools
String signal =
    RestClient.builder()
        .build()
        .get()
        .uri("http://localhost:8080/updateTools")
        .retrieve()
        .body(String.class);

// Wait for tool change notification
latch.await();

// Get updated tools
toolDescriptions =
    chatClientBuilder
        .build()
        .prompt(
            "What tools are available? Please list them and avoid any additional comments. Only JSON format.")
        .toolCallbacks(tools)
        .call()
        .entity(new ParameterizedTypeReference<List<ToolDescription>>() {});
```

## Key Insight

The example demonstrates a crucial aspect of the MCP implementation in Spring AI:

> The client implementation relies on the fact that the `ToolCallbackProvider#getToolCallbacks` implementation for MCP will always get the current list of MCP tools from the server.

This means that whenever a client requests the available tools, it will always get the most up-to-date list from the server, without needing to restart or reinitialize the client. Additionally, the MCP protocol provides a notification mechanism that allows clients to be informed when tools change, enabling real-time updates.

## Configuration

### Server Configuration

The server is configured with a name and version in `application.yaml`:

```yaml
spring:
  ai:
    mcp:
      server:
        name: my-mcp-server
        version: 0.0.1
```

### Client Configuration

The client is configured to connect to the server in `application.yaml`:

```yaml
spring:
  main:
    web-application-type: none
  ai:
    mcp:
      client:
        sse:
          connections:
            04-dynamic-tool-calling:
              url: http://localhost:8080
  config:
    import: "optional:classpath:/creds.yaml"
```

## Running the Example

1. Start the server application:
   ```
   cd server
   ./mvnw spring-boot:run
   ```

2. In a separate terminal, start the client application:
   ```
   cd client
   ./mvnw spring-boot:run
   ```

3. Observe the console output to see:
   - The initial list of tools (only weather forecast)
   - The tool update notification
   - The updated list of tools (weather forecast + math operations)

## Benefits of Dynamic Tool Updates

Dynamic tool updates provide several advantages:

1. **Runtime Flexibility**: Add, remove, or modify tools without restarting the server or clients
2. **Adaptive Capabilities**: Adjust available tools based on changing requirements or conditions
3. **Seamless Integration**: Clients automatically receive and can use new tools without manual intervention
4. **Resource Efficiency**: Load tools only when needed, rather than all at startup

## MCP Protocol Specification

For more information about the MCP protocol, refer to the official specification:
[https://modelcontextprotocol.io/specification/2024-11-05/server/tools](https://modelcontextprotocol.io/specification/2024-11-05/server/tools)

The Model Context Protocol (MCP) is designed to standardize how AI models interact with external tools and resources, enabling more powerful and flexible AI applications.
