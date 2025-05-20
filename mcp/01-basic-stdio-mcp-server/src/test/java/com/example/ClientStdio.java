/*
 * Copyright 2025 - 2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.ListToolsResult;
import java.util.Map;

/** With stdio transport, the MCP server is automatically started by the client. */
public class ClientStdio {

  public static void main(String[] args) {

    var stdioParams =
        ServerParameters.builder("java")
            .args(
                "-Dspring.ai.mcp.server.stdio=true",
                "-Dspring.main.web-application-type=none",
                "-Dlogging.pattern.console=",
                "-jar",
                "mcp/01-basic-stdio-mcp-server/target/01-basic-stdio-mcp-server-0.0.1-SNAPSHOT.jar")
            .build();

    var client = McpClient.sync(new StdioClientTransport(stdioParams)).build();

    client.initialize();

    client.ping();

    // List and demonstrate tools
    ListToolsResult toolsList = client.listTools();
    System.out.println("Available Tools = " + toolsList);

    CallToolResult weather =
        client.callTool(
            new CallToolRequest(
                "getTemperature",
                Map.of("latitude", "52.377956", "longitude", "4.897070", "city", "Amsterdam")));
    System.out.println("Weather = " + weather);

    client.closeGracefully();
  }
}
