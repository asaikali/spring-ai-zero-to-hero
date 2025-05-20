# Spring AI MCP Basic STDIO Server

A Spring Boot starter project demonstrating how to build a Model Context Protocol (MCP) server that provides weather-related tools using the Open-Meteo API. This project showcases the Spring AI MCP Server Boot Starter capabilities with STDIO transport implementation.

For more information, see the [MCP Server Boot Starter](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-server-boot-starter-docs.html) reference documentation.

## Prerequisites

- Java 17 or later
- Maven 3.6 or later
- Understanding of Spring Boot and Spring AI concepts
- (Optional) Claude Desktop for AI assistant integration

## About Spring AI MCP Server Boot Starter

The `spring-ai-starter-mcp-server` provides:
- Automatic configuration of MCP server components
- Support for both synchronous and asynchronous operation modes
- STDIO transport layer implementation
- Flexible tool registration through Spring beans
- Change notification capabilities

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/example/
│   │       ├── BasicStdioMcpServerApplication.java    # Main application class with tool registration
│   │       └── WeatherTools.java                      # Weather service implementation with MCP tools
│   └── resources/
│       └── application.properties                     # Server and transport configuration
└── test/
    └── java/
        └── com/example/
            └── ClientStdio.java                       # Test client implementation
```

## Building and Running

The server uses STDIO transport mode and is typically started automatically by the client. To build the server jar:

```bash
./mvnw clean install -DskipTests
```

## Tool Implementation

The project demonstrates how to implement and register MCP tools using Spring's dependency injection and auto-configuration:

```java
@Service
public class WeatherTools {
    @Tool(description = "Get the temperature (in celsius) for a specific location")
    public WeatherResponse getTemperature(
        @ToolParam(description = "The location latitude") double latitude,
        @ToolParam(description = "The location longitude") double longitude,
        @ToolParam(description = "The city name") String city) {
        // Implementation
    }
}

@SpringBootApplication
public class BasicStdioMcpServerApplication {
    @Bean
    public ToolCallbackProvider weatherToolsProvider(WeatherTools weatherTools) {
        var toolCallbackProvider =
            MethodToolCallbackProvider.builder().toolObjects(weatherTools).build();
        // Log available tools
        return toolCallbackProvider;
    }
}
```

The auto-configuration automatically registers these tools with the MCP server. The `MethodToolCallbackProvider` scans the provided objects for methods annotated with `@Tool` and registers them as tool callbacks.


## Client Integration

### Java Client Example

The project includes a test client implementation in `ClientStdio.java` that demonstrates how to create an MCP client that connects to the server:

```java
// Create server parameters
ServerParameters stdioParams = ServerParameters.builder("java")
    .args("-Dspring.ai.mcp.server.stdio=true", 
          "-Dspring.main.web-application-type=none",
          "-Dlogging.pattern.console=", 
          "-jar",
          "mcp/01-basic-stdio-mcp-server/target/01-basic-stdio-mcp-server-0.0.1-SNAPSHOT.jar")
    .build();

// Initialize transport and client
var client = McpClient.sync(new StdioClientTransport(stdioParams)).build();

// Initialize the client
client.initialize();

// Ping the server to check connectivity
client.ping();

// List available tools
ListToolsResult toolsList = client.listTools();
System.out.println("Available Tools = " + toolsList);

// Call the temperature tool
CallToolResult weather = client.callTool(
    new CallToolRequest("getTemperature",
        Map.of("latitude", "52.377956", 
               "longitude", "4.897070", 
               "city", "Amsterdam")
    )
);
System.out.println("Weather = " + weather);

// Close the client gracefully
client.closeGracefully();
```

### Claude Desktop Integration

To integrate with Claude Desktop, add the following configuration to your Claude Desktop settings:

```json
{
  "mcpServers": {
    "spring-ai-mcp-weather": {
      "command": "java",
      "args": [
        "-Dspring.ai.mcp.server.stdio=true",
        "-Dspring.main.web-application-type=none",
        "-Dlogging.pattern.console=",
        "-jar",
        "/absolute/path/to/01-basic-stdio-mcp-server-0.0.1-SNAPSHOT.jar"
      ]
    }
  }
}
```

Replace `/absolute/path/to/` with the actual path to your built jar file.

## Configuration

### Application Properties

All properties are prefixed with `spring.ai.mcp.server`:

```properties
# Required STDIO Configuration
spring.main.web-application-type=none
spring.main.banner-mode=off
logging.pattern.console=

# Server Configuration
spring.ai.mcp.server.enabled=true
spring.ai.mcp.server.name=basic-weather-server
spring.ai.mcp.server.version=0.0.1
# SYNC or ASYNC
spring.ai.mcp.server.type=SYNC

# Optional file logging
logging.file.name=basic-stdio-mcp-server.log
```

### Key Configuration Notes

1. **STDIO Mode Requirements**
   - Disable web application type (`spring.main.web-application-type=none`)
   - Disable Spring banner (`spring.main.banner-mode=off`)
   - Clear console logging pattern (`logging.pattern.console=`)

2. **Server Type**
   - `SYNC` (default): Uses `McpSyncServer` for straightforward request-response patterns
   - `ASYNC`: Uses `McpAsyncServer` for non-blocking operations with Project Reactor support

## Implementation Details

### WeatherResponse Structure

The `WeatherTools` class defines a nested record structure to parse the JSON response from the Open-Meteo API:

```java
public record WeatherResponse(Current current) {
    public record Current(LocalDateTime time, int interval, double temperature_2m) {}
}
```

### RestClient Usage

The implementation uses Spring's `RestClient` to make HTTP requests to the Open-Meteo API:

```java
this.restClient = RestClient.create();

WeatherResponse response =
    restClient
        .get()
        .uri(
            "https://api.open-meteo.com/v1/forecast?latitude={latitude}&longitude={longitude}&current=temperature_2m",
            latitude,
            longitude)
        .retrieve()
        .body(WeatherResponse.class);
```

## Additional Resources

- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
- [MCP Server Boot Starter](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-server-boot-starter-docs.html)
- [MCP Client Boot Starter](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-client-boot-starter-docs.html)
- [Model Context Protocol Specification](https://modelcontextprotocol.github.io/specification/)
- [Spring Boot Auto-configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration)
- [Open-Meteo API Documentation](https://open-meteo.com/en/docs)
