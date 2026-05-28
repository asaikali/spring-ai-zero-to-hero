# Spring AI Zero to Hero Workshop

A comprehensive educational workshop for learning Spring AI, covering APIs, patterns, and agentic systems across multiple AI providers.

## Project Structure

```
spring-ai-zero-to-hero/
├── components/
│   ├── apis/           # Core Spring AI APIs
│   │   ├── chat/       # 9 progressive chat examples (chat_01-chat_09)
│   │   ├── embedding/  # 4 embedding examples
│   │   ├── image/      # Image generation
│   │   ├── audio/      # Audio/transcription (OpenAI)
│   │   └── vector-store/
│   ├── patterns/       # AI application patterns
│   │   ├── 01-stuff-the-prompt/
│   │   ├── 02-retrieval-augmented-generation/
│   │   ├── 03-chat-memory/     # includes mem_04: JDBC persistent memory
│   │   ├── 04-recursive-advisors/  # ToolCallAdvisor (new in 2.0)
│   │   ├── chain-of-thought/
│   │   └── self-reflection-agent/
│   └── data/           # Example datasets
├── applications/       # Provider-specific apps (5 active + gateway; azure excluded)
├── agentic-system/     # Agent development examples
├── mcp/                # Model Context Protocol examples (5 modules)
└── docker/             # PostgreSQL + pgvector, observability
```

## Current Versions

- **Spring AI**: 2.0.0-M2 (Spring AI 2.0 GA targeted May 2026)
- **Spring Boot**: 4.0.3 (GA)
- **Spring Cloud**: 2025.1.1 (Oakwood, Boot 4.0 compatible)
- **Spring Shell**: 4.0.1 (Boot 4.0 compatible)
- **Java**: 21

## Features Covered

### Spring AI APIs
- ChatClient with fluent builder pattern
- Structured output (entity conversion, ListOutputConverter, MapOutputConverter)
- Tool/Function calling (annotation-based, return-direct, named selection)
- Multimodal (image input)
- Streaming responses
- Embeddings and Vector Stores (pgvector)
- Dynamic tool search (chat_09 — embedding-based tool selection, 34–64% token savings)

### Patterns
- RAG with document loaders
- Chat Memory (PromptChatMemoryAdvisor, MessageChatMemoryAdvisor)
- JDBC-backed persistent chat memory (mem_04 — survives restarts)
- ToolCallAdvisor — tool execution in advisor chain for observability (advisor_04)
- Stuff-the-prompt
- Chain-of-thought
- Self-reflection agents

### MCP Protocol
- STDIO and HTTP transports
- Client and server implementations
- Dynamic tool calling
- Resources, prompts, and completions

### Providers
- OpenAI, AWS Bedrock, Google Vertex AI, Anthropic, Ollama
- ~~Azure OpenAI~~ — excluded (Spring Cloud Azure 6.x targets Boot 3.x; see Known Incompatibilities)

### Observability
- Prometheus metrics, Zipkin tracing, Loki logging

---

## Known Incompatibilities (Spring AI 2.0 / Boot 4.0)

| Module | Reason | Status |
|--------|--------|--------|
| `applications/provider-azure` | Spring Cloud Azure 6.x targets Boot 3.x | Excluded — re-enable when Boot 4.0-compatible Azure SDK is released |

## Upgrade Recommendations (Future Work)

### Done in Spring AI 2.0 Migration

1. ✅ **Recursive Advisors / ToolCallAdvisor** — `components/patterns/04-recursive-advisors`
2. ✅ **Persistent JDBC ChatMemoryRepository** — `mem_04` in `components/patterns/03-chat-memory`
3. ✅ **Spring Boot 4.0 / Spring AI 2.0 Migration** — completed March 2026

### Remaining

4. **Dynamic Tool Search — full embedding integration** (chat_09 has skeleton; needs VectorStore wired)
   - Enable `chatClient.toolSearch(vectorStore)` when pgvector profile is active

5. **Re-enable Azure provider** when Spring Cloud Azure releases a Boot 4.0-compatible version

### Low Priority Additions

6. **Prompt Caching Examples**
   - Available for Anthropic and AWS Bedrock
   - Can reduce costs by up to 90%

7. **Anthropic Citations API**
   - For retrieving source citations in model responses

8. **New Model Providers**
   - Google GenAI
   - ElevenLabs text-to-speech

### Low Priority Additions

6. **Prompt Caching Examples**
   - Available for Anthropic and AWS Bedrock
   - Can reduce costs by up to 90%

7. **Anthropic Citations API**
   - For retrieving source citations in model responses

8. **New Model Providers**
   - Google GenAI
   - ElevenLabs text-to-speech

---

## Running the Workshop

### Prerequisites
- Java 21+
- Docker (for PostgreSQL, pgvector, observability stack)
- Ollama (for local models)
- HTTPie (for API testing)

### Pre-downloaded Ollama Models
```bash
ollama pull llama3.2      # Chat
ollama pull mxbai-embed-large  # Embeddings
ollama pull llava         # Multimodal vision
```

### Quick Start
```bash
# Start infrastructure
docker compose -f docker/docker-compose.yml up -d

# Run a provider application
./mvnw spring-boot:run -pl applications/provider-ollama
```

---

## Known Limitations

### Ollama / llama3.2 (3B) Tool Calling

The llama3.2 (3B) model has limited tool/function calling capabilities. Testing with `provider-ollama` shows:

| Feature | Status | Notes |
|---------|--------|-------|
| Direct tool injection `.tools(new ToolClass())` | ✅ Works | `/chat/05/time`, `/chat/05/dayOfWeek` |
| Named tool references `.toolNames("beanName")` | ❌ Broken | Model outputs JSON schema as text instead of invoking |
| Complex parameter types (LocalDate, LocalTime) | ❌ 500 Error | Parsing failures with `RestaurantSearch` |
| Structured output to POJO | ❌ Broken | `/chat/04/plays/object` fails |
| Structured output to List/Map | ✅ Works | `/chat/04/plays/list` works |

**Affected endpoints in chat_05:**
- `/chat/05/weather` - uses `.toolNames("weatherFunction")`
- `/chat/05/pack` - uses `.toolNames("weatherFunction")`
- `/chat/05/search` - complex tool with date/time parameters

**Workarounds:**
1. Use cloud providers (OpenAI, Anthropic, Azure) for full tool calling support
2. Use larger local models (llama3.3 70B) if hardware permits
3. Simplify tool parameters to basic types (String, Integer) for Ollama

**Working endpoints with Ollama:**
- All basic chat endpoints (chat_01 through chat_03)
- Tool calling with direct injection (chat_05/time, chat_05/dayOfWeek)
- Streaming (chat_08)
- Chat memory (mem_02, mem_03)
- Roles (chat_06)

---

## Resources

- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
- [Spring AI GitHub](https://github.com/spring-projects/spring-ai)
- [Spring AI Examples](https://github.com/spring-projects/spring-ai-examples)
