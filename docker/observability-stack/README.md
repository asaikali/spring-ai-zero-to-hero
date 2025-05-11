# observability-stack

This project includes a Docker Compose configuration to launch a local 
observability stack with **Grafana**, **Tempo**, **Loki**, and **Prometheus**. 
This setup is optimized for local development, providing an easy way to monitor,
trace, and log your applications using industry-standard tools. It’s ideal for
testing observability configurations on a laptop without needing a complex 
cloud setup.

## Components

- **Grafana**: Provides a dashboard for visualizing metrics, logs, and traces. 
  It integrates with Prometheus, Loki, and Tempo to offer a comprehensive 
  observability experience.
- **Prometheus**: Collects and stores application metrics, making them available
  for monitoring and alerting. Grafana can be used to visualize these metrics.
- **Loki**: Handles log aggregation, allowing you to query and analyze 
  application logs within Grafana.
- **Tempo**: Provides distributed tracing, enabling you to follow the flow of 
  requests across services. Grafana can visualize these traces to help identify 
  performance issues.

## Usage

The `ostack` script provides a simple interface to manage the observability stack containers. Make sure to make the script executable with `chmod +x ostack` before using it.

1. **Start the Stack**: Run `./ostack start`
   - This starts all containers in detached mode
   - Shows container status and connection information when complete

2. **Check Status**: Run `./ostack status`
   - Displays the status of all containers
   - Shows connection information for each service

3. **Stop the Stack**: Run `./ostack stop`
   - Stops all running containers
   - Removes associated networks

4. **Clean Up**: Run `./ostack clean`
   - Stops all containers
   - Removes all associated volumes (data cleanup)

5. **Fix Port Conflicts**: Run `./ostack fix`
   - Automatically detects and resolves port conflicts
   - Useful if you see errors about ports already being in use

6. **View Logs**: Run `docker compose logs -f <service-name>` to view logs for
   a specific service. For example, `docker compose logs -f tempo`.

## 🚀 Spring Boot Observability: Direct Integration with Backend Systems

### Export Formats: Prometheus Metrics (pull), Zipkin Traces (push), Loki Logs (push)

In this scenario, **Tempo** is configured with **Zipkin compatibility mode**, allowing it to receive traces in Zipkin format via the default Zipkin HTTP port (`9411`). The **Spring Boot application** uses **Micrometer's Brave bridge** to generate spans and send them directly to Tempo.

**Metrics** are exposed via `/actuator/prometheus`. Since **Prometheus** is a **pull-based** metrics system, it regularly scrapes this endpoint to collect application metrics.

For **logs**, Micrometer is not involved. Instead, log forwarding depends on the logging framework used. If the application uses **Logback**, you can configure a **Logback appender** to push logs to **Loki** in its native format. A community-maintained appender is available here:  
👉 [loki4j/loki-logback-appender](https://github.com/loki4j/loki-logback-appender)

In this setup, **Grafana** serves as the UI frontend for all observability data. It connects directly to **Tempo** (for traces), **Loki** (for logs), and **Prometheus** (for metrics), enabling you to build unified dashboards.

### 📊 Architecture Diagram

```text

   +-----------------------------------------------------------+
   |                  Spring Boot App                          |
   |  Micrometer prometheus metrics registry                   |
   |  Micrometer zipkin brave tracing bridge                   |
   |  Loki logback appender                                    |
   +-----------------------------------------------------------+
          |                   |                     ↑
POST :9411/api/v2/spans       |                     |
          |                   |          GET /actuator/prometheus
          |        POST /loki/api/v1/push           |
          ↓                   ↓                     |
+--------------------+ +--------------------+ +--------------------+
|       Tempo        | |        Loki        | |    Prometheus      |
|  OTLP   3200 HTTP  | |      3100 HTTP     | |    9090 HTTP       |
|  Zipkin 9411 HTTP  | |                    | |                    |
+--------------------+ +--------------------+ +--------------------+
          ↑                   ↑                     ↑
          |                   |                     |
          |                   |                     |
      +------------------------------------------------------------+
      |                         Grafana                            |
      |                      (3000 Web UI)                         |
      |  - Queries metrics from Prometheus                         |
      |  - Queries logs from Loki                                  |
      |  - Queries traces from Tempo                               |
      +------------------------------------------------------------+
```

## Spring Boot configuration 

### Dependencies 

```xml
<dependencies>
  <!--
  Enables Spring Boot Actuator endpoints like:
  - /actuator/health
  - /actuator/prometheus (for Prometheus metrics scraping)
  Required for Micrometer and Micrometer Tracing auto-configuration.
  Without this, observability features like metrics and tracing will not activate.
-->
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
  </dependency>

  <!--
    Registers Prometheus as the Micrometer backend.
    Exposes all metrics in Prometheus-compatible format at:
    - GET /actuator/prometheus
    Prometheus scrapes this endpoint at intervals.
  -->
  <dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
  </dependency>

  <!--
    Micrometer Tracing bridge for Brave (Zipkin).
    This provides the integration layer between Micrometer's tracing API
    and Brave's implementation of span creation, context propagation, etc.
  -->
  <dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-tracing-bridge-brave</artifactId>
  </dependency>

  <!--
    Required by Brave to send spans to a Zipkin-compatible backend (e.g., Tempo).
    Sends spans via HTTP to:
    - POST http://localhost:9411/api/v2/spans
    Tempo must be running with Zipkin compatibility enabled.
  -->
  <dependency>
    <groupId>io.zipkin.reporter2</groupId>
    <artifactId>zipkin-reporter-brave</artifactId>
  </dependency>

  <!--
    Loki Logback Appender for structured log shipping.
    - Formats logs into Loki's expected JSON push format
    - Sends via HTTP POST to /loki/api/v1/push
    Used with Spring Boot’s default Logback setup.
  -->
  <dependency>
    <groupId>com.github.loki4j</groupId>
    <artifactId>loki-logback-appender</artifactId>
    <version>1.6.0</version> <!-- Check for latest version -->
  </dependency>
</dependencies>
```

### Configuration


## 📡 Observability with Spring Boot and OpenTelemetry Collector

In this scenario, the **Spring Boot application** is configured to export all observability signals—**traces**, **metrics**, and **logs**—in **OTLP (OpenTelemetry Protocol)** format.

**Traces** are generated using **Micrometer Tracing** with the **OpenTelemetry bridge**, and exported over HTTP to the **OpenTelemetry Collector** via the standard OTLP HTTP endpoint (`POST /v1/traces`). The collector then forwards these traces to **Tempo**, which supports OTLP ingestion natively.

**Metrics** are exported via the **Micrometer OTLP registry** and sent to the collector using `POST /v1/metrics`. The **OpenTelemetry Collector** acts as a relay, exposing a Prometheus-compatible `/metrics` endpoint that **Prometheus scrapes**. This preserves the familiar **pull model** while decoupling the application from Prometheus directly.

For **logs**, the application uses the **OpenTelemetry Logback appender**, which serializes structured logs and sends them via OTLP to the collector (`POST /v1/logs`). The collector then pushes these logs to **Loki**, which accepts them in its native JSON format.

In this setup, **Grafana** serves as the central observability UI. It connects to:
- **Tempo** for traces
- **Loki** for logs
- **Prometheus** for metrics

This architecture cleanly separates signal production (inside the app) from signal routing and storage (via the collector), while using modern, vendor-neutral OTLP protocols end to end.

### Architecture 
```text
   +-----------------------------------------------------------+
   |                  Spring Boot App                          |
   |  Micrometer otel metrics registry                         |
   |  Micrometer otel tracing bridge                      |
   |  OpenTelemetry logback appender                           |
   +-----------------------------------------------------------+
          |                   |                     |
POST :4318/v1/traces          |           POST :4318/v1/metrics
          |                   |                     |
          |         POST :4318/v1/logs              |
          ↓                   ↓                     ↓
      +----------------------------------------------------+
      |            OpenTelemetry Collector                 |
      |   OTLP HTTP :4318           OTLP gRPC :4317        |
      +----------------------------------------------------+
          |                   |                     ↑
    POST :3200/v1/traces      |          GET /actuator/prometheus
          |                   |                     |
          |         POST /loki/api/v1/push          |
          ↓                   ↓                     |
+--------------------+ +--------------------+ +--------------------+
|       Tempo        | |        Loki        | |    Prometheus      |
|      3200 HTTP     | |      3100 HTTP     | |    9090 HTTP       |
+--------------------+ +--------------------+ +--------------------+
          ↑                   ↑                     ↑
          |                   |                     |
          |                   |                     |
      +------------------------------------------------------------+
      |                         Grafana                            |
      |                      (3000 Web UI)                         |
      |  - Queries metrics from Prometheus                         |
      |  - Queries logs from Loki                                  |
      |  - Queries traces from Tempo                               |
      +------------------------------------------------------------+
```

### dependencies 

```xml

<dependencies>

  <!--
    Enables Spring Boot Actuator endpoints like:
    - /actuator/health
    - /actuator/metrics
    Required for Micrometer and Micrometer Tracing auto-configuration.
    Without this, observability features like metrics and tracing will not activate.
  -->
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
  </dependency>

  <!--
    Micrometer Tracing bridge for OpenTelemetry.
    Connects Micrometer’s tracing API to the OpenTelemetry SDK.
    Exports traces in OTLP format to the OpenTelemetry Collector.
  -->
  <dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-tracing-bridge-otel</artifactId>
  </dependency>

  <!--
    Micrometer OTLP metrics registry.
    Sends metrics from Spring Boot to the OpenTelemetry Collector using OTLP.
    The collector is responsible for forwarding or exposing them.
  -->
  <dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-otlp</artifactId>
  </dependency>

  <!--
    OpenTelemetry Logback Appender.
    - Serializes logs to OTLP format
    - Sends them to the OpenTelemetry Collector via POST /v1/logs
    Used for pushing structured application logs to Loki or similar backends.
  -->
  <dependency>
    <groupId>io.opentelemetry.instrumentation</groupId>
    <artifactId>opentelemetry-logback-appender</artifactId>
    <version>1.32.0</version> <!-- Replace with latest version if needed -->
  </dependency>

</dependencies>
```

## otel collector can be configured with a zipkin listener 
```text
      +----------------------------------------------------+
      |                  Spring Boot App                   |
      |  Micrometer + otel Metrics Registry + otel Tracing |
      +----------------------------------------------------+
          |                   |                     |
POST :9411/api/v2/spans       |                     |
POST :4318/v1/traces          |           POST :4318/v1/metrics
          |         POST :4318/v1/logs              |
          ↓                   ↓                     ↓
      +----------------------------------------------------+
      |            OpenTelemetry Collector                 |
      |   OTLP HTTP :4318     |     OTLP gRPC :4317        |
      |   Zipkin HTTP :9411                                |
      +----------------------------------------------------+
          |                   |                     ↑
    POST :3200/v1/traces      |          GET /actuator/prometheus
          |                   |                     |
          ↓                   ↓                     |
+--------------------+ +--------------------+ +--------------------+
|       Tempo        | |        Loki        | |    Prometheus      |
|  3200 HTTP Native  | |      3100 HTTP     | |    9090 HTTP       |
+--------------------+ +--------------------+ +--------------------+
          ↑                   ↑                     ↑
          |                   |                     |
          |                   |                     |
      +------------------------------------------------------------+
      |                         Grafana                            |
      |                      (3000 Web UI)                         |
      |  - Queries metrics from Prometheus                         |
      |  - Queries logs from Loki                                  |
      |  - Queries traces from Tempo                               |
      +------------------------------------------------------------+
```
